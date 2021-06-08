package com.moondroid.project01_meetingapp.global;

import com.moondroid.project01_meetingapp.variableobject.ChatItemVO;
import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;
import java.util.ArrayList;


// 멤버값들을 전역변수 처럼 활용하기 위한 클래스
// 멤버값들은 public static 으로 앱 클래스 전체에서 활용될 수 있다.
public class G {

    public static UserBaseVO myProfile = new UserBaseVO();
    public static ItemBaseVO currentItemBase = new ItemBaseVO();
    public static ArrayList<String> currentItemMembers = new ArrayList<>();
    public static ArrayList<ChatItemVO> currentChatItems = new ArrayList<>();
}
