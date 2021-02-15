package com.moondroid.project01_meetingapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.moondroid.project01_meetingapp.MainActivity;
import com.moondroid.project01_meetingapp.R;

public class IntroActivity extends AppCompatActivity {

    Animation logoAnim;
    ImageView logoImg;
    ImageView campaignImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        logoImg = findViewById(R.id.intro_logo);
        campaignImg = findViewById(R.id.intro_campaign);

        logoImg.startAnimation(logoAnim);
        campaignImg.startAnimation(logoAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                String id = sharedPreferences.getString("userId", null);
                Intent intent;
                if (id == null) {
                    intent = new Intent(IntroActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(IntroActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}