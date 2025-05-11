package com.winflow.flowcore.core.model;

import com.winflow.flowcore.core.enums.JobFailEnum;
import com.winflow.flowcore.core.enums.JobStatusEnum;
import com.winflow.flowcore.core.enums.JobTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
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
