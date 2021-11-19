package com.moondroid.project01_meetingapp.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.GroupInfo;
import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.presenter.adapter.CategoryAdapter;
import com.moondroid.project01_meetingapp.presenter.adapter.MeetItemAdapter;
import com.moondroid.project01_meetingapp.ui.activity.BaseActivity;
import com.moondroid.project01_meetingapp.ui.activity.CreateActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerCategory;
    private String[] categories;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerItems;
    private ArrayList<GroupInfo> itemList;
    private MeetItemAdapter itemAdapter;
    private Resources resources;
    private FloatingActionButton buttonAdd;
    private ArrayList<GroupInfo> snapshots;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_tab1_meet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //xml Reference
        swipeRefreshLayout = view.findViewById(R.id.layout_swipe_refresh);
        recyclerCategory = view.findViewById(R.id.recycler_category);
        recyclerItems = view.findViewById(R.id.recycler_items);
        buttonAdd = view.findViewById(R.id.btn_add_meet);

        //리사이클러뷰 세팅
        snapshots = new ArrayList<>();
        itemList = new ArrayList<>();
        itemAdapter = new MeetItemAdapter(getContext(), itemList);
        recyclerItems.setAdapter(itemAdapter);


        //swipeRefreshLayout Listener implement
        try {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //Update Recycler Items
                    loadData();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } catch (Exception e) {
        }


        //categoryRecycler build
        resources = getResources();
        categories = resources.getStringArray(R.array.category_for_interest_in_meet);
        categoryAdapter = new CategoryAdapter(getActivity(), categories, this);
        recyclerCategory.setAdapter(categoryAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateActivity.class);
                startActivity(intent);
            }
        });
    }

    //DB에 저장된 모임 아이템을 불러와서 유저정보와 비교 후 화면 전환 (추후 모임 아이템이 많아지면 진행할 예정)
    public void loadData() {
        try {
            snapshots.clear();
            itemList.clear();
            itemAdapter.notifyDataSetChanged();
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
                                snapshots.addAll(groupList);
                                for (int i = 0; i < snapshots.size(); i++) {
                                    itemList.add(snapshots.get(i));
                                    itemAdapter.notifyItemInserted(itemList.size() - 1);
                                }
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                ((BaseActivity) getActivity()).showNtwrkFailToast("1");
                                break;
                        }
                    } catch (Exception e) {
                        logException(e);
                        ((BaseActivity) getActivity()).showNtwrkFailToast("2");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    DMFBCrash.logException(t);
                    ((BaseActivity) getActivity()).showNtwrkFailToast("3");
                }
            });

        } catch (Exception e) {
            logException(e);
        }
    }

    //카테고리 선택에 따른 아이템 선별 과정
    public void loadData(String interest) {
        try {
            snapshots.clear();
            itemList.clear();
            itemAdapter.notifyDataSetChanged();
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
                                snapshots.addAll(groupList);
                                for (int i = 0; i < snapshots.size(); i++) {
                                    if (snapshots.get(i).getMeetInterest().equals(interest)) {
                                        itemList.add(snapshots.get(i));
                                        itemAdapter.notifyItemInserted(itemList.size() - 1);
                                    }
                                }
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                ((BaseActivity) getActivity()).showNtwrkFailToast("1");
                                break;
                        }
                    } catch (Exception e) {
                        logException(e);
                        ((BaseActivity) getActivity()).showNtwrkFailToast("2");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    DMFBCrash.logException(t);
                    ((BaseActivity) getActivity()).showNtwrkFailToast("3");
                }
            });

        } catch (Exception e) {
            logException(e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }
}
