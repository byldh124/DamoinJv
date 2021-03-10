package com.moondroid.project01_meetingapp.main_bnv04location;

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
import com.naver.maps.map.util.FusedLocationSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFragmentBottomTab4 extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private FusedLocationSource locationSource;
    private NaverMap mNaverMap;
    private ArrayList<MoimVO> moimVOS;
    private int i = 0;
    private final int PERMISSION_REQUEST_CODE = 10;

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
        //네이버 맵에 대한 세팅
        //보여지는 화면 설정
        naverMap.setMapType(NaverMap.MapType.Basic);

        //건물 표시
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);
        naverMap.setIndoorEnabled(true);
    
        //카메라 포지션 설정 [위치, 줌, 방향, 기울기]
        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5670135, 126.9783740), 16, 0, 0);
        naverMap.setCameraPosition(cameraPosition);
        mNaverMap = naverMap;
        loadMoimInfo();
        mNaverMap.setLocationSource(locationSource);
        UiSettings uiSettings = mNaverMap.getUiSettings();
        mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        
        //네이버맵 UI 세팅
        uiSettings.setCompassEnabled(true);
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setLogoClickEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);

    }

    //MapView 사용시 프레그먼트, 액티비티의 생명주기에 따른 MapView 생명주기를 호출해 줘야 함.
    //fragment 사용하는 걸 권장하지만, fragment 사용시 마커가 잘 생성되지 않는 버그가 있음.
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
                MoimVO moimVO = response.body().get(i);
                if (Integer.parseInt(moimVO.getDate().replace(".","")) >= Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()))) {
                    moimVOS.add(response.body().get(i));
                }
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
