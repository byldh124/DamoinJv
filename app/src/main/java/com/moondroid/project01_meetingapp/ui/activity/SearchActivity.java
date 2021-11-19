package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.GroupInfo;
import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.helpers.utils.LinearLayoutManagerWrapper;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.presenter.adapter.MeetItemAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements MenuItem.OnActionExpandListener {
    private Toolbar toolbar;
    private ArrayList<GroupInfo> itemVOData;
    private RecyclerView recyclerViewSearchActivity;
    private MeetItemAdapter meetItemAdapter;
    private SearchView searchView;
    private String searchTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //xml 참조영역
        toolbar = (Toolbar) findViewById(R.id.toolbar_search_activity);
        recyclerViewSearchActivity = (RecyclerView) findViewById(R.id.recycler_search_activity);
        
        //리사이클러뷰 제어
        recyclerViewSearchActivity.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
        itemVOData = new ArrayList<>();
        meetItemAdapter = new MeetItemAdapter(this, itemVOData);
        recyclerViewSearchActivity.setAdapter(meetItemAdapter);

        //toolbar 제어
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //검색기능 구현
        getMenuInflater().inflate(R.menu.menu_search_view, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search_activity_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setQueryHint("모임 검색");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTarget = query.replace(" ", "");
                itemVOData.clear();
                meetItemAdapter.notifyDataSetChanged();
                
                //검색한 입력어에 따라 보여줄 아이템 선정
                RetrofitHelper.getRetrofit().create(RetrofitService.class).getGroupList().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            JSONObject res = new JSONObject(response.body());
                            int code = res.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                            switch (code) {
                                case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                    Gson gson = new Gson();
                                    ArrayList<GroupInfo> groupList = gson.fromJson(res.optString(GlobalKey.NTWRK_RTN_TYPE.RESULT), new TypeToken<ArrayList<GroupInfo>>() {
                                    }.getType());
                                    for (int i = 0; i < groupList.size(); i++) {
                                        GroupInfo groupInfo = groupList.get(i);
                                        if (groupInfo.getMeetName().contains(searchTarget) || groupInfo.getPurposeMessage().contains(searchTarget)) {
                                            itemVOData.add(0, groupInfo);
                                            meetItemAdapter.notifyItemInserted(itemVOData.size() - 1);
                                        }
                                    }
                                    searchView.clearFocus();
                                    break;
                                case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                    showNtwrkFailToast("1");
                                    break;
                            }
                        } catch (Exception e) {
                            logException(e);
                            showNtwrkFailToast("2");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        DMFBCrash.logException(t);
                        showNtwrkFailToast("3");
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //검색 바 설정
    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS |
                MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        //Call menu to be redrawn
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}