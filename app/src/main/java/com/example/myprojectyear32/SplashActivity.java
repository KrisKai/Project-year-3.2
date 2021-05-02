package com.example.myprojectyear32;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myprojectyear32.session.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN = 2000;
    Animation topAni;
    TextView splashTV;
    ImageView splashIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        topAni = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.top_animation);
        splashIV = findViewById(R.id.splashImageView);
        splashTV = findViewById(R.id.splashTV);
        splashTV.setAnimation(topAni);
        splashIV.setAnimation(topAni);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}