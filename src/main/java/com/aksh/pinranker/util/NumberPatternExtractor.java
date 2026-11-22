package com.aksh.pinranker.util;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Turns phone numbers / arbitrary "significant number" strings into 4-digit candidates.
 */
@Component
public class NumberPatternExtractor {

    /**
     * For long numbers (phone numbers etc.) - pulls first 4 and last 4 digits.
     */
    public Map<String, String> extractFromLongNumber(String rawNumber) {
        Map<String, String> candidates = new LinkedHashMap<>();
        String digits = stripNonDigits(rawNumber);
        if (digits.length() < 4) {
            return candidates;
        }

        candidates.put("PHONE_LAST4", digits.substring(digits.length() - 4));
        candidates.put("PHONE_FIRST4", digits.substring(0, 4));

        if (digits.length() >= 8) {
            // middle 4 digits can also be meaningful (common in Indian 10-digit numbers)
            int mid = digits.length() / 2;
            candidates.put("PHONE_MID4", digits.substring(mid - 2, mid + 2));
        }

        return candidates;
    }

    /**
     * For already-short "significant number" style fields (e.g. 2580, 7).
     * If shorter than 4 digits, left-pads AND right-pads as two separate guesses,
     * since the user could mean either reading.
     */
    public Map<String, String> extractFromCustomNumber(String rawNumber) {
        Map<String, String> candidates = new LinkedHashMap<>();
        String digits = stripNonDigits(rawNumber);
        if (digits.isEmpty()) {
            return candidates;
        }

        if (digits.length() == 4) {
            candidates.put("CUSTOM_EXACT", digits);
        } else if (digits.length() > 4) {
            candidates.put("CUSTOM_LAST4", digits.substring(digits.length() - 4));
        } else {
            candidates.put("CUSTOM_PAD_LEFT", String.format("%4s", digits).replace(' ', '0'));
            candidates.put("CUSTOM_REPEAT", digits.repeat(4).substring(0, 4));
        }

        return candidates;
    }

    /**
     * Significant year (e.g. 2015) -> direct 4-digit candidate plus doubled-last-2 (e.g. 1515).
     */
    public Map<String, String> extractFromYear(String rawYear) {
        Map<String, String> candidates = new LinkedHashMap<>();
        String digits = stripNonDigits(rawYear);
        if (digits.length() != 4) {
            return candidates;
        }
        candidates.put("YEAR_EXACT", digits);
        String lastTwo = digits.substring(2);
        candidates.put("YEAR_DOUBLED", lastTwo + lastTwo);
        return candidates;
    }

    private String stripNonDigits(String raw) {
        return raw == null ? "" : raw.replaceAll("\\D", "");
    }
}
