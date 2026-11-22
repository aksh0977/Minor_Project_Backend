package com.aksh.pinranker.controller;

import com.aksh.pinranker.dto.UploadResponseDTO;
import com.aksh.pinranker.model.Profile;
import com.aksh.pinranker.service.CsvParserService;
import com.aksh.pinranker.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final CsvParserService csvParserService;
    private final RankingService rankingService;

    public UploadController(CsvParserService csvParserService, RankingService rankingService) {
        this.csvParserService = csvParserService;
        this.rankingService = rankingService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Uploaded file is empty");
        }

        try {
            Profile profile = csvParserService.parse(file);
            String profileId = rankingService.storeProfile(profile);
            int attributesExtracted = rankingService.countExtractedAttributes(profile);

            UploadResponseDTO response = new UploadResponseDTO(
                    profileId,
                    profile.getRelationAttributes().size(),
                    attributesExtracted,
                    "Profile parsed successfully"
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Could not parse CSV: " + e.getMessage());
        }
    }
}
