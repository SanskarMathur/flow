package com.winflow.flowcore.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum JobTypeEnum {
    COMMAND("command"),
    SCRIPT("script"),
    NOTIFY("notify"),
    API_CALL("apiCall"),
    CUSTOM("custom");

    private final String type;

    JobTypeEnum(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static JobTypeEnum fromValue(String value) {
        for (JobTypeEnum typeEnum : values()) {
            if (typeEnum.type.equalsIgnoreCase(value)) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
