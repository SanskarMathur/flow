package com.winflow.flowcore.exception;

import lombok.Getter;

@Getter
public class JobExecutionException extends RuntimeException {
    private final String jobId;

    public JobExecutionException(String jobId, String message) {
        super("Exception occurred while job execution: "+message);
        this.jobId = jobId;
    }
}
