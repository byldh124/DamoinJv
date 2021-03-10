package com.moondroid.project01_meetingapp.main;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.account.InterestActivity;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.main_bnv01meet.MeetFragmentBottomTab1;
import com.moondroid.project01_meetingapp.main_bnv02charge.ChargeFragmentBottomTab2;
import com.moondroid.project01_meetingapp.main_bnv03mypage.MyPageFragmentBottomTab3;
import com.moondroid.project01_meetingapp.main_bnv04location.LocationFragmentBottomTab4;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.mypages.MyPageFavoriteActivity;
import com.moondroid.project01_meetingapp.mypages.MyPageMKChargeActivity;
import com.moondroid.project01_meetingapp.mypages.MyPagePremiumActivity;
import com.moondroid.project01_meetingapp.mypages.MyPageRecentActivity;
import com.moondroid.project01_meetingapp.mypages.MyPageSettingActivity;
import com.moondroid.project01_meetingapp.option01search.SearchActivity;
import com.moondroid.project01_meetingapp.option02notification.NotificationActivity;
import com.moondroid.project01_meetingapp.profileset.ProfileSetActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_FOR_PROFILE_SET = -1;
    private final int REQUEST_CODE_FOR_INTEREST_SET = -2;
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
    private String token;
    private CircleImageView ivNavigationUserProfileImg;
    private TextView tvNavigationUserName;
    private TextView tvNavigationUserMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //동적 퍼미션
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions, 100);
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
                        break;
                    case R.id.bnv_tab2:
                        if (fragments[1] == null) {
                            fragments[1] = new ChargeFragmentBottomTab2();
                            transaction.add(R.id.container_fragment_main, fragments[1]);
                        }
                        transaction.show(fragments[1]);
                        tvMainTitle.setText(R.string.bottom_tab2_title);
                        break;
                    case R.id.bnv_tab3:
                        if (fragments[2] == null) {
                            fragments[2] = new MyPageFragmentBottomTab3();
                            transaction.add(R.id.container_fragment_main, fragments[2]);
                        }
                        transaction.show(fragments[2]);
                        tvMainTitle.setText(R.string.bottom_tab3_title);
                        break;
                    case R.id.bnv_tab4:
                        if (fragments[3] == null) {
                            fragments[3] = new LocationFragmentBottomTab4();
                            transaction.add(R.id.container_fragment_main, fragments[3]);
                        }
                        transaction.show(fragments[3]);
                        tvMainTitle.setText(R.string.bottom_tab4_title);
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
                    token = task.getResult();
                    saveToken();
                } catch (Exception e) {

                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (G.myProfile != null) loadUserInformation();
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
        if (G.myProfile.getUserProfileImgUrl() != null) {
            if (G.myProfile.getUserProfileImgUrl().contains("http")) {
                Glide.with(MainActivity.this).load(G.myProfile.getUserProfileImgUrl()).into(ivNavigationUserProfileImg);
            } else {
                Glide.with(MainActivity.this).load(RetrofitHelper.getUrlForImg() + G.myProfile.getUserProfileImgUrl()).into(ivNavigationUserProfileImg);
            }
        }
        if (G.myProfile.getUserName() != null) {
            tvNavigationUserName.setText(G.myProfile.getUserName());
        }
        if (G.myProfile.getUserProfileMessage() != null) {
            tvNavigationUserMessage.setText(G.myProfile.getUserProfileMessage());
        }
    }

    //푸시 서비스를 위한 Token을 DB에 저장
    public void saveToken() {

        RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).saveFCMToken(G.myProfile.getUserId(), token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}