package com.moondroid.project01_meetingapp.data.model;

import java.util.ArrayList;

public class GroupMembers {
    private String master;
    private ArrayList<String> member;

    public GroupMembers() {
        member = new ArrayList<>();
    }
    public GroupMembers(String master, ArrayList<String> member) {
        this.master = master;
        this.member = member;
    }

    public String getMaster() {
        return master;
    }

    public ArrayList<String> getMember() {
        return member;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public void setMember(ArrayList<String> member) {
        this.member = member;
    }
}
