package com.xidian.quwanba.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xidian.quwanba.R;
import com.xidian.quwanba.app.BaseApplication;
import com.xidian.quwanba.utils.ServerConfig;
import com.xidian.quwanba.utils.StringUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NickNameActivity extends Activity {

    LinearLayout backLayout ;
    EditText nickNameEdt;
    Button saveBtn;

    String nickNameStr = "";
    BaseApplication myapplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        myapplication = (BaseApplication) this.getApplication();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }

        backLayout = findViewById(R.id.layout_back);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nickNameEdt = findViewById(R.id.nick_name_edt);
        nickNameStr = getIntent().getStringExtra("nickName");
        nickNameEdt.setText(nickNameStr);

        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             nickNameStr = nickNameEdt.getText().toString();
                                             if (StringUtil.isNotEmpty(nickNameStr.trim())){
                                                 saveBtn.setEnabled(false);
                                                 uploadingUserInfo();
                                             }else {
                                                 Toast.makeText(NickNameActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                                             }
                                         }
                                     }
        );
    }


    private void uploadingUserInfo(){
        String url = ServerConfig.UOLOAD_USERINDFO;
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("guestUserId",myapplication.userId+"")  //其他信息
                .addFormDataPart("nickname",nickNameStr)
                .build();


        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                Toast.makeText(NickNameActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
                saveBtn.setEnabled(true);
            }

            @Override
            public void onResponse(Call request, Response response) throws IOException {
                if (response.isSuccessful()) {
                    finish();
                }
            }
        });
    }


}