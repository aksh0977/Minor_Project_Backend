package com.aksh.pinranker.service;

import com.aksh.pinranker.model.Profile;
import com.aksh.pinranker.util.DatePatternExtractor;
import com.aksh.pinranker.util.NumberPatternExtractor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FeatureExtractionService {

    private final DatePatternExtractor dateExtractor;
    private final NumberPatternExtractor numberExtractor;

    public FeatureExtractionService(DatePatternExtractor dateExtractor, NumberPatternExtractor numberExtractor) {
        this.dateExtractor = dateExtractor;
        this.numberExtractor = numberExtractor;
    }

    /** One raw-attribute -> candidate-pin mapping, tagged with where it came from. */
    public static class ExtractedFragment {
        public final String relation;
        public final String attribute;
        public final String pattern;
        public final String rawValue;
        public final String pin;

        public ExtractedFragment(String relation, String attribute, String pattern, String rawValue, String pin) {
            this.relation = relation;
            this.attribute = attribute;
            this.pattern = pattern;
            this.rawValue = rawValue;
            this.pin = pin;
        }
    }

    public List<ExtractedFragment> extractAll(Profile profile) {
        List<ExtractedFragment> fragments = new ArrayList<>();

        for (Map.Entry<String, Map<String, String>> relationEntry : profile.getRelationAttributes().entrySet()) {
            String relation = relationEntry.getKey();

            for (Map.Entry<String, String> attrEntry : relationEntry.getValue().entrySet()) {
                String attribute = attrEntry.getKey();
                String rawValue = attrEntry.getValue();

                Map<String, String> patternToPin = extractByAttributeType(attribute, rawValue);
                for (Map.Entry<String, String> p : patternToPin.entrySet()) {
                    String pin = p.getValue();
                    if (isValidFourDigitPin(pin)) {
                        fragments.add(new ExtractedFragment(relation, attribute, p.getKey(), rawValue, pin));
                    }
                }
            }
        }

        return fragments;
    }

    private Map<String, String> extractByAttributeType(String attribute, String rawValue) {
        String attr = attribute.toLowerCase();

        if (attr.contains("dob") || attr.contains("date") || attr.contains("anniversary")) {
            return dateExtractor.extract(rawValue);
        }
        if (attr.contains("phone")) {
            return numberExtractor.extractFromLongNumber(rawValue);
        }
        if (attr.contains("year")) {
            return numberExtractor.extractFromYear(rawValue);
        }
        // catch-all: sig_num, custom_num, any other numeric "significant number" field
        return numberExtractor.extractFromCustomNumber(rawValue);
    }

    private boolean isValidFourDigitPin(String pin) {
        return pin != null && pin.matches("\\d{4}");
    }
}
