package com.winflow.flowcore.exception;

public class WorkflowValidationException extends RuntimeException {
    public WorkflowValidationException(String message) {
        super("Failed to load/validate workflow: " + message);
    }
}
