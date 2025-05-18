package com.winflow.flowcore.repo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winflow.flowcore.config.AppConfig;
import com.winflow.flowcore.core.model.Trigger;
import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.exception.WorkflowValidationException;
import com.winflow.flowcore.validator.WorkflowValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
@AllArgsConstructor
@Slf4j
public class FileSystemWorkflowRepository implements WorkflowRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AppConfig appConfig;
    private final Map<String, String> triggerFilePaths = new HashMap<>();
    private final WorkflowValidator workflowValidator;

    @Override
    public List<Trigger> loadAllTriggers() {
        List<Trigger> triggers = new ArrayList<>();

        File dir = new File(appConfig.workflowDirectory);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.getName().endsWith(".json")) {
                try {
                    JsonNode rootNode = objectMapper.readTree(file);

                    workflowValidator.validate(rootNode);

                    JsonNode triggerNode = rootNode.path("trigger");
                    Trigger trigger = objectMapper.treeToValue(triggerNode, Trigger.class);

                    triggers.add(trigger);
                    triggerFilePaths.put(trigger.getId(), file.getAbsolutePath());
                } catch (IOException e) {
                    log.error("Could not read file: {}: {}", file.getAbsolutePath(), e.getMessage());
                    throw new WorkflowValidationException(e.getMessage());
                }
            }
        }
        return triggers;
    }

    @Override
    public Workflow loadWorkflowByTriggerId(String triggerId) {
        File file = new File(triggerFilePaths.get(triggerId));

        try {
            Workflow workflow = objectMapper.readValue(file, Workflow.class);
            if (workflow.getTrigger().getId().equals(triggerId)) {
                return workflow;
            }
        } catch (IOException e) {
            log.error("Could not read file: {} to load workflow for trigger: {}: {}", file.getAbsolutePath(), triggerId, e.getMessage());
            throw new WorkflowValidationException(e.getMessage());
        }

        throw new WorkflowValidationException("No workflow found for triggerId: " + triggerId);
    }
}
