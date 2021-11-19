package com.moondroid.project01_meetingapp.data.model;

public class Group {
    private GroupInfo groupInfo;
    private GroupMembers groupMembers;
    private GroupSubInfo groupSubInfo;


    public Group() {
    }

    public Group(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }


    public Group(GroupInfo groupInfo, GroupMembers groupMembers, GroupSubInfo groupSubInfo) {
        this.groupInfo = groupInfo;
        this.groupMembers = groupMembers;
        this.groupSubInfo = groupSubInfo;
    }

    public GroupInfo getItemBaseVO() {
        return groupInfo;
    }

    public GroupMembers getItemMemberVO() {
        return groupMembers;
    }

    public GroupSubInfo getItemDetailVO() {
        return groupSubInfo;
    }

    public void setItemBaseVO(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public void setItemMemberVO(GroupMembers groupMembers) {
        this.groupMembers = groupMembers;
    }
    public void setItemDetailVO(GroupSubInfo groupSubInfo) {
        this.groupSubInfo = groupSubInfo;
    }


}
