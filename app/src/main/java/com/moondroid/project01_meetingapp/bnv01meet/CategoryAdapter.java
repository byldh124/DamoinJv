package com.moondroid.project01_meetingapp.bnv01meet;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moondroid.project01_meetingapp.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {

    Context context;
    String[] categories;

    public CategoryAdapter(Context context, String[] categories) {
        this.context = context;
        this.categories = categories;
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
        }
    }

}
