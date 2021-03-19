package com.moondroid.project01_meetingapp.variableobject;

public class MoimVO {
    private String meetName;
    private String address;
    private String date;
    private String time;
    private String pay;
    private double lat;
    private double lng;
    private String joinMembers;

    public MoimVO() {
    }

    public MoimVO(String meetName, String address, String date, String time, String pay, double lat, double lng, String joinMembers) {
        this.meetName = meetName;
        this.address = address;
        this.date = date;
        this.time = time;
        this.pay = pay;
        this.lat = lat;
        this.lng = lng;
        this.joinMembers = joinMembers;
    }

    public String getJoinMembers() {
        return joinMembers;
    }

    public void setJoinMembers(String joinMembers) {
        this.joinMembers = joinMembers;
    }

    public String getMeetName() {
        return meetName;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPay() {
        return pay;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
