package com.moondroid.project01_meetingapp.variableobject;

public class ItemBaseVO {
    public String meetName;
    public String meetLocation;
    public String purposeMessage;
    public String titleImgUrl;
    public String meetInterest;
    public String introImgUrl;
    public String message;
    public String masterId;

    public ItemBaseVO() {
    }

    public ItemBaseVO(String meetName, String meetLocation, String purposeMessage, String titleImgUrl, String meetInterest) {
        this.meetName = meetName;
        this.meetLocation = meetLocation;
        this.purposeMessage = purposeMessage;
        this.titleImgUrl = titleImgUrl;
        this.meetInterest = meetInterest;
    }

    public ItemBaseVO(String meetName, String meetLocation, String purposeMessage, String titleImgUrl, String meetInterest, String introImgUrl, String message, String masterId) {
        this.meetName = meetName;
        this.meetLocation = meetLocation;
        this.purposeMessage = purposeMessage;
        this.titleImgUrl = titleImgUrl;
        this.meetInterest = meetInterest;
        this.introImgUrl = introImgUrl;
        this.message = message;
        this.masterId = masterId;
    }
}
