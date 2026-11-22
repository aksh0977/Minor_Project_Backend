package com.aksh.pinranker.service;

import com.aksh.pinranker.model.Profile;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Expects a CSV with ONE header row and ONE data row (one person's full profile),
 * columns named like: self_name, self_dob, self_phone, partner_dob, son1_dob, daughter1_dob...
 * The prefix before the first underscore is the "relation", the rest is the "attribute".
 */
@Service
public class CsvParserService {

    public Profile parse(MultipartFile file) throws IOException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();
            if (rows.isEmpty()) {
                throw new IllegalArgumentException("CSV file is empty");
            }

            String[] headers = rows.get(0);
            if (rows.size() < 2) {
                throw new IllegalArgumentException("CSV must contain a header row and one data row");
            }
            String[] values = rows.get(1); // one profile per file, as agreed

            Profile profile = new Profile();
            for (int i = 0; i < headers.length && i < values.length; i++) {
                String header = headers[i].trim();
                String value = values[i];

                int underscoreIdx = header.indexOf('_');
                if (underscoreIdx <= 0) {
                    continue; // column doesn't follow relation_attribute convention - skip safely
                }

                String relation = header.substring(0, underscoreIdx).toLowerCase();
                String attribute = header.substring(underscoreIdx + 1).toLowerCase();
                profile.addAttribute(relation, attribute, value);
            }

            return profile;
        } catch (com.opencsv.exceptions.CsvException e) {
            throw new IOException("Malformed CSV: " + e.getMessage(), e);
        }
    }
}
