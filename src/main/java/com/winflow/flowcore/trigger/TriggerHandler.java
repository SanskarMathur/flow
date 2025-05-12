package com.winflow.flowcore.trigger;

import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;

public interface TriggerHandler {
    void initialize(Workflow workflow, WorkflowExecutor executor);

    void trigger();
}
