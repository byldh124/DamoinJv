package com.moondroid.project01_meetingapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.GroupInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.helpers.utils.LinearLayoutManagerWrapper;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.presenter.adapter.MeetItemAdapter;
import com.moondroid.project01_meetingapp.ui.activity.BaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ArrayList<GroupInfo> groupInfos;
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
        groupInfos = new ArrayList<>();
        adapter = new MeetItemAdapter(getActivity(), groupInfos);
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
        groupInfos.clear();
        adapter.notifyDataSetChanged();
        //유저가 모임장인 경우 우선적으로 불러옴
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
                            for (int i = 0; i < (groupList != null ? groupList.size() : 0); i++) {
                                if (groupList.get(i).getMasterId().equals(GlobalInfo.myProfile.getUserId())) {
                                    groupInfos.add(groupList.get(i));
                                    adapter.notifyItemInserted(groupInfos.size() - 1);
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

                //유저가 모임원인 경우 불러오는 작업
                RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadUserMeetItem(GlobalInfo.myProfile.getUserId()).enqueue(new Callback<ArrayList<GroupInfo>>() {
                    @Override
                    public void onResponse(Call<ArrayList<GroupInfo>> call, Response<ArrayList<GroupInfo>> response) {
                        for (int i = 0; i < (response.body() != null ? response.body().size() : 0); i++) {
                            if (response.body().get(i).getMasterId().equals(GlobalInfo.myProfile.getUserId())) continue;
                            groupInfos.add(response.body().get(i));
                            adapter.notifyItemInserted(groupInfos.size() - 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<GroupInfo>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadData();
            }
        });


    }
}

