package com.moondroid.project01_meetingapp.page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.page_tab1_info.CreateMoimActivity;
import com.moondroid.project01_meetingapp.page_tab1_info.OptionModifyActivity;
import com.naver.maps.map.e;

import java.nio.file.Files;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageActivity extends AppCompatActivity {
    public static Activity activity;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private ViewPagerAdapter adapter;
    private TextView tvTitle;
    private ImageView favoriteNone;
    private ImageView favoriteDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

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
        tvTitle.setText(G.currentItemBase.getMeetName());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.modify_page_activity:
                if (!G.currentItemBase.getMasterId().equals(G.myProfile.getUserId())) {
                    Toast.makeText(this, "모임장만 할수 있습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Intent intent = new Intent(this, OptionModifyActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.add_moim_info:
                if (!G.currentItemBase.getMasterId().equals(G.myProfile.getUserId())) {
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

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {

            } else {

            }
        }
    };

    public void checkFavorite(){
        RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).checkFavorite(G.myProfile.getUserId(), G.currentItemBase.getMeetName()).enqueue(new Callback<String>() {
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
                RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).deleteFavor(G.myProfile.getUserId(), G.currentItemBase.getMeetName()).enqueue(new Callback<String>() {
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
                RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).insertFavor(G.myProfile.getUserId(), G.currentItemBase.getMeetName()).enqueue(new Callback<String>() {
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