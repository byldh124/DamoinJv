package com.moondroid.project01_meetingapp.page_tab1_info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMoimActivity extends AppCompatActivity implements OnMapReadyCallback {
    final int REQUEST_CODE_FOR_LOCATION_CHOICE = 0;
    final int PERMISSION_REQUEST_CODE = 5;

    Geocoder geocoder;
    double lat;
    double lng;
    MapView mapView;
    FusedLocationSource locationSource;
    NaverMap mNaverMap;
    Marker marker;

    Toolbar toolbar;

    TextView tvLocation;
    TextView tvMoimDate;
    TextView tvMoimTime;
    EditText etMoimPay;

    String address;
    int y = 0, m = 0, d = 0, h = 0, mi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_moim);

        tvLocation = findViewById(R.id.tv_moim_location);
        tvMoimDate = findViewById(R.id.tv_moim_date);
        tvMoimTime = findViewById(R.id.tv_moim_time);
        etMoimPay = findViewById(R.id.et_moim_pay);

        toolbar = findViewById(R.id.toolbar_create_moim_activity);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

        if (moimDate.equals("")) return;
        if (moimTime.equals("")) return;
        if (moimPay.equals("")) return;
        if (moimAddress.equals("")) return;

        MoimVO moimVO = new MoimVO(G.currentItemBase.meetName, moimAddress, moimDate, moimTime, moimPay, lat, lng);
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).saveMoimInfo(moimVO).enqueue(new Callback<MoimVO>() {
            @Override
            public void onResponse(Call<MoimVO> call, Response<MoimVO> response) {
                Toast.makeText(CreateMoimActivity.this, "모임내용이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).sendFCMMessageMoim(G.currentItemBase.meetName).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        finish();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<MoimVO> call, Throwable t) {

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

        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5670135, 126.9783740), 16, 0, 0);
        naverMap.setCameraPosition(cameraPosition);
        mNaverMap = naverMap;
        mNaverMap.setLocationSource(locationSource);
        UiSettings uiSettings = mNaverMap.getUiSettings();
        mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        uiSettings.setCompassEnabled(true);
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setLogoClickEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);
    }

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