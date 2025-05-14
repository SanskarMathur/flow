package com.winflow.flowcore.trigger.impl;

import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
import com.winflow.flowcore.trigger.TriggerHandler;
import org.springframework.stereotype.Component;

@Component
public class FileTriggerHandler implements TriggerHandler {
    @Override
    public void register(Workflow workflow, WorkflowExecutor executor) {

    }

    @Override
    public void deregister(Workflow workflow, WorkflowExecutor executor) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void trigger(Workflow workflow, WorkflowExecutor executor) {

    }
}
