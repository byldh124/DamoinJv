package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.Nullable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.kakao.util.helper.Utility;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.UserBaseVO;
import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;

import org.json.JSONObject;

import java.util.jar.JarOutputStream;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {

    private final int REQUEST_EXIT = 0;
    private long id;
    private String inputId;
    private String inputPassword;
    private String name;
    private String profileImgUrl;
    private EditText etInputId;
    private EditText etInputPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //xml 참조영역
        etInputId = findViewById(R.id.log_in_edit_id);

        //EditText 입력 완료 후 키보드 Enter 이벤트 처리
        etInputId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                clickLogIn(etInputId);
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

        String keyHash = Utility.getKeyHash(this);
        Log.i("kakao Key Hash", keyHash);

    }

    public void clickLogIn(View view) {
        try {
            inputId = etInputId.getText().toString();
//        inputPassword = etInputPassword.getText().toString();

            //기입된 값 확인 작업
            if (inputId == null || inputId.equals(""))
                return;

            showProgress();

            //기입된 ID로 DB에 저장된 값을 불러오는 작업
            Retrofit retrofit = RetrofitHelper.getRetrofitInstanceScalars();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<String> call = retrofitService.loadUserBaseDBToIntroActivity(inputId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonRes = new JSONObject(response.body());
                        int code = jsonRes.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                        switch (code) {
                            case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                JSONObject result = jsonRes.getJSONObject(GlobalKey.NTWRK_RTN_TYPE.RESULT);
                                Gson gson = new Gson();
                                GlobalInfo.myProfile = gson.fromJson(String.valueOf(result), UserBaseVO.class);
                                saveSharedPreference(inputId);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                showNtwrkFailToast("1");
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.NOT_EXIST:
                                Toast.makeText(LoginActivity.this, R.string.cmn_id_not_exist, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (Exception e) {
                        logException(e);
                        showNtwrkFailToast("2");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    DMFBCrash.logException(t);
                    showNtwrkFailToast("3");
                }
            });
        } catch (Exception e) {
            logException(e);
            showNtwrkFailToast("4");
        } finally {
            hideProgress();
        }

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
        UserApiClient.getInstance().loginWithKakaoAccount(this, new Function2<OAuthToken, Throwable, Unit>() {
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
        intent.putExtra(GlobalKey.INTENT_PARAM_TYPE.SEND_ACTIVITY, GlobalKey.ACTIVITY_CODE.LOGIN_ACTIVITY);
        startActivity(intent);
        finish();
    }

    public void saveUserBaseDataOfKakao() {
        try {
            RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).loadUserBaseDBToIntroActivity(inputId).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject res = new JSONObject(response.body());
                        int code = res.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                        switch (code) {
                            case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                JSONObject result = res.getJSONObject(GlobalKey.NTWRK_RTN_TYPE.RESULT);
                                Gson gson = new Gson();
                                GlobalInfo.myProfile = gson.fromJson(String.valueOf(result), UserBaseVO.class);
                                saveSharedPreference(inputId);
                                moveToMainActivity();
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                showNtwrkFailToast("1");
                            case GlobalKey.NTWRK_RTN_TYPE.NOT_EXIST:
                                RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).saveUserBaseDataToKakao(inputId, name, profileImgUrl).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        try {
                                            JSONObject res = new JSONObject(response.body());
                                            int code = res.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                                            switch (code) {
                                                case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                                    GlobalInfo.myProfile.setUserId(inputId);
                                                    GlobalInfo.myProfile.setUserName(name);
                                                    GlobalInfo.myProfile.setUserProfileImgUrl(profileImgUrl);
                                                    saveSharedPreference(inputId);
                                                    moveToMainActivity();
                                                    break;
                                                case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                                    showNtwrkFailToast("2");
                                                    break;
                                            }

                                        } catch (Exception e) {
                                            logException(e);
                                            showNtwrkFailToast("3");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        DMFBCrash.logException(t);
                                        showNtwrkFailToast("4");
                                    }
                                });
                                break;
                        }

                    } catch (Exception e) {
                        logException(e);
                        showNtwrkFailToast("5");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    DMFBCrash.logException(t);
                    showNtwrkFailToast("6");
                }
            });
        } catch (Exception e) {
            logException(e);
            showNtwrkFailToast("7");
        }
    }
}