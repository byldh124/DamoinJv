package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.GroupInfo;
import com.moondroid.project01_meetingapp.data.model.UserInfo;
import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.helpers.utils.LinearLayoutManagerWrapper;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.network.URLMngr;
import com.moondroid.project01_meetingapp.presenter.adapter.ProfileMeetItemAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView tvName;
    private TextView tvBirth;
    private TextView tvLocation;
    private TextView tvMessage;
    private CircleImageView ivProfile;
    private CircleImageView ivInterest;
    private RecyclerView recyclerView;
    private ArrayList<GroupInfo> groupInfos;
    private ProfileMeetItemAdapter adapter;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //클릭이벤트에서 받아온 Json을 UserBaseVO로 가져오기
        String memberInformation = getIntent().getStringExtra("memberInformation");
        userInfo = new Gson().fromJson(memberInformation, UserInfo.class);

        //xml 참조영역
        toolbar = findViewById(R.id.toolbar_profile_activity);
        tvName = findViewById(R.id.tv_profile_name);
        tvBirth = findViewById(R.id.tv_profile_birth);
        tvLocation = findViewById(R.id.tv_profile_location);
        tvMessage = findViewById(R.id.tv_profile_message);
        ivProfile = findViewById(R.id.iv_profile_profile_img);
        ivInterest = findViewById(R.id.iv_profile_interest);
        recyclerView = findViewById(R.id.recycler_profile_activity);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));

        groupInfos = new ArrayList<>();
        adapter = new ProfileMeetItemAdapter(this, groupInfos);
        recyclerView.setAdapter(adapter);

        //액션바 세팅
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (userInfo.getUserName() != null) tvName.setText(userInfo.getUserName());
        if (userInfo.getUserBirthDate() != null) tvBirth.setText(userInfo.getUserBirthDate());
        else tvBirth.setText("비밀~");
        if (userInfo.getUserLocation() != null)
            tvLocation.setText(userInfo.getUserLocation().split(" ")[0]);
        else tvLocation.setText("비밀~");
        if (userInfo.getUserProfileMessage() != null)
            tvMessage.setText(userInfo.getUserProfileMessage());
        else tvMessage.setText("만나서 반갑습니다.");
        if (userInfo.getUserProfileImgUrl() != null) {
            if (userInfo.getUserProfileImgUrl().contains("http")) {
                Glide.with(this).load(userInfo.getUserProfileImgUrl()).into(ivProfile);
            } else {
                Glide.with(this).load(URLMngr.IMG_URL + userInfo.getUserProfileImgUrl()).into(ivProfile);
            }
        }
        if (userInfo.getUserInterest() != null) {
            int interestNum = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.interest_list))).indexOf(userInfo.getUserInterest());
            Glide.with(this).load(getResources().getStringArray(R.array.interest_icon_img_url)[interestNum]).into(ivInterest);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMaster();
    }

    public void loadMaster() {
        try {
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
                                for (int i = 0; i < groupList.size(); i++) {
                                    if (groupList.get(i).getMasterId().equals(userInfo.getUserId())) {
                                        groupInfos.add(groupList.get(i));
                                        adapter.notifyItemInserted(groupInfos.size() - 1);
                                        loadMembers();
                                    }
                                }
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

        } catch (Exception e) {
            logException(e);
            showNtwrkFailToast("4");
        }
    }

    public void loadMembers() {
        //유저가 모임원인 경우 불러오는 작업
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadUserMeetItem(userInfo.getUserId()).enqueue(new Callback<ArrayList<GroupInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<GroupInfo>> call, Response<ArrayList<GroupInfo>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).getMasterId().equals(GlobalInfo.myProfile.getUserId()))
                        continue;
                    groupInfos.add(response.body().get(i));
                    adapter.notifyItemInserted(groupInfos.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GroupInfo>> call, Throwable t) {
                loadMaster();
            }
        });
    }
}