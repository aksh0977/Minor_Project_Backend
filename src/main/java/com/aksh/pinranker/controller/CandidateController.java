package com.aksh.pinranker.controller;

import com.aksh.pinranker.dto.RankedCandidateDTO;
import com.aksh.pinranker.model.PinCandidate;
import com.aksh.pinranker.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api")
public class CandidateController {

    private final RankingService rankingService;

    public CandidateController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/candidates")
    public ResponseEntity<?> getCandidates(
            @RequestParam String profileId,
            @RequestParam(defaultValue = "100") int limit) {

        try {
            List<PinCandidate> ranked = rankingService.getRankedCandidates(profileId, limit);

            List<RankedCandidateDTO> dtos = IntStream.range(0, ranked.size())
                    .mapToObj(i -> {
                        PinCandidate c = ranked.get(i);
                        return new RankedCandidateDTO(
                                c.getPin(),
                                i + 1,
                                c.getFinalScore(),
                                c.getBasePriorScore(),
                                c.getPersonalBoostScore(),
                                c.getCategory().name()
                        );
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
