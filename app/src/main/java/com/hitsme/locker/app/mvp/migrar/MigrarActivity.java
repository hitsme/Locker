package com.hitsme.locker.app.mvp.migrar;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.migracion.MigrarUtilsDeprecated;
import com.hitsme.locker.app.utils.ActivityUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 10093 on 2017/4/26.
 */
public class MigrarActivity extends AppCompatActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.migrar_activity);
        ButterKnife.inject(this);
        // Set up the toolbar.
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        MigrarFragment addEditTaskFragment =
                (MigrarFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        MigrarUtilsDeprecated obje = (MigrarUtilsDeprecated) getIntent().getSerializableExtra(MigrarFragment.DATA_MIGRACION);;
        if (addEditTaskFragment == null) {
            addEditTaskFragment = MigrarFragment.newInstance();
            //actionBar.setTitle(R.string.z_add_task);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditTaskFragment, R.id.contentFrame);
        }
        new MigrarPresenter(
                obje,
                addEditTaskFragment,
                this.getApplicationContext());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }












}
