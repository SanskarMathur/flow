package com.winflow.flowcore.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum JobStatusEnum {
    PENDING("pending"),
    COMPLETED("completed"),
    IN_PROGRESS("inProgress"),
    FAILED("failed");

    private final String status;

    JobStatusEnum(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    @JsonCreator
    public static JobStatusEnum fromValue(String value) {
        for (JobStatusEnum statusEnum : values()) {
            if (statusEnum.status.equalsIgnoreCase(value)) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
