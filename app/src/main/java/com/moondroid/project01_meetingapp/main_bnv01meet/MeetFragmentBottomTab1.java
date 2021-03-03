package com.moondroid.project01_meetingapp.main_bnv01meet;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.createmeet.CreateActivity;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.page.PageActivity;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;

import java.util.ArrayList;
import java.util.function.LongBinaryOperator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetFragmentBottomTab1 extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerCategory;
    private String[] categories;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerItems;
    private ArrayList<ItemBaseVO> itemList;
    private MeetItemAdapter itemAdapter;
    private Resources resources;
    private FloatingActionButton buttonAdd;
    private ArrayList<ItemBaseVO> snapshots;

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
        snapshots.clear();
        itemList.clear();
        RetrofitHelper.getRetrofitInstanceGsonSetLenient().create(RetrofitService.class).getItemBaseDataOnMain().enqueue(new Callback<ArrayList<ItemBaseVO>>() {
            @Override
            public void onResponse(Call<ArrayList<ItemBaseVO>> call, Response<ArrayList<ItemBaseVO>> response) {
                for (int j = 0; j < response.body().size(); j++) {
                    snapshots.add(response.body().get(j));
                }
                for (int i = 0; i < snapshots.size(); i++) {
                    itemList.add(snapshots.get(i));
                    itemAdapter.notifyItemInserted(itemList.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ItemBaseVO>> call, Throwable t) {
            }
        });
    }

    //카테고리 선택에 따른 아이템 선별 과정
    public void loadData(String interest) {
        snapshots.clear();
        itemList.clear();
        itemAdapter.notifyDataSetChanged();
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).getItemBaseDataOnMain().enqueue(new Callback<ArrayList<ItemBaseVO>>() {
            @Override
            public void onResponse(Call<ArrayList<ItemBaseVO>> call, Response<ArrayList<ItemBaseVO>> response) {
                for (int j = 0; j < response.body().size(); j++) {
                    snapshots.add(response.body().get(j));
                }

                for (int i = 0; i < snapshots.size(); i++) {
                    if (snapshots.get(i).getMeetInterest().equals(interest)) {
                        itemList.add(snapshots.get(i));
                        itemAdapter.notifyItemInserted(itemList.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ItemBaseVO>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }
}
