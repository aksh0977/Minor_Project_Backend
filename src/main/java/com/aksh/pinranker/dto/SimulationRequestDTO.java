package com.aksh.pinranker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SimulationRequestDTO {

    @NotBlank
    private String profileId;

    @NotBlank
    @Pattern(regexp = "\\d{4}", message = "targetPin must be exactly 4 digits")
    private String targetPin; // the "actual" PIN on the simulated locked device

    private Integer maxAttempts = 10; // default lockout threshold

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getTargetPin() {
        return targetPin;
    }

    public void setTargetPin(String targetPin) {
        this.targetPin = targetPin;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
