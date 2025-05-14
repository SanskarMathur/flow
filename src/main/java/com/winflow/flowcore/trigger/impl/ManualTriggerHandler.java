package com.winflow.flowcore.trigger.impl;

import com.winflow.flowcore.core.model.Trigger;
import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
import com.winflow.flowcore.trigger.TriggerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ManualTriggerHandler implements TriggerHandler {
    @Override
    public void register(Workflow workflow, WorkflowExecutor executor) {
        trigger(workflow, executor);
    }

    @Override
    public void deregister(Workflow workflow, WorkflowExecutor executor) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void trigger(Workflow workflow, WorkflowExecutor executor) {
        log.info("Event: '{}' captured. Triggering workflow: '{}'", workflow.getTrigger().getType(), workflow.getMetadata().getName());
        executor.execute(workflow);
    }
}
