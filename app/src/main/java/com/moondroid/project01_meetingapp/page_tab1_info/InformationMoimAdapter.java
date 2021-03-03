package com.moondroid.project01_meetingapp.page_tab1_info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.variableobject.MoimVO;

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
        }
    }
}
