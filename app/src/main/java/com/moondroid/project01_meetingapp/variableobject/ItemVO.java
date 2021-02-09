package com.moondroid.project01_meetingapp.variableobject;

public class ItemVO {
    public String meetName;
    public String meetAddress;
    public String purposeMessage;
    public String imgUrl;
    public String interest;

    public ItemVO() {
    }

    public ItemVO(String meetName, String meetAddress, String purposeMessage, String imgUrl, String interest) {
        this.meetName = meetName;
        this.meetAddress = meetAddress;
        this.purposeMessage = purposeMessage;
        this.imgUrl = imgUrl;
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

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
