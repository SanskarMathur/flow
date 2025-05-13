package com.winflow.flowcore.bootstrap;

import com.winflow.flowcore.config.AppConfig;
import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.service.WorkflowLoaderService;
import com.winflow.flowcore.trigger.TriggerDispatcher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
@AllArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final WorkflowLoaderService workflowLoaderService;
    private AppConfig config;
    private TriggerDispatcher triggerDispatcher;

    @Override
    public void run(String... args) {
        File workflowDir = new File(config.workflowDirectory);
        if (!workflowDir.exists() || !workflowDir.isDirectory()) {
            throw new RuntimeException("Workflow directory not found: " + config.workflowDirectory);
        }

        File[] workflowFiles = workflowDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (workflowFiles == null || workflowFiles.length == 0) {
            log.warn("No workflow files found in {}", config.workflowDirectory);
            return;
        }

        for (File workflowFile : workflowFiles) {
            try {
                Workflow workflow = workflowLoaderService.loadWorkflow(workflowFile.getAbsolutePath());
                triggerDispatcher.registerTrigger(workflow);
                log.info("Registered trigger for: {}", workflow.getMetadata().getName());
            } catch (Exception e) {
                log.error("Failed to load workflow: {} : {}", workflowFile.getName(), e.getMessage());
            }
        }
    }
}
