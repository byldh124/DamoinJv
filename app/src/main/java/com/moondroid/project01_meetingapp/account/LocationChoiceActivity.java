package com.moondroid.project01_meetingapp.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.library.LinearLayoutManagerWrapper;

import java.util.ArrayList;

public class LocationChoiceActivity extends AppCompatActivity {

   private Toolbar toolbarLocationChoiceActivity;
   private EditText etLocationSearch;
   private RecyclerView recyclerLocationResult;
   private ArrayList<String> locations;
   private LocationAdapter locationAdapter;
   private String[] locationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_choice);

        //xml 참조영역
        toolbarLocationChoiceActivity = findViewById(R.id.toolbar_location_choice_activity);
        etLocationSearch = findViewById(R.id.et_location_activity_search_location);
        recyclerLocationResult = findViewById(R.id.recycler_location_search_result);
        recyclerLocationResult.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));

        locationData = getResources().getStringArray(R.array.location);

        //액션바 세팅
        setSupportActionBar(toolbarLocationChoiceActivity);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //리사이클러뷰 세팅
        locations = new ArrayList<>();
        locationAdapter = new LocationAdapter(this, locations);
        recyclerLocationResult.setAdapter(locationAdapter);

        //유저가 지역 입력시 리사이클러뷰 아이템 선택
        etLocationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locations.clear();
                //adapter 에 들어간 아이템이 clear 된 후에 notifyDataSetChanged()을 하지 않으면
                //RecyclerView 안에는 아이템이 포함되어 있지만 실제로 어댑터 내부에 데이터는 비어있는 현상이 발생된다.
                //(Click Event 처리시 Index Out of Bounds Exception 이 방생하기 좋음)
                //Data clear 한 이후에는 꼭 어댑터에 notifyDataSetChanged()를 해주자
                locationAdapter.notifyDataSetChanged();
                for (int i = 0 ; i<locationData.length; i++){
                    if (locationData[i].contains(s)){
                        locations.add(locationData[i]);
                        locationAdapter.notifyItemInserted(locations.size()-1);
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