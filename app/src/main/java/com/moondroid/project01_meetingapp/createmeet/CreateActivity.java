package com.moondroid.project01_meetingapp.createmeet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.account.InterestActivity;
import com.moondroid.project01_meetingapp.account.LocationChoiceActivity;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateActivity extends AppCompatActivity {
    final int REQUEST_CODE_FOR_INTEREST = 0;
    final int REQUEST_CODE_FOR_LOCATION = 1;
    final int REQUEST_CODE_FOR_IMAGE = 2;

    String interest;
    String meetAddress;
    String meetName;
    String titleImgUrl;
    String purposeMessage;

    Uri imgUri;

    String iconUrl;
    Toolbar toolbarCreateActivity;
    EditText etMeetName,etPurposeMessage;
    TextView locationInCreate;
    ImageView ivInterestChoose, ivTitleImage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference itemsRef;
    FirebaseStorage firebaseStorage;
    StorageReference titleImgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        locationInCreate = findViewById(R.id.tv_create_location);
        toolbarCreateActivity = findViewById(R.id.toolbar_create_activity);
        ivInterestChoose = findViewById(R.id.iv_choose_interest);
        etMeetName = findViewById(R.id.et_meet_name);
        etPurposeMessage = findViewById(R.id.et_purpose_message);
        ivTitleImage = findViewById(R.id.iv_title_image);

        setSupportActionBar(toolbarCreateActivity);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToInterestActivity();
            }
        }, 300);// 0.5초 정도 딜레이를 준 후 시작

    }

    public void moveToInterestActivity() {
        Intent intent = new Intent(this, InterestActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FOR_INTEREST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_FOR_INTEREST:
                if (resultCode == RESULT_OK) {
                    interest = data.getStringExtra("interest");
                    iconUrl = data.getStringExtra("iconUrl");
                    Glide.with(this).load(iconUrl).into(ivInterestChoose);

                }
            case REQUEST_CODE_FOR_LOCATION:
                if (resultCode == RESULT_OK) {
                    meetAddress = data.getStringExtra("location");
                    locationInCreate.setText(meetAddress);
                }

            case REQUEST_CODE_FOR_IMAGE:
                if (resultCode == RESULT_OK) {
                    imgUri = data.getData();
                    if (imgUri != null) {
                        Glide.with(this).load(imgUri).into(ivTitleImage);
                    }
                }
        }
    }

    public void clickSearchLocation(View view) {
        Intent intent = new Intent(this, LocationChoiceActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FOR_LOCATION);
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

        firebaseDatabase = FirebaseDatabase.getInstance();
        itemsRef = firebaseDatabase.getReference("items");
        firebaseStorage = FirebaseStorage.getInstance();
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).concat(".png");
        titleImgRef = firebaseStorage.getReference("meetTitleImg/" + fileName);
        meetName = etMeetName.getText().toString();
        purposeMessage = etPurposeMessage.getText().toString();

        if (meetName == null || meetName.equals("")){
            Toast.makeText(this, "모임 이름을 설정해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (meetAddress == null || meetAddress.equals("")){
            Toast.makeText(this, "모임 지역을 설정해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (purposeMessage == null || purposeMessage.equals("")){
            Toast.makeText(this,"모임 목표를 작성해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (interest == null || interest.equals("")){
            Toast.makeText(this, "관심사를 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        titleImgRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                titleImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        titleImgUrl = uri.toString();
                        itemsRef.child(meetName).child("base").setValue(new ItemBaseVO(meetName, meetAddress,purposeMessage,titleImgUrl,interest)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CreateActivity.this, "다올라갔습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                });
            }
        });

        
    }

    public void clickImageInput(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_IMAGE);
    }
}