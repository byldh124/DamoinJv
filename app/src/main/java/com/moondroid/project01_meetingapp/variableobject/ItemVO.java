package com.moondroid.project01_meetingapp.variableobject;

public class ItemVO {
    public ItemBaseVO itemBaseVO;

    public ItemVO() {
    }

    public ItemVO(ItemBaseVO itemBaseVO) {
        this.itemBaseVO = itemBaseVO;
    }

    public void setItemBaseVO(ItemBaseVO itemBaseVO) {
        this.itemBaseVO = itemBaseVO;
    }
}
