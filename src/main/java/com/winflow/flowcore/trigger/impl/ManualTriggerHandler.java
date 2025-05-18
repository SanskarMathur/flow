package com.winflow.flowcore.trigger.impl;

import com.winflow.flowcore.core.model.Trigger;
import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
import com.winflow.flowcore.repo.WorkflowRepository;
import com.winflow.flowcore.trigger.TriggerHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
@Slf4j
@AllArgsConstructor
public class ManualTriggerHandler implements TriggerHandler {
    private final Set<String> triggerRegistry = new ConcurrentSkipListSet<>();
    private final WorkflowRepository workflowRepository;
    private final WorkflowExecutor executor;

    @Override
    public void register(Trigger trigger) {
        triggerRegistry.add(trigger.getId());
    }

    @Override
    public void deregister(String triggerId) {
        triggerRegistry.remove(triggerId);
    }

    @Override
    public void trigger(String triggerId) {
        if (triggerRegistry.contains(triggerId)) {
            Workflow workflow = workflowRepository.loadWorkflowByTriggerId(triggerId);
            executor.executeSequentially(workflow);
        }
    }
}
