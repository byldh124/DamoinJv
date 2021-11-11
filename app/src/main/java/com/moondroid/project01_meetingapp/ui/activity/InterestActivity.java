package com.moondroid.project01_meetingapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.databinding.ActivityInterestBinding;
import com.moondroid.project01_meetingapp.presenter.adapter.InterestItemAdapter;

public class InterestActivity extends BaseActivity {

    private static final String TAG = InterestActivity.class.getSimpleName();
    private InterestItemAdapter adapter;
    private ActivityInterestBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            layout = DataBindingUtil.setContentView(this, R.layout.activity_interest);

            setSupportActionBar(layout.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            adapter = new InterestItemAdapter(this);
            layout.recycler.setAdapter(adapter);

            layout.setInterestActivity(this);
        } catch (Exception e) {
            logException(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}