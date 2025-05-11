package com.winflow.flowcore.core.enums;

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
}
