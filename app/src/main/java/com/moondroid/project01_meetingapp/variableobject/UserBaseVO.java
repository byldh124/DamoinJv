package com.moondroid.project01_meetingapp.variableobject;

public class UserBaseVO {
    public String userId;
    public String userPassword;
    public String userName;
    public String userBirthDate;
    public String userGender;
    public String userAddress;
    public String userInterest;
    public String userProfileImgUrl;
    public String userProfileMessage;

    public UserBaseVO() {
    }


    public UserBaseVO(String userId, String userPassword, String userName, String userBirthDate, String userGender, String userAddress, String userInterest, String userProfileImgUrl, String userProfileMessage) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userBirthDate = userBirthDate;
        this.userGender = userGender;
        this.userAddress = userAddress;
        this.userInterest = userInterest;
        this.userProfileImgUrl = userProfileImgUrl;
        this.userProfileMessage = userProfileMessage;
    }
}
