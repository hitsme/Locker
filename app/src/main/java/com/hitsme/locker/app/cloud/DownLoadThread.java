package com.hitsme.locker.app.cloud;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.URLUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 10093 on 2017/4/10.
 */

public class DownLoadThread extends Thread {
    private String downLoadUrl;
    private Context context;
    private String urlYun;
    private String fileName="null";
    private int downloadFileSize;
    private  int downloadedSize;
    public DownLoadThread(String downLoadUrl, Context context,String urlYun) {
        super();
        this.downLoadUrl = downLoadUrl;
        this.context = context;
        this.urlYun=urlYun;
    }
    public String getFileName()
    {
        return fileName;
    }
    public int getDownLoadFileSize()
    {
        return downloadFileSize;
    }
    public int getDownloadedSize()
    {
        return downloadedSize;
    }
    public void setDownloadFileSize(int downloadFileSize)
    {
        this.downloadFileSize=downloadFileSize;
    }

    public void sendIsCanDownload(Context context)
    {
        Looper.prepare();
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("你下载文件不是“密空间”.lock后缀的加密文件,请重新操作！");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
        Looper.loop();
        return;
    }

    @Override
    public void run() {
        try {

            FileOutputStream fos = null;
            HttpURLConnection conn = null;
            URL httpUrl = new URL(downLoadUrl);
            String cookies = CookieManager.getInstance().getCookie(downLoadUrl);
            conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setReadTimeout(30000);
            if (cookies != null && !cookies.isEmpty()) {
                conn.addRequestProperty("Cookie", cookies);


            }
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                String content_disposition=conn.getHeaderField("content-disposition");
                String header = conn.getHeaderField("Content-Type");
                setDownloadFileSize(conn.getContentLength());
                fileName =URLUtil.guessFileName(downLoadUrl,content_disposition,header);
                if (fileName.indexOf(".lock")>=0) {
                    File f = new File(Environment.getExternalStorageDirectory().getPath() + "/Lock/Vaults/" + fileName);
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ;
                    fos = new FileOutputStream(f);
                    byte[] b = new byte[1024];
                    int len = 0;
                    while ((len = is.read(b)) != -1) {
                        fos.write(b, 0, len);
                        downloadedSize+=len;
                    }

                    fos.flush();
                }
                else
                    sendIsCanDownload(context);
            }

        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
