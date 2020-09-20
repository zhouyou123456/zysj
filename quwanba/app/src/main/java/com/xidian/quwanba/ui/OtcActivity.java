package com.xidian.quwanba.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.webkit.JavascriptInterface;
import android.widget.Button;

import com.linkin.adsdk.AdSdk;
import com.xidian.quwanba.R;
import com.xidian.quwanba.app.BaseApplication;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import com.xidian.quwanba.utils.X5WebView;

public class OtcActivity extends Activity {

    BaseApplication myapplication;
    private X5WebView mWebView;
    private Button button;
    private View mADView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otc);
        myapplication = (BaseApplication) getApplication();

        mWebView = findViewById(R.id.web_view);
        WebSettings webSettings = mWebView.getSettings();

        mADView = findViewById(R.id.container);
        // 设置与Js交互的权限
//        webSettings.setJavaScriptEnabled(true);
//        // 设置允许JS弹窗
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        // 开启DOM缓存，开启LocalStorage存储（html5的本地存储方式）
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//默认使用缓存
//        webSettings.setAppCacheMaxSize(8*1024*1024);//缓存最多可以有8M
//        webSettings.setAllowFileAccess(true);//可以读取文件缓存(manifest生效)
//        webSettings.setAppCacheEnabled(true);//应用可以有缓存
        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html

        button = findViewById(R.id.adbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // gotoAD();
                // 通过Handler发送消息
//                mWebView.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        // 注意调用的JS方法名要对应上
//                        // 调用javascript的callJS()方法
                      mWebView.loadUrl("javascript:callJSComplete()");
//                    }
//                });

            }
        });

       // mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) { // Handle the error
            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.addJavascriptInterface(new AndroidtoJs(),
                "qwbAd");

//        mWebView.setWebChromeClient(new WebChromeClient() {
//
//            public boolean onJsCallVideo() {
//
//                return true;
//            }
//
////            public boolean onJsCallVideo(WebView view, String url, String message, final JsResult result) {
////
////                return true;
////            }
//
//        });

        mWebView.loadUrl("http://h5.goplayclub.top/#/otc/"+myapplication.userId);
    }

//    @JavascriptInterface
//    public void onJsCallVideo(){
//        Log.e("OtcActivity", "拉起广告");
//        gotoAD();
//    }

    public void gotoAD(){

        //mWebView.loadUrl("javascript:AndroidcallJS()");
        mADView.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
        AdSdk.getInstance().loadRewardVideoAd(this, "rv1", false,
                new AdSdk.RewardVideoAdListener() {
                    @Override
                    public void onAdLoad(String id) {
                    }

                    @Override
                    public void onVideoCached(String id) {
                    }

                    @Override
                    public void onAdShow(String id) {
                    }

                    /** 视频广告播完验证奖励有效性回调，建议在此回调给用户奖励 */
                    @Override
                    public void onReward(String id) {
                    }

                    @Override
                    public void onAdClick(String id) {
                    }

                    @Override
                    public void onVideoComplete(String id) {
                    }

                    @Override
                    public void onAdClose(String id) {
                        mADView.setVisibility(View.GONE);
                        mWebView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(String id, int code, String message) {
                    }
                });
    }

//    /**
//     * 不起作用
//     */
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i = new Intent();
//        i.putExtra("result", text1.getText().toString());
//        setResult(3, i);
//        finish();
//    }

    /**
     *   需要拦截press的事件
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent();
            Bundle bundle = new Bundle();
            setResult(3, i.putExtras(bundle));

//            Intent i = new Intent();
//            i.putExtra("result", text1.getText().toString());
//            setResult(3, i);
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //允许Javascript调用alert
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

            return super.onJsAlert(view, url, message, result);
        }
    }
}