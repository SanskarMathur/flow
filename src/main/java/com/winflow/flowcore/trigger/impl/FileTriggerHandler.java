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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
@AllArgsConstructor
public class FileTriggerHandler implements TriggerHandler {
    private final Set<String> triggerRegistry = new ConcurrentSkipListSet<>();
    private final Map<String, WatchService> watchServiceRegistry = new ConcurrentHashMap<>();
    private final Map<String, Path> watchPathRegistry = new ConcurrentHashMap<>();
    private final Map<String, Thread> watchThreadRegistry = new ConcurrentHashMap<>();
    private final Map<String, Boolean> runningMap = new ConcurrentHashMap<>();
    private final WorkflowRepository workflowRepository;
    private final WorkflowExecutor executor;

    @Override
    public void register(Trigger trigger) {
        if (triggerRegistry.contains(trigger.getId())) return;

        Path watchFilePath = Paths.get((String) trigger.getConfig(WorkflowConstants.TRIGGER_FILE_PATH));
        boolean isDirectory = Files.isDirectory(watchFilePath);

        // If it's a file, watch its parent directory
        if (!isDirectory) {
            Path parentPath = watchFilePath.getParent();
            if (parentPath == null) {
                parentPath = Paths.get(".");    // Fallback to current directory (if no parent exists) TODO: Check this logic
            }
            watchFilePath = parentPath;
        }

        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            watchFilePath.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY
            );

            triggerRegistry.add(trigger.getId());
            watchServiceRegistry.put(trigger.getId(), watchService);
            watchPathRegistry.put((String) trigger.getConfig(WorkflowConstants.TRIGGER_FILE_PATH), watchFilePath); // Required because files map to their parent directories

            Thread thread = new Thread(() -> eventListener(trigger.getId()));
            thread.setDaemon(true);
            thread.setName("file-watcher-" + trigger.getId());
            thread.start();
            watchThreadRegistry.put(trigger.getId(), thread);
        } catch (IOException e) {
            throw new RegisterTriggerException(e.getMessage(), trigger.getId());
        }

    }

    private void eventListener(String triggerId) {
        WatchService watchService = watchServiceRegistry.get(triggerId);
        Path watchFilePath = watchPathRegistry.get(triggerId);
        boolean isDirectory = Files.isDirectory(watchFilePath);

        try {
            while (Boolean.TRUE.equals(runningMap.getOrDefault(triggerId, false))) {
                WatchKey watchKey = watchService.take();
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    // A special event to indicate that events may have been lost or discarded
                    if (kind == StandardWatchEventKinds.OVERFLOW) continue;

                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path path = pathEvent.context();

                    Path parentPath = isDirectory ? watchFilePath : watchFilePath.getParent();
                    if (parentPath == null) {
                        parentPath = Paths.get(".");
                    }
                    Path resolvedPath = parentPath.resolve(path);

                    boolean shouldTrigger = isDirectory || resolvedPath.equals(watchFilePath);
                    if (shouldTrigger) {
                        trigger(triggerId);
                    }
                }

                boolean isValid = watchKey.reset();
                if (!isValid) {
                    log.error("Watch key is no longer valid. Directory may be inaccessible: {}", watchFilePath);
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deregister(String triggerId) {

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
