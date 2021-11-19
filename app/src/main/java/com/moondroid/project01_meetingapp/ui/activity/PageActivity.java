package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.presenter.adapter.ViewPagerAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageActivity extends BaseActivity {
    public static Activity activity;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private ViewPagerAdapter adapter;
    private TextView tvTitle;
    private ImageView favoriteNone;
    private ImageView favoriteDone;
    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        analytics = FirebaseAnalytics.getInstance(this);

        activity = PageActivity.this;
        //xml Reference
        toolbar = findViewById(R.id.toolbar_page_activity);
        tabLayout = findViewById(R.id.layout_page_activity_tab);
        viewPager = findViewById(R.id.view_pager);
        tvTitle = findViewById(R.id.tv_title_page);
        favoriteNone = findViewById(R.id.favorite_none);
        favoriteDone = findViewById(R.id.favorite_done);

        //actionBar setting
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //viewPager - TabLayout
        fragmentManager = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //title
        tvTitle.setText(GlobalInfo.currentGroup.getMeetName());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.modify_page_activity:
                if (!GlobalInfo.currentGroup.getMasterId().equals(GlobalInfo.myProfile.getUserId())) {
                    Toast.makeText(this, "모임장만 할수 있습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Intent intent = new Intent(this, OptionModifyActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.add_moim_info:
                if (!GlobalInfo.currentGroup.getMasterId().equals(GlobalInfo.myProfile.getUserId())) {
                    Toast.makeText(this, "모임장만 할수 있습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Intent intent = new Intent(this, CreateMoimActivity.class);
                    startActivity(intent);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkFavorite();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void checkFavorite(){
        RetrofitHelper.getRetrofit().create(RetrofitService.class).checkFavorite(GlobalInfo.myProfile.getUserId(), GlobalInfo.currentGroup.getMeetName()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    int checkNum = 0;
                    checkNum = Integer.parseInt(response.body());
                    if (checkNum > 0) {
                        favoriteDone.setVisibility(View.VISIBLE);
                        favoriteNone.setVisibility(View.INVISIBLE);
                    } else {
                        favoriteNone.setVisibility(View.VISIBLE);
                        favoriteDone.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void clickDone(View view) {
        new AlertDialog.Builder(PageActivity.this).setMessage("관심목록에서 삭제하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RetrofitHelper.getRetrofit().create(RetrofitService.class).deleteFavor(GlobalInfo.myProfile.getUserId(), GlobalInfo.currentGroup.getMeetName()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        favoriteDone.setVisibility(View.INVISIBLE);
                        favoriteNone.setVisibility(View.VISIBLE);
                        Toast.makeText(PageActivity.this, "관심목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        }).setNegativeButton("아니오", null).create().show();
    }

    public void clickNone(View view) {
        new AlertDialog.Builder(PageActivity.this).setMessage("관심목록에 추가하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RetrofitHelper.getRetrofit().create(RetrofitService.class).insertFavor(GlobalInfo.myProfile.getUserId(), GlobalInfo.currentGroup.getMeetName()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(PageActivity.this, "관심목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                        favoriteDone.setVisibility(View.VISIBLE);
                        favoriteNone.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        }).setNegativeButton("아니오", null).create().show();
    }
}