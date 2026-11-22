package com.aksh.pinranker.dto;

public class AttributeContributionDTO {

    private String relation;
    private String attribute;
    private String pattern;
    private String rawValue;
    private double weight;

    public AttributeContributionDTO(String relation, String attribute, String pattern,
                                     String rawValue, double weight) {
        this.relation = relation;
        this.attribute = attribute;
        this.pattern = pattern;
        this.rawValue = rawValue;
        this.weight = weight;
    }

    public String getRelation() {
        return relation;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getPattern() {
        return pattern;
    }

    public String getRawValue() {
        return rawValue;
    }

    public double getWeight() {
        return weight;
    }
}
