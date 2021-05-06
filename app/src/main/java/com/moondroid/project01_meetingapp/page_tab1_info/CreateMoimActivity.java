package com.moondroid.project01_meetingapp.page_tab1_info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.variableobject.MoimVO;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMoimActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int REQUEST_CODE_FOR_LOCATION_CHOICE = 0;
    private final int PERMISSION_REQUEST_CODE = 5;
    private Geocoder geocoder;
    private double lat;
    private double lng;
    private MapView mapView;
    private FusedLocationSource locationSource;
    private NaverMap mNaverMap;
    private Marker marker;
    private Toolbar toolbar;
    private TextView tvLocation;
    private TextView tvMoimDate;
    private TextView tvMoimTime;
    private EditText etMoimPay;
    private String address;
    private int y = 0, m = 0, d = 0, h = 0, mi = 0;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_moim);

        //xml 참조영역
        tvLocation = findViewById(R.id.tv_moim_location);
        tvMoimDate = findViewById(R.id.tv_moim_date);
        tvMoimTime = findViewById(R.id.tv_moim_time);
        etMoimPay = findViewById(R.id.et_moim_pay);

        //액션바 세팅
        toolbar = findViewById(R.id.toolbar_create_moim_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //네이버 맵 맵뷰 세팅
        //fragment 사용을 권장하지만 Marker가 제대로 보이지 않아서 맵뷰를 사용함
        mapView = findViewById(R.id.map_view_moim);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
        geocoder = new Geocoder(this, Locale.KOREAN);
        marker = new Marker();

    }

    public void clickDate(View view) {
        showDate();
    }

    public void clickTime(View view) {
        showTime();
    }

    public void clickSave(View view) {
        String moimDate = tvMoimDate.getText().toString();
        String moimTime = tvMoimTime.getText().toString();
        String moimPay = etMoimPay.getText().toString();
        String moimAddress = tvLocation.getText().toString();

        //기입된 정보 확인작업
        if (moimDate.equals("")) return;
        if (moimTime.equals("")) return;
        if (moimPay.equals("")) return;
        if (moimAddress.equals("")) return;

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("잠시만 기다려주십시오.");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        progressDialog.show();

        //정모 내용 서버에 저장
        ArrayList<String> joinMembersForJson = new ArrayList<>();
        joinMembersForJson.add(G.myProfile.getUserId());
        String joinMembers = new Gson().toJson(joinMembersForJson);
        MoimVO moimVO = new MoimVO(G.currentItemBase.getMeetName(), moimAddress, moimDate, moimTime, moimPay, lat, lng, joinMembers);
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).saveMoimInfo(moimVO).enqueue(new Callback<MoimVO>() {
            @Override
            public void onResponse(Call<MoimVO> call, Response<MoimVO> response) {
                Toast.makeText(CreateMoimActivity.this, "모임내용이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).sendFCMMessageMoim(G.currentItemBase.getMeetName()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        progressDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(Call<MoimVO> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreateMoimActivity.this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void clickChoiceMoimLocation(View view) {
        Intent intent = new Intent(this, ChoiceMoimActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FOR_LOCATION_CHOICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        //지역검색 화면에서 가져온 값으로 지도에 마커 표시.
        switch (requestCode) {
            case REQUEST_CODE_FOR_LOCATION_CHOICE:
                address = data.getStringExtra("address");
                tvLocation.setText(address);
                lat = data.getDoubleExtra("lat", 37.570975);
                lng = data.getDoubleExtra("lng", 126.977759);
                marker.setPosition(new LatLng(lat, lng));
                marker.setMap(mNaverMap);
                CameraPosition cameraPosition = new CameraPosition(new LatLng(lat, lng), 16, 0, 0);
                mNaverMap.setCameraPosition(cameraPosition);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setMapType(NaverMap.MapType.Basic);

        //건물 표시
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);
        naverMap.setIndoorEnabled(true);

        //카메라 세팅
        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5670135, 126.9783740), 16, 0, 0);
        naverMap.setCameraPosition(cameraPosition);
        mNaverMap = naverMap;
        mNaverMap.setLocationSource(locationSource);
        UiSettings uiSettings = mNaverMap.getUiSettings();
        mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    
        //네이버맵 UI 설정. 로고 클릭은 반드시 true 값으로 해야함(정책사항)
        uiSettings.setCompassEnabled(true);
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setLogoClickEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);
    }

    //맵뷰 사용시 액티비티의 생명주기에 따른 맵뷰 생명주기 설정.
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    void showDate() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month + 1;
                d = dayOfMonth;

                @SuppressLint("DefaultLocale") String year1 = String.format("%04d", y);
                @SuppressLint("DefaultLocale") String month1 = String.format("%02d", m);
                @SuppressLint("DefaultLocale") String day1 = String.format("%02d", d);
                tvMoimDate.setText(year1 + "." + month1 + "." + day1);
            }
        }, Integer.parseInt(new SimpleDateFormat("yyyy").format(date)), Integer.parseInt(new SimpleDateFormat("MM").format(date)) - 1, Integer.parseInt(new SimpleDateFormat("dd").format(date)));

        datePickerDialog.setMessage("메시지");
        datePickerDialog.show();
    }

    void showTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                h = hourOfDay;
                mi = minute;
                @SuppressLint("DefaultLocale") String hour = String.format("%02d", h);
                @SuppressLint("DefaultLocale") String minute2 = String.format("%02d", mi);
                tvMoimTime.setText(hour + ":" + minute2);
            }
        }, 12, 0, true);

        timePickerDialog.setMessage("메시지");
        timePickerDialog.show();
    }
}