package com.winflow.flowcore.exception;

import lombok.Getter;

@Getter
public class RegisterTriggerException extends RuntimeException {
    private final String triggerId;

    public RegisterTriggerException(String message, String triggerId) {
        super("Exception occurred while registering trigger: " + message);
        this.triggerId = triggerId;
    }
}
