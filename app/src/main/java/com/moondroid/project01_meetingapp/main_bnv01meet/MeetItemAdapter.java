package com.moondroid.project01_meetingapp.main_bnv01meet;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.page.PageActivity;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;
import com.moondroid.project01_meetingapp.variableobject.ItemDetailVO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

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

        Picasso.get().load(RetrofitHelper.getUrlForImg() + item.titleImgUrl).into(holder.ivProfile);

        int interestNum = new ArrayList<>(Arrays.asList(interestList)).indexOf(item.meetInterest);
        if (interestNum < 0) interestNum = 1;
        Glide.with(context).load(interestIconList[interestNum]).into(holder.iconImg);

        holder.meetName.setText(item.meetName);
        if (item.meetLocation != null) {
            String[] addresses = item.meetLocation.split(" ");
            String lastAddress = addresses[addresses.length - 1];
            holder.meetAddress.setText(lastAddress);
        }
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
                    itemTitle = itemList.get(pos).meetName;

                    G.currentItemBase = itemList.get(pos);
                    Intent intent = new Intent(context, PageActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
