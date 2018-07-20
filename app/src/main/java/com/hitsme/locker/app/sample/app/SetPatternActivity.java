/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.hitsme.locker.app.sample.app;

import android.os.Bundle;
import android.view.MenuItem;

import com.hitsme.locker.app.sample.patternlock.PatternView;
import com.hitsme.locker.app.sample.util.AppUtils;
import com.hitsme.locker.app.sample.util.PatternLockUtils;
import com.hitsme.locker.app.sample.util.ThemeUtils;

import java.util.List;

public class SetPatternActivity extends com.hitsme.locker.app.sample.patternlock.SetPatternActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);

   //     AppUtils.setActionBarDisplayUp(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppUtils.navigateUp(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSetPattern(List<PatternView.Cell> pattern) {
        PatternLockUtils.setPattern(pattern, this);
    }
}
