package com.winflow.flowcore.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winflow.flowcore.core.enums.TriggerTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Map<String, Object> configuration;

    public Object getConfig(String key) {
        return configuration.getOrDefault(key, null);
    }
}
