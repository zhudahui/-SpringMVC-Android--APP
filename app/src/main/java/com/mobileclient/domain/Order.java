package com.mobileclient.domain;

import java.io.Serializable;

public class Order implements Serializable {
    private int orderId;       //订单Id
    private String orderName;   //订单名
    private int userId;          //发布者
    private String expressCompanyName;//快递公司
    private String expressCompanyAddress;//取货地址
    private int receiveAddressId;//收货地址
    private String addTime;     //创建事件
    private String orderState;  //订单状态
    private String orderPay;    //订单酬金
    private String remark;      //备注
    private int receiveCode;    //取货码
    private String userPhone;    //发布者手机号
    private String orderEvaluate; //订单评价
    private int takeUserId;     //代取者Id



    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getExpressCompanyName() {
        return expressCompanyName;
    }

    public void setExpressCompanyName(String expressCompanyName) {
        this.expressCompanyName = expressCompanyName;
    }

    public String getExpressCompanyAddress() {
        return expressCompanyAddress;
    }

    public void setExpressCompanyAdress(String expressCompanyAddress) {
        this.expressCompanyAddress = expressCompanyAddress;
    }

    public int getReceiveAddressId() {
        return receiveAddressId;
    }

    public void setReceiveAdressId(int receiveAddressId) {
        this.receiveAddressId = receiveAddressId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(String orderPay) {
        this.orderPay = orderPay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(int receiveCode) {
        this.receiveCode = receiveCode;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


    public String getOrderEvaluate() {
        return orderEvaluate;
    }

    public void setOrderEvaluate(String orderEvaluate) {
        this.orderEvaluate = orderEvaluate;
    }

    public int getTakeUserId() {
        return takeUserId;
    }

    public void setTakeUserId(int takeUserId) {
        this.takeUserId = takeUserId;
    }
}
