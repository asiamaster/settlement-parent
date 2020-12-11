package com.dili.settlement.task;

import com.dili.settlement.config.CallbackConfiguration;

public abstract class QueueTask {

    protected CallbackConfiguration callbackConfiguration;

    public QueueTask(CallbackConfiguration callbackConfiguration) {
        this.callbackConfiguration = callbackConfiguration;
    }
}
