package com.winflow.flowcore.core.model;

import lombok.Data;

import java.util.Map;

@Data
public class ManualTriggerRequest {
    private String workflowId;
    Map<String, Object> params;
}
