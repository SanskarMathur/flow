package com.winflow.flowcore.trigger.impl;

import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
import com.winflow.flowcore.trigger.TriggerHandler;
import com.winflow.flowcore.util.CronUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class CronTriggerHandler implements TriggerHandler {
    private final ThreadPoolTaskScheduler scheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final CronUtil cronUtil;

    @Autowired
    public CronTriggerHandler(ThreadPoolTaskScheduler scheduler, CronUtil cronUtil) {
        this.scheduler = scheduler;
        this.cronUtil = cronUtil;
    }

    @Override
    public void register(Workflow workflow, WorkflowExecutor executor) {
        String cronExpression = workflow.getTrigger().getTriggerCronExpression();
        if (cronExpression == null || cronExpression.isEmpty()) {
            throw new IllegalArgumentException("Missing cron expression in workflow");
        }

        try {
            Date nextScheduleTime = cronUtil.getNextSchedulingTime(cronExpression);

            ScheduledFuture<?> future = scheduler.schedule(() -> {
                trigger(workflow, executor);
                register(workflow, executor);
            }, nextScheduleTime.toInstant());

            scheduledTasks.put(workflow.getTrigger().getId(), future);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid cron expression: " + cronExpression, e);
        }

    }

    @Override
    public void initialize() {

    }

    @Override
    public void trigger(Workflow workflow, WorkflowExecutor executor) {
        System.out.println("Triggering workflow: " + workflow.getTrigger().getId());
        executor.execute(workflow);
    }
}
