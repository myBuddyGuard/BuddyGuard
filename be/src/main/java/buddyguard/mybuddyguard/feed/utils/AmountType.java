package buddyguard.mybuddyguard.feed.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum AmountType {
    L("l"),
    ML("ml"),
    G("g"),
    COUNT("개");

    private final String unitType;

    AmountType(String unitType) {
        this.unitType = unitType;
    }

    @JsonCreator
    public static AmountType findByUnitType (String value) {
        return Arrays.stream(AmountType.values())
                .filter(type -> type.unitType.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow();
    }
}
