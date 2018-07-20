/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.hitsme.locker.app.sample.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.sample.util.PreferenceContract;
import com.hitsme.locker.app.sample.util.PreferenceUtils;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers;

public class MainFragment extends PreferenceFragmentCompatDividers
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_main);
    }

    @Override
    public void onResume() {
        super.onResume();

        PreferenceUtils.getPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        PreferenceUtils.getPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PreferenceContract.KEY_THEME:
                getActivity().recreate();
        }
    }
}
