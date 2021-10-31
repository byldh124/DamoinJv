package com.moondroid.project01_meetingapp.presenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.ui.fragment.MeetFragmentBottomTab1;

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
