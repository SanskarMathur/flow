package com.winflow.flowcore.trigger;

import com.winflow.flowcore.core.model.Trigger;

public interface TriggerHandler {
    void register(Trigger trigger);

    void deregister(String triggerId);

    void trigger(String triggerId);
}
