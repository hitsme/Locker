/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.hitsme.locker.app.sample.app;

import android.os.Bundle;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.sample.util.ThemeUtils;

//import com.rodrigo.lock.app.sample.app.PatternLockActivity.*;
public class MainActivity extends ThemedAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);

            setContentView(R.layout.main_activity);


    }
}