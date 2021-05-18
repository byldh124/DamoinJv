package com.moondroid.project01_meetingapp.page_tab1_info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.LinearLayoutManagerWrapper;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.main_bnv01meet.MeetItemAdapter;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvName;
    private TextView tvBirth;
    private TextView tvLocation;
    private TextView tvMessage;
    private CircleImageView ivProfile;
    private CircleImageView ivInterest;
    private RecyclerView recyclerView;
    private ArrayList<ItemBaseVO> itemBaseVOS;
    private ProfileMeetItemAdapter adapter;
    private UserBaseVO userBaseVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //클릭이벤트에서 받아온 Json을 UserBaseVO로 가져오기
        String memberInformation = getIntent().getStringExtra("memberInformation");
        userBaseVO = new Gson().fromJson(memberInformation, UserBaseVO.class);

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

        itemBaseVOS = new ArrayList<>();
        adapter = new ProfileMeetItemAdapter(this, itemBaseVOS);
        recyclerView.setAdapter(adapter);

        //액션바 세팅
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (userBaseVO.getUserName() != null) tvName.setText(userBaseVO.getUserName());
        if (userBaseVO.getUserBirthDate() != null) tvBirth.setText(userBaseVO.getUserBirthDate());
        if (userBaseVO.getUserLocation() != null)
            tvLocation.setText(userBaseVO.getUserLocation().split(" ")[0]);
        if (userBaseVO.getUserProfileMessage() != null)
            tvMessage.setText(userBaseVO.getUserProfileMessage());
        if (userBaseVO.getUserProfileImgUrl() != null) {
            if (userBaseVO.getUserProfileImgUrl().contains("http")) {
                Glide.with(this).load(userBaseVO.getUserProfileImgUrl()).into(ivProfile);
            } else {
                Glide.with(this).load(RetrofitHelper.getUrlForImg() + userBaseVO.getUserProfileImgUrl()).into(ivProfile);
            }
        }
        if (userBaseVO.getUserInterest() != null) {
            int interestNum = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.interest_list))).indexOf(userBaseVO.getUserInterest());
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
        loadData();
    }

    public void loadData() {
        itemBaseVOS.clear();
        adapter.notifyDataSetChanged();
        //유저가 모임장인 경우 우선적으로 불러옴
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).getItemBaseDataOnMain().enqueue(new Callback<ArrayList<ItemBaseVO>>() {
            @Override
            public void onResponse(Call<ArrayList<ItemBaseVO>> call, Response<ArrayList<ItemBaseVO>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).getMasterId().equals(userBaseVO.getUserId())) {
                        itemBaseVOS.add(response.body().get(i));
                        adapter.notifyItemInserted(itemBaseVOS.size() - 1);
                    }
                }
                //유저가 모임원인 경우 불러오는 작업
                RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadUserMeetItem(userBaseVO.getUserId()).enqueue(new Callback<ArrayList<ItemBaseVO>>() {
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