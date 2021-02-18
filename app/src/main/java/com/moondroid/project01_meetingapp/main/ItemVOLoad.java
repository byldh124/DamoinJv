package com.moondroid.project01_meetingapp.main;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.moondroid.project01_meetingapp.global.G;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;
import com.moondroid.project01_meetingapp.variableobject.ItemVO;

import java.util.ArrayList;

public class ItemVOLoad {
    ArrayList<ItemBaseVO> itemList;

    public ArrayList<ItemBaseVO> load() {
        itemList = new ArrayList<>();
        G.itemsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ItemBaseVO itemBaseVO = ds.getValue(ItemBaseVO.class);
                    itemList.add(itemBaseVO);
                }
            }
        });
        return itemList;
    }
}
