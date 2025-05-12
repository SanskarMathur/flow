package com.winflow.flowcore.trigger;

import com.winflow.flowcore.core.enums.TriggerTypeEnum;
import com.winflow.flowcore.core.model.Trigger;
import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Dispatches triggers to corresponding trigger handlers
 * Also prevents duplicate triggers registration
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TriggerDispatcher {
    private TriggerHandlerFactory factory;
    private WorkflowExecutor executor;

    private final Map<String, Trigger> registeredTriggers = new HashMap<>();

    public TriggerDispatcher(TriggerHandlerFactory factory, WorkflowExecutor executor) {
        this.factory = factory;
        this.executor = executor;
    }

    public void registerTrigger(Workflow workflow) {
        if (registeredTriggers.containsKey(workflow.getTrigger().getId())) {
            return;
        }

        TriggerTypeEnum triggerType = workflow.getTrigger().getType();
        TriggerHandler triggerHandler = factory.getHandler(triggerType);

        if (triggerHandler == null) {
            throw new IllegalArgumentException("Unsupported Trigger type " + triggerType);
        }

        triggerHandler.initialize(workflow, executor);
        registeredTriggers.put(workflow.getTrigger().getId(), workflow.getTrigger());
    }

    public void triggerNow(String triggerId) {
        Trigger trigger = registeredTriggers.get(triggerId);
        if (trigger == null) {
            log.error("Trigger with id {} not found", triggerId);
            return;
        }

        TriggerHandler triggerHandler = factory.getHandler(trigger.getType());
        triggerHandler.trigger();
    }
}
