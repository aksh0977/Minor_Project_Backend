package com.aksh.pinranker.model;

public class AttributeContribution {

    private final String relation;     // e.g. "self", "partner", "son1"
    private final String attribute;    // e.g. "dob", "phone", "sig_num"
    private final String pattern;      // e.g. "DDMM", "MMDD", "PHONE_LAST4", "CUSTOM"
    private final String rawValue;     // original CSV value this was derived from
    private final double weight;       // contribution weight added to personalBoostScore

    public AttributeContribution(String relation, String attribute, String pattern,
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
