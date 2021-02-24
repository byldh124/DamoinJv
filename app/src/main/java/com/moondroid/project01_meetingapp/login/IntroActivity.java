package com.moondroid.project01_meetingapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.main.MainActivity;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IntroActivity extends AppCompatActivity {

    Animation logoAnim;
    ImageView logoImg;
    ImageView campaignImg;

    String userId;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        logoImg = findViewById(R.id.intro_logo);
        campaignImg = findViewById(R.id.intro_campaign);

        logoImg.startAnimation(logoAnim);
        campaignImg.startAnimation(logoAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

                userId = sharedPreferences.getString("userId", null);
                if (userId == null) {
                    intent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    startApp();
//                    Retrofit retrofit = RetrofitHelper.getRetrofitInstanceGson();
//                    RetrofitService retrofitService = retrofit.create(RetrofitService.class);
//                    Call<UserBaseVO> call = retrofitService.loadUserBaseDBToIntroActivity(userId);
//                    call.enqueue(new Callback<UserBaseVO>() {
//                        @Override
//                        public void onResponse(Call<UserBaseVO> call, Response<UserBaseVO> response) {
//                            G.myProfile = response.body();
//                            intent = new Intent(IntroActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserBaseVO> call, Throwable t) {
//                            Toast.makeText(IntroActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });

//                    G.usersRef.child(userId).child("base").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                        @Override
//                        public void onSuccess(DataSnapshot dataSnapshot) {
//                            G.myProfile = dataSnapshot.getValue(UserBaseVO.class);
//                            intent = new Intent(IntroActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
                }
            }
        }, 1000);
    }

    public void startApp(){
        Retrofit retrofit = RetrofitHelper.getRetrofitInstanceGson();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<UserBaseVO> call = retrofitService.loadUserBaseDBToIntroActivity(userId);
        call.enqueue(new Callback<UserBaseVO>() {
            @Override
            public void onResponse(Call<UserBaseVO> call, Response<UserBaseVO> response) {
                G.myProfile = response.body();
                intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<UserBaseVO> call, Throwable t) {
                Toast.makeText(IntroActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                startApp();
            }
        });
    }
}