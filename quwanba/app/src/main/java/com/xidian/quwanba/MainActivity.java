package com.xidian.quwanba;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hjm.bottomtabbar.BottomTabBar;
import com.xidian.quwanba.app.BaseApplication;
import com.xidian.quwanba.ui.DabanFragment;
import com.xidian.quwanba.ui.EntertainFragment;
import com.xidian.quwanba.ui.FindFragment;
import com.xidian.quwanba.ui.HomeFragment;
import com.xidian.quwanba.ui.LoginActivity;
import com.xidian.quwanba.ui.MyFragment;
import com.xidian.quwanba.ui.OtcActivity;
import com.xidian.quwanba.ui.RouteFragment;
import com.xidian.quwanba.utils.AppConfig;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {
    BottomTabBar bottomtabbar;
    BaseApplication myapplication;
    public static final int REQUEST_PHONE_STATE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            toast("需要动态获取权限");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        }else{
//            toast("不需要动态获取权限");
//            TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//            Contants.IMEI = TelephonyMgr.getDeviceId();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }

        myapplication = (BaseApplication) getApplication();
        SharedPreferences shared = getSharedPreferences("quwanba",Context.MODE_PRIVATE);
        myapplication.userId = shared.getInt("userId", 0);
        myapplication.nickName = shared.getString("nickName", "");

        bottomtabbar = findViewById(R.id.bt_bar);
        bottomtabbar.init(getSupportFragmentManager())
                .setImgSize(20,20)
                .setFontSize(12)
                .setTabPadding(4,6,10)
                .setChangeColor(Color.parseColor("#4c9bff"),Color.parseColor("#6C6C6C"))
                .addTabItem("首页", R.mipmap.home_s, R.mipmap.home, HomeFragment.class)//图片随意添加
                .addTabItem("搭伴", R.mipmap.daban_s, R.mipmap.daban, DabanFragment.class)
                .addTabItem("管家游", R.mipmap.route_s, R.mipmap.route, RouteFragment.class)
                .addTabItem("发现", R.mipmap.find_s, R.mipmap.find, FindFragment.class)
                .addTabItem("我的", R.mipmap.my_s, R.mipmap.my,MyFragment.class)
                .setTabBarBackgroundColor(Color.WHITE)
                .isShowDivider(false);

        bottomtabbar.setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {

            @Override
            public void onTabChange(int position, String name, View view) {
                switch (position)
                {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;

                }
            }

            private boolean isLogin;


        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 1 && resultCode == 3) {
            bottomtabbar.setCurrentTab(0);
        }
        else if(requestCode == 5){
            if(myapplication.userId ==0){
                bottomtabbar.setCurrentTab(0);
            }
            else{
                Intent intent=new Intent(MainActivity.this, OtcActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, 1);
            }

        }
    }

}