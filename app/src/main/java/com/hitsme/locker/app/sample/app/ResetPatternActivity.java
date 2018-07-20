/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.hitsme.locker.app.sample.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.sample.util.ToastUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ResetPatternActivity extends ThemedAppCompatActivity {

    @InjectView(R.id.ok_button) Button mOkButton;
    @InjectView(R.id.cancel_button)
    Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reset_pattern_activity);
        ButterKnife.inject(this);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // PatternLockUtils.clearPattern(ResetPatternActivity.this);
                ToastUtils.show(R.string.pattern_reset, ResetPatternActivity.this);
                finish();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
