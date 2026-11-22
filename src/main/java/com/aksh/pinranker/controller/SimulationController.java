package com.aksh.pinranker.controller;

import com.aksh.pinranker.dto.SimulationRequestDTO;
import com.aksh.pinranker.model.SimulationResult;
import com.aksh.pinranker.service.SimulationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulate")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping
    public ResponseEntity<?> simulate(@Valid @RequestBody SimulationRequestDTO request) {
        try {
            SimulationResult result = simulationService.runSimulation(
                    request.getProfileId(),
                    request.getTargetPin(),
                    request.getMaxAttempts()
            );
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
