package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.MoimVO;
import com.moondroid.project01_meetingapp.data.model.UserInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.LinearLayoutManagerWrapper;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.presenter.adapter.InformationMemberAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoimInfoActivity extends AppCompatActivity {

    private MoimVO moimItem;
    private ArrayList<UserInfo> memberVOS;
    private InformationMemberAdapter memberAdapter;
    private RecyclerView recyclerViewMembers;

    private Toolbar toolbar;
    private TextView tvDate, tvTime, tvLocation, tvPay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_info);

        //ActionBar setting
        toolbar = findViewById(R.id.toolbar_moim_info_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //xml Reference
        tvDate = findViewById(R.id.tv_date_moim_info);
        tvTime = findViewById(R.id.tv_time_moim_info);
        tvLocation = findViewById(R.id.tv_location_moim_info);
        tvPay = findViewById(R.id.tv_pay_moim_info);

        if (getIntent().getStringExtra("moimInfo") != null) {
            moimItem = new Gson().fromJson(getIntent().getStringExtra("moimInfo"), MoimVO.class);

            tvDate.setText(moimItem.getDate());
            tvTime.setText(moimItem.getTime());
            tvLocation.setText(moimItem.getAddress());
            tvPay.setText(moimItem.getPay());
        }
        recyclerViewMembers = findViewById(R.id.recycler_moim_infor_members);
        recyclerViewMembers.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));

        memberVOS = new ArrayList<>();
        memberAdapter = new InformationMemberAdapter(this, memberVOS);
        recyclerViewMembers.setAdapter(memberAdapter);

        loadMembers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadMembers() {
        memberVOS.clear();
        memberAdapter.notifyDataSetChanged();
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadJoinMembers(moimItem.getJoinMembers()).enqueue(new Callback<ArrayList<UserInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<UserInfo>> call, Response<ArrayList<UserInfo>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    memberVOS.add(response.body().get(i));
                    memberAdapter.notifyItemInserted(memberVOS.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserInfo>> call, Throwable t) {
                loadMembers();
            }
        });
    }

    public void clickJoin(View view) {

        for (int i = 0; i < memberVOS.size(); i++) {
            if (memberVOS.get(i).getUserId().equals(GlobalInfo.myProfile.getUserId())) {
                Toast.makeText(this, "이미 신청하신 모임입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        new AlertDialog.Builder(this).setMessage("모임 신청 하시겠습니까?").setNegativeButton("아니오", null).setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RetrofitHelper.getRetrofit().create(RetrofitService.class).addJoinMember(moimItem.getMeetName(), moimItem.getDate(), GlobalInfo.myProfile.getUserId()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        memberVOS.add(GlobalInfo.myProfile);
                        memberAdapter.notifyItemInserted(memberVOS.size() - 1);
                        Toast.makeText(MoimInfoActivity.this, "모임에 참여했습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        }).create().show();
    }
}

