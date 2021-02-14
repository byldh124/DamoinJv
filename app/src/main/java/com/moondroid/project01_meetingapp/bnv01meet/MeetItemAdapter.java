package com.moondroid.project01_meetingapp.bnv01meet;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeetItemAdapter extends RecyclerView.Adapter<MeetItemAdapter.VH> {

    Context context;
    ArrayList<ItemBaseVO> itemList;
    String[] interestList;
    String[] interestIconList;
    Resources res;

    public MeetItemAdapter(Context context, ArrayList<ItemBaseVO> itemList) {
        this.context = context;
        this.itemList = itemList;
        res = context.getResources();
        interestList = res.getStringArray(R.array.interest_list);
        interestIconList = res.getStringArray(R.array.interest_icon_img_url);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.layout_recycler_meet_base_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ItemBaseVO item = itemList.get(position);
        Glide.with(context).load(item.titleImgUrl).into(holder.ivProfile);
        int interestNum = 0;
        for (int i = 0; i < interestList.length; i++){
            if (item.interest.equals(interestList[i])){
                interestNum = i;
                break;
            }
        }
        Glide.with(context).load(interestIconList[interestNum]).into(holder.iconImg);
        holder.meetName.setText(item.meetName);
        String[] addresses = item.meetAddress.split(" ");
        String lastAddress = addresses[addresses.length-1];
        holder.meetAddress.setText(lastAddress);
        holder.purposeMessage.setText(item.purposeMessage);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class VH extends RecyclerView.ViewHolder {
        CircleImageView ivProfile;
        ImageView iconImg;
        TextView meetName;
        TextView meetAddress;
        TextView purposeMessage;


        public VH(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.circle_image_view_meet_basic_recycler_item);
            iconImg = itemView.findViewById(R.id.iv_icon_meet_basic_recycler_item);
            meetName = itemView.findViewById(R.id.tv_meet_name_meet_basic_recycler_item);
            meetAddress = itemView.findViewById(R.id.tv_location_meet_basic_recycler_item);
            purposeMessage = itemView.findViewById(R.id.tv_purpose_of_meet_meet_basic_recycler_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "상세페이지로 이동", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
