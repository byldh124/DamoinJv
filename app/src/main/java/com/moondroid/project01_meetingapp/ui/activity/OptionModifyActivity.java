package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
import com.moondroid.project01_meetingapp.helpers.utils.DMUtil;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.network.URLMngr;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionModifyActivity extends BaseActivity {
    private final int REQUEST_CODE_FOR_INTRO_IMG_SELECT = 0;
    private final int REQUEST_CODE_FOR_TITLE_IMG_SELECT = 2;
    private final int REQUEST_CODE_FOR_INTEREST_ICON = 1;
    private Toolbar toolbar;
    private ImageView ivIntro;
    private ImageView ivIcon;
    private CircleImageView ivTitleImg;
    private TextView tvTitle;
    private TextView tvMessage;
    private TextView tvPurpose;
    private Uri introImgUri = null;
    private Uri titleImgUri = null;
    private String introImgPath;
    private String titleImgPath;
    private String meetInterest;
    private String iconUrl;
    private String meetName;
    private String message;
    private String purposeMessage;
    private EditText editText;
    private Map<String, String> dataPart;
    private String[] dstName;
    private boolean meetNameIsChanged = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_modify);

        //xml Reference
        toolbar = findViewById(R.id.toolbar_modify_activity);
        ivIntro = findViewById(R.id.iv_page_modify_intro_image);
        ivIcon = findViewById(R.id.iv_page_modify_interest_icon);
        tvTitle = findViewById(R.id.tv_page_modify_title);
        tvMessage = findViewById(R.id.tv_page_modify_message);
        ivTitleImg = findViewById(R.id.iv_page_modify_title_img);
        tvPurpose = findViewById(R.id.tv_page_modify_purpose);

        meetName = GlobalInfo.currentGroup.getMeetName();
        meetInterest = GlobalInfo.currentGroup.getMeetInterest();
        purposeMessage = GlobalInfo.currentGroup.getPurposeMessage();
        message = GlobalInfo.currentGroup.getMessage();

        //Action bar Setting
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //기존 정보 load
        if (GlobalInfo.currentGroup.getIntroImgUrl() != null)
            Picasso.get().load(URLMngr.IMG_URL + GlobalInfo.currentGroup.getIntroImgUrl()).into(ivIntro);

        ArrayList<String> interests = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.interest_list)));
        Glide.with(this).load(getResources().getStringArray(R.array.interest_icon_img_url)[interests.indexOf(meetInterest)]).into(ivIcon);

        tvTitle.setText(meetName);

        if (GlobalInfo.currentGroup.getTitleImgUrl() != null)
            Picasso.get().load(URLMngr.IMG_URL + GlobalInfo.currentGroup.getTitleImgUrl()).into(ivTitleImg);
        if (purposeMessage != null) tvPurpose.setText(purposeMessage);
        if (message == null || message.equals("")) {
            tvMessage.setText("모임 설명을 작성해주세요");
        } else {
            tvMessage.setText(message);
        }
    }


    public void clickTitleImg(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_TITLE_IMG_SELECT);
    }

    public void clickIntro(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_INTRO_IMG_SELECT);
    }

    public void clickInterestIcon(View view) {
        Intent intent = new Intent(this, InterestActivity.class);
        intent.putExtra(GlobalKey.INTENT_PARAM_TYPE.SEND_ACTIVITY, GlobalKey.ACTIVITY_CODE.OPTION_MODIFY_ACTIVITY);
        startActivityForResult(intent, REQUEST_CODE_FOR_INTEREST_ICON);
    }

    public void clickTitle(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.view_for_modify_title_edit, null);
        editText = view1.findViewById(R.id.edit_modify_title);
        editText.setText(meetName);
        builder.setTitle("모임명 설정").setView(view1).setNegativeButton("저장 안함", null).setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                meetName = editText.getText().toString();
                tvTitle.setText(meetName);
                if (!meetName.equals(GlobalInfo.currentGroup.getMeetName())) {
                    meetNameIsChanged = true;
                } else {
                    meetNameIsChanged = false;
                }
            }
        }).create().show();
    }

    public void clickPurposeMessage(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.view_for_modify_title_edit, null);
        editText = view1.findViewById(R.id.edit_modify_title);
        editText.setText(purposeMessage);
        editText.setMaxLines(2);
        editText.setLines(2);
        editText.setMaxEms(40);
        builder.setTitle("모임목표 설정").setView(view1).setNegativeButton("저장 안함", null).setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                purposeMessage = editText.getText().toString();
                tvPurpose.setText(purposeMessage);
            }
        }).create().show();
    }

    public void clickMessage(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.view_for_modify_message_edit, null);
        editText = view1.findViewById(R.id.edit_modify_title);
        if (message != null) editText.setText(message);
        builder.setTitle("모임명 설정").setView(view1).setNegativeButton("저장 안함", null).setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                message = editText.getText().toString();
                tvMessage.setText(message);
            }
        }).create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_FOR_INTRO_IMG_SELECT:
                introImgUri = data.getData();
                Glide.with(this).load(introImgUri).into(ivIntro);
                introImgPath = DMUtil.getRealPathFromUri(this, introImgUri);
                break;
            case REQUEST_CODE_FOR_INTEREST_ICON:
                meetInterest = data.getStringExtra(GlobalKey.INTENT_PARAM_TYPE.INTEREST);
                iconUrl = data.getStringExtra(GlobalKey.INTENT_PARAM_TYPE.ICON_URL);
                Glide.with(this).load(iconUrl).into(ivIcon);
                break;
            case REQUEST_CODE_FOR_TITLE_IMG_SELECT:
                titleImgUri = data.getData();
                Glide.with(this).load(titleImgUri).into(ivTitleImg);
                titleImgPath = DMUtil.getRealPathFromUri(this, titleImgUri);
                break;
        }
    }

    public void clickSave(View view) {
        try {
            if (meetNameIsChanged) {
                RetrofitHelper.getRetrofit().create(RetrofitService.class).checkGroupName(meetName).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            JSONObject jsonRes = new JSONObject(response.body());
                            int code = jsonRes.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                            switch (code) {
                                case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                    boolean isUsable = jsonRes.getBoolean(GlobalKey.NTWRK_RTN_TYPE.RESULT);
                                    if (isUsable) {
                                        saveData();
                                    } else {
                                        Toast.makeText(OptionModifyActivity.this, R.string.cmn_group_name_exist, Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                    showNtwrkFailToast("1");
                                    break;
                            }
                        } catch (Exception e) {
                            showNtwrkFailToast("2");
                            logException(e);
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        DMFBCrash.logException(t);
                        showNtwrkFailToast("3");
                    }
                });
            } else {
                saveData();
            }
        } catch (Exception e) {
            logException(e);
            showNtwrkFailToast("4");
        }

    }

    public void saveData() {
        try{
            showProgress();
            dataPart = new HashMap<>();
            dataPart.put("originMeetName", GlobalInfo.currentGroup.getMeetName());
            dataPart.put("meetName", meetName);
            dataPart.put("meetInterest", meetInterest);
            dataPart.put("purposeMessage", purposeMessage);
            dataPart.put("message", message);

            MultipartBody.Part titlePart = null;
            MultipartBody.Part introPart = null;

            if (titleImgPath != null) {
                File file = new File(titleImgPath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                titlePart = MultipartBody.Part.createFormData("titleImg", file.getName(), requestBody);
            }

            if (introImgPath != null) {
                File file = new File(introImgPath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                introPart = MultipartBody.Part.createFormData("introImg", file.getName(), requestBody);
            }


            RetrofitService retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);
            retrofitService.updateItemBaseDataToModifyActivity(dataPart, titlePart, introPart).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject res = new JSONObject(response.body());
                        int code  = res.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                        switch (code) {
                            case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                JSONObject result = res.getJSONObject(GlobalKey.NTWRK_RTN_TYPE.RESULT);
                                GlobalInfo.currentGroup.setMeetName(meetName);
                                GlobalInfo.currentGroup.setMeetInterest(meetInterest);
                                GlobalInfo.currentGroup.setPurposeMessage(purposeMessage);
                                GlobalInfo.currentGroup.setMessage(message);
                                if (result.has("titleImgUrl")){
                                    GlobalInfo.currentGroup.setTitleImgUrl(result.getString("titleImgUrl"));
                                }
                                if (result.has("introImgUrl")){
                                    GlobalInfo.currentGroup.setIntroImgUrl(result.getString("introImgUrl"));
                                }
                                hideProgress();
                                toPrev();
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                showNtwrkFailToast("1");
                                break;
                            default:
                                showNtwrkFailToast("2");
                                break;
                        }
                    } catch (Exception e){
                        showNtwrkFailToast("3");
                        logException(e);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    showNtwrkFailToast("4");
                    DMFBCrash.logException(t);
                }
            });
        } catch (Exception e) {
            showNtwrkFailToast("5");
            logException(e);
        }
    }
}



