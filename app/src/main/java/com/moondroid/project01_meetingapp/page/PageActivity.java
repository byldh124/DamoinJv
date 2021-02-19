package com.moondroid.project01_meetingapp.page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.page_tab1_info.OptionModifyActivity;

public class PageActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentManager fragmentManager;
    ViewPagerAdapter adapter;
    TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        //xml Reference
        toolbar = findViewById(R.id.toolbar_page_activity);
        tabLayout = findViewById(R.id.layout_page_activity_tab);
        viewPager = findViewById(R.id.view_pager);
        tvTitle = findViewById(R.id.tv_title_page);

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
        tvTitle.setText(G.currentItemBase.meetName);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.modify_page_activity:
                if (!G.currentItemMember.master.equals(G.myProfile.userId)){
                    Toast.makeText(this, "모임장만 할수 있습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Intent intent = new Intent(this, OptionModifyActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.add_moim_info:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_page, menu);
        return super.onCreateOptionsMenu(menu);
    }
}