package com.moondroid.project01_meetingapp.main_bnv03mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.LinearLayoutManagerWrapper;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.main_bnv01meet.MeetItemAdapter;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyPageFragmentBottomTab3 extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ItemBaseVO> itemBaseVOS;
    private MeetItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_tab3_my_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //리사이클러뷰 참조 및 어댑터, 리스트 세팅
        recyclerView = view.findViewById(R.id.recycler_my_page);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false));
        itemBaseVOS = new ArrayList<>();
        adapter = new MeetItemAdapter(getActivity(), itemBaseVOS);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //DB에 저장된 아이템 리스트를 불러온후 유저가 가입한 아이템만 선별하여 리사이클러 리스트에 전달.
    public void loadData() {
        itemBaseVOS.clear();
        adapter.notifyDataSetChanged();
        //유저가 모임장인 경우 우선적으로 불러옴
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).getItemBaseDataOnMain().enqueue(new Callback<ArrayList<ItemBaseVO>>() {
            @Override
            public void onResponse(Call<ArrayList<ItemBaseVO>> call, Response<ArrayList<ItemBaseVO>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).getMasterId().equals(G.myProfile.getUserId())) {
                        itemBaseVOS.add(response.body().get(i));
                        adapter.notifyItemInserted(itemBaseVOS.size() - 1);
                    }
                }
                //유저가 모임원인 경우 불러오는 작업
                RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadUserMeetItem(G.myProfile.getUserId()).enqueue(new Callback<ArrayList<ItemBaseVO>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ItemBaseVO>> call, Response<ArrayList<ItemBaseVO>> response) {
                        for (int i = 0; i < response.body().size(); i++) {
                            if (response.body().get(i).getMasterId().equals(G.myProfile.getUserId())) continue;
                            itemBaseVOS.add(response.body().get(i));
                            adapter.notifyItemInserted(itemBaseVOS.size() - 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ItemBaseVO>> call, Throwable t) {
                        loadData();
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<ItemBaseVO>> call, Throwable t) {
                loadData();
            }
        });


    }
}

