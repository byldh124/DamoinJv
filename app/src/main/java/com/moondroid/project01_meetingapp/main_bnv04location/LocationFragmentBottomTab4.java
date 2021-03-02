package com.moondroid.project01_meetingapp.main_bnv04location;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.variableobject.MoimVO;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFragmentBottomTab4 extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    FusedLocationSource locationSource;
    NaverMap mNaverMap;
    ArrayList<MoimVO> moimVOS;
    int i = 0;

    final int PERMISSION_REQUEST_CODE = 10;

    public LocationFragmentBottomTab4() {
    }

    public static LocationFragmentBottomTab4 newInstance() {
        LocationFragmentBottomTab4 fragment = new LocationFragmentBottomTab4();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_bottom_tab4_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.naver_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        moimVOS = new ArrayList<>();

        locationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setMapType(NaverMap.MapType.Basic);

        //건물 표시
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);
        naverMap.setIndoorEnabled(true);

        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5670135, 126.9783740), 16, 0, 0);
        naverMap.setCameraPosition(cameraPosition);
        mNaverMap = naverMap;
        loadMoimInfo();
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
    public void onStart() {
        String addr;

        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void loadMoimInfo() {
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadMoimsAll().enqueue(new Callback<ArrayList<MoimVO>>() {
            @Override
            public void onResponse(Call<ArrayList<MoimVO>> call, Response<ArrayList<MoimVO>> response) {

                moimVOS = response.body();
                for (i = 0; i < moimVOS.size(); i++) {
                    Marker marker = new Marker(new LatLng(moimVOS.get(i).getLat(), moimVOS.get(i).getLng()));
                    marker.setMap(mNaverMap);
                    InfoWindow infoWindow = new InfoWindow();
                    infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getContext()) {
                        @NonNull
                        @Override
                        public CharSequence getText(@NonNull InfoWindow infoWindow) {
                            return moimVOS.get(i).getMeetName() + "\n" + moimVOS.get(i).getDate() + " " + moimVOS.get(i).getTime();
                        }
                    });
                    infoWindow.open(marker);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MoimVO>> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}
