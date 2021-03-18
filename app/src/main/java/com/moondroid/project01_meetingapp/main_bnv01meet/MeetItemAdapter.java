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
import com.moondroid.project01_meetingapp.library.RetrofitService;
import com.moondroid.project01_meetingapp.page.PageActivity;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;
import com.moondroid.project01_meetingapp.variableobject.ItemDetailVO;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetItemAdapter extends RecyclerView.Adapter<MeetItemAdapter.VH> {

   private Context context;
   private ArrayList<ItemBaseVO> itemList;
   private String[] interestList;
   private String[] interestIconList;
   private Resources res;

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
                    RetrofitHelper.getRetrofitInstanceScalars().create(RetrofitService.class).uploadRecentMoim(G.myProfile.getUserId(), G.currentItemBase.getMeetName(), new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {


                            Intent intent = new Intent(context, PageActivity.class);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });


                }
            });
        }
    }
}
