package com.mobileclient.app;
 
import java.io.File;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;

import com.mobileclient.util.HttpUtil;


public class Declare extends Application {

	private static Declare sInstance;
	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		CrashHandler crashHandler = CrashHandler.getInstance();    
	    crashHandler.init(getApplicationContext()); 
	    context = this.getApplicationContext(); 
	    File path = new File(HttpUtil.FILE_PATH);

	    if(!path.exists()) path.mkdirs();
	}
	public static Declare getInstance() {
		return sInstance;
	}
	public static Context context;
	 
	
	private int userId;//用户Id
    private String userName;

    private List<Map<String, Object>> list;
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
	private int studentId;             //学号
	private String userAuthState;       //认证状态
	private String payPwd;  //
	//记载默认地址信息
	private int receiveId;   //地址Id
	private String receiveAddressName;  //地址名
	private int receiveUserId;  //  地址用户Id
	private String receiveName;
	private String receivePhone;
    private int receiveAddressId;


    private int adminUserId;  //管理员管理用户ID；
	private String adminNickName;//管理员管理用户



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

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public String getUserAuthState() {
		return userAuthState;
	}

	public void setUserAuthState(String userAuthState) {
		this.userAuthState = userAuthState;
	}




	//退出
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}


	public int getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(int receiveId) {
		this.receiveId = receiveId;
	}

	public String getReceiveAddressName() {
		return receiveAddressName;
	}

	public void setReceiveAddressName(String receiveAddressName) {
		this.receiveAddressName = receiveAddressName;
	}

	public int getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(int receiveUserId) {
		this.receiveUserId = receiveUserId;
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

	public int getReceiveAddressId() {
		return receiveAddressId;
	}

	public void setReceiveAddressId(int receiveAddressId) {
		this.receiveAddressId = receiveAddressId;
	}

	public int getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(int adminUserId) {
		this.adminUserId = adminUserId;
	}

	public String getAdminNickName() {
		return adminNickName;
	}

	public void setAdminNickName(String adminNickName) {
		this.adminNickName = adminNickName;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
}
