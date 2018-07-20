package com.hitsme.locker.app.robot.robot;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.hitsme.locker.app.R;
import com.hitsme.locker.app.robot.robot.util.Aes;
import com.hitsme.locker.app.robot.robot.util.Md5;
import com.hitsme.locker.app.robot.robot.util.PostServer;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String secret = "dec6594963aaf6f3";
    //图灵网站上的apiKey
    private String apiKey = "61230e0be2b34fb4805b564ef99321d2";
    private ListviewAdapter adapter = null;
    private ListView listview;
    //private Button btn_left;
    private EditText et_meg;
    private Button btn_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.robot_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        initView();

        adapter = new ListviewAdapter(this);
        listview.setAdapter(adapter);
        adapter.addDataToAdapter(new MsgInfo("你好，我是小密！有什么不懂的，可以问我喔。", null));
        adapter.notifyDataSetChanged();


    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            adapter.addDataToAdapter(new MsgInfo(val, null));
            adapter.notifyDataSetChanged();
            listview.smoothScrollToPosition(listview.getCount() -1);
            // TODO
            // UI界面的更新等相关操作
        }
    };

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
       // btn_left = (Button) findViewById(R.id.btn_left);
        et_meg = (EditText) findViewById(R.id.et_meg);
        btn_right = (Button) findViewById(R.id.btn_right);

        //btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String msg = et_meg.getText().toString().trim();

        switch (v.getId()) {
//            case R.id.btn_left:
//                adapter.addDataToAdapter(new MsgInfo(msg,null));
//                adapter.notifyDataSetChanged();
//                break;
            case R.id.btn_right:
                adapter.addDataToAdapter(new MsgInfo(null, msg));
                adapter.notifyDataSetChanged();
                if(isNetworkAvailable(this)) {
                    new Thread(runnable).start();
                }
                    listview.smoothScrollToPosition(listview.getCount() + 10);

                break;
        }



        et_meg.setText("");

    }

//    public void startTalk() {
//        msg_T = et_meg.getText().toString().trim();
//        adapter.addDataToAdapter(new MsgInfo(null, msg_T));
//        adapter.notifyDataSetChanged();
         Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
           String msg_T = et_meg.getText().toString().trim();
                String data_R = "{\"key\":\"" + apiKey + "\",\"info\":\"" + msg_T + "\"}";
                //获取时间戳

                String timestamp = String.valueOf(System.currentTimeMillis());

                //生成密钥
                String keyParam = secret + timestamp + apiKey;
                String key = Md5.MD5(keyParam);

                //加密
                Aes mc = new Aes(key);
                data_R = mc.encrypt(data_R);
                //封装请求参数
                JSONObject json = new JSONObject();
                json.put("key", apiKey);
                json.put("timestamp", timestamp);
                json.put("data", data_R);
                //请求图灵api
                String result = PostServer.SendPost(json.toString(), "http://www.tuling123.com/openapi/api");
                String msg_L=result.substring(result.indexOf("text\":\"")+7,result.length()-2);

                data.putString("value", msg_L);
                msg.setData(data);
                handler.sendMessage(msg);
            }


        };
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {

                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    }
//}
