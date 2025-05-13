package com.winflow.flowcore.trigger;

import com.winflow.flowcore.core.enums.TriggerTypeEnum;
import com.winflow.flowcore.trigger.impl.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TriggerHandlerFactory {
    private final SystemTriggerHandler systemTriggerHandler;
    private final ManualTriggerHandler manualTriggerHandler;
    private final FileTriggerHandler fileTriggerHandler;
    private final CronTriggerHandler cronTriggerHandler;
    private final ApiTriggerHandler apiTriggerHandler;
    private final ExternalTriggerHandler externalTriggerHandler;

    public TriggerHandler getHandler(TriggerTypeEnum triggerType) {
        return switch (triggerType) {
            case SYSTEM_EVENT -> systemTriggerHandler;
            case MANUAL_EVENT -> manualTriggerHandler;
            case FILE_EVENT -> fileTriggerHandler;
            case CRON_EVENT -> cronTriggerHandler;
            case API_EVENT -> apiTriggerHandler;
            case EXTERNAL_EVENT -> externalTriggerHandler;
        };
    }
}
