package com.moondroid.project01_meetingapp.mypages;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.login.LoginActivity;
import com.moondroid.project01_meetingapp.main.MainActivity;

public class MyPageSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_setting);
    }

    public void clickLogout(View view) {
        G.myProfile = null;
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        sharedPreferences.edit().putString("userId",null).commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        setResult(RESULT_OK, null);
        finish();
    }
}