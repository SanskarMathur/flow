package com.winflow.flowcore.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum WorkflowStatusEnum {
    ACTIVE("active"),
    INACTIVE("inactive"),
    ARCHIVED("archived");

    private final String status;

    WorkflowStatusEnum(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    @JsonCreator
    public static WorkflowStatusEnum fromValue(String value) {
        for (WorkflowStatusEnum statusEnum : values()) {
            if (statusEnum.status.equalsIgnoreCase(value)) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
