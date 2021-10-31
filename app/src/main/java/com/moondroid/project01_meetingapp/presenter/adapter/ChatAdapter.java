package com.moondroid.project01_meetingapp.presenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.data.model.ChatItemVO;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChatItemVO> chatItems;

    public ChatAdapter(Context context, ArrayList<ChatItemVO> chatItems) {
        this.context = context;
        this.chatItems = chatItems;
    }

    @Override
    public int getCount() {
        return chatItems.size();
    }

    @Override
    public Object getItem(int position) {
        return chatItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatItemVO chatItem = chatItems.get(position);
        View itemView = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (chatItem.getUserId().equals(GlobalInfo.myProfile.getUserId())) {
            itemView = inflater.inflate(R.layout.layout_chat_list_view_my_box, parent, false);
        } else {
            itemView = inflater.inflate(R.layout.layout_chat_list_view_other_box, parent, false);
        }

        CircleImageView civ = itemView.findViewById(R.id.chat_profile_img);
        TextView tvName = itemView.findViewById(R.id.tv_chat_name);
        TextView tvMsg = itemView.findViewById(R.id.tv_chat_message);
        TextView tvTime = itemView.findViewById(R.id.tv_chat_time);

        tvName.setText(chatItem.getUserName());
        tvMsg.setText(chatItem.getMessage());
        tvTime.setText(chatItem.getTime());

        if (chatItem.getProfileImgUrl() != null) {
            if (chatItem.getProfileImgUrl().contains("http")) {
                Glide.with(context).load(chatItem.getProfileImgUrl()).into(civ);
            } else {
                Glide.with(context).load(RetrofitHelper.getUrlForImg() + chatItem.getProfileImgUrl()).into(civ);
            }
        }

        return itemView;
    }

    public void loadMemberChatInfo(){

    }
}
