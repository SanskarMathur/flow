package com.winflow.flowcore.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winflow.flowcore.core.enums.WorkflowStatusEnum;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
    private String name;
    private String description;
    private String author;
    private String createdAt;
    private String updatedAt;
    private WorkflowStatusEnum status;
    private Integer version;
}
