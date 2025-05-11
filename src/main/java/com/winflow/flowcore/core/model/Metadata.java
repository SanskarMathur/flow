package com.winflow.flowcore.core.model;

import com.winflow.flowcore.core.enums.WorkflowStatusEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
public class Metadata {
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private WorkflowStatusEnum status;
    private Integer version;
}
