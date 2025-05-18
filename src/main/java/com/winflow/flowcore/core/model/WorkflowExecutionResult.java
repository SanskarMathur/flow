package com.winflow.flowcore.core.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class WorkflowExecutionResult {
    private String executionId;
    private String workflowId;
    private boolean successful;
    private Map<String, JobExecutionResult> jobResults;
}
