package com.winflow.flowcore.core.enums;

import lombok.Getter;

@Getter
public enum JobStatusEnum {
    PENDING("pending"),
    COMPLETED("completed"),
    IN_PROGRESS("inProgress"),
    FAILED("failed");

    private final String status;

    private JobStatusEnum(String status) {
        this.status = status;
    }
}
