package com.moondroid.project01_meetingapp.variableobject;

public class ItemVO {
    private ItemBaseVO itemBaseVO;
    private ItemMemberVO itemMemberVO;
    private ItemDetailVO itemDetailVO;


    public ItemVO() {
    }

    public ItemVO(ItemBaseVO itemBaseVO) {
        this.itemBaseVO = itemBaseVO;
    }


    public ItemVO(ItemBaseVO itemBaseVO, ItemMemberVO itemMemberVO, ItemDetailVO itemDetailVO) {
        this.itemBaseVO = itemBaseVO;
        this.itemMemberVO = itemMemberVO;
        this.itemDetailVO = itemDetailVO;
    }

    public ItemBaseVO getItemBaseVO() {
        return itemBaseVO;
    }

    public ItemMemberVO getItemMemberVO() {
        return itemMemberVO;
    }

    public ItemDetailVO getItemDetailVO() {
        return itemDetailVO;
    }

    public void setItemBaseVO(ItemBaseVO itemBaseVO) {
        this.itemBaseVO = itemBaseVO;
    }

    public void setItemMemberVO(ItemMemberVO itemMemberVO) {
        this.itemMemberVO = itemMemberVO;
    }
    public void setItemDetailVO(ItemDetailVO itemDetailVO) {
        this.itemDetailVO = itemDetailVO;
    }


}
