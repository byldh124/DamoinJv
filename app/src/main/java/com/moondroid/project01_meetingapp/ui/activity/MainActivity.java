package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.message.template.FeedTemplate;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.ItemBaseVO;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.ui.fragment.ChargeFragmentBottomTab2;
import com.moondroid.project01_meetingapp.ui.fragment.LocationFragmentBottomTab4;
import com.moondroid.project01_meetingapp.ui.fragment.MeetFragmentBottomTab1;
import com.moondroid.project01_meetingapp.ui.fragment.MyPageFragmentBottomTab3;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_FOR_PROFILE_SET = -1;
    private final int REQUEST_CODE_FOR_INTEREST_SET = -2;
    private final int REQUEST_CODE_FOR_PERMISSION = 3;
    private final int REQUEST_EXIT = 0;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private BottomNavigationView bottomNavigationView;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;
    private TextView tvMainTitle;
    private NavigationView navigationView;
    private String userId;
    private View headerView;
    private CircleImageView ivNavigationUserProfileImg;
    private TextView tvNavigationUserName;
    private TextView tvNavigationUserMessage;
    private int clickedBnvPage = 0;
    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        analytics = FirebaseAnalytics.getInstance(this);
        analytics.setUserId(GlobalInfo.myProfile.getUserId());
        //카카오 SDK의 경우 Release Version과 Debug Version의 KeyHash 값이 다르다
        //그래고 release버전과 debug 버전 둘다의 키 해시값을 개발자 사이트에서 앱등록할때 해줘야 한다.
