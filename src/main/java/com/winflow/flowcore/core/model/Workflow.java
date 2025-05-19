package com.winflow.flowcore.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Workflow {
    private Metadata metadata;
    private Trigger trigger;
    private List<Job> jobs;
    private Execution execution;
    private Map<String, Object> contextParams;
}
