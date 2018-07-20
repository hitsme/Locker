package com.hitsme.locker.app.cloud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hitsme.locker.app.R;

/**
 * Created by 10093 on 2017/5/13.
 */

public class webActivity extends AppCompatActivity {

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private  String  urlYun;
    ProgressDialog mProgressDialog;
    TextView tv;
    int downLoadFileSize;
    String fileEx,fileNa,filename;
    int fileSize;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.web_cloud_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        final WebView web = (WebView) findViewById(R.id.webView);
        Bundle bundleUrl=this.getIntent().getExtras();
        urlYun=bundleUrl.getString("url");
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setDisplayZoomControls(false);
        if(!urlYun.equals("http://www.22kb.club/"))
        web.getSettings() .setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        web.loadUrl(urlYun);
        web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


        });


        web.setWebChromeClient(new WebChromeClient() {


            public void openFileChooser(ValueCallback<Uri> uploadMsg) {


                uploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                webActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
            }


            public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                // Log.d(TAG, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
                uploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                webActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILE_CHOOSER_RESULT_CODE);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                //Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                uploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                webActivity.this.startActivityForResult( Intent.createChooser( i, "File Browser" ), webActivity.FILE_CHOOSER_RESULT_CODE );
            }
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL =filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                webActivity.this.startActivityForResult( Intent.createChooser( i, "File Browser" ), webActivity.FILE_CHOOSER_RESULT_CODE );
                return true;
            }
        });



        Button back_bt=(Button)findViewById(R.id.go_back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(web.canGoBack())
                web.goBack();
            }
        });
        Button forwad_bt=(Button)findViewById(R.id.go_forward_bt);
        forwad_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(web.canGoForward())
                    web.goForward();
            }
        });
   Button reload_bt=(Button)findViewById(R.id.reload_bt);
        reload_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                web.reload();
            }
        });

        web.setDownloadListener(new DownloadListener() {
                                            @Override
                                            public void onDownloadStart(String
                                                                                url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                                           final     DownLoadThread thread=        new DownLoadThread(url, webActivity.this,urlYun);
                                                thread.start();
                                                mProgressDialog = new ProgressDialog(webActivity.this);
                                                  while(downLoadFileSize==0)
                                                  {
                                                      downLoadFileSize = thread.getDownLoadFileSize();

                                                  }
                                              while(thread.getFileName().equals("null"))
                                              {
                                                  filename=thread.getFileName();
                                              }
                                                mProgressDialog.setMessage(filename+"正在下载...");
                                                mProgressDialog.setIndeterminate(false);
                                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                  sendMessage(0);
                                                if(filename.lastIndexOf(".lock")>0) {
                                                    mProgressDialog.show();
                                                    mProgressDialog.setCanceledOnTouchOutside(false);
                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            super.run();
                                                            while (fileSize < downLoadFileSize) {
                                                                fileSize = thread.getDownloadedSize();

                                                                if (fileSize < downLoadFileSize) {
                                                                    sendMessage(1);
                                                                    System.out.println(thread.getFileName());
                                                                } else
                                                                    sendMessage(2);


                                                            }
                                                        }

                                                    }.start();
                                                }
                                            }
                                        });


    }
private void sendMessage(int what)
{
    Message m=new Message();
    m.what=what;
    handler.sendMessage(m);
}

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

                switch (msg.what)
                {
                    case 0:
                        mProgressDialog.setMax(downLoadFileSize);
                    case 1:
                        mProgressDialog.setProgress(fileSize);
                      break;
                    case 2:
                        mProgressDialog.dismiss();
                        fileSize=0;
                        downLoadFileSize=0;
                        Toast.makeText(webActivity.this,"下载完成！",Toast.LENGTH_SHORT).show();
                        break;
                    case -1:
                        Toast.makeText(webActivity.this,"下载出错！",Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        break;
                    default:
                        mProgressDialog.dismiss();
                        break;


            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}




