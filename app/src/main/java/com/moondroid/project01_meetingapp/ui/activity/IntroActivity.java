package com.moondroid.project01_meetingapp.ui.activity;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.UserBaseVO;
import com.moondroid.project01_meetingapp.databinding.ActivityIntroBinding;
import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
import com.moondroid.project01_meetingapp.helpers.utils.DMShrdPref;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IntroActivity extends BaseActivity {

    private static final String TAG = IntroActivity.class.getSimpleName();

    private ActivityIntroBinding layout;

    private Animation logoAnim;

    private String userId;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            layout = DataBindingUtil.setContentView(this, R.layout.activity_intro);

            //로고 애니메이션 작업
            logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
            layout.imgVwLogo.startAnimation(logoAnim);
            layout.imgVwCampaign.startAnimation(logoAnim);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    DMShrdPref shrdPref = DMShrdPref.getInstance(getBaseContext());
                    String chatSetting = shrdPref.getString(GlobalKey.SHRD_PREF_KEY.CHAT);
                    String meetSetting = shrdPref.getString(GlobalKey.SHRD_PREF_KEY.MEET);
                    //SharedPreferences 에 저장된 값 확인 후 Login, Main 액티비티로 화면 전환

                    userId = shrdPref.getString(GlobalKey.SHRD_PREF_KEY.USER_ID);
                    if (userId == null) {
                        intent = new Intent(IntroActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        startApp();
                    }
                }
            }, 1000);
        } catch (Exception e) {
            logException(e);
        }

    }

    /**
     * SharedPreferences 에 저장된 유저 ID에 따라 DB에 저장된 유저의 정보들을 가져오는 작업
     **/
    public void startApp() {
        try {
            Retrofit retrofit = RetrofitHelper.getRetrofitInstanceScalars();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<String> call = retrofitService.loadUserBaseDBToIntroActivity(userId);
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
                                intent = new Intent(IntroActivity.this, MainActivity.class);
                                if (getIntent().getStringExtra("meetName") != null) {
                                    String meetName = getIntent().getStringExtra("meetName");
                                    if (!meetName.isEmpty()) {
                                        intent.putExtra("meetName", meetName);
                                    }
                                }
                                startActivity(intent);
                                finish();
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                Toast.makeText(IntroActivity.this, String.format(getString(R.string.cmn_fail_load_user_info), "1"), Toast.LENGTH_SHORT).show();
                            case GlobalKey.NTWRK_RTN_TYPE.NOT_EXIST:
                                Toast.makeText(IntroActivity.this, String.format(getString(R.string.cmn_fail_load_user_info), "2"), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    } catch (Exception e) {
                        logException(e);
                        showNtwrkFailToast("1");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    DMFBCrash.logException(t);
                    showNtwrkFailToast("2");
                }
            });

        } catch (Exception e) {
            logException(e);
            showNtwrkFailToast("3");
        }
    }
}
