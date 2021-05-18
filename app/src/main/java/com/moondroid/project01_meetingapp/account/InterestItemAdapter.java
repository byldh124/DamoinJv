package com.moondroid.project01_meetingapp.account;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
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
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.library.RetrofitHelper;
import com.moondroid.project01_meetingapp.library.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InterestItemAdapter extends RecyclerView.Adapter<InterestItemAdapter.VH> {
    private Activity context;
    private Resources resources;
    private String[] imgUrls;
    private String[] itemTitles;
    private String interest;
    private String iconUrl;

    public InterestItemAdapter(Activity context) {
        this.context = context;
        resources = context.getResources();
        imgUrls = resources.getStringArray(R.array.interest_icon_img_url);
        itemTitles = resources.getStringArray(R.array.interest_list);
        this.interest = itemTitles[itemTitles.length - 1];
        this.iconUrl = imgUrls[imgUrls.length - 1];
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.layout_interest_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Glide.with(context).load(imgUrls[position]).into(holder.ivInterestIcon);
        holder.tvInterestTitle.setText(itemTitles[position]);
    }

    @Override
    public int getItemCount() {
        return imgUrls.length;
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView ivInterestIcon;
        TextView tvInterestTitle;
        Intent intent;

        public VH(@NonNull View itemView) {
            super(itemView);
            ivInterestIcon = itemView.findViewById(R.id.iv_interest_icon);
            tvInterestTitle = itemView.findViewById(R.id.tv_interest_title);
            intent = context.getIntent();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    String sendClass = intent.getStringExtra("sendClass");
                    interest = itemTitles[pos];
                    iconUrl = imgUrls[pos];

                    // 여러개의 액티비티에서 하나의 액티비티로 화면이 이동될때,
                    // 이전 액티비티를 알고 싶다면 Intent 의 Extra 를 동일한 이름으로 하여 보낸 액티비티의 정보를 넣어놓고
                    // 받는 쪽에서 그 정보를 해석하는 방법으로 동일 액티비티에서 여러가지 역할을 할 수 있다.
                    // (사실 이러면 안되지... 액티비티에서 다양한 역할을 하면 안되지....만 아직 초보니깐)
                    switch (sendClass){
                        case "Main":
                            //개인 설정 화면(메인)에서 설정시 DB 업데이트
                            G.myProfile.setUserInterest(interest);
                            Retrofit retrofit = RetrofitHelper.getRetrofitInstanceScalars();
                            retrofit.create(RetrofitService.class).updateUserInterest(G.myProfile.getUserId(), interest).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body().equals("changed")){
                                        Toast.makeText(context, "관심사가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                        context.finish();
                                    } else {
                                        Toast.makeText(context, "관심사 변경이 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.i("interestError", t.getMessage());
                                }
                            });
                            break;
                            //모임 관심사 설정시 값 전달
                        case "Create":
                        case "Modify":
                            intent.putExtra("interest", interest);
                            intent.putExtra("iconUrl", iconUrl);
                            context.setResult(Activity.RESULT_OK, intent);
                            context.finish();
                            break;
                            //회원가입에서 관심사 설정시 값 전달
                        case "Account":
                            intent.putExtra("interest", interest);
                            context.setResult(Activity.RESULT_OK, intent);
                            context.finish();
                            break;

                    }
                }
            });
        }
    }
}
