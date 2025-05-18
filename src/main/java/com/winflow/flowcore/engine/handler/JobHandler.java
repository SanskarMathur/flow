package com.winflow.flowcore.engine.handler;

import com.winflow.flowcore.core.model.ExecutionContext;
import com.winflow.flowcore.core.model.Job;
import com.winflow.flowcore.core.model.JobExecutionResult;
import com.winflow.flowcore.exception.JobExecutionException;

public interface JobHandler {
    /**
     * Execute a job and return the execution result
     *
     * @param job     The job to execute
     * @param context Execution context containing runtime information and variables
     * @return The execution result
     * @throws JobExecutionException If execution fails
     */
    JobExecutionResult execute(Job job, ExecutionContext context) throws JobExecutionException;

    /**
     * Validates if the job can be executed with the given parameters
     *
     * @param job Job to validate
     * @return true if valid, false otherwise
     */
    boolean validateExecution(Job job);

    /**
     * Attempts to stop a running job if supported
     *
     * @param jobId ID of the job to stop
     * @return true if successfully stopped, false otherwise
     */
    boolean stopExecution(String jobId);
}
