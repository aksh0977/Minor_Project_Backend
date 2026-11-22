package com.aksh.pinranker.service;

import com.aksh.pinranker.model.PinCandidate;
import com.aksh.pinranker.model.PinCategory;
import com.aksh.pinranker.model.Profile;
import com.aksh.pinranker.util.PinCategoryClassifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RankingService {

    private final FrequencyPriorService frequencyPriorService;
    private final FeatureExtractionService featureExtractionService;
    private final CandidateGenerationService candidateGenerationService;
    private final PinCategoryClassifier classifier;

    // in-memory, session-scoped storage - no persistence needed for this demo tool
    private final Map<String, Profile> profileStore = new ConcurrentHashMap<>();
    private final Map<String, List<PinCandidate>> rankingCache = new ConcurrentHashMap<>();

    public RankingService(FrequencyPriorService frequencyPriorService,
                           FeatureExtractionService featureExtractionService,
                           CandidateGenerationService candidateGenerationService,
                           PinCategoryClassifier classifier) {
        this.frequencyPriorService = frequencyPriorService;
        this.featureExtractionService = featureExtractionService;
        this.candidateGenerationService = candidateGenerationService;
        this.classifier = classifier;
    }

    public String storeProfile(Profile profile) {
        profileStore.put(profile.getProfileId(), profile);
        rankingCache.remove(profile.getProfileId()); // invalidate any stale cache for this id (shouldn't happen, UUIDs are fresh)
        return profile.getProfileId();
    }

    public Profile getProfile(String profileId) {
        Profile profile = profileStore.get(profileId);
        if (profile == null) {
            throw new IllegalArgumentException("Unknown profileId: " + profileId + " (did you upload a CSV first?)");
        }
        return profile;
    }

    public int countExtractedAttributes(Profile profile) {
        return featureExtractionService.extractAll(profile).size();
    }

    /**
     * Returns the full ranked candidate list for a profile (all 10,000 pins), computed once and cached.
     * score(pin) = base_prior(pin) + personal_boost(pin)
     */
    public List<PinCandidate> getRankedCandidates(String profileId) {
        return rankingCache.computeIfAbsent(profileId, id -> computeRanking(getProfile(id)));
    }

    public List<PinCandidate> getRankedCandidates(String profileId, int limit) {
        List<PinCandidate> all = getRankedCandidates(profileId);
        return all.subList(0, Math.min(limit, all.size()));
    }

    private List<PinCandidate> computeRanking(Profile profile) {
        var fragments = featureExtractionService.extractAll(profile);
        Map<String, PinCandidate> personalCandidates = candidateGenerationService.generate(fragments);

        List<PinCandidate> allCandidates = new ArrayList<>();

        for (int i = 0; i <= 9999; i++) {
            String pin = String.format("%04d", i);

            PinCandidate candidate = personalCandidates.get(pin);
            boolean hasPersonalMatch = candidate != null;
            if (candidate == null) {
                candidate = new PinCandidate(pin);
            }

            double prior = frequencyPriorService.getPrior(pin);
            candidate.setBasePriorScore(prior);
            candidate.setCategory(classifier.classify(prior > 0, hasPersonalMatch));

            allCandidates.add(candidate);
        }

        allCandidates.sort(Comparator.comparingDouble(PinCandidate::getFinalScore).reversed());
        return allCandidates;
    }

    /** Looks up one specific candidate (with its contributions) from the cached ranking - used by the attribute/chart endpoint. */
    public PinCandidate getCandidate(String profileId, String pin) {
        return getRankedCandidates(profileId).stream()
                .filter(c -> c.getPin().equals(pin))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pin not found: " + pin));
    }
}
