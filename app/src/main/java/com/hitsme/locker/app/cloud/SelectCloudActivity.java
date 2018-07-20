package com.hitsme.locker.app.cloud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.sample.util.ThemeUtils;

/**
 * Created by 10093 on 2017/5/15.
 */

public class SelectCloudActivity extends AppCompatActivity {

    String yunUrlEdit=null;
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.applyTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_cloud_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        Button baiduyun_bt = (Button) findViewById(R.id.baiduyun_bt);
        final Intent intentYun=new Intent(this,webActivity.class);
        final Bundle bundleYun=new Bundle();
        baiduyun_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundleYun.putString("url","http://pan.baidu.com/");
                intentYun.putExtras(bundleYun);
                startActivity(intentYun);

            }
        });

        Button weiyun_bt = (Button) findViewById(R.id.weiyun_bt);
        weiyun_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundleYun.putString("url","http://www.22kb.club/");
                intentYun.putExtras(bundleYun);
                startActivity(intentYun);
            }
        });
        Button go_bt=(Button)findViewById(R.id.go_bt);
        final EditText yun_edit=(EditText) findViewById(R.id.edit_cloud);
        go_bt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
               yunUrlEdit=yun_edit.getText().toString();
               if( isCurrentUrl(yunUrlEdit)) {
                   bundleYun.putString("url", "http://" + yunUrlEdit + "/");
                   intentYun.putExtras(bundleYun);
                   startActivity(intentYun);
               }
               else
                   Toast.makeText(SelectCloudActivity.this,"网址输入有误！",Toast.LENGTH_SHORT).show();
            }


        });


    }

    public boolean isCurrentUrl(String yunurl)
    {
        if (Patterns.WEB_URL.matcher(yunurl).matches()) {
           return true;
        } else{

          return false;
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
