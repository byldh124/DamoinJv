package com.moondroid.project01_meetingapp.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.moondroid.project01_meetingapp.R;

import java.util.ArrayList;

public class LocationChoiceActivity extends AppCompatActivity {

    Toolbar toolbarLocationChoiceActivity;
    EditText etLocationSearch;
    RecyclerView recyclerLocationResult;
    ArrayList<String> locations;
    LocationAdapter locationAdapter;
    String[] locationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_choice);

        toolbarLocationChoiceActivity = findViewById(R.id.toolbar_location_choice_activity);
        etLocationSearch = findViewById(R.id.et_location_activity_search_location);
        recyclerLocationResult = findViewById(R.id.recycler_location_search_result);

        locationData = getResources().getStringArray(R.array.location);

        setSupportActionBar(toolbarLocationChoiceActivity);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locations = new ArrayList<>();
        locationAdapter = new LocationAdapter(this, locations);
        recyclerLocationResult.setAdapter(locationAdapter);

        etLocationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locations.clear();
                for (int i = 0 ; i<locationData.length; i++){
                    if (locationData[i].contains(s)){
                        locations.add(locationData[i]);
                        locationAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}