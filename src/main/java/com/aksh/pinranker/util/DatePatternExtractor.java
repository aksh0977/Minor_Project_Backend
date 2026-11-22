package com.aksh.pinranker.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Turns a date string like "2003-05-14" into candidate 4-digit patterns.
 * Returned map: pattern-name -> 4-digit candidate (e.g. "DDMM" -> "1405")
 */
@Component
public class DatePatternExtractor {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

    public Map<String, String> extract(String rawDate) {
        Map<String, String> candidates = new LinkedHashMap<>();
        LocalDate date = parse(rawDate);
        if (date == null) {
            return candidates;
        }

        String dd = pad(date.getDayOfMonth());
        String mm = pad(date.getMonthValue());
        String yyyy = String.valueOf(date.getYear());
        String yy = yyyy.substring(2);

        candidates.put("DDMM", dd + mm);
        candidates.put("MMDD", mm + dd);
        candidates.put("YYMM", yy + mm);
        candidates.put("MMYY", mm + yy);
        candidates.put("DDYY", dd + yy);
        candidates.put("YYYY_TAIL", yyyy); // only meaningful if yyyy.length()==4, always true here

        return candidates;
    }

    private LocalDate parse(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(rawDate.trim(), ISO);
        } catch (DateTimeParseException e) {
            return null; // malformed date cell - skip rather than crash the whole pipeline
        }
    }

    private String pad(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }
}
