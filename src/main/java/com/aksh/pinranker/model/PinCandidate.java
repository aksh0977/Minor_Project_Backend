package com.aksh.pinranker.model;

import java.util.ArrayList;
import java.util.List;

public class PinCandidate {

    private final String pin;
    private double basePriorScore;      // from the global frequency table, 0 if not present
    private double personalBoostScore;  // sum of weighted contributions from this profile
    private PinCategory category;
    private final List<AttributeContribution> contributions = new ArrayList<>();

    public PinCandidate(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public double getBasePriorScore() {
        return basePriorScore;
    }

    public void setBasePriorScore(double basePriorScore) {
        this.basePriorScore = basePriorScore;
    }

    public double getPersonalBoostScore() {
        return personalBoostScore;
    }

    public void addPersonalBoost(AttributeContribution contribution) {
        this.contributions.add(contribution);
        this.personalBoostScore += contribution.getWeight();
    }

    public double getFinalScore() {
        return basePriorScore + personalBoostScore;
    }

    public PinCategory getCategory() {
        return category;
    }

    public void setCategory(PinCategory category) {
        this.category = category;
    }

    public List<AttributeContribution> getContributions() {
        return contributions;
    }
}
