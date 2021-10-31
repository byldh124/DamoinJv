package com.moondroid.project01_meetingapp.ui.activity;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moondroid.project01_meetingapp.ui.widget.DMProgressDialog;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog dmProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    public static void logException(Exception e) {
        Log.e(TAG, "[BaseActivity::logException] E : " + e);
        //CMFBCrshl.logException(e);
    }

    public void showProgress(){
        dmProgressDialog = DMProgressDialog.getInstance();

    }

    public void hideProgress(){

    }
}
