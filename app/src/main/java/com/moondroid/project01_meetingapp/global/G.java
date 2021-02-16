package com.moondroid.project01_meetingapp.global;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

public class G {
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static DatabaseReference itemsRef = firebaseDatabase.getReference("items");
    public static DatabaseReference usersRef = firebaseDatabase.getReference("users");

    public static UserBaseVO userBaseInformation = null;
}
