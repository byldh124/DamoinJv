package com.moondroid.project01_meetingapp.variableobject;

public class ItemVO {
    public String meetName;
    public String meetAddress;
    public String objectMessage;
    public String imgUrl;

    public ItemVO() {
    }

    public ItemVO(String meetName, String meetAddress, String objectMessage, String imgUrl) {
        this.meetName = meetName;
        this.meetAddress = meetAddress;
        this.objectMessage = objectMessage;
        this.imgUrl = imgUrl;
    }
}
