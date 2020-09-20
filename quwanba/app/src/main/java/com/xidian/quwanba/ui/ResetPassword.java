package com.xidian.quwanba.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @author yd
 * @version 1.0
 * @date 2014-9-10
 * @Description 重设密码
 * 
 */

public class ResetPassword extends Activity {


	LinearLayout layoutBack;
	EditText phoneNumEdit,checkCode;
	ImageView appLeftImg;   ///左边图像
	TextView appMidTxt;    ///标题

	Button checkCodeBtn,commitButt;

	private static final int HANDLER_UI = 101;
	private static final int HANDLER_UI_FAIL = 102;
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
					Toast.makeText(ResetPassword.this, codeStr, Toast.LENGTH_SHORT).show();
					break;
				case HANDLER_UI_FAIL:
					commitButt.setEnabled(true);
					Toast.makeText(ResetPassword.this, codeStr, Toast.LENGTH_SHORT).show();
					break;
				case CHECK_CODE_ONE:
					checkCodeBtn.setText(codeStr);
				break;
				case CHECK_CODE_TWO:
					Toast.makeText(ResetPassword.this, codeStr, Toast.LENGTH_SHORT).show();
					checkCodeBtn.setEnabled(true);
					commitButt.setEnabled(true);
					break;
				case CODE_ERR:
					Toast.makeText(ResetPassword.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
					checkCodeBtn.setEnabled(true);
					commitButt.setEnabled(true);
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			View decorView = getWindow().getDecorView();
//			int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//			decorView.setSystemUiVisibility(uiOptions);
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//			getWindow().setStatusBarColor(Color.TRANSPARENT);
//
//		}

		layoutBack = findViewById(R.id.layout_back);
		layoutBack.setOnClickListener(new  View.OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		phoneNumEdit = findViewById(R.id.accountEdit);

		checkCode = findViewById(R.id.checkCode);

		checkCodeBtn = findViewById(R.id.checkCodeBtn);
		checkCodeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkCodeBtn.setEnabled(false);
				checkCodeBtn();
			}
		});

		commitButt = findViewById(R.id.commitButt);
		commitButt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				commitButt.setEnabled(false);
				sendPasswordBtn();
			}
		});
	}

	void checkCodeBtn(){
		String phoneNum = phoneNumEdit.getText().toString();
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


	void sendPasswordBtn() {

		String phoneNum = phoneNumEdit.getText().toString();
		String chCode = checkCode.getText().toString();
		if (StringUtil.isNotEmpty(phoneNum) && StringUtil.isNumeric(phoneNum)  && phoneNum.length() == 11
				&&StringUtil.isNotEmpty(chCode)){
			String url = ServerConfig.GET_PASSWORD;
			FormBody body = new FormBody.Builder()
					.add("userMobile", phoneNum)
					.add("checkCode",chCode)
					.build();
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.url(url)
					.post(body)
					.build();
			client.newCall(request).enqueue(new Callback(){
					@Override
					public void onFailure(Call request, IOException e) {
						Message message = Message.obtain();
						message.what = CODE_ERR;
						handlerUI.sendMessage(message);
					}

					@Override
					public void onResponse(Call request, Response response) throws IOException {
						int code = HANDLER_UI_FAIL;
						if (response.isSuccessful()) {
							String json = response.body().string();
							try {
								JSONObject responseobj = new JSONObject(json);
								int succ = responseobj.getInt("error_no");
								codeStr = responseobj.getString("error_info");
								if ( 1== succ){
									code = HANDLER_UI;
								}
							} catch (Exception e) {
								codeStr = "程序异常";
							}
						}else {
							codeStr = "请求失败，请重试!";
						}
						Message message = Message.obtain();
						message.what = code;
						handlerUI.sendMessage(message);
					}
			} );

		} else {
			Toast.makeText(ResetPassword.this, "信息提交不全", Toast.LENGTH_SHORT).show();
		}
	}




}
