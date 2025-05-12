package com.winflow.flowcore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${workflow.directory.path:src/main/resources/workflows/}")
    public String workflowDirectory;
}
