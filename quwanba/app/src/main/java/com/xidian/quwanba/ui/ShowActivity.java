package com.xidian.quwanba.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xidian.quwanba.R;

public class ShowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }

        int nIndex = getIntent().getIntExtra("type", 0);
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           finish();
                                       }
                                   }
        );

        ImageView iv_bg = findViewById(R.id.iv_bg);

        if(nIndex == 1){
            Glide.with(this)
                    .load(R.mipmap.travel_bg)
                    .into(iv_bg);
            Glide.with(this)
                    .load(R.mipmap.back)
                    .into(iv_back);
        }
        else if(nIndex == 2){
            Glide.with(this)
                    .load(R.mipmap.hotel_bg)
                    .into(iv_bg);

            Glide.with(this)
                    .load(R.mipmap.login_return)
                    .into(iv_back);
        }
        else if(nIndex == 3){
            Glide.with(this)
                    .load(R.mipmap.plane_bg)
                    .into(iv_bg);
            Glide.with(this)
                    .load(R.mipmap.back)
                    .into(iv_back);
        }
        else if(nIndex == 4){
            Glide.with(this)
                    .load(R.mipmap.scenic_bg)
                    .into(iv_bg);
            Glide.with(this)
                    .load(R.mipmap.login_return)
                    .into(iv_back);
        }
        else if(nIndex == 8){
            Glide.with(this)
                    .load(R.mipmap.ticket_bg)
                    .into(iv_bg);
            Glide.with(this)
                    .load(R.mipmap.login_return)
                    .into(iv_back);
        }

    }
}