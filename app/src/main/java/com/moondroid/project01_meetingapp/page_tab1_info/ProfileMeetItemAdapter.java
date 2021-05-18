package com.moondroid.project01_meetingapp.page_tab1_info;

import android.content.Context;
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
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.page.PageActivity;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMeetItemAdapter extends RecyclerView.Adapter<ProfileMeetItemAdapter.VH> {

   private Context context;
   private ArrayList<ItemBaseVO> itemList;
   private String[] interestList;
   private String[] interestIconList;
   private Resources res;
   //기존 PageActivity 를 닫고 새로운 PageActivity 를 열기 위한 PageActivity Instance
   PageActivity pageActivity = (PageActivity) PageActivity.activity;

    public ProfileMeetItemAdapter(Context context, ArrayList<ItemBaseVO> itemList) {
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

        Picasso.get().load(RetrofitHelper.getUrlForImg() + item.getTitleImgUrl()).into(holder.ivProfile);

        int interestNum = new ArrayList<>(Arrays.asList(interestList)).indexOf(item.getMeetInterest());
        if (interestNum < 0) interestNum = 1;
        Glide.with(context).load(interestIconList[interestNum]).into(holder.iconImg);

        holder.meetName.setText(item.getMeetName());
        if (item.getMeetLocation() != null) {
            String[] addresses = item.getMeetLocation().split(" ");
            String lastAddress = addresses[addresses.length - 1];
            holder.meetAddress.setText(lastAddress);
        }
        holder.purposeMessage.setText(item.getPurposeMessage());


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
        String itemTitle;

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
                    int pos = getAdapterPosition();
                    itemTitle = itemList.get(pos).getMeetName();

                    G.currentItemBase = itemList.get(pos);
                    pageActivity.finish();
                    Intent intent = new Intent(context, PageActivity.class);
                    context.startActivity(intent);
                    ((ProfileActivity)context).finish();
                }
            });
        }
    }
}
