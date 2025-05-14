package com.winflow.flowcore.trigger.impl;

import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
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

    @Override
    public void register(Workflow workflow, WorkflowExecutor executor) {
        triggerRegistry.add(workflow.getTrigger().getId());
    }

    @Override
    public void deregister(Workflow workflow) {
        triggerRegistry.remove(workflow.getTrigger().getId());
    }

    @Override
    public void trigger(Workflow workflow, WorkflowExecutor executor) {
        if (triggerRegistry.contains(workflow.getTrigger().getId())) {
            executor.execute(workflow);
        }
    }
}
