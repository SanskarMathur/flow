package com.winflow.flowcore.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum JobFailEnum {
    STOP("stop"),
    CONTINUE("continue"),
    RETRY("retry");

    private final String value;

    JobFailEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static JobFailEnum fromValue(String value) {
        for (JobFailEnum statusEnum : values()) {
            if (statusEnum.value.equalsIgnoreCase(value)) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
