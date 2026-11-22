package com.aksh.pinranker.util;

import com.aksh.pinranker.model.PinCategory;
import org.springframework.stereotype.Component;

@Component
public class PinCategoryClassifier {

    public PinCategory classify(boolean hasBasePrior, boolean hasPersonalMatch) {
        if (hasBasePrior && hasPersonalMatch) {
            return PinCategory.BOTH;
        } else if (hasPersonalMatch) {
            return PinCategory.PERSONAL_DERIVED;
        } else {
            return PinCategory.GENERIC_WEAK;
        }
    }

    /** True if the pin is purely numeric-sequential (1234, 2345, 6789...). */
    public boolean isSequential(String pin) {
        for (int i = 0; i < pin.length() - 1; i++) {
            if (pin.charAt(i + 1) - pin.charAt(i) != 1) {
                return false;
            }
        }
        return true;
    }

    /** True if all 4 digits are the same (1111, 0000...). */
    public boolean isRepeatedDigit(String pin) {
        return pin.chars().distinct().count() == 1;
    }

    /** True if the pin traces a straight/known shape on a phone keypad (e.g. 2580). */
    public boolean isKeypadPattern(String pin) {
        return pin.equals("2580") || pin.equals("0852") || pin.equals("1397") || pin.equals("7931");
    }
}
