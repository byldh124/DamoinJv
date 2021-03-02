package com.moondroid.project01_meetingapp.variableobject;

public class MoimVO {
    String meetName;
    String address;
    String date;
    String time;
    String pay;
    double lat;
    double lng;

    public MoimVO() {
    }

    public MoimVO(String meetName, String address, String date, String time, String pay, double lat, double lng) {
        this.meetName = meetName;
        this.address = address;
        this.date = date;
        this.time = time;
        this.pay = pay;
        this.lat = lat;
        this.lng = lng;
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
}
