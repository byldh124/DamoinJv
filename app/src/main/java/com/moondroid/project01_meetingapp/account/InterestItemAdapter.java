package com.moondroid.project01_meetingapp.account;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;

public class InterestItemAdapter extends RecyclerView.Adapter<InterestItemAdapter.VH> {
    Activity context;
    Resources resources;
    String[] imgUrls;
    String[] itemTitles;
    String interest;
    String iconUrl;

    public InterestItemAdapter(Activity context) {
        this.context = context;
        resources = context.getResources();
        imgUrls = resources.getStringArray(R.array.interest_icon_img_url);
        itemTitles = resources.getStringArray(R.array.interest_list);
        this.interest = itemTitles[itemTitles.length - 1];
        this.iconUrl = imgUrls[imgUrls.length - 1];
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.layout_interest_recycler_item, parent, false));
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

    class VH extends RecyclerView.ViewHolder {
        ImageView ivInterestIcon;
        TextView tvInterestTitle;
        Intent intent;

        public VH(@NonNull View itemView) {
            super(itemView);
            ivInterestIcon = itemView.findViewById(R.id.iv_interest_icon);
            tvInterestTitle = itemView.findViewById(R.id.tv_interest_title);
            intent = context.getIntent();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    String sendClass = intent.getStringExtra("sendClass");
                    interest = itemTitles[pos];
                    iconUrl = imgUrls[pos];

                    switch (sendClass){
                        case "Main":
                            //TODO interest 받아서 DB에 넣기 users/userName/interest 교체
                            G.myProfile.userInterest = interest;
                            G.usersRef.child(G.myProfile.userId).child("base").setValue(G.myProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    context.finish();
                                }
                            });
                            break;
                        case "Create":
                            intent.putExtra("interest", interest);
                            intent.putExtra("iconUrl", iconUrl);
                            context.setResult(Activity.RESULT_OK, intent);
                            context.finish();
                            break;
                        case "Account":
                            intent.putExtra("interest", interest);
                            context.setResult(Activity.RESULT_OK, intent);
                            context.finish();
                            break;

                    }
                }
            });
        }
    }
}
