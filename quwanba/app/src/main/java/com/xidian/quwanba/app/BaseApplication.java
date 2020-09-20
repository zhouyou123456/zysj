package com.xidian.quwanba.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hjm.bottomtabbar.BuildConfig;
import com.linkin.adsdk.AdConfig;
import com.linkin.adsdk.AdSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.xidian.quwanba.ui.SplashActivity;
import com.xidian.quwanba.utils.AppConfig;

public class BaseApplication  extends Application {
    private static final String APP_ID = "ba0063bfbc1a5ad878"; // 这是测试 appId，请替换成分配到的正式 appId
    private static final long MIN_SPLASH_INTERVAL = 3 * 60 * 1000L;

    public int userId = 0;
    public String nickName ="";

    @Override
    public void onCreate() {
        super.onCreate();

        // AdSdk 在 VideoSdk 之前初始化，视频流中才能展现广告
        AdSdk.getInstance().init(getApplicationContext(),
                new AdConfig.Builder()
                        .appId(APP_ID)
                        .userId("10001") // 未登录可不设置 userId，登录时再设置
                        .multiProcess(false)
                        .debug(BuildConfig.DEBUG)
                        .build(),
                null);


        UMConfigure.init(this, "5f5b2625b4739632429db213", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
       //  选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        setMinSplashInterval(MIN_SPLASH_INTERVAL);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        AppConfig.phoneWidth = width;
        AppConfig.phoneHeight = height;
        AppConfig.phoneDensity = density;
        AppConfig.phoneDPI = densityDpi;

    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        super.startActivity(intent);
    }

    /** 设置最短开屏间隔时间，如果切到后台超出此间隔时间，回到前台任意页面都会显示开屏，以提升开屏广告的曝光量 */
    private void setMinSplashInterval(final long minSplashInterval) {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            private int activityCount = 0;
            private long leaveTime = 0;

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                if (0 == activityCount++) {
                    // App 回到前台
                    if (System.currentTimeMillis() - leaveTime >= minSplashInterval) {
                        if (!(activity instanceof SplashActivity)) {
                            Intent intent = new Intent(activity, SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // 无动画
                            activity.startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                if (0 == --activityCount) {
                    // App 切到后台
                    leaveTime = System.currentTimeMillis();
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
            }
        });
    }
}
