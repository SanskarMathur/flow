package com.winflow.flowcore.engine.handler;

import com.winflow.flowcore.core.model.Job;
import com.winflow.flowcore.core.model.JobExecutionResult;
import com.winflow.flowcore.core.model.ExecutionContext;
import com.winflow.flowcore.exception.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class FileJobHandler implements JobHandler {
    @Override
    public JobExecutionResult execute(Job job, ExecutionContext context) throws JobExecutionException {
        return null;
    }

    @Override
    public boolean validateExecution(Job job) {
        return false;
    }

    @Override
    public boolean stopExecution(String jobId) {
        return false;
    }
}
