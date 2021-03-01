package com.moondroid.project01_meetingapp.page_tab1_info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moondroid.project01_meetingapp.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateMoimActivity extends AppCompatActivity implements OnMapReadyCallback {

    FragmentManager fm;
    MapFragment mapFragment;

    Geocoder geocoder;

    String clientId = "csit8ak1h9";
    String clientPw = "MfeaQuyD4fb728rDiVQIXFvIWmgLnVFg8wxPVBhl";

    EditText etLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_moim);

        fm = getSupportFragmentManager();
        mapFragment = (MapFragment)fm.findFragmentById(R.id.map_view_moim);

        etLocation = findViewById(R.id.et_moim_location);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_view_moim, mapFragment).commit();
        }

        geocoder = new Geocoder(this, Locale.KOREAN);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        UiSettings uiSettings = naverMap.getUiSettings();

        uiSettings.setLocationButtonEnabled(false);

        // 지도 유형 위성사진으로 설정
        naverMap.setMapType(NaverMap.MapType.Basic);
        Marker marker = new Marker();
        naverMap.setLocationTrackingMode(LocationTrackingMode.None);
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                marker.setPosition(latLng);
                marker.setMap(naverMap);
            }
        });
    }

    public void clickDate(View view) {
    }

    public void clickTime(View view) {
    }

    public void clickSave(View view) {
    }

    public void clickFindLocation(View view) {
        String locationQuery = etLocation.getText().toString();
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationQuery, 10);
            Toast.makeText(this, "" + addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}