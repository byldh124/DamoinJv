package com.moondroid.project01_meetingapp.profileset;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.account.LocationChoiceActivity;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileSetActivity extends AppCompatActivity {

    private final int REQUEST_CODE_FOR_LOCATION_CHOICE = 0;
    private final int REQUEST_CODE_FOR_PROFILE_IMAGE_SELECT = 1;

    private Toolbar toolbar;
    private CircleImageView ivProfileImg;
    private EditText etName;
    private RadioGroup radioGroupGender;
    private TextView tvBirthDate;
    private TextView tvLocation;
    private EditText etMessage;
    private TextView tvMessageLength;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Uri uriFromGallery;

    private String imgPath;

    private boolean imgIsChanged = false;

    private int y = 0, m = 0, d = 0;

    private String location, gender;

    private Map<String, String> dataPart;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);

        //xml Reference
        toolbar = findViewById(R.id.toolbar_profile_set_activity);
        ivProfileImg = findViewById(R.id.iv_profile_set_profile_img);
        etName = findViewById(R.id.et_profile_set_name);
        radioGroupGender = findViewById(R.id.radio_group_profile_set);
        tvBirthDate = findViewById(R.id.tv_profile_set_birth);
        tvLocation = findViewById(R.id.tv_profile_set_location);
        etMessage = findViewById(R.id.et_profile_set_message);
        tvMessageLength = findViewById(R.id.tv_message_length);
        radioButtonMale = findViewById(R.id.radio_button_profile_set_male);
        radioButtonFemale = findViewById(R.id.radio_button_profile_set_female);

        //Action Bar Setting
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadBasicInfo();

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvMessageLength.setText(String.valueOf(s.length()).concat("/50자"));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (G.myProfile.getUserGender() != null) gender = G.myProfile.getUserGender();
        else gender = "남자";
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (checkedId == R.id.radio_button_profile_set_male) gender = "남자";
                else gender = "여자";
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void clickSaveToRetrofit(View view) {

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("잠시만 기다려주십시오.");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        progressDialog.show();

        Retrofit retrofit = RetrofitHelper.getRetrofitInstanceScalars();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        dataPart = new HashMap<>();
        dataPart.put("userId", G.myProfile.getUserId());
        dataPart.put("userName", etName.getText().toString());
        dataPart.put("userBirthDate", tvBirthDate.getText().toString());
        dataPart.put("userGender", gender);
        dataPart.put("userLocation", G.myProfile.getUserLocation());
        dataPart.put("userProfileMessage", etMessage.getText().toString());

        MultipartBody.Part filePart = null;

        if (imgIsChanged && imgPath != null) {
            File file = new File(imgPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            filePart = MultipartBody.Part.createFormData("img", file.getName(), requestBody);
        }

        Call<String> call = retrofitService.updateUserProfileImg(dataPart, filePart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (imgIsChanged) G.myProfile.setUserProfileImgUrl(response.body());
                G.myProfile.setUserName(dataPart.get("userName"));
                G.myProfile.setUserGender(dataPart.get("userGender"));
                G.myProfile.setUserLocation(dataPart.get("userLocation"));
                G.myProfile.setUserBirthDate(dataPart.get("userBirthDate"));
                G.myProfile.setUserProfileMessage(dataPart.get("userProfileMessage"));
                progressDialog.dismiss();
                finish();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfileSetActivity.this, "서버에 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clickBirth(View view) {
        String birthDate = tvBirthDate.getText().toString();
        String[] birthDates = birthDate.split("\\.");
        int userYear, userMonth, userDate;
        try {
            userYear = Integer.parseInt(birthDates[0]);
            userMonth = Integer.parseInt(birthDates[1]);
            userDate = Integer.parseInt(birthDates[2]);
        } catch (Exception e) {
            userYear = 1990;
            userMonth = 1;
            userDate = 1;
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month + 1;
                d = dayOfMonth;
                tvBirthDate.setText(String.valueOf(y).concat(".").concat(String.valueOf(m)).concat(".").concat(String.valueOf(d)));

            }
        }, userYear, userMonth - 1, userDate);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }

    public void clickLocation(View view) {
        Intent intent = new Intent(this, LocationChoiceActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FOR_LOCATION_CHOICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case REQUEST_CODE_FOR_LOCATION_CHOICE:
                location = data.getStringExtra("location");
                G.myProfile.setUserLocation(location);
                String[] locations = location.split(" ");
                tvLocation.setText(locations[0]);
                break;
            case REQUEST_CODE_FOR_PROFILE_IMAGE_SELECT:
                uriFromGallery = data.getData();
                if (uriFromGallery != null) {
                    imgPath = getRealPathFromUri(uriFromGallery);
                    Bitmap bitMap = BitmapFactory.decodeFile(imgPath);
                    int h = bitMap.getHeight();
                    int w = bitMap.getWidth();
                    if (h>=2400 || w>=2400){
                        new AlertDialog.Builder(this).setMessage("파일 용량이 너무 큽니다.").setPositiveButton("확인", null).create().show();
                    } else {
                        imgIsChanged = true;
                        Glide.with(this).load(uriFromGallery).into(ivProfileImg);
                    }
                }
                break;


        }
    }

    public void loadBasicInfo() {
        if (G.myProfile == null) return;
        if (G.myProfile.getUserName() != null) etName.setText(G.myProfile.getUserName());
        if (G.myProfile.getUserProfileImgUrl() != null) {
            if (G.myProfile.getUserProfileImgUrl().contains("http")) {
                Glide.with(this).load(G.myProfile.getUserProfileImgUrl()).into(ivProfileImg);
            } else {
                Glide.with(this).load(RetrofitHelper.getUrlForImg() + G.myProfile.getUserProfileImgUrl()).into(ivProfileImg);
            }
        } else {
            Glide.with(this).load(R.mipmap.ic_launcher).into(ivProfileImg);
        }
        if (G.myProfile.getUserGender() != null && G.myProfile.getUserGender().equals("여자")) {
            radioButtonMale.setChecked(false);
            radioButtonFemale.setChecked(true);
        }
        if (G.myProfile.getUserBirthDate() != null)
            tvBirthDate.setText(G.myProfile.getUserBirthDate());
        if (G.myProfile.getUserLocation() != null) {
            String[] locations = G.myProfile.getUserLocation().split(" ");
            tvLocation.setText(locations[0]);
        }
        if (G.myProfile.getUserProfileMessage() != null) {
            etMessage.setText(G.myProfile.getUserProfileMessage());
            tvMessageLength.setText(String.valueOf(etMessage.getText().toString().length()).concat("/50자"));
        }

    }

    public void clickProfileImg(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_PROFILE_IMAGE_SELECT);
    }

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
