package com.winflow.flowcore.engine;

import com.winflow.flowcore.core.model.Trigger;
import com.winflow.flowcore.repo.WorkflowRepository;
import com.winflow.flowcore.trigger.TriggerDispatcher;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class WorkflowEngine {
    private final WorkflowRepository repository;
    private final TriggerDispatcher dispatcher;

    @PostConstruct
    public void init() {
        List<Trigger> triggers = repository.loadAllTriggers();
        for (Trigger trigger : triggers) {
            dispatcher.registerTrigger(trigger);
        }
    }

    @PreDestroy
    public void destroy() {
        dispatcher.deRegisterAllTriggers();
    }
}
