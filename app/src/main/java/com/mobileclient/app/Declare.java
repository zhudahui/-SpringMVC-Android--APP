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
    private String identify; //用户身份
    private  String userPhoto;
    private int  companyLength;
	private int query;//判断查询状态

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
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
}
