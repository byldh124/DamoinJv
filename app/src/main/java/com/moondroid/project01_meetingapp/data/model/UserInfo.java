package com.moondroid.project01_meetingapp.data.model;

public class UserInfo {
    private String userId;
    private String userName;
    private String userBirthDate;
    private String userGender;
    private String userLocation;
    private String userInterest;
    private String userProfileImgUrl;
    private String userProfileMessage;
    private String FCMToken;

    public UserInfo() {
    }


    public UserInfo(String userId, String userName, String userBirthDate, String userGender, String userLocation, String userInterest, String userProfileImgUrl, String userProfileMessage, String FCMToken) {
        this.userId = userId;
        this.userName = userName;
        this.userBirthDate = userBirthDate;
        this.userGender = userGender;
        this.userLocation = userLocation;
        this.userInterest = userInterest;
        this.userProfileImgUrl = userProfileImgUrl;
        this.userProfileMessage = userProfileMessage;
        this.FCMToken = FCMToken;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserBirthDate(String userBirthDate) {
        this.userBirthDate = userBirthDate;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public void setUserInterest(String userInterest) {
        this.userInterest = userInterest;
    }

    public void setUserProfileImgUrl(String userProfileImgUrl) {
        this.userProfileImgUrl = userProfileImgUrl;
    }

    public void setUserProfileMessage(String userProfileMessage) {
        this.userProfileMessage = userProfileMessage;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserBirthDate() {
        return userBirthDate;
    }

    public String getUserGender() {
        return userGender;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public String getUserInterest() {
        return userInterest;
    }

    public String getUserProfileImgUrl() {
        return userProfileImgUrl;
    }

    public String getUserProfileMessage() {
        return userProfileMessage;
    }

    public String getFCMToken() {
        return FCMToken;
    }
}
