package com.moondroid.project01_meetingapp.option01search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.main_bnv01meet.MeetItemAdapter;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements MenuItem.OnActionExpandListener {
    Toolbar toolbar;
    ArrayList<ItemBaseVO> itemVOData;
    RecyclerView recyclerViewSearchActivity;
    MeetItemAdapter meetItemAdapter;
    SearchView searchView;
    String searchTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar_search_activity);
        recyclerViewSearchActivity = findViewById(R.id.recycler_search_activity);
        itemVOData = new ArrayList<>();
        meetItemAdapter = new MeetItemAdapter(this, itemVOData);
        recyclerViewSearchActivity.setAdapter(meetItemAdapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).getItemBaseDataOnMain().enqueue(new Callback<ArrayList<ItemBaseVO>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ItemBaseVO>> call, Response<ArrayList<ItemBaseVO>> response) {
                        for (int i = 0; i < response.body().size(); i++) {
                            ItemBaseVO itemBaseVO = response.body().get(i);
                            if (itemBaseVO.meetName.contains(searchTarget) || itemBaseVO.purposeMessage.contains(searchTarget)) {
                                itemVOData.add(0, itemBaseVO);
                                meetItemAdapter.notifyItemInserted(itemVOData.size() - 1);
                            }
                        }
                        searchView.clearFocus();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ItemBaseVO>> call, Throwable t) {

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