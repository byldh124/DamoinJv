package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.loader.content.CursorLoader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.databinding.ActivityCreateBinding;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends BaseActivity {
    private static final String TAG = CreateActivity.class.getSimpleName();
    private String meetInterest;
    private String meetLocation;
    private String meetName;
    private String purposeMessage;
    private Uri imgUri;
    private String iconUrl;
    private EditText etMeetName, etPurposeMessage;
    private ImageView ivInterestChoose, ivTitleImage;
    private String imgPath;

    private ActivityCreateBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            layout = DataBindingUtil.setContentView(this, R.layout.activity_create);

            //xml 참조영역
            ivInterestChoose = findViewById(R.id.iv_choose_interest);
            etMeetName = findViewById(R.id.et_meet_name);
            etPurposeMessage = findViewById(R.id.et_purpose_message);
            ivTitleImage = findViewById(R.id.iv_title_image);

            //액션바 세팅
            setSupportActionBar(layout.toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToInterestActivity();
                }
            }, 300);// 0.5초 정도 딜레이를 준 후 시작

            layout.setCreateActivity(this);
        } catch (Exception e) {
            logException(e);
        }

    }

    //관심사 선택화면으로 전환
    public void moveToInterestActivity() {
        try {
            goToInterest(GlobalKey.ACTIVITY_CODE.CREATE_ACTIVITY, GlobalKey.REQUEST_CODE.CREATE01);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GlobalKey.REQUEST_CODE.CREATE01:
                if (resultCode == RESULT_OK) {
                    meetInterest = data.getStringExtra("interest");
                    iconUrl = data.getStringExtra("iconUrl");
                    Glide.with(this).load(iconUrl).into(ivInterestChoose);
                }
                break;
            case GlobalKey.REQUEST_CODE.CREATE02:
                if (resultCode == RESULT_OK) {
                    meetLocation = data.getStringExtra("location");
                    layout.txtVwLocation.setText(meetLocation);
                    layout.setCreateActivity(this);
                }
                break;

            case GlobalKey.REQUEST_CODE.CREATE03:
                if (resultCode == RESULT_OK) {
                    imgUri = data.getData();
                    if (imgUri != null) {
                        Glide.with(this).load(imgUri).into(ivTitleImage);
                        imgPath = getRealPathFromUri(imgUri);
                    }
                }
                break;
        }
    }

    //활동지역 설정 화면으로 전환
    public void clickLocation(View view) {
        try {
            super.goToLocation(GlobalKey.ACTIVITY_CODE.CREATE_ACTIVITY, GlobalKey.REQUEST_CODE.CREATE02);
        } catch (Exception e) {
            logException(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickInterestIcon(View view) {
        moveToInterestActivity();
    }

    public void clickCreateMeet(View view) {

        meetName = etMeetName.getText().toString();
        purposeMessage = etPurposeMessage.getText().toString();

        RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).checkMeetName(meetName).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    //모임 설정에 대해 기입된 값 확인 작업
                    int isExist = Integer.parseInt(response.body());
                    if (isExist > 0) {
                        Toast.makeText(CreateActivity.this, "동일한 모임명이 존재합니다.\n다른 이름을 생성해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (meetName == null || meetName.equals("")) {
                            Toast.makeText(CreateActivity.this, "모임 이름을 설정해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (meetLocation == null || meetLocation.equals("")) {
                            Toast.makeText(CreateActivity.this, "모임 지역을 설정해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (purposeMessage == null || purposeMessage.equals("")) {
                            Toast.makeText(CreateActivity.this, "모임 목표를 작성해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (meetInterest == null || meetInterest.equals("")) {
                            Toast.makeText(CreateActivity.this, "관심사를 선택해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (imgPath == null) {
                            Toast.makeText(CreateActivity.this, "사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        showProgress();

                        //확인 작업이 끝난 후 모임 내용에 대해 DB에 저장
                        MultipartBody.Part filePart = null;

                        if (imgPath != null) {
                            File file = new File(imgPath);
                            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                            filePart = MultipartBody.Part.createFormData("img", file.getName(), requestBody);
                        }
                        HashMap<String, String> dataPart = new HashMap<>();
                        dataPart.put("meetName", meetName);
                        dataPart.put("meetLocation", meetLocation);
                        dataPart.put("meetInterest", meetInterest);
                        dataPart.put("purposeMessage", purposeMessage);
                        dataPart.put("masterId", GlobalInfo.myProfile.getUserId());
                        RetrofitService retrofitService = RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class);
                        retrofitService.saveItemBaseDataToCreateActivity(dataPart, filePart).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                //DB에 저장된 내역을 전역변수로 세팅
                                GlobalInfo.currentMoim.setMeetName(meetName);
                                GlobalInfo.currentMoim.setMeetLocation(meetLocation);
                                GlobalInfo.currentMoim.setPurposeMessage(purposeMessage);
                                GlobalInfo.currentMoim.setMeetInterest(meetInterest);
                                GlobalInfo.currentMoim.setTitleImgUrl(response.body());
                                GlobalInfo.currentMoim.setMasterId(GlobalInfo.myProfile.getUserId());
                                hideProgress();
                                onBackPressed();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                hideProgress();
                                Toast.makeText(CreateActivity.this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(CreateActivity.this, "동일한 모임명이 존재합니다.\n다른 이름을 생성해주세요", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    //갤러리에서 이미지 불러오는 작업
    public void clickImageInput(View view) {
        try {
            goToGallery(GlobalKey.REQUEST_CODE.CREATE03);
        } catch (Exception e) {
            logException(e);
        }
    }

    //DB에 저장하기 위해 이미지의 절대 경로를 String 값으로 변환
    String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}