package com.moondroid.project01_meetingapp.main_bnv03mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.main_bnv01meet.MeetItemAdapter;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class MyPageFragmentBottomTab3 extends Fragment {
    RecyclerView recyclerView;
    ArrayList<ItemBaseVO> itemBaseVOS;
    ArrayList<String> strings = new ArrayList<>();
    MeetItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_tab3_my_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_my_page);
        itemBaseVOS = new ArrayList<>();
        adapter = new MeetItemAdapter(getActivity(),itemBaseVOS);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void loadData() {
        strings.clear();
        itemBaseVOS.clear();
        G.usersRef.child(G.myProfile.userId).child("meets").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    strings.add(ds.getValue(String.class));
                }
                G.itemsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            for (int i =0 ; i < strings.size(); i++){
                                if (ds.getKey().equals(strings.get(i))){
                                    itemBaseVOS.add(ds.child("base").getValue(ItemBaseVO.class));
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}

