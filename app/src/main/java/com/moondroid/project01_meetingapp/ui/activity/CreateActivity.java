package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.ExceptionPassthroughInputStream;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.databinding.ActivityCreateBinding;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;

import org.json.JSONObject;

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
    private String imgPath;

    private ActivityCreateBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            layout = DataBindingUtil.setContentView(this, R.layout.activity_create);

            //액션바 세팅
            setSupportActionBar(layout.toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToInterest(null);
                }
            }, 300);// 0.5초 정도 딜레이를 준 후 시작

            layout.setCreateActivity(this);
        } catch (Exception e) {
            logException(e);
        }

    }

    /**
     * 관심사 선택화면으로 전환
     **/
    public void goToInterest(View view) {
        try {
            goToInterest(GlobalKey.ACTIVITY_CODE.CREATE_ACTIVITY, GlobalKey.REQUEST_CODE.CREATE01);
        } catch (Exception e) {
            logException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    // 관심사 선택
                    case GlobalKey.REQUEST_CODE.CREATE01: {
                        meetInterest = data.getStringExtra("interest");
                        iconUrl = data.getStringExtra("iconUrl");
                        Glide.with(this).load(iconUrl).into(layout.imgVwInterest);
                        break;
                    }
                    // 지역 선택
                    case GlobalKey.REQUEST_CODE.CREATE02: {
                        meetLocation = data.getStringExtra("location");
                        layout.txtVwLocation.setText(meetLocation);
                        break;
                    }
                    // 대표 이미지 선택
                    case GlobalKey.REQUEST_CODE.CREATE03: {
                        imgUri = data.getData();
                        if (imgUri != null) {
                            Glide.with(this).load(imgUri).into(layout.imgVwTitle);
                            imgPath = getRealPathFromUri(imgUri);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logException(e);
        }
    }

    /**
     * 활동지역 설정 화면으로 전환
     **/
    public void goToLocation(View view) {
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

    /**
     * 그룹 정보 유효성 체크
     **/
    public void checkGroupInfo(View view) {
        try {
            meetName = layout.edtVwGroupName.getText().toString();
            purposeMessage = layout.edtVwObjectMsg.getText().toString();

            if (meetName == null || meetName.equals("")) {
                Toast.makeText(CreateActivity.this, R.string.cmn_group_name_set, Toast.LENGTH_SHORT).show();
                return;
            } else if (meetLocation == null || meetLocation.equals("")) {
                Toast.makeText(CreateActivity.this, R.string.cmn_group_location_set, Toast.LENGTH_SHORT).show();
                return;
            } else if (purposeMessage == null || purposeMessage.equals("")) {
                Toast.makeText(CreateActivity.this, R.string.cmn_group_object_set, Toast.LENGTH_SHORT).show();
                return;
            } else if (meetInterest == null || meetInterest.equals("")) {
                Toast.makeText(CreateActivity.this, R.string.cmn_group_interest_set, Toast.LENGTH_SHORT).show();
                return;
            } else if (imgPath == null) {
                Toast.makeText(CreateActivity.this, R.string.cmn_group_title_img_set, Toast.LENGTH_SHORT).show();
                return;
            }

            RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).checkMeetName(meetName).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        //모임 설정에 대해 기입된 값 확인 작업
                        JSONObject jsonRes = new JSONObject(response.body());
                        int code = jsonRes.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                        switch (code) {
                            case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                boolean isUsable = jsonRes.getBoolean(GlobalKey.NTWRK_RTN_TYPE.RESULT);
                                if (isUsable) {
                                    saveGroup();
                                } else {
                                    Toast.makeText(CreateActivity.this, R.string.cmn_group_name_exist, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                showNtwrkFailToast();
                                break;
                        }
                    } catch (Exception e) {
                        logException(e);
                        showNtwrkFailToast("1");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    showNtwrkFailToast("2");
                }
            });
        } catch (Exception e) {
            logException(e);
            showNtwrkFailToast("3");
        }
    }

    /**
     * 그룹 정보 저장
     **/
    public void saveGroup() {
        try {
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
                    try {
                        JSONObject jsonRes = new JSONObject(response.body());
                        int code = jsonRes.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                        switch (code) {
                            case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:

                                // 그룹 정보 저장 후 그룹 화면으로 이동
                                GlobalInfo.currentMoim.setMeetName(meetName);
                                GlobalInfo.currentMoim.setMeetLocation(meetLocation);
                                GlobalInfo.currentMoim.setPurposeMessage(purposeMessage);
                                GlobalInfo.currentMoim.setMeetInterest(meetInterest);
                                GlobalInfo.currentMoim.setTitleImgUrl(response.body());
                                GlobalInfo.currentMoim.setMasterId(GlobalInfo.myProfile.getUserId());
                                hideProgress();
                                Intent intent = new Intent(CreateActivity.this, MoimInfoActivity.class);
                                intent.putExtra(GlobalKey.INTENT_PARAM_TYPE.SEND_ACTIVITY, GlobalKey.ACTIVITY_CODE.CREATE_ACTIVITY);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                break;
                        }

                        //DB에 저장된 내역을 전역변수로 세팅
                    } catch (Exception e) {
                        logException(e);
                        showNtwrkFailToast("4");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    hideProgress();
                    showNtwrkFailToast("5");
                }
            });
        } catch (Exception e) {
            logException(e);
            showNtwrkFailToast("6");
        }
    }

    /**
     * 갤러리에서 이미지 불러오는 작업
     **/
    public void goToGallery(View view) {
        try {
            goToGallery(GlobalKey.REQUEST_CODE.CREATE03);
        } catch (Exception e) {
            logException(e);
        }
    }

    /**
     * DB에 저장하기 위해 이미지의 절대 경로를 String 값으로 변환
     **/
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