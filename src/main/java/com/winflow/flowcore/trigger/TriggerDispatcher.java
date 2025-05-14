package com.winflow.flowcore.trigger;

import com.winflow.flowcore.core.enums.TriggerTypeEnum;
import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

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

    private final Set<String> registeredTriggers = new ConcurrentSkipListSet<>();

    public void registerTrigger(Workflow workflow) {
        if (registeredTriggers.contains(workflow.getTrigger().getId())) {
            return;
        }

        TriggerTypeEnum triggerType = workflow.getTrigger().getType();
        TriggerHandler triggerHandler = factory.getHandler(triggerType);
        if (triggerHandler == null) {
            throw new IllegalArgumentException("Unsupported Trigger type " + triggerType);
        }

        triggerHandler.register(workflow, executor);
        registeredTriggers.add(workflow.getTrigger().getId());
    }
}
