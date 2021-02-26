package com.moondroid.project01_meetingapp.variableobject;

public class UserBaseVO {
    public String userId;
    public String userPassword;
    public String userName;
    public String userBirthDate;
    public String userGender;
    public String userLocation;
    public String userInterest;
    public String userProfileImgUrl;
    public String userProfileMessage;
    public String FCMToken;

    public UserBaseVO() {
    }


    public UserBaseVO(String userId, String userPassword, String userName, String userBirthDate, String userGender, String userLocation, String userInterest, String userProfileImgUrl, String userProfileMessage, String FCMToken) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userBirthDate = userBirthDate;
        this.userGender = userGender;
        this.userLocation = userLocation;
        this.userInterest = userInterest;
        this.userProfileImgUrl = userProfileImgUrl;
        this.userProfileMessage = userProfileMessage;
        this.FCMToken = FCMToken;
    }
}
