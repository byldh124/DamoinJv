package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.UserInfo;
import com.moondroid.project01_meetingapp.databinding.ActivityLoginBinding;
import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
import com.moondroid.project01_meetingapp.helpers.utils.DMShrdPref;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;

import org.json.JSONObject;

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
    private String name;
    private String profileImgUrl;

    private ActivityLoginBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            layout = DataBindingUtil.setContentView(this, R.layout.activity_login);

            //EditText 입력 완료 후 키보드 Enter 이벤트 처리
            layout.edtVwId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    signIn(null);
                    return true;
                }
            });

            layout.setLoginActivity(this);
        } catch (Exception e) {
            DMFBCrash.logException(e);
        }
    }

    /**
     * 로그인
     **/
    public void signIn(View view) {
        try {
            inputId = layout.edtVwId.getText().toString();

            //기입된 값 확인 작업
            if (inputId.equals("")) return;

            showProgress();

            //기입된 ID로 DB에 저장된 값을 불러오는 작업
            Retrofit retrofit = RetrofitHelper.getRetrofit();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<String> call = retrofitService.getUserInfo(inputId);
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
                                GlobalInfo.myProfile = gson.fromJson(String.valueOf(result), UserInfo.class);
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

    /**
     * 회원가입 화면으로 전환
     **/
    public void clickAddAccount(View view) {
        try {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivityForResult(intent, REQUEST_EXIT);
        } catch (Exception e) {
            logException(e);
        }
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

    /**
     * 카카오 로그인 버튼 클릭시 카카오 서버에서 유저 정보를 가져온 후 DB에 저장
     * 기존에 DB에 저장된 값이 있으면 DB에서 유저 정보를 가져옴
     **/
    public void checkKakao(View view) {
        try {
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
                                    signInKakao();
                                }
                                return null;
                            }
                        });
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            DMFBCrash.logException(e);
        }
    }

    /**
     * 카카오 아이디 접속 이력 확인
     * 이력 o : signIn, 이력 x : signUp
     **/
    public void signInKakao() {
        try {
            RetrofitHelper.getRetrofit().create(RetrofitService.class).getUserInfo(inputId).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject res = new JSONObject(response.body());
                        int code = res.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                        switch (code) {
                            case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                JSONObject result = res.getJSONObject(GlobalKey.NTWRK_RTN_TYPE.RESULT);
                                Gson gson = new Gson();
                                GlobalInfo.myProfile = gson.fromJson(String.valueOf(result), UserInfo.class);
                                saveSharedPreference(inputId);
                                goToMain();
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                showNtwrkFailToast("1");
                            case GlobalKey.NTWRK_RTN_TYPE.NOT_EXIST:
                                signUpKakao();
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

    /**
     * 기존에 카카오 아이디록 접속한 이력이 없는 경우 카카오 아이디로 회원가입
     **/
    public void signUpKakao() {
        try {
            RetrofitHelper.getRetrofit().create(RetrofitService.class).saveUserBaseDataToKakao(inputId, name, profileImgUrl).enqueue(new Callback<String>() {
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
                                goToMain();
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
        } catch (Exception e) {
            logException(e);
        }
    }


    public void saveSharedPreference(String inputId) {
        try {
            DMShrdPref.getInstance(this).setString(GlobalKey.SHRD_PREF_KEY.USER_ID, inputId);
        } catch (Exception e) {
            logException(e);
        }
    }

    public void goToMain() {
        try {
            goToMain(GlobalKey.ACTIVITY_CODE.LOGIN_ACTIVITY);
        } catch (Exception e) {
            logException(e);
        }
    }

}