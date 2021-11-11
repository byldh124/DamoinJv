package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.UserBaseVO;
import com.moondroid.project01_meetingapp.databinding.ActivityAccountBinding;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountActivity extends BaseActivity {

    private final String TAG = AccountActivity.class.getSimpleName();


    private String userId, userName, userBirthDate, userGender, userAddress, userInterest;

    private int y = 0, m = 0, d = 0;

    private boolean idChecked = false;

    private UserBaseVO userBaseVO;

    private ActivityAccountBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            layout = DataBindingUtil.setContentView(this, R.layout.activity_account);

            //액션바 세팅
            setSupportActionBar(layout.tbAccount);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            userGender = getString(R.string.male);
            layout.rgAccount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = group.findViewById(checkedId);
                    userGender = radioButton.getText().toString();
                }
            });

            //중복확인 후 아이디 기입을 다시 할시 중복확인 제거
            layout.etId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    idChecked = false;
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            layout.setAccountActivity(this);

        } catch (Exception e) {
            logException(e);
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            logException(e);
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode != RESULT_OK) return;

            switch (requestCode) {
                case GlobalKey.REQUEST_CODE.ACCOUNT01:
                    userAddress = data.getStringExtra(GlobalKey.INTENT_PARAM_TYPE.LOCATION);
                    layout.tvLocation.setText(userAddress);
                    break;
                case GlobalKey.REQUEST_CODE.ACCOUNT02:
                    userInterest = data.getStringExtra(GlobalKey.INTENT_PARAM_TYPE.INTEREST);
                    layout.tvInterest.setText(userInterest);
                    break;
            }
            layout.setAccountActivity(this);
        } catch (Exception e) {
            logException(e);
        }

    }

    /**
     * 회원가입 정보 유효성 체크 작업
     **/
    public void clickSave(View view) {
        try {
            userId = layout.etId.getText().toString();
            userName = layout.etName.getText().toString();
            userBirthDate = layout.tvBirth.getText().toString();
            userAddress = layout.tvLocation.getText().toString();

            //유저가 기입한 정보 확인
            if (!idChecked) {
                Toast.makeText(this, R.string.cmn_account_id_check, Toast.LENGTH_SHORT).show();
                return;
            } else if (userId == null || userId.equals("")) {
                Toast.makeText(this, R.string.cmn_account_id_input, Toast.LENGTH_SHORT).show();
                return;
            } else if (userName == null || userName.equals("")) {
                Toast.makeText(this, R.string.cmn_account_name_input, Toast.LENGTH_SHORT).show();
                return;
            } else if (userBirthDate == null || userBirthDate.equals("")) {
                Toast.makeText(this, R.string.cmn_account_birth_input, Toast.LENGTH_SHORT).show();
                return;
            } else if (userAddress.equals("")) {
                Toast.makeText(this, R.string.cmn_account_address_input, Toast.LENGTH_SHORT).show();
                return;
            } else if (userInterest == null || userInterest.equals("")) {
                Toast.makeText(this, R.string.cmn_account_interest_input, Toast.LENGTH_SHORT).show();
                return;
            }

            saveDataToRetrofit();

        } catch (Exception e) {
            logException(e);
        }
    }

    /**
     * 생년월일 기입에 대한 다이얼로그
     **/
    public void clickBirth(View view) {
        try {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    y = year;
                    m = month + 1;
                    d = dayOfMonth;

                    layout.tvBirth.setText(String.format(getString(R.string.string_type_date), y, m, d));
                    layout.setAccountActivity(AccountActivity.this);

                }
            }, 1990, 0, 1);
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            datePickerDialog.show();

        } catch (Exception e) {
            logException(e);
        }
    }

    /**
     * 지역 설정 화면으로 전환
     **/
    public void clickLocationAccount(View view) {
        try {
            super.goToLocation(GlobalKey.ACTIVITY_CODE.ACCOUNT_ACTIVITY, GlobalKey.REQUEST_CODE.ACCOUNT01);
        } catch (Exception e) {
            logException(e);
        }
    }

    /**
     * 관심사 선택 화면으로 전환
     **/
    public void clickInterest(View view) {
        try {
            super.goToInterest(GlobalKey.ACTIVITY_CODE.ACCOUNT_ACTIVITY, GlobalKey.REQUEST_CODE.ACCOUNT02);
        } catch (Exception e) {
            logException(e);
        }
    }

    /**
     * 아이디 중복 확인 작업
     **/
    public void clickAccountCheck(View view) {
        try {

            String userId = layout.etId.getText().toString();

            if (userId.equals("")) {
                Toast.makeText(getBaseContext(), getString(R.string.please_input_id), Toast.LENGTH_SHORT).show();
                return;
            }

            Retrofit retrofit = RetrofitHelper.getRetrofitInstanceScalars();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<String> call = retrofitService.checkUserId(layout.etId.getText().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject responseJson = new JSONObject(response.body());
                        int code = responseJson.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                        switch (code) {
                            case GlobalKey.NTWRK_RTN_TYPE.SUCCESS: {
                                boolean isUsable = responseJson.getBoolean(GlobalKey.NTWRK_RTN_TYPE.RESULT);
                                if (isUsable) {
                                    new AlertDialog.Builder(AccountActivity.this).setMessage(getString(R.string.usabe_id)).setPositiveButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            idChecked = true;
                                        }
                                    }).create().show();
                                } else {
                                    new AlertDialog.Builder(AccountActivity.this).setMessage(getString(R.string.aready_exist_id)).setPositiveButton(getString(R.string.agree), null).create().show();
                                }
                                break;
                            }
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                Toast.makeText(AccountActivity.this, getString(R.string.fail_connect_server), Toast.LENGTH_SHORT).show();
                                break;
                            default:
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
                    showNtwrkFailToast("3");
                }
            });

        } catch (Exception e) {
            showNtwrkFailToast("4");
            logException(e);
        }
    }

    /**
     * 유저 회원 가입
     */
    public void saveDataToRetrofit() {
        try {
            showProgress();

            userBaseVO = new UserBaseVO(userId, userName, userBirthDate, userGender, userAddress, userInterest, "./userProfileImg/IMG_20210302153242unnamed.jpg", "만나서 반갑습니다.", null);
            Retrofit retrofit = RetrofitHelper.getRetrofitInstanceScalars();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<String> call = retrofitService.saveUserBaseDataToAccountActivity(userBaseVO);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        hideProgress();
                        JSONObject jsonRes = new JSONObject(response.body());
                        int code = jsonRes.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                        switch (code) {
                            case GlobalKey.NTWRK_RTN_TYPE.SUCCESS: {
                                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", userId).commit();
                                GlobalInfo.myProfile = userBaseVO;
                                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                                intent.putExtra(GlobalKey.INTENT_PARAM_TYPE.SEND_ACTIVITY, GlobalKey.ACTIVITY_CODE.LOGIN_ACTIVITY);
                                startActivity(intent);
                                setResult(RESULT_OK, null);
                                finish();
                                break;
                            }
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL: {
                                Toast.makeText(getBaseContext(), String.format(getString(R.string.fail_sign_up), "1"), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            default:
                                Toast.makeText(getBaseContext(), String.format(getString(R.string.fail_sign_up), "2"), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (Exception e) {
                        logException(e);
                        Toast.makeText(getBaseContext(), String.format(getString(R.string.fail_sign_up), "3"), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(getBaseContext(), String.format(getString(R.string.fail_sign_up), "4"), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            logException(e);
            Toast.makeText(getBaseContext(), String.format(getString(R.string.fail_sign_up), "5"), Toast.LENGTH_SHORT).show();
        }
    }
}