package com.winflow.flowcore.engine;

import com.winflow.flowcore.core.model.Job;
import com.winflow.flowcore.core.model.Workflow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class WorkflowExecutor {
    public void execute(Workflow workflow) {
        List<Job> jobs = workflow.getJobs();

        for (Job job : jobs) {
            log.info("{}: {}", job.getId(), job.getCommand());
        }
    }
}
