package com.aksh.pinranker.dto;

public class RankedCandidateDTO {

    private String pin;
    private int rank;
    private double finalScore;
    private double basePriorScore;
    private double personalBoostScore;
    private String category;

    public RankedCandidateDTO(String pin, int rank, double finalScore,
                               double basePriorScore, double personalBoostScore, String category) {
        this.pin = pin;
        this.rank = rank;
        this.finalScore = finalScore;
        this.basePriorScore = basePriorScore;
        this.personalBoostScore = personalBoostScore;
        this.category = category;
    }

    public String getPin() {
        return pin;
    }

    public int getRank() {
        return rank;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public double getBasePriorScore() {
        return basePriorScore;
    }

    public double getPersonalBoostScore() {
        return personalBoostScore;
    }

    public String getCategory() {
        return category;
    }
}
