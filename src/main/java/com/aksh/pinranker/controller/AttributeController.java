package com.aksh.pinranker.controller;

import com.aksh.pinranker.dto.AttributeContributionDTO;
import com.aksh.pinranker.model.AttributeContribution;
import com.aksh.pinranker.model.PinCandidate;
import com.aksh.pinranker.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AttributeController {

    private final RankingService rankingService;

    public AttributeController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/attributes")
    public ResponseEntity<?> getAttributeContributions(
            @RequestParam String profileId,
            @RequestParam String pin) {

        try {
            PinCandidate candidate = rankingService.getCandidate(profileId, pin);

            List<AttributeContributionDTO> dtos = candidate.getContributions().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private AttributeContributionDTO toDto(AttributeContribution c) {
        return new AttributeContributionDTO(
                c.getRelation(), c.getAttribute(), c.getPattern(), c.getRawValue(), c.getWeight()
        );
    }
}
