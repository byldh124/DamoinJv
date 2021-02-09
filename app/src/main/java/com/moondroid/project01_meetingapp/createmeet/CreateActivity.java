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

import com.bumptech.glide.Glide;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.account.InterestActivity;
import com.moondroid.project01_meetingapp.account.LocationChoiceActivity;

public class CreateActivity extends AppCompatActivity {
    final int REQUEST_CODE_FOR_INTEREST = 0;
    final int REQUEST_CODE_FOR_LOCATION = 1;
    final int REQUEST_CODE_FOR_IMAGE = 2;

    String interest;
    String location;
    String iconUrl;
    String meetName;
    String titleImgUrl;


    Toolbar toolbarCreateActivity;
    EditText etMeetName;
    TextView locationInCreate;
    ImageView ivInterestChoose, ivTitleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        locationInCreate = findViewById(R.id.tv_create_location);
        toolbarCreateActivity = findViewById(R.id.toolbar_create_activity);
        ivInterestChoose = findViewById(R.id.iv_choose_interest);
        etMeetName = findViewById(R.id.et_meet_name);
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
                    location = data.getStringExtra("location");
                    locationInCreate.setText(location);
                }

            case REQUEST_CODE_FOR_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri imgUri = data.getData();
                    if (imgUri != null) {
                        titleImgUrl = imgUri.toString();
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
    }

    public void clickImageInput(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_IMAGE);
    }
}