package com.winflow.flowcore.core.enums;

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
}
