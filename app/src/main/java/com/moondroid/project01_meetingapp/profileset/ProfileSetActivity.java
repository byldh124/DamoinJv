package com.moondroid.project01_meetingapp.profileset;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.account.LocationChoiceActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetActivity extends AppCompatActivity {

    final int REQUEST_CODE_FOR_LOCATION_CHOICE = 0;

    Toolbar toolbar;
    CircleImageView ivProfileImg;
    EditText etName;
    RadioGroup radioGroupGender;
    TextView tvBirthDate;
    TextView tvLocation;
    EditText etMessage;
    TextView tvMessageLength;

    int y = 0, m = 0, d = 0;

    String location;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void clickSave(View view) {
    }

    public void clickBirth(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth ,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month + 1;
                d = dayOfMonth;

                tvBirthDate.setText("" + y + "." + m + "." + d);

            }
        }, 1990, 0, 1);
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
        if (requestCode == REQUEST_CODE_FOR_LOCATION_CHOICE){
            location = data.getStringExtra("location");
            String[] locations = location.split(" ");
            tvLocation.setText(locations[0]);
        }
    }
}