//        String keyHash = Utility.getKeyHash(this);
//        new AlertDialog.Builder(this).setMessage(keyHash).setPositiveButton("ok", null).create().show();

        //동적 퍼미션
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_FOR_PERMISSION);
        }

        //xml Reference
        toolbar = findViewById(R.id.toolbar_main);
        drawerLayout = findViewById(R.id.layout_drawer);
        bottomNavigationView = findViewById(R.id.bottom_navigation_main);
        tvMainTitle = findViewById(R.id.tv_title_main);
        navigationView = findViewById(R.id.navigation_view_main);
        headerView = navigationView.getHeaderView(0);
        ivNavigationUserProfileImg = headerView.findViewById(R.id.navigation_header_profile_image);
        tvNavigationUserName = headerView.findViewById(R.id.navigation_header_tv_name);
        tvNavigationUserMessage = headerView.findViewById(R.id.navigation_header_tv_message);

        //ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        //BottomNavigation
        fragments = new Fragment[4];
        fragmentManager = getSupportFragmentManager();
        fragments[0] = new MeetFragmentBottomTab1();
        fragmentManager.beginTransaction().add(R.id.container_fragment_main, fragments[0]).commit();

        //BottomNavigation fragment 전환시 화면 깨짐을 방지하기 위해 .hide, .show 메소드 사용
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                for (int i = 0; i < fragments.length; i++) {
                    if (fragments[i] != null) {
                        transaction.hide(fragments[i]);
                    }
                }
                switch (item.getItemId()) {
                    case R.id.bnv_tab1:
                        if (fragments[0] == null) {
                            fragments[0] = new MeetFragmentBottomTab1();
                            transaction.add(R.id.container_fragment_main, fragments[0]);
                        }
                        transaction.show(fragments[0]);
                        tvMainTitle.setText(R.string.bottom_tab1_title);
                        clickedBnvPage = 0;
                        break;
                    case R.id.bnv_tab2:
                        if (fragments[1] == null) {
                            fragments[1] = new ChargeFragmentBottomTab2();
                            transaction.add(R.id.container_fragment_main, fragments[1]);
                        }
                        transaction.show(fragments[1]);
                        tvMainTitle.setText(R.string.bottom_tab2_title);
                        clickedBnvPage = 1;
                        break;
                    case R.id.bnv_tab3:
                        if (fragments[2] == null) {
                            fragments[2] = new MyPageFragmentBottomTab3();
                            transaction.add(R.id.container_fragment_main, fragments[2]);
                        }
                        transaction.show(fragments[2]);
                        tvMainTitle.setText(R.string.bottom_tab3_title);
                        clickedBnvPage = 2;
                        break;
                    case R.id.bnv_tab4:
                        if (fragments[3] == null) {
                            fragments[3] = new LocationFragmentBottomTab4();
                            transaction.add(R.id.container_fragment_main, fragments[3]);
                        }
                        transaction.show(fragments[3]);
                        tvMainTitle.setText(R.string.bottom_tab4_title);
                        clickedBnvPage = 3;
                        break;
                }
                transaction.commit();
                return true;
            }
        });


        //NavigationView
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "android");
        navigationView.setItemIconTintList(null);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileSetActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FOR_PROFILE_SET);
            }
        });

        //네이게이션뷰의 아이템 클릭에 대한 화면 전환
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.navigation_menu_interest:
                        intent = new Intent(MainActivity.this, InterestActivity.class);
                        intent.putExtra("sendClass", "Main");
                        startActivity(intent);
                        break;
                    case R.id.navigation_menu_favorite_meet:
                        intent = new Intent(MainActivity.this, MyPageFavoriteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_menu_recent_meet:
                        intent = new Intent(MainActivity.this, MyPageRecentActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_menu_premium_meet:
                        intent = new Intent(MainActivity.this, MyPagePremiumActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_menu_mk_charge_class:
                        intent = new Intent(MainActivity.this, MyPageMKChargeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_menu_setting:
                        intent = new Intent(MainActivity.this, MyPageSettingActivity.class);
                        startActivityForResult(intent, REQUEST_EXIT);
                        break;
                }
                return true;


            }
        });

        //푸시 메세지를 보내기 위한 기기의 Token 값 가져오는 작업
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isComplete()) {
                    Toast.makeText(MainActivity.this, "앱 등록 실패", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String token = task.getResult();
                    saveToken(token);
                } catch (Exception e) {

                }
            }
        });

        // Firebase Dynamic 링크를 통해서 들어온 정보를 필터 pendingDynamicLinkData 에 Dynamic Link 의 정보가 담겨져온다.
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData != null){
//                            //TODO control Deep Link from Dynamic Link get uri
//                            Uri deepLinkUri = pendingDynamicLinkData.getLink();
////                            switch (deepLinkUri.toString){}
//
//                            Toast.makeText(MainActivity.this, deepLinkUri.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (getIntent().getStringExtra("meetName") != null){
            final String meetName = getIntent().getStringExtra("meetName");
            if (!meetName.equals("data")){
                RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).getItemBaseDataOnMain().enqueue(new Callback<ArrayList<ItemBaseVO>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ItemBaseVO>> call, Response<ArrayList<ItemBaseVO>> response) {
                        for (int j = 0; j < response.body().size(); j++) {
                            if (response.body().get(j).getMeetName().equals(meetName)){
                                GlobalInfo.currentMoim = response.body().get(j);
                                startActivity(new Intent(MainActivity.this, PageActivity.class));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ItemBaseVO>> call, Throwable t) {
                    }
                });
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_FOR_PERMISSION){
            if (getIntent().getStringExtra("sendActivity") != null && getIntent().getStringExtra("sendActivity").equals("loginActivity")){
                startActivity(new Intent(this, ProfileSetActivity.class).putExtra("sendActivity", "loginActivity"));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalInfo.myProfile != null) loadUserInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_option_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //옵션 메뉴 선택에 대한 화면 전환
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.option_search:
                intent = new Intent(this, SearchActivity.class);
                break;
            case R.id.option_notify:
                intent = new Intent(this, NotificationActivity.class);
                break;
            case R.id.option_share:

                //카카오 링크를 통해 보낼 템플릿 메세지 동적 설정
                //카카오 개발자 사이트에서 기본 템블릿 메세지를 만들어서 보낼 수 도 있다.
                FeedTemplate params = FeedTemplate
                        .newBuilder(
                        ContentObject.newBuilder(
                                "우리들의 모임 앱 다모임",
                                "https://firebasestorage.googleapis.com/v0/b/project01meetingapp.appspot.com/o/logo.png?alt=media&token=029dbc61-ab40-4c21-8da9-7dd8f56fd117",
                                LinkObject.newBuilder()
                                        .setWebUrl("https://moondroid.page.link/Zi7X")
                                        .setMobileWebUrl("https://moondroid.page.link/Zi7X").build())
                                .setDescrption("다모임에서 다양한 사람들과 새로운 취미를 시작해보세요").build())
                        .addButton(new ButtonObject("바로가기", LinkObject.newBuilder()
                                .setWebUrl("https://moondroid.page.link/Zi7X")
                                .setMobileWebUrl("https://moondroid.page.link/Zi7X").build())).build();

                //카카오링크 서비스를 통해 템플릿 메세지 보내기
                //메세지가 정상적으로 돌아갔는지는 카카오 개발자 사이트에 등록한 콜백 페이지를 통해서만 확인이 가능함.
                //앱에서는 카카오 링크가 정상적으로 열렸는지만 확인 [onSuccess()]
                KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
//                        Log.i("link error", errorResult.getErrorMessage());
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                    }
                });

                break;

        }
        if (intent != null) startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case REQUEST_CODE_FOR_PROFILE_SET:
                break;
            case REQUEST_CODE_FOR_INTEREST_SET:
                String interest = data.getStringExtra("interest");
                break;
            case REQUEST_EXIT:
                finish();
        }

    }

    //네이게이션뷰 개인 프로필 화면 설정
    public void loadUserInformation() {
        if (GlobalInfo.myProfile.getUserProfileImgUrl() != null) {
            if (GlobalInfo.myProfile.getUserProfileImgUrl().contains("http")) {
                Glide.with(MainActivity.this).load(GlobalInfo.myProfile.getUserProfileImgUrl()).into(ivNavigationUserProfileImg);
            } else {
                Glide.with(MainActivity.this).load(RetrofitHelper.getUrlForImg() + GlobalInfo.myProfile.getUserProfileImgUrl()).into(ivNavigationUserProfileImg);
            }
        }
        if (GlobalInfo.myProfile.getUserName() != null) {
            tvNavigationUserName.setText(GlobalInfo.myProfile.getUserName());
        }
        if (GlobalInfo.myProfile.getUserProfileMessage() != null) {
            tvNavigationUserMessage.setText(GlobalInfo.myProfile.getUserProfileMessage());
        }
    }

    //푸시 서비스를 위한 Token을 DB에 저장
    public void saveToken(String token) {
        RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).saveFCMToken(GlobalInfo.myProfile.getUserId(), token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        // 바텀 네비게이션 또는 네비게이션 레이아웃이 활성화 되어 있을 경우
        // 네이게이션을 비활성화 시킨 후 앱 종료 요청
        if (clickedBnvPage != 0){
            bottomNavigationView.setSelectedItemId(R.id.bnv_tab1);
            if(navigationView.getVisibility() == View.VISIBLE) {
                drawerLayout.closeDrawers();
            }
            return;
        }
        if(navigationView.getVisibility() == View.VISIBLE){
            drawerLayout.closeDrawers();
            return;
        }
        new AlertDialog.Builder(this).setMessage("나가시겠습니까?").setNegativeButton("아니오", null).setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        }).create().show();
    }
}