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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.ChatItemVO;
import com.moondroid.project01_meetingapp.data.model.MoimVO;
import com.moondroid.project01_meetingapp.data.model.UserInfo;
import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
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
    private ArrayList<UserInfo> memberVOS;
    private InformationMemberAdapter memberAdapter;
    private ArrayList<MoimVO> moimVOS;
    private InformationMoimAdapter moimAdapter;
    private BaseActivity mActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        memberVOS = new ArrayList<>();
        moimVOS = new ArrayList<>();
        mActivity = (BaseActivity) getActivity();
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
                        if (GlobalInfo.myProfile.getUserId().equals(GlobalInfo.currentGroup.getMasterId())) {
                            Toast.makeText(getContext(), "you're master", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        chckMeet();
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
        if (GlobalInfo.currentGroup.getIntroImgUrl() != null) {
            Picasso.get().load(URLMngr.IMG_URL + GlobalInfo.currentGroup.getIntroImgUrl()).into(ivIntroImg);
        }

        interestList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.interest_list)));
        int interestNum = interestList.indexOf(GlobalInfo.currentGroup.getMeetInterest());
        if (interestNum < 0) interestNum = 0;
        Glide.with(getContext()).load(getResources().getStringArray(R.array.interest_icon_img_url)[interestNum]).into(ivInterestIcon);
        tvMeetName.setText(GlobalInfo.currentGroup.getMeetName());

        if (GlobalInfo.currentGroup.getMessage() == null || GlobalInfo.currentGroup.getMessage().equals("")) {
            GlobalInfo.currentGroup.setMessage("모임 설명을 작성해 주십시오");
        }
        tvMessage.setText(GlobalInfo.currentGroup.getMessage());
    }

    public void loadMembers() {
        memberVOS.clear();
        memberAdapter.notifyDataSetChanged();
        GlobalInfo.currentMoimMembers.clear();
        GlobalInfo.currentChatItems.clear();
        RetrofitHelper.getRetrofit().create(RetrofitService.class).getUserInfo(GlobalInfo.currentGroup.getMasterId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject res = new JSONObject(response.body());
                    int code = res.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                    switch (code) {
                        case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                            JSONObject result = res.getJSONObject(GlobalKey.NTWRK_RTN_TYPE.RESULT);
                            Gson gson = new Gson();
                            UserInfo userInfo = gson.fromJson(String.valueOf(result), UserInfo.class);
                            memberVOS.add(0, userInfo);
                            GlobalInfo.currentMoimMembers.add(GlobalInfo.currentGroup.getMasterId());
                            GlobalInfo.currentChatItems.add(new ChatItemVO(userInfo.getUserId(), userInfo.getUserName(), null, userInfo.getUserProfileImgUrl(), null));
                            memberAdapter.notifyItemInserted(0);

                            RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadMembers(GlobalInfo.currentGroup.getMeetName()).enqueue(new Callback<ArrayList<UserInfo>>() {
                                @Override
                                public void onResponse(Call<ArrayList<UserInfo>> call, Response<ArrayList<UserInfo>> response) {
                                    for (int i = 0; i < response.body().size(); i++) {
                                        UserInfo userInfo = response.body().get(i);
                                        if (userInfo.getUserId().equals(GlobalInfo.currentGroup.getMasterId()))
                                            continue;
                                        memberVOS.add(userInfo);
                                        GlobalInfo.currentMoimMembers.add(userInfo.getUserId());
                                        GlobalInfo.currentChatItems.add(new ChatItemVO(userInfo.getUserId(), userInfo.getUserName(), null, userInfo.getUserProfileImgUrl(), null));
                                        memberAdapter.notifyItemInserted(memberVOS.size() - 1);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ArrayList<UserInfo>> call, Throwable t) {

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

    public void chckMeet() {
        try {
            RetrofitHelper.getRetrofit().create(RetrofitService.class).checkUserMeetData(GlobalInfo.myProfile.getUserId(), GlobalInfo.currentGroup.getMeetName()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject res = new JSONObject(response.body());
                        int code = res.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                        switch (code) {
                            case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                boolean isUsable = res.getBoolean(GlobalKey.NTWRK_RTN_TYPE.RESULT);
                                if (isUsable) {
                                    saveMeet();
                                } else {
                                    Toast.makeText(getContext(), "이미 가입된 모임입니다.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                break;
                        }
                    } catch (Exception e) {
                        mActivity.showNtwrkFailToast("1");
                        logException(e);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mActivity.showNtwrkFailToast("2");
                    DMFBCrash.logException(t);
                }
            });
        } catch (Exception e) {
            logException(e);
        }
    }

    public void saveMeet() {
        try {
            RetrofitHelper.getRetrofit().create(RetrofitService.class).saveUserMeetData(GlobalInfo.myProfile.getUserId(), GlobalInfo.currentGroup.getMeetName()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject res = new JSONObject(response.body());
                        int code = res.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                        switch (code) {
                            case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                loadMembers();
                                break;

                            case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                mActivity.showNtwrkFailToast("1");
                                break;
                        }
                    } catch (Exception e) {
                        mActivity.showNtwrkFailToast("2");
                        logException(e);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mActivity.showNtwrkFailToast("3");
                    DMFBCrash.logException(t);
                }
            });
        } catch (Exception e) {
            logException(e);
        }
    }

    public void loadMoims() {
        moimVOS.clear();
        moimAdapter.notifyDataSetChanged();
        RetrofitHelper.getRetrofitInstanceGson().create(RetrofitService.class).loadMoims(GlobalInfo.currentGroup.getMeetName()).enqueue(new Callback<ArrayList<MoimVO>>() {
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
