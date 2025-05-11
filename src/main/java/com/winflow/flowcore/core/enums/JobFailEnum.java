package com.winflow.flowcore.core.enums;

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
}
