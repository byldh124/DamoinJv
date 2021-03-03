package com.moondroid.project01_meetingapp.page_tab1_info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.variableobject.LocationVO;

import java.util.ArrayList;
import java.util.List;

public class ChoiceMoimLocationAdapter extends RecyclerView.Adapter<ChoiceMoimLocationAdapter.VH> {

    private Context context;
    private ArrayList<LocationVO> addresses;

    public ChoiceMoimLocationAdapter(Context context, ArrayList<LocationVO> addresses) {
        this.context = context;
        this.addresses = addresses;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.layout_recycler_item_choice_moim_location, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tvLocationResult.setText(addresses.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tvLocationResult;
        public VH(@NonNull View itemView) {
            super(itemView);
            tvLocationResult = itemView.findViewById(R.id.tv_choice_moim_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent intent = ((ChoiceMoimActivity)context).getIntent();
                    intent.putExtra("address", addresses.get(pos).getAddress());
                    intent.putExtra("lat", addresses.get(pos).getLatLng().latitude);
                    intent.putExtra("lng", addresses.get(pos).getLatLng().longitude);
                    ((ChoiceMoimActivity) context).setResult(Activity.RESULT_OK, intent);
                    ((ChoiceMoimActivity) context).finish();

                }
            });
        }
    }
}
