package com.winflow.flowcore.trigger;

import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;

public interface TriggerHandler {
    void register(Workflow workflow, WorkflowExecutor executor);

    void initialize();

    void trigger(Workflow workflow, WorkflowExecutor executor);
}
