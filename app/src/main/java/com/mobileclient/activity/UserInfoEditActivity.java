package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;

import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoEditActivity extends Activity {
	private static final String TAG = "uploadImage";
	// 声明登录、取消按钮
	private Button btnRegister;
	// 声明用户名、密码输入框
	private EditText userId,userName,userPassword,userType,userPhone,userGender,userEmail;
	public static final int TO_SELECT_PHOTO = 3;
	private CircleImageView userPhoto;
	User user1=new User();
	User user=new User();
	UserService userService=new UserService();
	Intent intent=new Intent();
	private String picPath = null;
	private Uri imageUri;
	public static final int TAKE_PHOTO = 1;//启动相机标识
	public static final int SELECT_PHOTO = 2;//启动相册标识
	private Bitmap orc_bitmap;//拍照和相册获取图片的Bitmap
	private File outputImagepath;//存储拍完照后的图片
	String imagePath=null;//存储路径
	String reuslt;
	String photo=null;
	private String user_name;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.userinfo_edit);
		userId = findViewById(R.id.ET_userId);
		userName = findViewById(R.id.ET_userName);
		userPassword = findViewById(R.id.ET_userPwd);
		userPhoto=findViewById(R.id.userPhoto);
		//userType = findViewById(R.id.ET_userType);
		userPhone = findViewById(R.id.ET_userPhone);
		userGender = findViewById(R.id.ET_userGender);
		userEmail = findViewById(R.id.ET_userEmail);
		btnRegister = findViewById(R.id.btnRegister);
		userPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			//	xiangceClick();
			}
		});
		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(userId.getText().toString().equals(""))
				{
					Toast.makeText(UserInfoEditActivity.this, "学号输入不能为空!", Toast.LENGTH_LONG).show();
					userId.setFocusable(true);
					userId.requestFocus();
					return;
				}
				if(userName.getText().toString().equals(""))
				{
					Toast.makeText(UserInfoEditActivity.this, "姓名输入不能为空!", Toast.LENGTH_LONG).show();
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}if(userPassword.getText().toString().equals(""))
				{
					Toast.makeText(UserInfoEditActivity.this, "密码输入不能为空!", Toast.LENGTH_LONG).show();
					userPassword.setFocusable(true);
					userPassword.requestFocus();
					return;
				}

				if(userPhone.getText().toString().equals(""))
				{
					Toast.makeText(UserInfoEditActivity.this, "手机号输入不能为空!", Toast.LENGTH_LONG).show();
					userPhone.setFocusable(true);
					userPhone.requestFocus();
					return;
				}if(userGender.getText().toString().equals(""))
				{
					Toast.makeText(UserInfoEditActivity.this, "性别输入不能为空!", Toast.LENGTH_LONG).show();
					userGender.setFocusable(true);
					userGender.requestFocus();
					return;
				}if(userEmail.getText().toString().equals(""))
				{
					Toast.makeText(UserInfoEditActivity.this, "邮箱输入不能为空!", Toast.LENGTH_LONG).show();
					userEmail.setFocusable(true);
					userEmail.requestFocus();
					return;
				}
				//register();
			}
		});
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == REQ_CODE_CAMERA_userPhoto  && resultCode == Activity.RESULT_OK) {
//			carmera_path = HttpUtil.FILE_PATH + "/carmera_userPhoto.bmp";
//			BitmapFactory.Options opts = new BitmapFactory.Options();
//			opts.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(carmera_path, opts);
//			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
//			opts.inJustDecodeBounds = false;
//			try {
//				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
//				String jpgFileName = "carmera_userPhoto.jpg";
//				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
//				try {
//					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
//					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件
//					File bmpFile = new File(carmera_path);
//					bmpFile.delete();
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//				this.iv_userPhoto.setImageBitmap(booImageBm);
//				this.iv_userPhoto.setScaleType(ScaleType.FIT_CENTER);
//				this.userInfo.setUserPhoto(jpgFileName);
//			} catch (OutOfMemoryError err) {  }
//		}
//
//		if(requestCode == REQ_CODE_SELECT_IMAGE_userPhoto && resultCode == Activity.RESULT_OK) {
//			Bundle bundle = data.getExtras();
//			String filename =  bundle.getString("fileName");
//			String filepath = HttpUtil.FILE_PATH + "/" + filename;
//			BitmapFactory.Options opts = new BitmapFactory.Options();
//			opts.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(filepath, opts);
//			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);
//			opts.inJustDecodeBounds = false;
//			try {
//				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
//				this.iv_userPhoto.setImageBitmap(bm);
//				this.iv_userPhoto.setScaleType(ScaleType.FIT_CENTER);
//			} catch (OutOfMemoryError err) {  }
//			userInfo.setUserPhoto(filename);
//		}
//		if(requestCode == REQ_CODE_SELECT_FILE_authFile && resultCode == Activity.RESULT_OK) {
//			Bundle bundle = data.getExtras();
//			String filename =  bundle.getString("fileName");
//			String filepath = HttpUtil.FILE_PATH + "/upload/" + filename;
//			this.TV_authFile.setText(filepath);
//			String authFile = HttpUtil.uploadFile(filename);
//			userInfo.setAuthFile(authFile);
//		}
	}
}
