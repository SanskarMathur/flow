package com.winflow.flowcore.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winflow.flowcore.core.enums.TriggerTypeEnum;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trigger {
    private String id;
    private TriggerTypeEnum type;
    private String description;
    private String triggerEvent;
    private String triggerCronExpression;
    private String triggerFilePath;
    private Map<String, Object> additionalProperties;
}
