package com.winflow.flowcore.trigger;

import com.winflow.flowcore.core.enums.TriggerTypeEnum;
import com.winflow.flowcore.trigger.impl.*;
import org.springframework.stereotype.Component;

@Component
public class TriggerHandlerFactory {
    public TriggerHandler getHandler(TriggerTypeEnum triggerType) {
        switch (triggerType) {
            case SYSTEM_EVENT -> {
                return new SystemTriggerHandler();
            }
            case MANUAL_EVENT -> {
                return new ManualTriggerHandler();
            }
            case FILE_EVENT -> {
                return new FileTriggerHandler();
            }
            case CRON_EVENT -> {
                return new CronTriggerHandler();
            }
            case API_EVENT -> {
                return new ApiTriggerHandler();
            }
            case EXTERNAL_EVENT -> {
                return new ExternalTriggerHandler();
            }
            default -> {
                return null;
            }
        }
    }
}
