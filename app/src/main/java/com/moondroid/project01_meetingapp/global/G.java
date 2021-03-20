package com.moondroid.project01_meetingapp.global;

import com.moondroid.project01_meetingapp.variableobject.ChatItemVO;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;
import java.util.ArrayList;

public class G {

    public static UserBaseVO myProfile = new UserBaseVO();
    public static ItemBaseVO currentItemBase = new ItemBaseVO();
    public static ArrayList<String> currentItemMembers = new ArrayList<>();
    public static ArrayList<ChatItemVO> currentChatItems = new ArrayList<>();

}
