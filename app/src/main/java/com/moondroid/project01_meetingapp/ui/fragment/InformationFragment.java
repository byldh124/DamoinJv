package com.moondroid.project01_meetingapp.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.ChatItemVO;
import com.moondroid.project01_meetingapp.data.model.MoimVO;
import com.moondroid.project01_meetingapp.data.model.UserBaseVO;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.helpers.utils.LinearLayoutManagerWrapper;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.network.URLMngr;
import com.moondroid.project01_meetingapp.presenter.adapter.InformationMemberAdapter;
import com.moondroid.project01_meetingapp.presenter.adapter.InformationMoimAdapter;
import com.moondroid.project01_meetingapp.ui.activity.BaseActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformationFragment extends BaseFragment {

    private ImageView ivIntroImg, ivInterestIcon;
    private TextView tvMessage, tvMeetName;
    private RecyclerView recyclerViewJungMo, recyclerViewMembers;
    private ArrayList<String> interestList;
    private Button btnJoin;
    private ArrayList<UserBaseVO> memberVOS;
    private InformationMemberAdapter memberAdapter;
    private ArrayList<MoimVO> moimVOS;
    private InformationMoimAdapter moimAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        memberVOS = new ArrayList<>();
        moimVOS = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_page_tab1_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //xml Reference
        ivInterestIcon = view.findViewById(R.id.iv_page_modify_interest_icon);
        ivIntroImg = view.findViewById(R.id.iv_page_modify_intro_image);
        tvMessage = view.findViewById(R.id.tv_page_modify_message);
        tvMeetName = view.findViewById(R.id.tv_page_modify_title);
        recyclerViewJungMo = view.findViewById(R.id.recycler_page_moim_information);
        recyclerViewJungMo.setLayoutManager(new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewMembers = view.findViewById(R.id.recycler_page_members);
        recyclerViewMembers.setLayoutManager(new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false));
        btnJoin = view.findViewById(R.id.btn_join);

        //멤버 리사이클러뷰 세팅
        memberAdapter = new InformationMemberAdapter(getContext(), memberVOS);
        recyclerViewMembers.setAdapter(memberAdapter);

        //정모 리사이클러뷰 세팅
        moimAdapter = new InformationMoimAdapter(getContext(), moimVOS);
        recyclerViewJungMo.setAdapter(moimAdapter);


        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setMessage("가입하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (GlobalInfo.myProfile.getUserId().equals(GlobalInfo.currentMoim.getMasterId())) {
                            Toast.makeText(getContext(), "you're master", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        saveUserMeet();
                    }
                }).setNegativeButton("아니요", null).create().show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadMembers();
        loadMoims();

        //기존에 저장되어있던 모임정보들을 기입하는 작업 (모임명, 이미지, 설명 등등)
        if (GlobalInfo.currentMoim.getIntroImgUrl() != null) {
            Picasso.get().load(URLMngr.BASE_URL_DEFAULT + GlobalInfo.currentMoim.getIntroImgUrl()).into(ivIntroImg);
        }

        interestList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.interest_list)));
        int interestNum = interestList.indexOf(GlobalInfo.currentMoim.getMeetInterest());
        if (interestNum < 0) interestNum = 0;
        Glide.with(getContext()).load(getResources().getStringArray(R.array.interest_icon_img_url)[interestNum]).into(ivInterestIcon);
        tvMeetName.setText(GlobalInfo.currentMoim.getMeetName());

        if (GlobalInfo.currentMoim.getMessage() == null || GlobalInfo.currentMoim.getMessage().equals("")) {
            GlobalInfo.currentMoim.setMessage("모임 설명을 작성해 주십시오");
        }
        tvMessage.setText(GlobalInfo.currentMoim.getMessage());
    }

    public void loadMembers() {
        memberVOS.clear();
        memberAdapter.notifyDataSetChanged();
        GlobalInfo.currentMoimMembers.clear();
        GlobalInfo.currentChatItems.clear();
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadUserBaseDBToIntroActivity(GlobalInfo.currentMoim.getMasterId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject res = new JSONObject(response.body());
                    int code = res.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                    switch (code) {
                        case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                            JSONObject result = res.getJSONObject(GlobalKey.NTWRK_RTN_TYPE.RESULT);
                            Gson gson = new Gson();
                            UserBaseVO userBaseVO = gson.fromJson(String.valueOf(result), UserBaseVO.class);
                            memberVOS.add(0, userBaseVO);
                            GlobalInfo.currentMoimMembers.add(GlobalInfo.currentMoim.getMasterId());
                            GlobalInfo.currentChatItems.add(new ChatItemVO(userBaseVO.getUserId(), userBaseVO.getUserName(), null, userBaseVO.getUserProfileImgUrl(), null));
                            memberAdapter.notifyItemInserted(0);
                            RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadMembers(GlobalInfo.currentMoim.getMeetName()).enqueue(new Callback<ArrayList<UserBaseVO>>() {
                                @Override
                                public void onResponse(Call<ArrayList<UserBaseVO>> call, Response<ArrayList<UserBaseVO>> response) {
                                    for (int i = 0; i < response.body().size(); i++) {
                                        UserBaseVO userBaseVO = response.body().get(i);
                                        if (userBaseVO.getUserId().equals(GlobalInfo.currentMoim.getMasterId()))
                                            continue;
                                        memberVOS.add(userBaseVO);
                                        GlobalInfo.currentMoimMembers.add(userBaseVO.getUserId());
                                        GlobalInfo.currentChatItems.add(new ChatItemVO(userBaseVO.getUserId(), userBaseVO.getUserName(), null, userBaseVO.getUserProfileImgUrl(), null));
                                        memberAdapter.notifyItemInserted(memberVOS.size() - 1);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ArrayList<UserBaseVO>> call, Throwable t) {

                                }
                            });

                            break;
                        case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                        case GlobalKey.NTWRK_RTN_TYPE.NOT_EXIST:
                            ((BaseActivity) getActivity()).showNtwrkFailToast("1");
                            break;
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadMembers();
            }
        });
    }

    public void saveUserMeet() {
        RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).checkUserMeetData(GlobalInfo.myProfile.getUserId(), GlobalInfo.currentMoim.getMeetName()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    int checkNum = 0;
                    checkNum = Integer.parseInt(response.body());
                    if (checkNum > 0) {
                        Toast.makeText(getContext(), "이미 가입된 모임입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).saveUserMeetData(GlobalInfo.myProfile.getUserId(), GlobalInfo.currentMoim.getMeetName()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(getContext(), "" + response.body(), Toast.LENGTH_SHORT).show();
                                loadMembers();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                saveUserMeet();
                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "다른문제", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                saveUserMeet();
            }
        });
    }

    public void loadMoims() {
        moimVOS.clear();
        moimAdapter.notifyDataSetChanged();
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadMoims(GlobalInfo.currentMoim.getMeetName()).enqueue(new Callback<ArrayList<MoimVO>>() {
            @Override
            public void onResponse(Call<ArrayList<MoimVO>> call, Response<ArrayList<MoimVO>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    MoimVO moimVO = response.body().get(i);
                    if (Integer.parseInt(moimVO.getDate().replace(".", "")) >= Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()))) {
                        moimVOS.add(response.body().get(i));
                        moimAdapter.notifyItemInserted(moimVOS.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MoimVO>> call, Throwable t) {
                loadMoims();
            }
        });
    }
}
