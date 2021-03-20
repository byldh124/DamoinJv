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

    private final int REQUEST_EXIT = 0;
    private long id;
    private String inputId;
    private String inputPassword;
    private String name;
    private String profileImgUrl;
    private EditText etInputId;
    private EditText etInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //xml 참조영역
        etInputId = findViewById(R.id.log_in_edit_id);
        etInputId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                clickLogIn(etInputPassword);
                return true;
            }
        });
//        etInputPassword = findViewById(R.id.log_in_edit_pass);

//        //EditText 입력 완료 후 Enter 이벤트 처리
//        etInputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                clickLogIn(etInputPassword);
//                return true;
//            }
//        });
    }

    public void clickLogIn(View view) {
        inputId = etInputId.getText().toString();
//        inputPassword = etInputPassword.getText().toString();

        //기입된 값 확인 작업
        if (inputId == null || inputId.equals(""))
            return;

        //기입된 ID로 DB에 저장된 값을 불러오는 작업
        Retrofit retrofit = RetrofitHelper.getRetrofitInstanceGson();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<UserBaseVO> call = retrofitService.loadUserBaseDBToIntroActivity(inputId);
        call.enqueue(new Callback<UserBaseVO>() {
            @Override
            public void onResponse(Call<UserBaseVO> call, Response<UserBaseVO> response) {
                //DB에 아이디가 저장되어 있는지 확인하는 작업
                if (response.body() == null) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해 주십시오", Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    UserBaseVO userBaseVO = response.body();
                    G.myProfile = userBaseVO;
                    saveSharedPreference(inputId);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                //저장되어 있는 패스워드와 기입된 패스워드를 확인
//                if (userBaseVO.getUserPassword().equals(inputPassword)) {
//                    //아이디가 저장된 값이 있고 비밀번호가 일치하면 sharedPreferences에 아이디 저장후 메인 화면으로 전환
//                    G.myProfile = userBaseVO;
//                    saveSharedPreference(inputId);
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    //기입된 패스워드와 저장된 패스워드가 틀릴시 발동
//                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해 주십시오", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(Call<UserBaseVO> call, Throwable t) {
            }
        });
    }

    //회원가입 버튼 클릭시 회원가입 화면으로 전환
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

    //카카오 로그인 버튼 클릭시 카카오 서버에서 유저 정보를 가져온 후 DB에 저장
    //기존에 DB에 저장된 값이 있으면 DB에서 유저 정보를 가져옴
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
                //기존에 카카오아이디로 로그인 한 적이 있는지 확인
                String str = response.body();
                if (str.equals("isExist")) {
                    RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadUserBaseDBToIntroActivity(inputId).enqueue(new Callback<UserBaseVO>() {
                        @Override
                        public void onResponse(Call<UserBaseVO> call, Response<UserBaseVO> response) {
                            //로그인 기록이 있을 시 DB에서 불러온 유저 정보를 전역으로 세팅하고 sharedPreferences에 아이디 저장
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
                    //기존에 카카오 아이디로 로그인 한 기록 없을 시 DB에 정보 저장하고 현재 유저 정보를 전역으로 세팅하고 sharedPreferences에 아이디 저장
                    RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).saveUserBaseDataToKakao(inputId, name, profileImgUrl).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            G.myProfile.setUserId(inputId);
                            G.myProfile.setUserName(name);
                            G.myProfile.setUserProfileImgUrl(profileImgUrl);
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