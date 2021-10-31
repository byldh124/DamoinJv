package com.moondroid.project01_meetingapp.presenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.MoimVO;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.ui.activity.MoimInfoActivity;

import java.util.ArrayList;

public class InformationMoimAdapter extends RecyclerView.Adapter<InformationMoimAdapter.VH> {

    private Context context;
    private ArrayList<MoimVO> moimVOS;

    public InformationMoimAdapter(Context context, ArrayList<MoimVO> moimVOS) {
        this.context = context;
        this.moimVOS = moimVOS;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.layout_recycler_item_information_moim, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MoimVO moimVO = moimVOS.get(position);
        holder.tvDate.setText(moimVO.getDate());
        holder.tvTime.setText(moimVO.getTime());
        holder.tvAddress.setText(moimVO.getAddress());
        holder.tvPay.setText(moimVO.getPay() + "원");
    }

    @Override
    public int getItemCount() {
        return moimVOS.size();
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvTime;
        TextView tvAddress;
        TextView tvPay;

        public VH(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_information_moim_dates);
            tvTime = itemView.findViewById(R.id.tv_information_moim_time);
            tvAddress = itemView.findViewById(R.id.tv_information_moim_location);
            tvPay = itemView.findViewById(R.id.tv_information_moim_pay);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(GlobalInfo.currentMoimMembers.contains(GlobalInfo.myProfile.getUserId()))){
                        Toast.makeText(context, "모임에 가입된 사람만 가능합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int pos = getAdapterPosition();
                    MoimVO item = moimVOS.get(pos);
                    String moimItem = new Gson().toJson(item);
                    Intent intent = new Intent(context, MoimInfoActivity.class);
                    intent.putExtra("moimInfo", moimItem);
                    context.startActivity(intent);
                }
            });
        }
    }
}
