package com.dili.settlement.task;

import com.dili.settlement.config.CallbackConfiguration;

import java.util.concurrent.ExecutorService;

public abstract class DamonTask {

    protected CallbackConfiguration callbackConfiguration;
    protected ExecutorService executorService;

    public DamonTask(CallbackConfiguration callbackConfiguration, ExecutorService executorService) {
        this.callbackConfiguration = callbackConfiguration;
        this.executorService = executorService;
    }
}
