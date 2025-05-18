package com.winflow.flowcore.trigger;

import com.winflow.flowcore.core.enums.TriggerTypeEnum;
import com.winflow.flowcore.trigger.impl.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TriggerHandlerFactory {
    private final ApplicationTriggerHandler applicationTriggerHandler;
    private final CronTriggerHandler cronTriggerHandler;
    private final FileTriggerHandler fileTriggerHandler;
    private final HotkeyTriggerHandler hotkeyTriggerHandler;
    private final ManualTriggerHandler manualTriggerHandler;
    private final SystemTriggerHandler systemTriggerHandler;
    private final UserActivityTriggerHandler userActivityTriggerHandler;

    public TriggerHandler getHandler(TriggerTypeEnum triggerType) {
        if (triggerType == null) {
            throw new IllegalArgumentException("Trigger type cannot be null");
        }

        return switch (triggerType) {
            case APPLICATION_EVENT -> applicationTriggerHandler;
            case CRON_EVENT -> cronTriggerHandler;
            case FILE_EVENT -> fileTriggerHandler;
            case HOTKEY_EVENT -> hotkeyTriggerHandler;
            case MANUAL_EVENT -> manualTriggerHandler;
            case SYSTEM_EVENT -> systemTriggerHandler;
            case USER_ACTIVITY_EVENT -> userActivityTriggerHandler;
        };
    }
}
