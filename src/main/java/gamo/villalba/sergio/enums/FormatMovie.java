package gamo.villalba.sergio.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FormatMovie {
    DVD("DVD"),
    DIGITAL("Digital"),
    BLU_RAY("Blu-Ray");

    private String value;

    FormatMovie(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
