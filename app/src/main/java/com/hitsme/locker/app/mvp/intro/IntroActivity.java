package com.hitsme.locker.app.mvp.intro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hitsme.locker.app.Constants;
import com.hitsme.locker.app.R;
import com.hitsme.locker.app.data.source.Preferences;
import com.hitsme.locker.app.mvp.listVaults.VaultsActivity;
import com.hitsme.locker.app.sample.app.ConfirmPatternActivity;
import com.hitsme.locker.app.sample.util.PatternLockUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 10093 on 2017/4/3.
 */
public class IntroActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if( !PatternLockUtils.hasPattern(this)) {
            if (!Preferences.getPreference(Constants.Preferences.PREFERENCE_PRIMERA_VEZ_EN_APP, true)
                    && isStoragePermissionGranted()) {
                startNextActivity();
            }


                setContentView(R.layout.intro_activity);
                ButterKnife.inject(this);


     //   }
        //else
            //startActivity(new Intent(IntroActivity.this,ConfirmPatternActivity.class));



    }

    @OnClick(R.id.fab_done)
    public void clickEnNext(){
        if (isStoragePermissionGranted()){
            startNextActivity();
        }else{
            askPermission();
        }
    }

    public void startNextActivity(){
        Preferences.savePreference(Constants.Preferences.PREFERENCE_PRIMERA_VEZ_EN_APP, false);
        Intent i = new Intent(this, VaultsActivity.class);
        startActivity(i);
        finish();
    }


    public void askPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(IntroActivity.class.getName(),"Permission is granted");
                return true;
            } else {
                Log.v(IntroActivity.class.getName(),"Permission is revoked");
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(IntroActivity.class.getName(),"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(IntroActivity.class.getName(),"Permission: "+permissions[0]+ "was "+grantResults[0]);
            startNextActivity();
        }
    }




}
