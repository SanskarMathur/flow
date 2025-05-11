package com.winflow.flowcore.core.model;

import com.winflow.flowcore.core.enums.TriggerTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Data
@Getter
@Setter
@Builder
public class Trigger {
    private String id;
    private TriggerTypeEnum type;
    private String description;
    private String triggerEvent;
    private LocalTime triggerTime;
    private String triggerFilePath;
}
