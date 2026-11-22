package com.aksh.pinranker.model;

public enum PinCategory {
    GENERIC_WEAK,     // high global frequency, no personal match (1234, 0000, 2580...)
    PERSONAL_DERIVED, // generated from this profile's CSV data (dob, phone, custom numbers)
    BOTH              // appears in both the global frequency table AND personal data
}
