package com.moondroid.project01_meetingapp.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.main.MainActivity;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.account.AccountActivity;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    final int REQUEST_EXIT = 0;

    long id;
    String inputId;
    String inputPassword;
    String name;
    String profileImgUrl;

    EditText etInputId;
    EditText etInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etInputId = findViewById(R.id.log_in_edit_id);
        etInputPassword = findViewById(R.id.log_in_edit_pass);

        etInputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                clickLogIn(etInputPassword);
                return true;
            }
        });
    }

    public void clickLogIn(View view) {
        inputId = etInputId.getText().toString();
        inputPassword = etInputPassword.getText().toString();
        if (inputId == null || inputId.equals("") || inputPassword == null || inputPassword.equals(""))
            return;

        Retrofit retrofit = RetrofitHelper.getRetrofitInstanceGson();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<UserBaseVO> call = retrofitService.loadUserBaseDBToIntroActivity(inputId);
        call.enqueue(new Callback<UserBaseVO>() {
            @Override
            public void onResponse(Call<UserBaseVO> call, Response<UserBaseVO> response) {
                UserBaseVO userBaseVO = response.body();
                if (userBaseVO.userPassword.equals(inputPassword)) {
                    if (response.body() == null) {
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해 주십시오", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    G.myProfile = userBaseVO;
                    saveSharedPreference(inputId);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해 주십시오", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserBaseVO> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clickAddAccount(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivityForResult(intent, REQUEST_EXIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_EXIT:
                finish();
                break;
        }
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

                                id = user.getId();
                                inputId = String.valueOf(id);
                                name = user.getKakaoAccount().getProfile().getNickname();
                                profileImgUrl = user.getKakaoAccount().getProfile().getProfileImageUrl();
                                saveUserBaseDataOfKakao();
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
        finish();
    }

    public void saveUserBaseDataOfKakao() {
        RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).checkUserId(inputId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String str = response.body();
                if (str.equals("isExist")) {
                    RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadUserBaseDBToIntroActivity(inputId).enqueue(new Callback<UserBaseVO>() {
                        @Override
                        public void onResponse(Call<UserBaseVO> call, Response<UserBaseVO> response) {
                            G.myProfile = response.body();
                            saveSharedPreference(inputId);
                            moveToMainActivity();
                        }

                        @Override
                        public void onFailure(Call<UserBaseVO> call, Throwable t) {
                            saveUserBaseDataOfKakao();
                        }
                    });
                } else {
                    RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).saveUserBaseDataToKakao(inputId, name, profileImgUrl).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            G.myProfile.userId = inputId;
                            G.myProfile.userName = name;
                            G.myProfile.userProfileImgUrl = profileImgUrl;
                            saveSharedPreference(inputId);
                            moveToMainActivity();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            saveUserBaseDataOfKakao();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                saveUserBaseDataOfKakao();
            }
        });
    }
}