package com.hitsme.locker.app.mvp.preguntasFrecuentes;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.hitsme.locker.app.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 10093 on 2017/5/1.
 */
public class PreguntasFrecuentes extends AppCompatActivity {

    @InjectView(R.id.r0)
    TextView r0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preguntas_frecuentes_activity);
        ButterKnife.inject(this);


        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        r0.setMovementMethod(LinkMovementMethod.getInstance());
        r0.setText(Html.fromHtml(getString(R.string.r00)));

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
