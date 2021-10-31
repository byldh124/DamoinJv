package com.moondroid.project01_meetingapp.presenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moondroid.project01_meetingapp.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.VH> {

    private Activity context;
    private ArrayList<String> locations;

    public LocationAdapter(Activity context, ArrayList<String> locations) {
        this.context = context;
        this.locations = locations;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.layout_recycler_location_search_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tvLocationResult.setText(locations.get(position));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView tvLocationResult;
        Intent intent;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvLocationResult = itemView.findViewById(R.id.tv_location_search_result);
            intent = context.getIntent();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    try {
                        intent.putExtra("location", locations.get(pos));
                    } catch (Exception e) {
                        intent.putExtra("location", locations.get(getLayoutPosition()));
                    } finally {
                        context.setResult(Activity.RESULT_OK, intent);
                        context.finish();
                    }
                }
            });
        }
    }
}
