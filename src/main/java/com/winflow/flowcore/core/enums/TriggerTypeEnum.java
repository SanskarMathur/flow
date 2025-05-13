package com.winflow.flowcore.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TriggerTypeEnum {
    SYSTEM_EVENT("system"),
    CRON_EVENT("cron"),
    FILE_EVENT("file"),
    MANUAL_EVENT("manual"),
    API_EVENT("api"),
    EXTERNAL_EVENT("external");

    private final String event;

    TriggerTypeEnum(String event) {
        this.event = event;
    }

    @JsonValue
    public String getEvent() {
        return event;
    }

    @JsonCreator
    public static TriggerTypeEnum fromValue(String value) {
        for (TriggerTypeEnum eventEnum : values()) {
            if (eventEnum.event.equalsIgnoreCase(value)) {
                return eventEnum;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
