package com.aksh.pinranker.service;

import com.aksh.pinranker.model.PinCandidate;
import com.aksh.pinranker.model.SimulationResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationService {

    private final RankingService rankingService;

    public SimulationService(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    /**
     * Tries the ranked candidate list, highest score first, against targetPin,
     * stopping at maxAttempts to mimic a real device lockout.
     */
    public SimulationResult runSimulation(String profileId, String targetPin, int maxAttempts) {
        List<PinCandidate> ranked = rankingService.getRankedCandidates(profileId);
        SimulationResult result = new SimulationResult();

        int attempts = 0;
        for (PinCandidate candidate : ranked) {
            if (attempts >= maxAttempts) {
                result.setLockedOut(true);
                break;
            }

            attempts++;
            result.addTriedPin(candidate.getPin());

            if (candidate.getPin().equals(targetPin)) {
                result.setCracked(true);
                result.setCrackedPin(candidate.getPin());
                break;
            }
        }

        result.setAttemptsUsed(attempts);
        return result;
    }
}
