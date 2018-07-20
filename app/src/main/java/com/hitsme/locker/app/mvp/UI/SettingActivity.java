package com.hitsme.locker.app.mvp.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.cloud.SelectCloudActivity;
import com.hitsme.locker.app.sample.app.ThemedAppCompatActivity;
import com.hitsme.locker.app.sample.util.ThemeUtils;
import com.hitsme.locker.app.security.finger.fingerprintidentify.aosp.FingerprintManagerCompat;

import static android.R.attr.button;
import static com.takisoft.fix.support.v7.preference.R.styleable.CompoundButton;

/**
 * Created by 10093 on 2017/5/12.
 */

public class SettingActivity extends ThemedAppCompatActivity implements android.widget.CompoundButton.OnCheckedChangeListener{
   private SwitchCompat mSwitchCompat;
    private SharedPreferences sp;
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.setting_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        mSwitchCompat = (SwitchCompat) findViewById(R.id.sc_switch_compat);
        mSwitchCompat.setOnCheckedChangeListener(this);

        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean isProtecting = sp.getBoolean("isProtected", false);//每次进来的时候读取保存的数据
        if (isProtecting) {

            mSwitchCompat.setChecked(true);
        }
//        mSwitchCompat.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//// TODO Auto-generated method stub
//                if (isChecked) {
//
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putBoolean("isProtected", true);
//                    editor.commit();//提交数据保存
//                } else {
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putBoolean("isProtected", false);
//                    editor.commit();//提交数据保存
//                }
//            }
//        });
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean checked) {
      //  mSwitchCompat.setChecked(false);
      //  Toast.makeText(SettingActivity.this,"该功能存在部分机型不适配，故暂停用！",Toast.LENGTH_SHORT).show();
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(this);
        if (fingerprintManager.isHardwareDetected()) {
            if (checked) {

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isProtected", true);
                editor.commit();//提交数据保存
            } else {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isProtected", false);
                editor.commit();//提交数据保存
            }


        }
        else {
            button.setChecked(false);
            Toast.makeText(SettingActivity.this, "你的手机没有指纹传感器！", Toast.LENGTH_SHORT).show();
        }

    }
//        Button Parttlock_bt=(Button)findViewById(R.id.partternlock_bt);
//        Parttlock_bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //startActivity(new Intent(SettingActivity.this,MainActivity.class));
//                Toast.makeText(SettingActivity.this, "此功能存在热进入黑屏Bug,暂未开放", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        Button apptheme_bt=(Button)findViewById(R.id.apptheme_bt);
//        apptheme_bt.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Toast.makeText(SettingActivity.this, "功能暂未开放", Toast.LENGTH_SHORT).show();
//            }
//        });
//        Button others_bt=(Button)findViewById(R.id.others_bt);
//        others_bt.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Toast.makeText(SettingActivity.this, "功能暂未开放", Toast.LENGTH_SHORT).show();
//            }
//        });





    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
