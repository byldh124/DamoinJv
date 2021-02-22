package com.moondroid.project01_meetingapp.variableobject;

public class ItemBaseVO {
    public String meetName;
    public String meetLocation;
    public String purposeMessage;
    public String titleImgUrl;
    public String meetInterest;

    public ItemBaseVO() {
    }

    public ItemBaseVO(String meetName, String meetLocation, String purposeMessage, String titleImgUrl, String meetInterest) {
        this.meetName = meetName;
        this.meetLocation = meetLocation;
        this.purposeMessage = purposeMessage;
        this.titleImgUrl = titleImgUrl;
        this.meetInterest = meetInterest;
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
}
