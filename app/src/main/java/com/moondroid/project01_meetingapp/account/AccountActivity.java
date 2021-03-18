package com.moondroid.project01_meetingapp.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class AccountActivity extends AppCompatActivity {

    private final int REQUEST_CODE_FOR_LOCATION_CHOICE = 0;
    private final int REQUEST_CODE_FOR_INTEREST_SELECT = 1;

    private Toolbar toolbarAccountActivity;
    private TextView tvLocation, tvBirthDate, tvUserInterest;
    private EditText etId, etPassword, etPasswordCheck, etName;
    private RadioGroup radioGroup;
    private String userId, userPassword, userName, userBirthDate, userGender, userAddress, userInterest;

    private int y = 0, m = 0, d = 0;

    private boolean idChecked = false;

    private UserBaseVO userBaseVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //xml Reference
        tvLocation = findViewById(R.id.tv_location_add_account);
        tvBirthDate = findViewById(R.id.tv_birth_date_add_account);
        tvUserInterest = findViewById(R.id.tv_interest_add_account);
        etId = findViewById(R.id.et_id_add_account);
        etPassword = findViewById(R.id.et_password_add_account);
        etPasswordCheck = findViewById(R.id.et_password_check_add_account);
        etName = findViewById(R.id.et_name_add_account);
        radioGroup = findViewById(R.id.radio_group_account);
        toolbarAccountActivity = findViewById(R.id.toolbar_account_activity);
        
        //액션바 세팅
        setSupportActionBar(toolbarAccountActivity);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userGender = "남자";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                userGender = radioButton.getText().toString();
            }
        });
        
        //중복확인 후 아이디 기입을 다시 할시 중복확인 제거
        etId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {idChecked = false;}
            @Override
            public void afterTextChanged(Editable s) {}
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //지역 설정 화면으로 전환
    public void clickLocationAccount(View view) {
        Intent intent = new Intent(this, LocationChoiceActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FOR_LOCATION_CHOICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case REQUEST_CODE_FOR_LOCATION_CHOICE:
                userAddress = data.getStringExtra("location");
                tvLocation.setText(userAddress);
                break;
            case REQUEST_CODE_FOR_INTEREST_SELECT:
                userInterest = data.getStringExtra("interest");
                tvUserInterest.setText(userInterest);
                break;
        }

    }

    public void clickSave(View view) {

        userId = etId.getText().toString();
        userPassword = etPassword.getText().toString();
        userName = etName.getText().toString();
        userBirthDate = tvBirthDate.getText().toString();
        userAddress = tvLocation.getText().toString();
        
        //유저가 기입한 정보 확인
        if (idChecked == false) {
            Toast.makeText(this, "아이디 중복을 확인해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (userId == null || userId.equals("")) {
            Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (userPassword == null || userPassword.equals("") || !(userPassword.equals(etPasswordCheck.getText().toString()))) {
            Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (userName == null || userName.equals("")) {
            Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (userBirthDate == null || userBirthDate.equals("")) {
            Toast.makeText(this, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (userAddress == null || userAddress.equals("")) {
            Toast.makeText(this, "주소를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (userInterest == null || userInterest.equals("")) {
            Toast.makeText(this, "관심사를 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        saveDataToRetrofit();

    }

    //생년월일 기입에 대한 다이얼로그
    public void clickBirth(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
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

    //관심사 선택 화면으로 전환
    public void clickInterest(View view) {
        Intent intent = new Intent(this, InterestActivity.class);
        intent.putExtra("sendClass", "Account");
        startActivityForResult(intent, REQUEST_CODE_FOR_INTEREST_SELECT);
    }

    //아이디 중복 확인 작업
    public void clickAccountCheck(View view) {
        Retrofit retrofit = RetrofitHelper.getRetrofitInstanceScalars();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<String> call = retrofitService.checkUserId(etId.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String str = response.body();
                if (str.equals("isExist")) {
                    new AlertDialog.Builder(AccountActivity.this).setMessage("존재하는 아이디 입니다.").setPositiveButton("확인", null).create().show();
                } else {
                    new AlertDialog.Builder(AccountActivity.this).setMessage("사용할 수 있는 아이디 입니다.\n이 아이디를 사용하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            idChecked = true;
                        }
                    }).create().show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    //유저가 기입한 정보를 DB에 저장
    public void saveDataToRetrofit() {
        userBaseVO = new UserBaseVO(userId, userPassword, userName, userBirthDate, userGender, userAddress, userInterest, null, null, null);
        Retrofit retrofit = RetrofitHelper.getRetrofitInstanceGson();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<UserBaseVO> call = retrofitService.saveUserBaseDataToAccountActivity(userBaseVO);
        call.enqueue(new Callback<UserBaseVO>() {
            @Override
            public void onResponse(Call<UserBaseVO> call, Response<UserBaseVO> response) {
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userId", userId).commit();
                G.myProfile = userBaseVO;
                if (G.myProfile.getUserProfileImgUrl() == null || G.myProfile.getUserProfileImgUrl().equals("")){
                    G.myProfile.setUserProfileImgUrl("./userProfileImg/IMG_20210302153242unnamed.jpg");
                }
                if (G.myProfile.getUserProfileMessage() == null || G.myProfile.getUserProfileMessage().equals("")){
                    G.myProfile.setUserProfileMessage("만나서 반갑습니다.");
                }
                Toast.makeText(AccountActivity.this, "" + response.body().getUserId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                intent.putExtra("sendActivity", "loginActivity");
                startActivity(intent);
                setResult(RESULT_OK, null);
                finish();
            }

            @Override
            public void onFailure(Call<UserBaseVO> call, Throwable t) {

            }
        });
    }
}