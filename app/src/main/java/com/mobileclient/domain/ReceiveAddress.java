package com.mobileclient.domain;

import java.io.Serializable;

public class ReceiveAddress implements Serializable {
    private int receiveId;   //地址Id
    private String receiveAddressName;  //地址名
    private int userId;  //用户Id
    private String receiveName;
    private String receivePhone;
    private String receiveState;

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(String receiveState) {
        this.receiveState = receiveState;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReceiveAddressName() {
        return receiveAddressName;
    }

    public void setReceiveAddressName(String receiveAddressName) {
        this.receiveAddressName = receiveAddressName;
    }
}
