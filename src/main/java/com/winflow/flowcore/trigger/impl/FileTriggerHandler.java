package com.winflow.flowcore.trigger.impl;

import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
import com.winflow.flowcore.trigger.TriggerHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class FileTriggerHandler implements TriggerHandler {
    private final Set<String> workflowRegistry = new ConcurrentSkipListSet<>();

    @Override
    public void register(Workflow workflow, WorkflowExecutor executor) {
        String watchFilePath = workflow.getTrigger().getTriggerFilePath();

        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deregister(Workflow workflow) {

    }

    @Override
    public void trigger(Workflow workflow, WorkflowExecutor executor) {

    }
}
