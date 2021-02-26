package com.moondroid.project01_meetingapp.variableobject;

public class ChatItemVO {
    public String userId;
    public String userName;
    public String time;
    public String profileImgUrl;
    public String message;

    public ChatItemVO() {
    }

    public ChatItemVO(String userId, String userName, String time, String profileImgUrl, String message) {
        this.userId = userId;
        this.userName = userName;
        this.time = time;
        this.profileImgUrl = profileImgUrl;
        this.message = message;
    }
}
