package com.moondroid.project01_meetingapp.profileset;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetActivity extends AppCompatActivity {

    final int REQUEST_CODE_FOR_LOCATION_CHOICE = 0;
    final int REQUEST_CODE_FOR_PROFILE_IMAGE_SELECT = 1;

    Toolbar toolbar;
    CircleImageView ivProfileImg;
    EditText etName;
    RadioGroup radioGroupGender;
    TextView tvBirthDate;
    TextView tvLocation;
    EditText etMessage;
    TextView tvMessageLength;
    RadioButton radioButtonMale, radioButtonFemale;
    Uri uriFromGallery;

    FirebaseStorage firebaseStorage;
    StorageReference profileImgRef;

    boolean imgIsChanged = false;

    int y = 0, m = 0, d = 0;

    String location, gender;

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
                tvMessageLength.setText(String.valueOf(s.length()).concat("/40자"));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (G.myProfile.userGender != null) gender = G.myProfile.userGender;
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

    public void clickSave(View view) {
        if (imgIsChanged){
            firebaseStorage = FirebaseStorage.getInstance();
            profileImgRef = firebaseStorage.getReference("userProfileImages/"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".png");
            profileImgRef.putFile(uriFromGallery).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            G.myProfile = new UserBaseVO(G.myProfile.userId, G.myProfile.userPassword, etName.getText().toString(), tvBirthDate.getText().toString(), gender, G.myProfile.userAddress, G.myProfile.userInterest, uri.toString(), etMessage.getText().toString());
                            G.usersRef.child(G.myProfile.userId).child("base").setValue(G.myProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProfileSetActivity.this, "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    });
                }
            });
        } else {
            G.myProfile = new UserBaseVO(G.myProfile.userId, G.myProfile.userPassword, etName.getText().toString(), tvBirthDate.getText().toString(), gender, G.myProfile.userAddress, G.myProfile.userInterest, G.myProfile.userProfileImgUrl, etMessage.getText().toString());
            G.usersRef.child(G.myProfile.userId).child("base").setValue(G.myProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ProfileSetActivity.this, "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }


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
                G.myProfile.userAddress = location;
                String[] locations = location.split(" ");
                tvLocation.setText(locations[0]);
                break;
            case REQUEST_CODE_FOR_PROFILE_IMAGE_SELECT:
                uriFromGallery = data.getData();
                imgIsChanged = true;
                Glide.with(this).load(uriFromGallery).into(ivProfileImg);
                break;


        }
    }

    public void loadBasicInfo() {
        if (G.myProfile == null) return;
        if (G.myProfile.userName != null) etName.setText(G.myProfile.userName);
        if (G.myProfile.userProfileImgUrl != null)
            Glide.with(this).load(G.myProfile.userProfileImgUrl).into(ivProfileImg);
        if (G.myProfile.userGender != null && G.myProfile.userGender.equals("여자")) {
            radioButtonMale.setChecked(false);
            radioButtonFemale.setChecked(true);
        }
        if (G.myProfile.userBirthDate != null) tvBirthDate.setText(G.myProfile.userBirthDate);
        if (G.myProfile.userAddress != null) {
            String[] locations = G.myProfile.userAddress.split(" ");
            tvLocation.setText(locations[0]);
        }
        if (G.myProfile.userProfileMessage != null)
            etMessage.setText(G.myProfile.userProfileMessage);

    }

    public void clickProfileImg(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_PROFILE_IMAGE_SELECT);
    }
}
