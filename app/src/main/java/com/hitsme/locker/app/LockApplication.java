package com.hitsme.locker.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by 10093 on 2017/5/8.
 */

public class LockApplication  extends Application {

    // private static final String TAG = Tags.getTag(SpringApplication.class);
    private static Context context;

    //TODO: configure AndroLog
    @Override
    public void onCreate() {
        super.onCreate();
        LockApplication.context = getApplicationContext();


    }



    public static Context getAppContext() {
        return LockApplication.context;
    }


}
