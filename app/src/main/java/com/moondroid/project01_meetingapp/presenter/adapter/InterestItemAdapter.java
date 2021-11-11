package com.moondroid.project01_meetingapp.presenter.adapter;

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
import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalInfo;
import com.moondroid.project01_meetingapp.helpers.utils.GlobalKey;
import com.moondroid.project01_meetingapp.network.RetrofitHelper;
import com.moondroid.project01_meetingapp.network.RetrofitService;
import com.moondroid.project01_meetingapp.ui.activity.BaseActivity;

import org.json.JSONObject;

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
            try {
                ivInterestIcon = itemView.findViewById(R.id.iv_interest_icon);
                tvInterestTitle = itemView.findViewById(R.id.tv_interest_title);
                intent = context.getIntent();
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        int sendClass = intent.getIntExtra(GlobalKey.INTENT_PARAM_TYPE.SEND_ACTIVITY, 0);
                        interest = itemTitles[pos];
                        iconUrl = imgUrls[pos];

                        // 여러개의 액티비티에서 하나의 액티비티로 화면이 이동될때,
                        // 이전 액티비티를 알고 싶다면 Intent 의 Extra 를 동일한 이름으로 하여 보낸 액티비티의 정보를 넣어놓고
                        // 받는 쪽에서 그 정보를 해석하는 방법으로 동일 액티비티에서 여러가지 역할을 할 수 있다.
                        // (사실 이러면 안되지... 액티비티에서 다양한 역할을 하면 안되지....만 아직 초보니깐)
                        switch (sendClass) {
                            case GlobalKey.ACTIVITY_CODE.MAIN_ACTIVITY:
                                //개인 설정 화면(메인)에서 설정시 DB 업데이트
                                GlobalInfo.myProfile.setUserInterest(interest);
                                Retrofit retrofit = RetrofitHelper.getRetrofitInstanceScalars();
                                retrofit.create(RetrofitService.class).updateUserInterest(GlobalInfo.myProfile.getUserId(), interest).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        try {
                                            JSONObject jsonRes = new JSONObject(response.body());
                                            int code = jsonRes.getInt(GlobalKey.NTWRK_RTN_TYPE.CODE);
                                            switch (code) {
                                                case GlobalKey.NTWRK_RTN_TYPE.SUCCESS:
                                                    context.setResult(Activity.RESULT_OK);
                                                    break;
                                                case GlobalKey.NTWRK_RTN_TYPE.FAIL:
                                                    context.setResult(Activity.RESULT_CANCELED);
                                                    break;
                                            }
                                            context.finish();
                                        } catch (Exception e) {
                                            DMFBCrash.logException(e);
                                            ((BaseActivity) context).showNtwrkFailToast("1");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        ((BaseActivity) context).showNtwrkFailToast("2");
                                    }
                                });
                                break;
                            //모임 관심사 설정시 값 전달
                            case GlobalKey.ACTIVITY_CODE.CREATE_ACTIVITY:
                            case GlobalKey.ACTIVITY_CODE.OPTION_MODIFY_ACTIVITY:
                                intent.putExtra(GlobalKey.INTENT_PARAM_TYPE.INTEREST, interest);
                                intent.putExtra(GlobalKey.INTENT_PARAM_TYPE.ICON_URL, iconUrl);
                                context.setResult(Activity.RESULT_OK, intent);
                                context.finish();
                                break;
                            //회원가입에서 관심사 설정시 값 전달
                            case GlobalKey.ACTIVITY_CODE.ACCOUNT_ACTIVITY:
                                intent.putExtra(GlobalKey.INTENT_PARAM_TYPE.INTEREST, interest);
                                context.setResult(Activity.RESULT_OK, intent);
                                context.finish();
                                break;

                        }
                    }
                });
            } catch (Exception e) {
                DMFBCrash.logException(e);
            }
        }
    }
}
