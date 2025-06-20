package com.winflow.flowcore.trigger.impl;

import com.winflow.flowcore.core.WorkflowConstants;
import com.winflow.flowcore.core.model.Trigger;
import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
import com.winflow.flowcore.exception.RegisterTriggerException;
import com.winflow.flowcore.repo.WorkflowRepository;
import com.winflow.flowcore.trigger.TriggerHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
@AllArgsConstructor
public class CronTriggerHandler implements TriggerHandler {
    private final Set<String> triggerRegistry = new ConcurrentSkipListSet<>();
    private final ThreadPoolTaskScheduler scheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final WorkflowRepository workflowRepository;
    private final WorkflowExecutor executor;

    @Override
    public void register(Trigger trigger) {
        String cronExpression = (String) trigger.getConfig(WorkflowConstants.TRIGGER_CRON_EXPRESSION);
        if (cronExpression == null || cronExpression.isEmpty()) {
            throw new RegisterTriggerException("Missing cron expression in workflow", trigger.getId());
        }

        log.info("Scheduling trigger '{}' for cron '{}'", trigger.getId(), cronExpression);

        ScheduledFuture<?> future = scheduler.schedule(() -> {
            trigger(trigger.getId());
            register(trigger);
        }, new CronTrigger(cronExpression));

        scheduledTasks.put(trigger.getId(), future);
        triggerRegistry.add(trigger.getId());
    }

    @Override
    public void deregister(String triggerId) {
        if (scheduledTasks.containsKey(triggerId)) {
            ScheduledFuture<?> future = scheduledTasks.get(triggerId);
            future.cancel(true);

            scheduledTasks.remove(triggerId);
            triggerRegistry.remove(triggerId);
            log.info("Cancelled scheduled cron trigger: {}", triggerId);
        }
    }

    @Override
    public void trigger(String triggerId) {
        if (triggerRegistry.contains(triggerId)) {
            Workflow workflow = workflowRepository.loadWorkflowByTriggerId(triggerId);
            log.info("Event: '{}' captured. Triggering workflow: '{}'", workflow.getTrigger().getType(), workflow.getMetadata().getName());
            executor.execute(workflow);
        }
    }
}
