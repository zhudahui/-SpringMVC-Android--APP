package com.mobileclient.activity;

import java.util.Date;

import com.mobileclient.app.IdentityImageView;
import com.mobileclient.app.PopWindow;
import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;
import com.mobileclient.util.ActivityUtils;
import com.mobileclient.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoDetailActivity extends Activity implements View.OnClickListener{
	private IdentityImageView userPhoto;
	private EditText userName,userPhone,userGender,userEmail,nickName,userId,studentId,userMoney,userAuthfile,userReputation;
	private Button btnDown;
	/* 用户管理业务逻辑层 */
    User user=new User();
    UserService userService=new UserService();
    Bundle extras;
    String userType;
    private ImageView plus;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.userinfo_detail);
//		ImageView search = (ImageView) this.findViewById(R.id.search);
//		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看用户详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
        plus=findViewById(R.id.plus);
        plus.setOnClickListener(this);
		studentId = findViewById(R.id.tv_studentId);
		userName = findViewById(R.id.tv_userName);
		userPhoto=findViewById(R.id.iiv_userPhoto);
		//userType = findViewById(R.id.ET_userType);
		userPhone = findViewById(R.id.tv_userPhone);
		userGender = findViewById(R.id.tv_userGender);
		userEmail = findViewById(R.id.tv_userEmail);
		userMoney=findViewById(R.id.tv_userMoney);
		userReputation=findViewById(R.id.tv_userReputation);
		nickName=findViewById(R.id.tv_nickName);
		userId=findViewById(R.id.tv_userId);
		//btnEdit = findViewById(R.id.btnEdit);

		back=findViewById(R.id.back_btn);
		title=findViewById(R.id.title);
		title.setText("个人信息");



        search=findViewById(R.id.search);
        search.setVisibility(View.GONE);
		//====================
        studentId.setOnClickListener(this);
        userName.setOnClickListener(this);
        userPhoto.setOnClickListener(this);
        //userType = findViewById(R.id.ET_userType);
        userPhone.setOnClickListener(this);
        userGender.setOnClickListener(this);
        userEmail.setOnClickListener(this);
        userMoney.setOnClickListener(this);
        userReputation.setOnClickListener(this);
        nickName.setOnClickListener(this);
        //btnEdit = findViewById(R.id.btnEdit);
        //=========================================


		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		userPhoto.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//xiangceClick();
//			}
//		});
        btnDown = (Button)findViewById(R.id.btnDown);
		 extras = this.getIntent().getExtras();

        userType=extras.getString("userType");
        if(userType.equals("普通用户")){
        	if(extras.getString("userAuthFile").equals("--")) {
				userName.setText("未认证");
				studentId.setText("未认证");
			}
        	else {
				userName.setText("待认证" + extras.getString("userName"));
				studentId.setText("待认证"+extras.getInt("studentId"));
                   //如果是待审核用户，则出现审核按钮
                search.setVisibility(View.VISIBLE);
                search.setImageResource(R.drawable.shenhe);
			}
		}else {
			userName.setText(extras.getString("userName"));
			studentId.setText(extras.getInt("studentId"));
		}
        byte[] userPhoto_data = null;
        // 获取图片数据
        userPhoto_data=extras.getByteArray("photo");
        Bitmap photo = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
        userPhoto.getBigCircleImageView().setImageBitmap(photo);
        if(userType.equals("快递员")) {
            btnDown.setVisibility(View.VISIBLE);
            userPhoto.getSmallCircleImageView().setImageResource(R.drawable.v);
        }
        //userType = findViewById(R.id.ET_userType);
        userPhone.setText(extras.getString("userPhone"));
        userGender.setText(extras.getString("userGender"));
        userEmail.setText(extras.getString("userEmail"));
        userMoney.setText(extras.getString("userMoney"));
        userReputation.setText(extras.getString("userReputation"));
        nickName.setText(extras.getString("nickName"));
		Log.i("ppppp",""+extras.get("userAuthFile"));
		btnDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						HttpUtil.downloadFile(extras.get("userAuthFile").toString());
					}
				}).start();
				UserInfoDetailActivity.this.setTitle("正在开始下载认证文件....");
				Toast.makeText(getApplicationContext(), "下载成功，你也可以在mobileclient/upload目录查看！", Toast.LENGTH_SHORT).show();
			}
		});
		search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {    //审核通过
                        user.setUserId(extras.getInt("userId"));
                        user.setUserName(extras.getString("userName"));
                        user.setUserType("快递员");
                        user.setUserPhoto(extras.getString("userPhoto"));
                        user.setUserMoney(extras.getString("userMoney"));
                        user.setUserGender(extras.getString("userGender"));
                        user.setRegTime(extras.getString("regTime"));
                        user.setUserEmail(extras.getString("userEmail"));
                        user.setUserReputation(extras.getInt("userReputation"));
                        user.setUserAuthFile(extras.getString("userAuthFile"));
                        user.setUserPassword(extras.getString("userPassword"));
                        user.setNickName(extras.getString("nickName"));
                        user.setStudentId(extras.getInt("studentId"));
                        userService.UpdateUserInfo(user);
                    }
                }).start();
            }
        });
	}
//	/* 初始化显示详情界面的数据 */
//	private void initViewData() {
//
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				user = userService.GetUserInfo(nickName);
//				Message msg=new Message();
//				Bundle bundle=new Bundle();
//
//			}
//		}).start();



//		Date birthDate = new Date(userInfo.getBirthDate().getTime());
//		String birthDateStr = (birthDate.getYear() + 1900) + "-" + (birthDate.getMonth()+1) + "-" + birthDate.getDate();
//		this.TV_birthDate.setText(birthDateStr);
//		byte[] userPhoto_data = null;
//		try {
//			// 获取图片数据
//			userPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + userInfo.getUserPhoto());
//			Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0,userPhoto_data.length);
//			this.iv_userPhoto.setImageBitmap(userPhoto);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		this.TV_telephone.setText(userInfo.getTelephone());
//		this.TV_email.setText(userInfo.getEmail());
//		this.TV_address.setText(userInfo.getAddress());
//		this.TV_authFile.setText(userInfo.getAuthFile());
//		if(userInfo.getAuthFile().equals("")) {
//			// 获取认证文件数据
//			this.btnDownAuthFile.setVisibility(View.GONE);
//		}
//		this.TV_shenHeState.setText(userInfo.getShenHeState());
//		this.TV_regTime.setText(userInfo.getRegTime());
	//}

	final Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
			case R.id.plus:
				PopWindow popWindow = new PopWindow(this);
				popWindow.showPopupWindow(findViewById(R.id.plus));
				break;

        }

    }
    public void  update(){

	}
}
