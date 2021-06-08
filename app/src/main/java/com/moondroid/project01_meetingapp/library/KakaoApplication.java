package com.moondroid.project01_meetingapp.library;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        KakaoSdk.init(this, "f33663a89ab7e4f6ace89f2ebeade100");
    }
}
