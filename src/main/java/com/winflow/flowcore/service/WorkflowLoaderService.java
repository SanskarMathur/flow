package com.winflow.flowcore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.exception.WorkflowValidationException;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;

@Service
@Slf4j
public class WorkflowLoaderService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Workflow loadWorkflow(String filePath) {
        try {
            InputStream workflowInput = new FileInputStream(filePath);
            JSONObject workflowObj = objectMapper.readValue(workflowInput, JSONObject.class);

            InputStream validatorSchemaInput = getClass().getClassLoader().getResourceAsStream("workflow.schema.json");
            if (validatorSchemaInput == null) {
                throw new WorkflowValidationException("Could not find workflow schema");
            }
            JSONObject validatorSchemaObj = objectMapper.readValue(validatorSchemaInput, JSONObject.class);
            Schema schema = SchemaLoader.load(validatorSchemaObj);

            schema.validate(workflowObj);

            return objectMapper.readValue(workflowObj.toString(), Workflow.class);
        } catch (Exception e) {
            log.error("Failed to load/validate workflow: {}", e.getMessage());
            throw new WorkflowValidationException("Failed to load/validate workflow: " + e.getMessage());
        }
    }
}
