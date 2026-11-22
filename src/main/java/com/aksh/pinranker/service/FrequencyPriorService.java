package com.aksh.pinranker.service;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads resources/data/pin_frequency_table.csv once at startup.
 * File columns: pin,count,frequency  (frequency already normalized to sum to 1.0 across all 10,000 pins)
 * Source: github.com/sr-lab/pin-bank, reconstructed from the DataGenetics (Nick Berry, 2012) PIN study.
 */
@Service
public class FrequencyPriorService {

    private final Map<String, Double> priorByPin = new HashMap<>();
    private boolean loaded = false;

    @PostConstruct
    public void loadTable() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/pin_frequency_table.csv").getInputStream(), StandardCharsets.UTF_8))) {

            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;
                String pin = parts[0].trim();
                double frequency = Double.parseDouble(parts[2].trim());
                priorByPin.put(pin, frequency);
            }
            loaded = true;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load pin_frequency_table.csv - check it exists under resources/data/", e);
        }
    }

    /** Returns the global base prior for a pin, or 0.0 if it's not in the table (shouldn't happen if table has all 10,000). */
    public double getPrior(String pin) {
        return priorByPin.getOrDefault(pin, 0.0);
    }

    public boolean hasPrior(String pin) {
        return priorByPin.containsKey(pin);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public int size() {
        return priorByPin.size();
    }
}
