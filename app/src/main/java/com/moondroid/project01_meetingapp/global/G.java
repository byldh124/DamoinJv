package com.moondroid.project01_meetingapp.global;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;
import com.moondroid.project01_meetingapp.variableobject.ItemDetailVO;
import com.moondroid.project01_meetingapp.variableobject.ItemMemberVO;
import com.moondroid.project01_meetingapp.variableobject.ItemVO;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

public class G {
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static DatabaseReference itemsRef = firebaseDatabase.getReference("items");
    public static DatabaseReference usersRef = firebaseDatabase.getReference("users");

    public static UserBaseVO myProfile = new UserBaseVO();
    public static ItemVO currentItem = new ItemVO();
    public static ItemBaseVO currentItemBase = new ItemBaseVO();
    public static String ItemMasterId = new String();
    public static ItemDetailVO currentItemDetail = new ItemDetailVO();
    public static ItemMemberVO currentItemMember = new ItemMemberVO();
}
