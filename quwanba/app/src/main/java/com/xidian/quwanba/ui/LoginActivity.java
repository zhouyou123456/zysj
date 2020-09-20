package com.xidian.quwanba.ui;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xidian.quwanba.R;
import com.xidian.quwanba.app.BaseApplication;
import com.xidian.quwanba.utils.ServerConfig;
import com.xidian.quwanba.utils.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity {

    EditText accountEdit, passWordEdit;
    BaseApplication myapplication;
    //public static LoginHandler myHandler;

   // private LoginModel mLoginModel;

    //private static String oldAccount; // /老账号
    private static String mAccount; // /账号
    private static String mPsw; // /密码
    private static final int HANDLER_UI = 101;


    ProgressDialog mProgressDlg;

    Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UI:
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        accountEdit = findViewById(R.id.accountEdit);
        passWordEdit = findViewById(R.id.passWordEdit);
        Button loginButt = findViewById(R.id.loginButt);
        TextView forgetPassText = findViewById(R.id.forgetPassText);
        TextView registerButt = findViewById(R.id.registerButt);
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             finish();
                                         }
                                     }
        );

        loginButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginButt();
                }
             }
        );

        forgetPassText.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             startActivity(new Intent(LoginActivity.this, ResetPassword.class));
                                         }
                                     }
        );

        registerButt.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                         }
                                     }
        );
    }


    void loginButt() {
        // 隐藏输入法
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        mAccount = accountEdit.getText().toString();
        mPsw = passWordEdit.getText().toString();

        if (mAccount.length()==0 || mPsw.length() == 0) {
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            String url = ServerConfig.LOGIN;
            OkHttpClient client = new OkHttpClient();

            JSONObject json = new JSONObject();
            json.put("userMobile", mAccount);
            json.put("password", mPsw);
            String postjson = json.toString();

            MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
            final RequestBody requestBody = RequestBody.create(mediaType, postjson);
//        FormBody body = new FormBody.Builder()
//                .add("userMobile", mAccount)
//                .add("password", mPsw)
//                .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
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
                            JSONObject responseobj1 = new JSONObject(json);
                            myapplication = (BaseApplication) getApplication();
                            JSONObject responseobj = responseobj1.getJSONObject("data");
                            myapplication.userId = responseobj.getInt("id");
                            myapplication.nickName = responseobj.getString("nickName");

                            SharedPreferences shared = getSharedPreferences("quwanba",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putInt("userId",myapplication.userId);
                            editor.putString("nickName",myapplication.nickName);
                            editor.commit();

                            Intent i = new Intent();
                            setResult(5, i);
                            finish();
                        } catch (Exception e) {

                        }

                    }
                    else{
                        Message message = Message.obtain();
                        message.what = HANDLER_UI;
                        handlerUI.sendMessage(message);
                    }
                }
            });
        }
        catch(Exception e){

        }

    }


    protected void onResume() {
        // 友盟统
        super.onResume();

    }

    protected void onPause() {

        super.onPause();
    }

    /**
     * 返回键事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent();
            setResult(5, i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}