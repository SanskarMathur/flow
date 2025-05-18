package com.winflow.flowcore.core.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class ExecutionContext {
    private String executionId;
    private String workflowId;
    private Map<String, Object> variables;
    private Map<String, JobExecutionResult> results;

    public ExecutionContext(String workflowId) {
        this.executionId = UUID.randomUUID().toString();
        this.workflowId = workflowId;
        this.variables = new HashMap<>();
        this.results = new HashMap<>();
    }
}
