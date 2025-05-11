package com.winflow.flowcore.core.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Builder
public class Workflow {
    private Metadata metadata;
    private Trigger trigger;
    private List<Job> jobs;
    private Integer maxRetries;
    private boolean logOutput;
}
