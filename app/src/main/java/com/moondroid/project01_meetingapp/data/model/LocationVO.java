package com.moondroid.project01_meetingapp.data.model;

import com.naver.maps.geometry.LatLng;

public class LocationVO {
    private String address;
    private LatLng latLng;

    public LocationVO() {
    }

    public LocationVO(String address, LatLng latLng) {
        this.address = address;
        this.latLng = latLng;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
