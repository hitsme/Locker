/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.hitsme.locker.app.sample.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.sample.preference.ClearPatternPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers;

public class PatternLockFragment extends PreferenceFragmentCompatDividers {

    public static PatternLockFragment newInstance() {
        return new PatternLockFragment();
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_pattern_lock);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (!ClearPatternPreference.onDisplayPreferenceDialog(this, preference)) {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
