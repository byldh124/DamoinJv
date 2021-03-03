package com.moondroid.project01_meetingapp.main_bnv01meet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.main_bnv03mypage.MyPageFragmentBottomTab3;
import com.moondroid.project01_meetingapp.option01search.SearchActivity;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {

    private Context context;
    private String[] categories;
    private Fragment fragment;

    public CategoryAdapter(Context context, String[] categories, Fragment fragment) {
        this.context = context;
        this.categories = categories;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.layout_bottom_tab1_category_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tvCategory.setText(categories[position]);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView tvCategory;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_bottom_tab1_category_recycler);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    String interest = categories[pos];
                    if (pos == 0) {
                        ((MeetFragmentBottomTab1) fragment).loadData();
                    } else {
                        ((MeetFragmentBottomTab1) fragment).loadData(interest);
                    }
                }
            });
        }
    }

}
