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
    RecyclerView recyclerView;
    ArrayList<ItemBaseVO> itemBaseVOS;
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

    public void loadData() {
        itemBaseVOS.clear();
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).getItemBaseDataOnMain().enqueue(new Callback<ArrayList<ItemBaseVO>>() {
            @Override
            public void onResponse(Call<ArrayList<ItemBaseVO>> call, Response<ArrayList<ItemBaseVO>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).masterId.equals(G.myProfile.userId)){
                        itemBaseVOS.add(response.body().get(i));
                        adapter.notifyItemInserted(itemBaseVOS.size() - 1);
                    }
                }

                RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadUserMeetItem(G.myProfile.userId).enqueue(new Callback<ArrayList<ItemBaseVO>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ItemBaseVO>> call, Response<ArrayList<ItemBaseVO>> response) {
                        for (int i = 0; i < response.body().size(); i++) {
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

