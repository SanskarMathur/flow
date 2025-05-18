package com.winflow.flowcore.core.model;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;

@Data
@Builder
public class JobExecutionResult {
    private String jobId;
    private String successMessage;
    private String errorMessage;
    private int exitCode;
    private boolean success;
    private Instant startTime;
    private Instant endTime;
    private Duration executionTime;

    public static JobExecutionResult failed(String jobId, String errorMessage) {
        return JobExecutionResult.builder()
                .jobId(jobId)
                .errorMessage(errorMessage)
                .exitCode(-1)
                .success(false)
                .build();
    }
}
