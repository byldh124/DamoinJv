package com.moondroid.project01_meetingapp.variableobject;

public class ItemBaseVO {
    public String meetName;
    public String meetAddress;
    public String purposeMessage;
    public String titleImgUrl;
    public String interest;

    public ItemBaseVO() {
    }

    public ItemBaseVO(String meetName, String meetAddress, String purposeMessage, String titleImgUrl, String interest) {
        this.meetName = meetName;
        this.meetAddress = meetAddress;
        this.purposeMessage = purposeMessage;
        this.titleImgUrl = titleImgUrl;
        this.interest = interest;
    }

    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    public void setMeetAddress(String meetAddress) {
        this.meetAddress = meetAddress;
    }

    public void setPurposeMessage(String purposeMessage) {
        this.purposeMessage = purposeMessage;
    }

    public void setTitleImgUrl(String titleImgUrl) {
        this.titleImgUrl = titleImgUrl;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
