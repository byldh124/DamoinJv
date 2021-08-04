package com.moondroid.project01_meetingapp.library;

import android.app.Application;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

import com.igaworks.v2.core.application.AbxActivityHelper;
import com.igaworks.v2.core.application.AbxActivityLifecycleCallbacks;
import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        KakaoSdk.init(this, "f33663a89ab7e4f6ace89f2ebeade100");

        AbxActivityHelper.initializeSdk(KakaoApplication.this, "gBXaN9gbJ0GxuULA6oDpmw", "seRs0IMXO0Ki300xW544Rg");

        if (Build.VERSION.SDK_INT >= 14) {
            registerActivityLifecycleCallbacks(new AbxActivityLifecycleCallbacks());
        }
    }
}
