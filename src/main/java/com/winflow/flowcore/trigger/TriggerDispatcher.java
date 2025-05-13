package com.winflow.flowcore.trigger;

import com.winflow.flowcore.core.enums.TriggerTypeEnum;
import com.winflow.flowcore.core.model.Trigger;
import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
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


    public void registerTrigger(Workflow workflow) {
        if (registeredTriggers.containsKey(workflow.getTrigger().getId())) {
            return;
        }

        TriggerTypeEnum triggerType = workflow.getTrigger().getType();
        TriggerHandler triggerHandler = factory.getHandler(triggerType);

        if (triggerHandler == null) {
            throw new IllegalArgumentException("Unsupported Trigger type " + triggerType);
        }

        triggerHandler.register(workflow, executor);
        registeredTriggers.put(workflow.getTrigger().getId(), workflow.getTrigger());
    }
}
