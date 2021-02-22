package com.moondroid.project01_meetingapp.main_bnv01meet;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;

import java.util.ArrayList;

public class MeetFragmentBottomTab1 extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerCategory;
    String[] categories;
    CategoryAdapter categoryAdapter;

    RecyclerView recyclerItems;
    ArrayList<ItemBaseVO> itemList;
    MeetItemAdapter itemAdapter;

    Resources resources;

    FloatingActionButton buttonAdd;
    ArrayList<ItemBaseVO> snapshots;

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
        } catch (Exception e){
            Toast.makeText(getContext(), "데이터 갱신에 문제가 발생했습니다.\n 화면을 잡아 당겨주세요", Toast.LENGTH_SHORT).show();
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

    public void loadData() {
        snapshots = new ArrayList<>();
        itemList.clear();
        G.itemsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ItemBaseVO itemBaseVO = ds.child("base").getValue(ItemBaseVO.class);
                    snapshots.add(itemBaseVO);
                }
                for (int i = 0; i < snapshots.size(); i++) {
                    //TODO 조건에 맞게 아이템 맞추기 (지역, 관심사)
                    itemList.add(0, snapshots.get(i));
                }
                itemAdapter.notifyDataSetChanged();
            }
        });

    }

    public void loadData(String interest) {
        snapshots = new ArrayList<>();
        itemList.clear();
        G.itemsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ItemBaseVO itemBaseVO = ds.child("base").getValue(ItemBaseVO.class);
                    snapshots.add(itemBaseVO);
                }
                for (int i = 0; i < snapshots.size(); i++) {
                    if (snapshots.get(i).meetInterest.equals(interest))
                        itemList.add(0, snapshots.get(i));
                }
                itemAdapter.notifyDataSetChanged();
            }
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        loadData();
//    }


    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }
}
