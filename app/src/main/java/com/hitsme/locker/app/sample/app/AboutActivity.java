/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.hitsme.locker.app.sample.app;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.sample.patternlock.PatternView;
import com.hitsme.locker.app.sample.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

//import butterknife.BindView;

public class AboutActivity extends ThemedAppCompatActivity {

    private static final List<PatternView.Cell> LOGO_PATTERN = new ArrayList<>();
    static {
        LOGO_PATTERN.add(PatternView.Cell.of(0, 1));
        LOGO_PATTERN.add(PatternView.Cell.of(1, 0));
        LOGO_PATTERN.add(PatternView.Cell.of(2, 1));
        LOGO_PATTERN.add(PatternView.Cell.of(1, 2));
        LOGO_PATTERN.add(PatternView.Cell.of(1, 1));
    }

    @InjectView(R.id.pattern_view)
    PatternView mPatternView=(PatternView) findViewById(R.id.pattern_view);
    @InjectView(R.id.version_text)
    TextView mVersionText=(TextView) findViewById(R.id.version_text);
    @InjectView(R.id.github_text)
    TextView mGitHubText=(TextView) findViewById(R.id.github_text);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppUtils.setActionBarDisplayUp(this);

        setContentView(R.layout.about_activity);
        ButterKnife.inject(this);

        mPatternView.setPattern(PatternView.DisplayMode.Animate, LOGO_PATTERN);
        String version = getString(R.string.about_version,
                AppUtils.getPackageInfo(this).versionName);
        mVersionText.setText(version);
        mGitHubText.setMovementMethod(LinkMovementMethod.getInstance());
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
}
