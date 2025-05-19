package com.winflow.flowcore.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Execution {
    private String concurrency;
    private Integer maxRetries;
    private boolean logOutput;
}
