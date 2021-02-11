package com.moondroid.project01_meetingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.moondroid.project01_meetingapp.account.InterestActivity;
import com.moondroid.project01_meetingapp.bnv01meet.MeetFragmentBottomTab1;
import com.moondroid.project01_meetingapp.bnv02charge.ChargeFragmentBottomTab2;
import com.moondroid.project01_meetingapp.bnv03mypage.MyPageFragmentBottomTab3;
import com.moondroid.project01_meetingapp.bnv04location.LocationFragmentBottomTab4;
import com.moondroid.project01_meetingapp.mypages.MyPageFavoriteActivity;
import com.moondroid.project01_meetingapp.mypages.MyPageMKChargeActivity;
import com.moondroid.project01_meetingapp.mypages.MyPagePremiumActivity;
import com.moondroid.project01_meetingapp.mypages.MyPageRecentActivity;
import com.moondroid.project01_meetingapp.mypages.MyPageSettingActivity;
import com.moondroid.project01_meetingapp.option01search.SearchActivity;
import com.moondroid.project01_meetingapp.option02notification.NotificationActivity;
import com.moondroid.project01_meetingapp.profileset.ProfileSetActivity;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_CODE_FOR_PROFILE_SET = -1;
    final int REQUEST_CODE_FOR_INTEREST_SET = -2;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    BottomNavigationView bottomNavigationView;
    Fragment[] fragments;
    FragmentManager fragmentManager;
    TextView tvMainTitle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xml Reference
        toolbar = findViewById(R.id.toolbar_main);
        drawerLayout = findViewById(R.id.layout_drawer);
        bottomNavigationView = findViewById(R.id.bottom_navigation_main);
        tvMainTitle = findViewById(R.id.tv_title_main);
        navigationView = findViewById(R.id.navigation_view_main);

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
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileSetActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FOR_PROFILE_SET);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.navigation_menu_interest:
                        intent = new Intent(MainActivity.this, InterestActivity.class);
                        intent.putExtra("sendClass", "Main");
                        break;
                    case R.id.navigation_menu_favorite_meet:
                        intent = new Intent(MainActivity.this, MyPageFavoriteActivity.class);
                        break;
                    case R.id.navigation_menu_recent_meet:
                        intent = new Intent(MainActivity.this, MyPageRecentActivity.class);
                        break;
                    case R.id.navigation_menu_premium_meet:
                        intent = new Intent(MainActivity.this, MyPagePremiumActivity.class);
                        break;
                    case R.id.navigation_menu_mk_charge_class:
                        intent = new Intent(MainActivity.this, MyPageMKChargeActivity.class);
                        break;
                    case R.id.navigation_menu_setting:
                        intent = new Intent(MainActivity.this, MyPageSettingActivity.class);
                        break;
                }
                if (intent != null) startActivity(intent);

                return true;


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_option_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

        switch ( requestCode ){
            case REQUEST_CODE_FOR_PROFILE_SET:
                break;
            case REQUEST_CODE_FOR_INTEREST_SET:
                String interest = data.getStringExtra("interest");
                //TODO firebase/users/username/interest 교체하면 됨
                break;
        }

    }
}