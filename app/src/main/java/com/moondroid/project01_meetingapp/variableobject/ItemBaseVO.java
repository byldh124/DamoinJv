package com.moondroid.project01_meetingapp.variableobject;

public class ItemBaseVO {
    private String meetName;
    private String meetLocation;
    private String purposeMessage;
    private String titleImgUrl;
    private String meetInterest;
    private String introImgUrl;
    private String message;
    private String masterId;

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

    public String getMeetName() {
        return meetName;
    }

    public String getMeetLocation() {
        return meetLocation;
    }

    public String getPurposeMessage() {
        return purposeMessage;
    }

    public String getTitleImgUrl() {
        return titleImgUrl;
    }

    public String getMeetInterest() {
        return meetInterest;
    }

    public String getIntroImgUrl() {
        return introImgUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    public void setMeetLocation(String meetLocation) {
        this.meetLocation = meetLocation;
    }

    public void setPurposeMessage(String purposeMessage) {
        this.purposeMessage = purposeMessage;
    }

    public void setTitleImgUrl(String titleImgUrl) {
        this.titleImgUrl = titleImgUrl;
    }

    public void setMeetInterest(String meetInterest) {
        this.meetInterest = meetInterest;
    }

    public void setIntroImgUrl(String introImgUrl) {
        this.introImgUrl = introImgUrl;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }
}
