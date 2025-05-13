package com.winflow.flowcore.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winflow.flowcore.core.enums.JobFailEnum;
import com.winflow.flowcore.core.enums.JobStatusEnum;
import com.winflow.flowcore.core.enums.JobTypeEnum;
import lombok.*;

@Data
@Getter
@Setter
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
}
