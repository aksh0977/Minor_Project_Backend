package com.aksh.pinranker.dto;

public class UploadResponseDTO {

    private String profileId;
    private int relationsParsed;
    private int attributesExtracted;
    private String message;

    public UploadResponseDTO(String profileId, int relationsParsed, int attributesExtracted, String message) {
        this.profileId = profileId;
        this.relationsParsed = relationsParsed;
        this.attributesExtracted = attributesExtracted;
        this.message = message;
    }

    public String getProfileId() {
        return profileId;
    }

    public int getRelationsParsed() {
        return relationsParsed;
    }

    public int getAttributesExtracted() {
        return attributesExtracted;
    }

    public String getMessage() {
        return message;
    }
}
