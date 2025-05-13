package com.winflow.flowcore.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winflow.flowcore.core.model.Workflow;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
@Slf4j
public class WorkflowLoaderService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Workflow loadWorkflow(String filePath) {
        try {
            JsonNode jsonNode = objectMapper.readTree(new FileInputStream(filePath));
            JSONObject jsonObject = new JSONObject(objectMapper.writeValueAsString(jsonNode));

            InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("workflow.schema.json");
            if (schemaStream == null) {
                throw new FileNotFoundException("Could not find workflow.schema.json");
            }

            JSONObject rawSchema = new JSONObject(new JSONTokener(schemaStream));
            Schema schema = SchemaLoader.load(rawSchema);

            schema.validate(jsonObject);

            return objectMapper.treeToValue(jsonNode, Workflow.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load and parse workflow: " + e.getMessage(), e);
        }
    }
}
