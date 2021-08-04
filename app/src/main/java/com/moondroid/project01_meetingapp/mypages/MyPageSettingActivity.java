package com.moondroid.project01_meetingapp.mypages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.igaworks.v2.core.AdBrixRm;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.login.LoginActivity;
import com.moondroid.project01_meetingapp.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageSettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch chatSwitch, meetSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_setting);

        Toolbar toolbar = findViewById(R.id.toolbar_my_page_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatSwitch = findViewById(R.id.chat_switch);
        meetSwitch = findViewById(R.id.meet_switch);

        chatSwitch.setOnCheckedChangeListener(this);
        meetSwitch.setOnCheckedChangeListener(this);

        settingSwitch();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void settingSwitch(){
        RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).loadFCMSetting(G.myProfile.getUserId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                if (response.body() == null){
                    body = "1::1";
                }
                String[] settings = body.split("::");

                chatSwitch.setChecked(settings[0].equals("1"));
                meetSwitch.setChecked(settings[1].equals("1"));

//                Toast.makeText(MyPageSettingActivity.this, body, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(MyPageSettingActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //로그아웃시 모든 정보 초기화 후 로그인 액티비티로 화면 전환
    public void clickLogout(View view) {
        G.myProfile = null;
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        sharedPreferences.edit().putString("userId", null).commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        setResult(RESULT_OK, null);
        AdBrixRm.logout();
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        String target = "";
        String value = "1";

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("잠시만 기다려 주십시오");
        dialog.show();
        switch (buttonView.getId()) {
            case R.id.chat_switch:
                target = "chataccess";
                if (!isChecked) value = "0";
                break;
            case R.id.meet_switch:
                target = "meetaccess";
                if (!isChecked) value = "0";
                break;
        }

        RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).updateFCMSetting(G.myProfile.getUserId(), target, value).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }
}