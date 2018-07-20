package com.hitsme.locker.app.util.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Created by 10093 on 2017/5/11.
 */
public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
