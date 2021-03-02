package com.moondroid.project01_meetingapp.page_tab1_info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.library.LinearLayoutManagerWrapper;
import com.moondroid.project01_meetingapp.variableobject.LocationVO;
import com.naver.maps.geometry.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChoiceMoimActivity extends AppCompatActivity {

    //    ArrayList<String> addresses;
    EditText etLocation;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Geocoder geocoder;
    ChoiceMoimLocationAdapter adapter;
    ArrayList<LocationVO> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_moim);

        toolbar = findViewById(R.id.toolbar_choice_moim);
        etLocation = findViewById(R.id.et_location_input);
        recyclerView = findViewById(R.id.recycler_moim_choice);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
        geocoder = new Geocoder(this, Locale.KOREA);
        addresses = new ArrayList<>();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new ChoiceMoimLocationAdapter(this, addresses);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickSearchLocation(View view) {
        addresses.clear();
        String objStr = etLocation.getText().toString();
        try {
            List<Address> result = geocoder.getFromLocationName(objStr, 10);
            for (int i = 0; i < result.size(); i++) {
                addresses.add(new LocationVO(result.get(i).getAddressLine(0).replace("대한민국 ", ""), new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude())));
                adapter.notifyItemInserted(addresses.size() - 1);
            }
            Toast.makeText(this, "" + addresses.size(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}