package com.moondroid.project01_meetingapp.account;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moondroid.project01_meetingapp.R;

public class InterestItemAdapter extends RecyclerView.Adapter<InterestItemAdapter.VH> {
    Context context;
    Resources resources;
    String[] imgUrls;
    String[] itemTitles;

    public InterestItemAdapter(Context context) {
        this.context = context;
        resources = context.getResources();
        imgUrls = resources.getStringArray(R.array.interest_icon_img_url);
        itemTitles = resources.getStringArray(R.array.interest_list);
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.layout_interest_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Glide.with(context).load(imgUrls[position]).into(holder.ivInterestIcon);

        holder.tvInterestTitle.setText(itemTitles[position]);

    }

    @Override
    public int getItemCount() {
        return imgUrls.length;
    }

    class VH extends RecyclerView.ViewHolder{
        ImageView ivInterestIcon;
        TextView tvInterestTitle;

        public VH(@NonNull View itemView) {
            super(itemView);
            ivInterestIcon = itemView.findViewById(R.id.iv_interest_icon);
            tvInterestTitle = itemView.findViewById(R.id.tv_interest_title);
        }
    }
}
