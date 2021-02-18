package com.moondroid.project01_meetingapp.variableobject;

public class ItemDetailVO {
    public String introImgUrl;
    public String message;

    public ItemDetailVO() {
    }

    public ItemDetailVO(String introImgUrl, String message) {
        this.introImgUrl = introImgUrl;
        this.message = message;
    }

    public String getIntroImgUrl() {
        return introImgUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setIntroImgUrl(String introImgUrl) {
        this.introImgUrl = introImgUrl;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
