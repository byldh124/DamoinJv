package com.moondroid.project01_meetingapp.presenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.UserBaseVO;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.ui.activity.ProfileActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformationMemberAdapter extends RecyclerView.Adapter<InformationMemberAdapter.VH> {

    private Context context;
    private ArrayList<UserBaseVO> memberVOS;

    public InformationMemberAdapter(Context context, ArrayList<UserBaseVO> memberVOS) {
        this.context = context;
        this.memberVOS = memberVOS;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.layout_recycler_item_information_member, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (memberVOS.get(position) == null) return;
        if (position == 0) holder.tvMaster.setVisibility(View.VISIBLE);

        if (memberVOS.get(position).getUserProfileImgUrl() != null) {
            if (memberVOS.get(position).getUserProfileImgUrl().contains("http")) {
                Glide.with(context).load(memberVOS.get(position).getUserProfileImgUrl()).into(holder.ivProfile);
            } else {
                Glide.with(context).load(RetrofitHelper.getUrlForImg() + memberVOS.get(position).getUserProfileImgUrl()).into(holder.ivProfile);
            }
        }

        holder.tvName.setText(memberVOS.get(position).getUserName());

        if (memberVOS.get(position).getUserProfileMessage() != null) {
            holder.tvMsg.setText(memberVOS.get(position).getUserProfileMessage());
        }
    }

    @Override
    public int getItemCount() {
        return memberVOS.size();
    }

    class VH extends RecyclerView.ViewHolder {
        CircleImageView ivProfile;
        TextView tvName, tvMsg, tvMaster;

        public VH(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_information_member_profile);
            tvName = itemView.findViewById(R.id.tv_information_member_name);
            tvMsg = itemView.findViewById(R.id.tv_information_member_msg);
            tvMaster = itemView.findViewById(R.id.tv_master);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    UserBaseVO userBaseVO = memberVOS.get(pos);
                    String memberInformation = new Gson().toJson(userBaseVO);
                    context.startActivity(new Intent(context, ProfileActivity.class).putExtra("memberInformation", memberInformation));
                }
            });
        }
    }
}

