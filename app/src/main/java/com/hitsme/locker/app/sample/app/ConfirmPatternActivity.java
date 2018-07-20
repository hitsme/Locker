/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.hitsme.locker.app.sample.app;

import android.content.Intent;
import android.os.Bundle;

import com.hitsme.locker.app.sample.patternlock.PatternView;
import com.hitsme.locker.app.sample.util.PatternLockUtils;
import com.hitsme.locker.app.sample.util.PreferenceContract;
import com.hitsme.locker.app.sample.util.PreferenceUtils;
import com.hitsme.locker.app.sample.util.ThemeUtils;

import java.util.List;

public class ConfirmPatternActivity extends com.hitsme.locker.app.sample.patternlock.ConfirmPatternActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isStealthModeEnabled() {
        return !PreferenceUtils.getBoolean(PreferenceContract.KEY_PATTERN_VISIBLE,
                PreferenceContract.DEFAULT_PATTERN_VISIBLE, this);
    }

    @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        return PatternLockUtils.isPatternCorrect(pattern, this);
    }

    @Override
    protected void onForgotPassword() {

        startActivity(new Intent(this, ResetPatternActivity.class));

        // Finish with RESULT_FORGOT_PASSWORD.
        super.onForgotPassword();
    }
}
