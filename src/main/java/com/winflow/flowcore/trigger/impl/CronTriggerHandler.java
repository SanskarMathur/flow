package com.winflow.flowcore.trigger.impl;

import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
import com.winflow.flowcore.trigger.TriggerHandler;
import com.winflow.flowcore.util.CronUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
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
            log.info("Scheduling workflow '{}' for cron '{}', next run at: '{}'", workflow.getMetadata().getName(), cronExpression, nextScheduleTime);

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
    public void deregister(Workflow workflow, WorkflowExecutor executor) {
        String triggerId = workflow.getTrigger().getId();

        if (scheduledTasks.containsKey(triggerId)) {
            ScheduledFuture<?> future = scheduledTasks.get(triggerId);
            future.cancel(true);
            scheduledTasks.remove(triggerId);
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void trigger(Workflow workflow, WorkflowExecutor executor) {
        log.info("Event: '{}' captured. Triggering workflow: '{}'", workflow.getTrigger().getType(), workflow.getMetadata().getName());
        executor.execute(workflow);
    }
}
