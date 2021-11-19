package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.GroupInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.LinearLayoutManagerWrapper;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.presenter.adapter.MeetItemAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageFavoriteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<GroupInfo> items;
    private MeetItemAdapter meetItemAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_favorite);

        toolbar = findViewById(R.id.toolbar_my_page_favorite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recycler_my_page_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
        items = new ArrayList<>();
        meetItemAdapter = new MeetItemAdapter(this, items);
        recyclerView.setAdapter(meetItemAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData(){
        items.clear();
        meetItemAdapter.notifyDataSetChanged();
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadUserFavoriteItem(GlobalInfo.myProfile.getUserId()).enqueue(new Callback<ArrayList<GroupInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<GroupInfo>> call, Response<ArrayList<GroupInfo>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    items.add(response.body().get(i));
                    meetItemAdapter.notifyItemInserted(items.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GroupInfo>> call, Throwable t) {

            }
        });
    }
}