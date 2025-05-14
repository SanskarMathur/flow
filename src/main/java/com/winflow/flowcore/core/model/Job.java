package com.winflow.flowcore.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winflow.flowcore.core.enums.JobFailEnum;
import com.winflow.flowcore.core.enums.JobStatusEnum;
import com.winflow.flowcore.core.enums.JobTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
    private String id;
    private JobTypeEnum type;
    private Script script;
    private String command;
    private Long timeout;
    private Integer maxRetries;
    private JobStatusEnum status;
    private JobFailEnum onFailure;
    private List<String> dependencies;
    private Map<String, Object> params;
}
