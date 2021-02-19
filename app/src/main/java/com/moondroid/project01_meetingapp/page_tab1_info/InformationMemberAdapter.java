package com.moondroid.project01_meetingapp.page_tab1_info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformationMemberAdapter extends RecyclerView.Adapter<InformationMemberAdapter.VH> {

    Context context;
    ArrayList<UserBaseVO> memberVOS;

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
        if (position ==0) holder.tvMaster.setVisibility(View.VISIBLE);
        if (memberVOS.get(position).userProfileImgUrl != null) {
            Glide.with(context).load(memberVOS.get(position).userProfileImgUrl).into(holder.ivProfile);
        }

        holder.tvName.setText(memberVOS.get(position).userName);

        if (memberVOS.get(position).userProfileMessage != null) {
            holder.tvMsg.setText(memberVOS.get(position).userProfileMessage);
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
        }
    }
}

