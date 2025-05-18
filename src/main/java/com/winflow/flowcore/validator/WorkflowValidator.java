package com.winflow.flowcore.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winflow.flowcore.exception.WorkflowValidationException;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Component
public class WorkflowValidator {
    private final Schema schema;

    public WorkflowValidator() throws IOException {
        InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("workflow.schema.json");
        if (schemaStream == null) {
            throw new FileNotFoundException("Could not find workflow.schema.json");
        }

        JSONObject rawSchema = new JSONObject(new JSONTokener(schemaStream));
        this.schema = SchemaLoader.load(rawSchema);
    }

    public void validate(JsonNode workflowJson) throws WorkflowValidationException {
        try {
            JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(workflowJson));
            schema.validate(jsonObject);
        } catch (Exception e) {
            throw new WorkflowValidationException("Schema validation failed: " + e.getMessage());
        }
    }
}