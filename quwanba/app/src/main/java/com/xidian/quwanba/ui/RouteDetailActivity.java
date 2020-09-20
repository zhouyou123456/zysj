package com.xidian.quwanba.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.xidian.quwanba.R;
import com.xidian.quwanba.bean.RouteBean;
import com.xidian.quwanba.utils.ServerConfig;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RouteDetailActivity extends Activity {

    private static final int HANDLER_UI = 101;
    private RouteBean mRouteBean;
    private String mRouteId = "";
    private WebView mWebView;
    TextView title_view;
    TextView tese_view ;
    TextView chufa_view;
    TextView xingcheng_view;
    TextView jiaotong_view;
    TextView feiyong_view;
    TextView qianzheng_view ;
    TextView buchong_view;
    ImageView cover_view;

    Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UI:
                    updateRouteInfo();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        Intent intent = getIntent();
        mRouteId = getIntent().getStringExtra("routeid");

        cover_view = findViewById(R.id.cover_view);
        title_view =  findViewById(R.id.title_view);
        tese_view =  findViewById(R.id.tese_view);
        chufa_view =  findViewById(R.id.chufa_view);
        xingcheng_view =  findViewById(R.id.xingcheng_view);
        jiaotong_view =  findViewById(R.id.jiaotong_view);
        feiyong_view =  findViewById(R.id.feiyong_view);
        qianzheng_view =  findViewById(R.id.qianzheng_view);
        buchong_view =  findViewById(R.id.buchong_view);

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           finish();
                                       }
                                   }
        );

        getRouteDetail();
    }

    private void updateRouteInfo(){

        Glide.with(this)
                .load(mRouteBean.imagePath)
                .centerCrop()
                .into(cover_view);

        title_view.setText(mRouteBean.title);

        if(mRouteBean.feature != null ){
            tese_view.setText(Html.fromHtml(mRouteBean.feature));
        }

        chufa_view.setText("出发地："+ mRouteBean.startPlace);

        if(mRouteBean.trafficDescription != null ){
            jiaotong_view.setText(Html.fromHtml(mRouteBean.trafficDescription));
        }
        if(mRouteBean.costDescription != null ){
            feiyong_view.setText(Html.fromHtml(mRouteBean.costDescription));
        }
        if(mRouteBean.visa != null ){
            qianzheng_view.setText(mRouteBean.visa);
        }
        if(mRouteBean.remark != null ){
            buchong_view.setText(Html.fromHtml(mRouteBean.remark));
        }
    }

    private void getRouteDetail(){
        String url = ServerConfig.GET_ROUTEDETAIL;
        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("routeId", mRouteId)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {

            }

            @Override
            public void onResponse(Call request, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    String jsonstring = "";
                    try {
                        JSONObject responseobj = JSONObject.parseObject(json);
                        String strJson = responseobj.getString("data");
                        mRouteBean = JSONObject.parseObject(strJson, RouteBean.class);

                        Message message = Message.obtain();
                        message.what = HANDLER_UI;
                        handlerUI.sendMessage(message);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}