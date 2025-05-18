package com.winflow.flowcore.engine.handler;

import com.winflow.flowcore.core.enums.JobTypeEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JobHandlerStrategy {
    private CommandJobHandler commandJobHandler;
    private FileJobHandler fileJobHandler;
    private NotificationJobHandler notificationJobHandler;

    public JobHandler getJobExecutorHandler(JobTypeEnum jobType) {
        if (jobType == null) {
            throw new IllegalArgumentException("Job Type cannot be null");
        }
        return switch (jobType) {
            case COMMAND -> commandJobHandler;
            case SCRIPT -> fileJobHandler;
            case NOTIFY -> notificationJobHandler;
            default -> null;
        };
    }
}
