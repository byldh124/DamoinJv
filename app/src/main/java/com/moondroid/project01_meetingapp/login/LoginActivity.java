package com.moondroid.project01_meetingapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Gender;
import com.kakao.sdk.user.model.User;
import com.moondroid.project01_meetingapp.main.MainActivity;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.account.AccountActivity;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    String inputId;
    String name;
    String profileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void clickLogIn(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void clickAddAccount(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void clickKakaoLogin(View view) {
        LoginClient.getInstance().loginWithKakaoAccount(this, new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                        @Override
                        public Unit invoke(User user, Throwable throwable) {
                            if (user != null) {
                                long id = user.getId();
                                inputId = String.valueOf(id);
                                name = user.getKakaoAccount().getProfile().getNickname();
                                profileImageUrl = user.getKakaoAccount().getProfile().getProfileImageUrl();

                                G.usersRef.child(inputId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        saveSharedPreference(inputId);
                                        moveToMainActivity();
                                    }
                                });

                                G.usersRef.child(inputId).child("base").setValue(new UserBaseVO(inputId, null, name, null, null, null, null, profileImageUrl, null)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        saveSharedPreference(inputId);
                                        moveToMainActivity();
                                    }
                                });
                            }
                            return null;
                        }
                    });
                }
                return null;
            }
        });
    }

    public void saveSharedPreference(String inputId) {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", inputId).commit();
    }

    public void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}