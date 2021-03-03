package com.moondroid.project01_meetingapp.variableobject;

public class ChatItemVO {
    private String userId;
    private String userName;
    private String time;
    private String profileImgUrl;
    private String message;

    public ChatItemVO() {
    }

    public ChatItemVO(String userId, String userName, String time, String profileImgUrl, String message) {
        this.userId = userId;
        this.userName = userName;
        this.time = time;
        this.profileImgUrl = profileImgUrl;
        this.message = message;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTime() {
        return time;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public String getMessage() {
        return message;
    }
}
