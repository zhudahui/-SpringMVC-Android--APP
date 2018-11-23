package com.mobileclient.domain;

import java.io.Serializable;

public class User implements Serializable {
    private int userId;                 //用户ID
    private String userName;            //用户名
    private String userPassword;        //用户密码
    private String userPhoto;           //用户头像
    private String userType;            //用户类型
    private String userPhone;           //用户手机号
    private String userGender;          //用户性别
    private String userEmail;           //用户邮箱
    private int  userPeputation;        //用户信誉分
    private String userMoney;           //用户资金
    private String userAuthFile;        //认证文件
    private String regTime;             //创建事件

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }



    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserPeputation() {
        return userPeputation;
    }

    public void setUserPeputation(int userPeputation) {
        this.userPeputation = userPeputation;
    }

    public String getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(String userMoney) {
        this.userMoney = userMoney;
    }

    public String getUserAuthFile() {
        return userAuthFile;
    }

    public void setUserAuthFile(String userAuthFile) {
        this.userAuthFile = userAuthFile;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
