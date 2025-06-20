package com.winflow.flowcore.trigger;

import com.winflow.flowcore.core.enums.TriggerTypeEnum;
import com.winflow.flowcore.core.model.Trigger;
import com.winflow.flowcore.engine.WorkflowExecutor;
import com.winflow.flowcore.exception.RegisterTriggerException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dispatches triggers to corresponding trigger handlers
 * Also prevents duplicate triggers registration
 */
@Component
@AllArgsConstructor
@Slf4j
public class TriggerDispatcher {
    private TriggerHandlerFactory factory;
    private WorkflowExecutor executor;

    private final Map<String, Trigger> registeredTriggers = new ConcurrentHashMap<>();

    public void registerTrigger(Trigger trigger) {
        if (registeredTriggers.containsKey(trigger.getId())) {
            return;
        }

        TriggerTypeEnum triggerType = trigger.getType();
        TriggerHandler triggerHandler = factory.getHandler(triggerType);
        if (triggerHandler == null) {
            throw new RegisterTriggerException("Unsupported Trigger type: " + triggerType, trigger.getId());
        }

        triggerHandler.register(trigger);
        registeredTriggers.put(trigger.getId(), trigger);
    }

    public void deRegisterTrigger(String triggerId) {
        if (registeredTriggers.containsKey(triggerId)) {
            Trigger trigger = registeredTriggers.get(triggerId);
            TriggerTypeEnum triggerType = trigger.getType();

            TriggerHandler triggerHandler = factory.getHandler(triggerType);
            if (triggerHandler == null) {
                throw new IllegalArgumentException("Unsupported Trigger type " + triggerType);
            }

            triggerHandler.deregister(trigger.getId());
            registeredTriggers.remove(triggerId);
        }
    }

    public void deRegisterAllTriggers() {
        for (Trigger trigger : registeredTriggers.values()) {
            deRegisterTrigger(trigger.getId());
        }
        log.info("All triggers have been de-registered");
    }
}
