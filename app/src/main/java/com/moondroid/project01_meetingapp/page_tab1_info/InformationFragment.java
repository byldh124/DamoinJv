package com.moondroid.project01_meetingapp.page_tab1_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;

import java.util.ArrayList;
import java.util.Arrays;

public class InformationFragment extends Fragment {

    ImageView ivIntroImg, ivInterestIcon;
    TextView tvMessage, tvMeetName;
    RecyclerView recyclerViewJungMo, recyclerViewMembers;
    ArrayList<String> interestList;
    Button btnJoin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_tab1_information,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //xml Reference
        ivInterestIcon = view.findViewById(R.id.iv_page_information_interest_icon);
        ivIntroImg = view.findViewById(R.id.iv_page_information_intro_image);
        tvMessage = view.findViewById(R.id.tv_page_information_message);
        tvMeetName = view.findViewById(R.id.tv_page_information_title);
        recyclerViewJungMo = view.findViewById(R.id.recycler_page_moim_information);
        recyclerViewMembers = view.findViewById(R.id.recycler_page_members);
        btnJoin = view.findViewById(R.id.btn_join);

        interestList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.interest_list)));
        int interestNum = interestList.indexOf(G.currentItemBase.interest);
        Glide.with(getContext()).load(getResources().getStringArray(R.array.interest_icon_img_url)[interestNum]).into(ivInterestIcon);
        tvMeetName.setText(G.currentItemBase.meetName);


    }
}
