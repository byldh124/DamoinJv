package com.moondroid.project01_meetingapp.data.model;

public class GroupSubInfo {
    private String introImgUrl;
    private String message;

    public GroupSubInfo() {
    }

    public GroupSubInfo(String introImgUrl, String message) {
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
