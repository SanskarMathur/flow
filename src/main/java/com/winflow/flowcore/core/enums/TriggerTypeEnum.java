package com.winflow.flowcore.core.enums;

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

}
