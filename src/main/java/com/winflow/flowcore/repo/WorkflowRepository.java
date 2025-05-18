package com.winflow.flowcore.repo;

import com.winflow.flowcore.core.model.Trigger;
import com.winflow.flowcore.core.model.Workflow;

import java.util.List;

public interface WorkflowRepository {
    List<Trigger> loadAllTriggers();

    Workflow loadWorkflowByTriggerId(String triggerId);
}
