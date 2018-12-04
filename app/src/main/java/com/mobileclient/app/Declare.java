package com.mobileclient.app;
 
import java.io.File;

import android.app.Application;
import android.content.Context;

import com.mobileclient.util.HttpUtil;

public class Declare extends Application {

	
	@Override
	public void onCreate() {
		super.onCreate(); 
		CrashHandler crashHandler = CrashHandler.getInstance();    
	    crashHandler.init(getApplicationContext()); 
	    context = this.getApplicationContext(); 
	    File path = new File(HttpUtil.FILE_PATH);
	    if(!path.exists()) path.mkdirs();
	}
	 
	public static Context context;
	 
	
	private int userId;//用户Id
    private String userName;


	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	private String userType; //用户身份
    private  String userPhoto;//用户头像
    private int  companyLength;
	private int query;//判断查询状态
    private  String identify; //user或者admin
	private String userPassword;        //用户密码
	private String userPhone;           //用户手机号
	private String userGender;          //用户性别
	private String userEmail;           //用户邮箱
	private int  userReputation;        //用户信誉分
	private String userMoney;           //用户资金
	private String userAuthFile;        //认证文件
	private String regTime;             //创建时间
	private String nickName;           //登录名
	private int studentId;         //学号
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}


	public int getCompanyLength() {
		return companyLength;
	}

	public void setCompanyLength(int companyLength) {
		this.companyLength = companyLength;
	}

	public int getQuery() {
		return query;
	}

	public void setQuery(int query) {
		this.query = query;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
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

	public String getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(String userMoney) {
		this.userMoney = userMoney;
	}


	public int getUserReputation() {
		return userReputation;
	}

	public void setUserReputation(int userReputation) {
		this.userReputation = userReputation;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
}
