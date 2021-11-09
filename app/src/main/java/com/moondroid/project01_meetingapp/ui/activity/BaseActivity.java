package com.moondroid.project01_meetingapp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.ui.dialog.DMProgressDialog;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog dmProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    public void logException(Exception e) {
        Log.e(TAG, "[BaseActivity::logException] E : " + e);
        DMFBCrash.logException(e);
    }

    public void showProgress() {
        try {
            if (dmProgressDialog == null) {
                dmProgressDialog = DMProgressDialog.getInstance();
            }
            dmProgressDialog.show();
        } catch (Exception e) {
            logException(e);
        }

    }

    public void hideProgress() {
        try {
            if (dmProgressDialog != null) {
                dmProgressDialog.hide();
            }
        } catch (Exception e) {
            logException(e);
        }
    }

    public void goToInterest(int activityCode, int requestCode){
        try {
            Intent intent = new Intent(this, InterestActivity.class);
            intent.putExtra(GlobalKey.INTENT_PARAM_TYPE.SEND_ACTIVITY, activityCode);
            startActivityForResult(intent, requestCode);
            overridePendingTransition(0 , android.R.anim.fade_out);
        } catch (Exception e){
            logException(e);
        }
    }

    public void goToLocation(int activityCode, int requestCode){
        try {
            Intent intent = new Intent(this, LocationChoiceActivity.class);
            intent.putExtra(GlobalKey.INTENT_PARAM_TYPE.SEND_ACTIVITY, activityCode);
            startActivityForResult(intent, requestCode);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            logException(e);
        }
    }

    public void goToGallery(int requestCode){
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            logException(e);
        }
    }
}
