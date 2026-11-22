package com.aksh.pinranker.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents one uploaded CSV row, fully parsed.
 * Columns are grouped by their relation prefix (self_, partner_, son1_, daughter1_, ...).
 * Example raw column: "self_dob" -> relation="self", attribute="dob", value="2003-05-14"
 */
public class Profile {

    private final String profileId;

    // relation -> (attributeName -> rawValue)
    // e.g. "self" -> {"dob": "2003-05-14", "phone": "9876543210"}
    //      "partner" -> {"dob": "2004-02-10"}
    //      "son1" -> {"dob": "2023-01-01"}
    private final Map<String, Map<String, String>> relationAttributes = new LinkedHashMap<>();

    public Profile() {
        this.profileId = UUID.randomUUID().toString();
    }

    public String getProfileId() {
        return profileId;
    }

    public void addAttribute(String relation, String attribute, String value) {
        if (value == null || value.isBlank()) {
            return; // skip empty cells, no point carrying them through the pipeline
        }
        relationAttributes
                .computeIfAbsent(relation, r -> new LinkedHashMap<>())
                .put(attribute, value.trim());
    }

    public Map<String, Map<String, String>> getRelationAttributes() {
        return relationAttributes;
    }
}
