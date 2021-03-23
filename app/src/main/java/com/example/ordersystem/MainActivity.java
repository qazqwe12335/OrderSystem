package com.example.ordersystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    ImageView back_image,main_icon;
    TextView title_text;
    Animation title_anim;

    private static int STAY_TIME = 3600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //APP第一個頁面，LOGO動畫功能
        title_anim = AnimationUtils.loadAnimation(this, R.anim.title_animation);

        main_icon = findViewById(R.id.main_icon_img);
        back_image = findViewById(R.id.back_image);
        title_text = findViewById(R.id.title_text);

        back_image.animate().alpha(1.0f).setDuration(2300);
        main_icon.setAnimation(title_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
            //3.6秒後直接進入下一個頁面，並關閉此頁
        }, STAY_TIME);
    }
}