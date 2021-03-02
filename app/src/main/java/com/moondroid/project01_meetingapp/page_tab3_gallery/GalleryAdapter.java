package com.moondroid.project01_meetingapp.page_tab3_gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moondroid.project01_meetingapp.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.VH> {

    Context context;
    ArrayList<String> imgs;

    public GalleryAdapter(Context context, ArrayList<String> imgs) {
        this.context = context;
        this.imgs = imgs;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.layout_recycler_item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Glide.with(context).load(imgs.get(position)).into(holder.ivGalleryItem);
    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView ivGalleryItem;
        public VH(@NonNull View itemView) {
            super(itemView);
            ivGalleryItem = itemView.findViewById(R.id.iv_gallery_item);
        }
    }
}
