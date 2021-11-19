package com.moondroid.project01_meetingapp.helpers.utils;

import com.moondroid.project01_meetingapp.data.model.ChatItemVO;
import com.moondroid.project01_meetingapp.data.model.GroupInfo;
import com.moondroid.project01_meetingapp.data.model.UserInfo;
import java.util.ArrayList;


// 멤버값들을 전역변수 처럼 활용하기 위한 클래스
// 멤버값들은 public static 으로 앱 클래스 전체에서 활용될 수 있다.
public class GlobalInfo {

    public static UserInfo myProfile = new UserInfo();
    public static GroupInfo currentGroup = new GroupInfo();
    public static ArrayList<String> currentMoimMembers = new ArrayList<>();
    public static ArrayList<ChatItemVO> currentChatItems = new ArrayList<>();
}
