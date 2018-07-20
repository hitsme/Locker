package com.hitsme.locker.app.utils;

import com.hitsme.locker.app.util.schedulers.BaseSchedulerProvider;
import com.hitsme.locker.app.util.schedulers.SchedulerProvider;

/**
 * Created by 10093 on 2017/5/6.
 */

public class Injection {

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

}
