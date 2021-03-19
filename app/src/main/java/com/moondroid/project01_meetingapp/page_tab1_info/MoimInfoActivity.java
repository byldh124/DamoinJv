package com.moondroid.project01_meetingapp.page_tab1_info;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.LinearLayoutManagerWrapper;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.variableobject.MoimVO;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoimInfoActivity extends AppCompatActivity {

    private MoimVO moimItem;
    private ArrayList<UserBaseVO> memberVOS;
    private InformationMemberAdapter memberAdapter;
    private RecyclerView recyclerViewMembers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_info);

        if (getIntent().getStringExtra("moimInfo") != null) {
            moimItem = new Gson().fromJson(getIntent().getStringExtra("moimInfo"), MoimVO.class);
        }
        recyclerViewMembers = findViewById(R.id.recycler_moim_infor_members);
        recyclerViewMembers.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));

        memberVOS = new ArrayList<>();
        memberAdapter = new InformationMemberAdapter(this, memberVOS);
        recyclerViewMembers.setAdapter(memberAdapter);

        loadMembers();
    }

    public void loadMembers() {
        memberVOS.clear();
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadJoinMembers(moimItem.getJoinMembers()).enqueue(new Callback<ArrayList<UserBaseVO>>() {
            @Override
            public void onResponse(Call<ArrayList<UserBaseVO>> call, Response<ArrayList<UserBaseVO>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    memberVOS.add(response.body().get(i));
                    memberAdapter.notifyItemInserted(memberVOS.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserBaseVO>> call, Throwable t) {
                loadMembers();
            }
        });
    }

    public void clickJoin(View view) {
        if(memberVOS.contains(G.myProfile)){
            Toast.makeText(this, "이미 신청하신 모임입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this).setMessage("모임 신청 하시겠습니까?").setNegativeButton("아니오", null).setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).addJoinMember(moimItem.getMeetName(), moimItem.getDate(), G.myProfile.getUserId()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        }).create().show();
    }
}

