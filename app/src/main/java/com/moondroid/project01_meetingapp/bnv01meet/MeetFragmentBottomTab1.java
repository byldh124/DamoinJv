package com.moondroid.project01_meetingapp.bnv01meet;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    Resources resources;

    FloatingActionButton buttonAdd;
    ArrayList<ItemBaseVO> itemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_tab1_meet,container,false);
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
        loadData();

        //swipeRefreshLayout Listener implement
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Update Recycler Items
                loadData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //categoryRecycler build
        resources = getResources();
        categories = resources.getStringArray(R.array.category_for_interest_in_meet);
        categoryAdapter = new CategoryAdapter(getActivity(), categories);
        recyclerCategory.setAdapter(categoryAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loadData(){
        final ArrayList<ItemBaseVO> snapshots = new ArrayList<>();
        G.itemsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ItemBaseVO itemBaseVO = ds.getValue(ItemBaseVO.class);
                    snapshots.add(itemBaseVO);
                    //TODO Data를 처음에 시작할때 받아오는게 좋지 않나?
                }
            }
        });
    }
}
