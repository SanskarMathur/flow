package com.winflow.flowcore.trigger.impl;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.winflow.flowcore.core.WorkflowConstants;
import com.winflow.flowcore.core.model.Trigger;
import com.winflow.flowcore.core.model.Workflow;
import com.winflow.flowcore.engine.WorkflowExecutor;
import com.winflow.flowcore.repo.WorkflowRepository;
import com.winflow.flowcore.trigger.TriggerHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotkeyTriggerHandler implements TriggerHandler, NativeKeyListener {
    private final WorkflowRepository workflowRepository;
    private final Set<String> triggerRegistry;
    private final Map<String, String> hotkeyMap = new ConcurrentHashMap<>();  // Hotkey - TriggerId
    private final WorkflowExecutor executor;

    private boolean ctrlPressed = false;
    private boolean altPressed = false;
    private boolean shiftPressed = false;
    private boolean metaPressed = false;

    @Override
    public void register(Trigger trigger) {
        String triggerId = trigger.getId();
        String hotkeyCombo = (String) trigger.getConfig(WorkflowConstants.TRIGGER_HOTKEY);

        if (hotkeyCombo != null) {
            triggerRegistry.add(triggerId);
            hotkeyMap.put(hotkeyCombo, triggerId);

            log.info("Registered hotkey: {}", hotkeyCombo);
        } else {
            log.warn("Invalid Hotkey combo: {}", hotkeyCombo);
        }
    }

    @Override
    public void deregister(String triggerId) {
        triggerRegistry.remove(triggerId);
        hotkeyMap.entrySet().removeIf(entry -> entry.getValue().equals(triggerId));

        log.info("Deregistered triggerId: {}", triggerId);
    }

    @Override
    public void trigger(String triggerId) {
        if (triggerRegistry.contains(triggerId)) {
            Workflow workflow = workflowRepository.loadWorkflowByTriggerId(triggerId);
            log.info("Event: '{}' captured. Triggering workflow: '{}'", workflow.getTrigger().getType(), workflow.getMetadata().getName());
            executor.execute(workflow);
        }
    }

    @PostConstruct
    public void init() {
        try {
            // Disable JNativeHook Logging
            Logger log = Logger.getLogger(this.getClass().getName());
            log.setLevel(Level.WARNING);
            log.setUseParentHandlers(false);

            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);

            log.info("Global Hotkey Listener initialized successfully");
        } catch (NativeHookException e) {
            log.error("Failed to register global hotkey listener", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            GlobalScreen.removeNativeKeyListener(this);
            GlobalScreen.unregisterNativeHook();

            log.info("Global Hotkey Listener cleaned up successfully");
        } catch (NativeHookException e) {
            log.error("Failed to cleanup global hotkey listener", e);
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        updateModifierKeyStates(e, true);

        String currentCombo = buildHotkeyCombo(e);

        if (hotkeyMap.containsKey(currentCombo)) {
            trigger(hotkeyMap.get(currentCombo));
            log.debug("Hotkey combination '{}' detected", currentCombo);
        }
    }

    private String buildHotkeyCombo(NativeKeyEvent e) {
        StringBuilder combo = new StringBuilder();

        if (ctrlPressed) combo.append("CTRL+");
        if (altPressed) combo.append("ALT+");
        if (shiftPressed) combo.append("SHIFT+");
        if (metaPressed) combo.append("META+");

        if (!isModifierKey(e.getKeyCode())) {
            combo.append(getKeyName(e.getKeyCode()));
        }

        String comboString = combo.toString();
        return comboString.endsWith("+") ? comboString.substring(0, comboString.length() - 1) : comboString;
    }

    private String getKeyName(int keyCode) {
        // Function Keys
        if (keyCode >= NativeKeyEvent.VC_F1 && keyCode <= NativeKeyEvent.VC_F24) {
            return "F" + (keyCode - NativeKeyEvent.VC_F1 + 1);
        }

        // A-Z
        if (keyCode >= NativeKeyEvent.VC_A && keyCode <= NativeKeyEvent.VC_Z) {
            return String.valueOf((char) ('A' + keyCode - NativeKeyEvent.VC_A));
        }

        // 0-9
        if (keyCode >= NativeKeyEvent.VC_0 && keyCode <= NativeKeyEvent.VC_9) {
            return String.valueOf((char) ('0' + keyCode - NativeKeyEvent.VC_0));
        }

        return switch (keyCode) {
            case NativeKeyEvent.VC_SPACE -> "Space";
            case NativeKeyEvent.VC_ENTER -> "Enter";
            case NativeKeyEvent.VC_TAB -> "Tab";
            case NativeKeyEvent.VC_ESCAPE -> "Esc";
            case NativeKeyEvent.VC_BACKSPACE -> "Backspace";
            case NativeKeyEvent.VC_DELETE -> "Delete";
            case NativeKeyEvent.VC_INSERT -> "Insert";
            case NativeKeyEvent.VC_HOME -> "Home";
            case NativeKeyEvent.VC_END -> "End";
            case NativeKeyEvent.VC_PAGE_UP -> "PageUp";
            case NativeKeyEvent.VC_PAGE_DOWN -> "PageDown";
            case NativeKeyEvent.VC_UP -> "Up";
            case NativeKeyEvent.VC_DOWN -> "Down";
            case NativeKeyEvent.VC_LEFT -> "Left";
            case NativeKeyEvent.VC_RIGHT -> "Right";
            default -> NativeKeyEvent.getKeyText(keyCode);
        };
    }

    private boolean isModifierKey(int keyCode) {
        return keyCode == NativeKeyEvent.VC_CONTROL ||
               keyCode == NativeKeyEvent.VC_ALT ||
               keyCode == NativeKeyEvent.VC_SHIFT ||
               keyCode == NativeKeyEvent.VC_META;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        updateModifierKeyStates(e, false);
    }

    private void updateModifierKeyStates(NativeKeyEvent e, boolean state) {
        switch (e.getKeyCode()) {
            case NativeKeyEvent.VC_CONTROL -> ctrlPressed = state;
            case NativeKeyEvent.VC_ALT -> altPressed = state;
            case NativeKeyEvent.VC_SHIFT -> shiftPressed = state;
            case NativeKeyEvent.VC_META -> metaPressed = state;
        }
    }
}
