package com.aksh.pinranker.service;

import com.aksh.pinranker.model.AttributeContribution;
import com.aksh.pinranker.model.PinCandidate;
import com.aksh.pinranker.service.FeatureExtractionService.ExtractedFragment;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CandidateGenerationService {

    /**
     * Weight given to a fragment, based on how strongly that (attribute, pattern)
     * combination tends to predict a real-world PIN choice. Grounded in the general
     * finding that direct DOB readings (DDMM/MMDD) are the most common personal-PIN
     * source, with secondary date slices, phone fragments and custom numbers trailing.
     */
    private double resolveWeight(String attribute, String pattern) {
        String attr = attribute.toLowerCase();

        boolean isPrimaryDatePattern = pattern.equals("DDMM") || pattern.equals("MMDD");
        boolean isSecondaryDatePattern = pattern.equals("YYMM") || pattern.equals("MMYY")
                || pattern.equals("DDYY") || pattern.equals("YYYY_TAIL");

        if (attr.contains("anniversary")) {
            return isPrimaryDatePattern ? 0.25 : 0.15;
        }
        if (attr.contains("dob") || attr.contains("date")) {
            return isPrimaryDatePattern ? 0.35 : 0.20;
        }
        if (attr.contains("phone")) {
            return pattern.equals("PHONE_LAST4") ? 0.15 : 0.10;
        }
        if (attr.contains("year")) {
            return 0.15;
        }
        // custom/significant numbers
        return 0.15;
    }

    /**
     * Builds the personal candidate pool: pin -> PinCandidate (basePriorScore not yet set,
     * that's filled in later by RankingService from FrequencyPriorService).
     */
    public Map<String, PinCandidate> generate(List<ExtractedFragment> fragments) {
        Map<String, PinCandidate> candidates = new LinkedHashMap<>();

        for (ExtractedFragment fragment : fragments) {
            PinCandidate candidate = candidates.computeIfAbsent(fragment.pin, PinCandidate::new);

            double weight = resolveWeight(fragment.attribute, fragment.pattern);
            AttributeContribution contribution = new AttributeContribution(
                    fragment.relation, fragment.attribute, fragment.pattern, fragment.rawValue, weight
            );
            candidate.addPersonalBoost(contribution);
        }

        return candidates;
    }
}
