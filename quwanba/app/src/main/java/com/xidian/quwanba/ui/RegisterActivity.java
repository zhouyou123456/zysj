package com.xidian.quwanba.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xidian.quwanba.R;
import com.xidian.quwanba.utils.ServerConfig;
import com.xidian.quwanba.utils.StringUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {
    EditText phoneEdit;
    EditText codeEdit , checkCodeRdt;
    EditText passwordEdit;
    EditText confirmEdit;
    EditText recommendEdit;
    String mStrPhone;
    String mStrCode;
    Button btn_sendSMS , btnCheckCode , registerButt;
    LinearLayout backLayout ;
    CountDownTimer mTimer;
    private static final int HANDLER_UI = 101;
    private static final int HANDLER_UI_REGISSTER = 102;

    private static final int CHECK_CODE_ONE = 10;
    private static final int CHECK_CODE_TWO= 11;
    private static final int CODE_ERR = 20;
    String codeStr = "";

    Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UI:
                    Toast.makeText(RegisterActivity.this,"验证码发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case HANDLER_UI_REGISSTER:
                    Toast.makeText(RegisterActivity.this,"注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case CHECK_CODE_ONE:
                    btnCheckCode.setText(codeStr);
                    break;
                case CHECK_CODE_TWO:
                    Toast.makeText(RegisterActivity.this, codeStr, Toast.LENGTH_SHORT).show();
                    btnCheckCode.setEnabled(true);
                    break;
                case CODE_ERR:
                    Toast.makeText(RegisterActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
                    btnCheckCode.setEnabled(true);
                    registerButt.setEnabled(true);
                    break;
                default:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        phoneEdit = findViewById(R.id.phoneEdit);
        checkCodeRdt = findViewById(R.id.check_code_edt);
        codeEdit = findViewById(R.id.codeEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        confirmEdit = findViewById(R.id.confirmEdit);
        recommendEdit = findViewById(R.id.recommendEdit);
        btnCheckCode = findViewById(R.id.btn_check_code);
        btn_sendSMS = findViewById(R.id.btn_sms);
        registerButt = findViewById(R.id.registerButt);


        btnCheckCode = findViewById(R.id.btn_check_code);
        btnCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCheckCode.setEnabled(false);
                checkCodeBtn();
            }
        });
        btn_sendSMS.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             sendSMS();
                                         }
                                     }
        );

        registerButt.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             doRegister();
                                         }
                                     }
        );
    }


    void checkCodeBtn(){
        String phoneNum = phoneEdit.getText().toString();
        if (StringUtil.isNotEmpty(phoneNum) && StringUtil.isNumeric(phoneNum)  && phoneNum.length() == 11)	{
            String url = ServerConfig.GET_CHECK_CODE;
            OkHttpClient checkCodeClient = new OkHttpClient();

            FormBody body = new FormBody.Builder()
                    .add("userMobile", phoneNum)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            checkCodeClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message message = Message.obtain();
                    message.what = CHECK_CODE_TWO;
                    handlerUI.sendMessage(message);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    int code = CHECK_CODE_TWO;
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        try {
                            JSONObject responseobj = new JSONObject(json);
                            int succ = responseobj.getInt("error_no");
                            codeStr = responseobj.getString("data");
                            if (1== succ){
                                code = CHECK_CODE_ONE;
                            }
                        } catch (Exception e) {

                        }
                    }

                    Message message = Message.obtain();
                    message.what = code;
                    handlerUI.sendMessage(message);

                }
            });
        }else {
            codeStr = "手机号不能为空";
            Message message = Message.obtain();
            message.what = CHECK_CODE_TWO;
            handlerUI.sendMessage(message);
        }
    }

    void destroyTimer() {
        Log.v("TIMER", "销毁定时器");
        ///销毁定时器
//        if (mTimer != null) {
//            mTimer.cancel();
//            mTimer = null;
//        }
    }

    public void sendSMS(){
        btn_sendSMS.setEnabled(false);
        //btn_sendSMS.setText();
        String strPhone = phoneEdit.getText().toString();
        String chCode = checkCodeRdt.getText().toString();
        if(StringUtil.isNumeric(strPhone) && strPhone.length() == 11&&StringUtil.isNotEmpty(chCode)) {
            mStrPhone = strPhone;
            String url = ServerConfig.REG_GET_SMS_CODE + "?userMobile=" +mStrPhone+"&checkCode="+chCode;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call request, IOException e) {
                    btn_sendSMS.setEnabled(true);
                }

                @Override
                public void onResponse(Call request, Response response) throws IOException {

                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            mStrCode = jsonObject.getString("data");
                        }
                        catch(Exception e){
                        }
                        Message message = Message.obtain();
                        message.what = HANDLER_UI;
                        handlerUI.sendMessage(message);

                    }
                }
            });

            if(mTimer != null){
                mTimer.cancel();
                mTimer = null;
            }
            mTimer = new CountDownTimer(60*1000, 1000) {
                @Override
                 public void onTick(long millisUntilFinished) {
                                       // TODO Auto-generated method stub
                        btn_sendSMS.setText(+millisUntilFinished/1000+"秒后重新获取");
                                   }

                          @Override
                 public void onFinish() {
                              btn_sendSMS.setText("发送验证码");
                              btn_sendSMS.setEnabled(true);
                                    }
             }.start();

        }
        else{
            btn_sendSMS.setEnabled(true);
            Toast.makeText(this,"手机号输入有误", Toast.LENGTH_SHORT).show();
        }
    }

    public void doRegister(){
        String strPhone = phoneEdit.getText().toString();
        String strCode = codeEdit.getText().toString();
        String strPassword =  passwordEdit.getText().toString();
        String strConfirm =  confirmEdit.getText().toString();
        String strRecommend =  recommendEdit.getText().toString();
        if(strPhone.length() == 0){
            Toast.makeText(this,"手机号有误", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!strCode.equalsIgnoreCase(mStrCode)){
            Toast.makeText(this,"验证码错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if(strPassword.length() < 6){
            Toast.makeText(this,"密码不能少于6位", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!strPassword.equalsIgnoreCase(strConfirm)){
            Toast.makeText(this,"两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        if(strRecommend.length() > 0){
            if(StringUtil.isNumeric(strRecommend) && strRecommend.length() == 11) {

            }
            else{
                Toast.makeText(this,"推荐人手机号输入有误", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        registerButt.setEnabled(false);

        String url = ServerConfig.REG_REGISTER;
        OkHttpClient client = new OkHttpClient();



        try {
            JSONObject json = new JSONObject();
            json.put("userMobile", strPhone);
            json.put("password", strPassword);
            json.put("msgCode", strCode);
            json.put("recommendMobile", strRecommend);
            String postjson = json.toString();

            MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
            final RequestBody requestBody = RequestBody.create(mediaType, postjson);

//        FormBody body = new FormBody.Builder()
//                .add("userMobile", strPhone)
//                .add("password", strPassword)
//                .add("fromUserMoblie", strRecommend)
//                .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call request, IOException e) {
                    Message message = Message.obtain();
                    message.what = CODE_ERR;
                    handlerUI.sendMessage(message);
                }

                @Override
                public void onResponse(Call request, Response response) throws IOException {

                    if (response.isSuccessful()) {

                        Message message = Message.obtain();
                        message.what = HANDLER_UI_REGISSTER;
                        handlerUI.sendMessage(message);
//                        String json = response.body().string();
//                        String jsonstring = "";
//                        try {
//                            JSONObject responseobj = new JSONObject(response.body().toString());
//
//                        } catch (Exception e) {
//
//                        }

                    }else{
                        Message message = Message.obtain();
                        message.what = CODE_ERR;
                        handlerUI.sendMessage(message);
                    }
                }
            });
        }
        catch(Exception e){

        }
    }
}