package com.hitsme.locker.app.robot.robot;

import com.alibaba.fastjson.JSONObject;
import com.hitsme.locker.app.robot.robot.util.Aes;
import com.hitsme.locker.app.robot.robot.util.Md5;
import com.hitsme.locker.app.robot.robot.util.PostServer;


/**
 * Created by 10093 on 2017/6/10.
 */

public class TuringBB extends Thread {
    private String msg;
    private String secret = "dec6594963aaf6f3";
    //图灵网站上的apiKey
    private String apiKey = "61230e0be2b34fb4805b564ef99321d2";
    TuringBB(String msg){
        this.msg=msg;
    }
    @Override
    public void run() {
        super.run();
        String data = "{\"key\":\""+apiKey+"\",\"info\":\""+msg+"\"}";
        //获取时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());

        //生成密钥
        String keyParam = secret+timestamp+apiKey;
        String key = Md5.MD5(keyParam);

        //加密
        Aes mc = new Aes(key);
        data = mc.encrypt(data);
        //封装请求参数
        JSONObject json = new JSONObject();
        json.put("key", apiKey);
        json.put("timestamp", timestamp);
        json.put("data", data);
        //请求图灵api
        String result = PostServer.SendPost(json.toString(), "http://www.tuling123.com/openapi/api");
        //String msg_L=result.substring(result.indexOf("text\":\"")+7,result.length()-2);

    }
}
