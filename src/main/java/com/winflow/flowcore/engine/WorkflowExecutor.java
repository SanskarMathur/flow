package com.winflow.flowcore.engine;

import com.winflow.flowcore.core.model.*;
import com.winflow.flowcore.engine.handler.JobHandler;
import com.winflow.flowcore.engine.handler.JobHandlerStrategy;
import com.winflow.flowcore.exception.JobExecutionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@AllArgsConstructor
public class WorkflowExecutor {
    private final JobHandlerStrategy jobHandlerStrategy;

    public void execute(Workflow workflow) {
        String executionStrategy = workflow.getExecution().getConcurrency();

        if (executionStrategy.equals("sequential")) {
            WorkflowExecutionResult executionResult = executeSequentially(workflow);
        } else if (executionStrategy.equals("parallel")) {
            CompletableFuture<WorkflowExecutionResult> executionResult = executeParallel(workflow);
        }
    }

    private CompletableFuture<WorkflowExecutionResult> executeParallel(Workflow workflow) {
        // TODO: To implement
        return null;
    }

    public WorkflowExecutionResult executeSequentially(Workflow workflow) {
        String workflowId = workflow.getMetadata().getWorkflowId();
        List<Job> jobs = workflow.getJobs();

        ExecutionContext executionContext = new ExecutionContext(workflowId);
        Map<String, JobExecutionResult> results = new HashMap<>();
        boolean success = true;

        try {
            for (Job job : jobs) {
                JobExecutionResult result = executeJob(job, executionContext);
                results.put(job.getId(), result);

                if (!result.isSuccess()) {
                    success = false;
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Workflow execution failed: {}", e.getMessage());
            success = false;
        }


        log.info("Workflow execution successful: {}", workflowId);
        return WorkflowExecutionResult.builder()
                .executionId(executionContext.getExecutionId())
                .workflowId(workflowId)
                .jobResults(results)
                .successful(success)
                .build();
    }

    private JobExecutionResult executeJob(Job job, ExecutionContext executionContext) {
        try {
            JobHandler handler = jobHandlerStrategy.getJobExecutorHandler(job.getType());

            if (handler == null) throw new JobExecutionException(job.getId(), "Unsupported Job type: " + job.getType());
            if (!handler.validateExecution(job)) throw new JobExecutionException(job.getId(), "Job validation failed");

            return handler.execute(job, executionContext);
        } catch (Exception e) {
            throw new JobExecutionException(job.getId(), e.getMessage());
        }
    }
}
