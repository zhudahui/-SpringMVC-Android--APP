package com.mobileclient.activity;

import com.mobileclient.app.IdentityImageView;
import com.mobileclient.dialog.ActionItem;
import com.mobileclient.dialog.TitlePopup;
import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;
import com.mobileclient.util.HttpUtil;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobileclient.dialog.TitlePopup.OnItemOnClickListener;
import com.mobileclient.util.ImageService;

import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoDetailActivity extends Activity implements View.OnClickListener{
	private static final String TAG = "uploadImage";
	private IdentityImageView userPhoto;
	private EditText userName,userPhone,userGender,userEmail,nickName,userId,studentId,userMoney,userAuthfile,userReputation,regTime,userType1,userPassword;
	private Button btnDown;
	/* 用户管理业务逻辑层 */
    User user=new User();
    UserService userService=new UserService();
    Bundle extras;
    String userType;
    private ImageView plus;
	private TitlePopup titlePopup;
	private ImageView img_right;
	User userInfo;
	public static final int TAKE_PHOTO = 1;//启动相机标识
	public static final int SELECT_PHOTO = 2;//启动相册标识
	private Bitmap orc_bitmap;//拍照和相册获取图片的Bitmap
	private File outputImagepath;//存储拍完照后的图片
	String imagePath=null;//存储路径
	String photoPath=null;
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
	//	TextView title = (TextView) this.findViewById(R.id.title);
	//	title.setText("查看用户详情");
		//ImageView back = (ImageView) this.findViewById(R.id.back_btn);
//		back.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});
       // plus=findViewById(R.id.plus);
       // plus.setOnClickListener(this);
		userId=findViewById(R.id.tv_userId);
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

		regTime=findViewById(R.id.tv_regTime);
		userType1=findViewById(R.id.tv_userType);
		userPassword=findViewById(R.id.tv_userPassword);
		//======================================

		//=========加号=====
		img_right =  findViewById(R.id.img_right);
		img_right.setImageResource(R.drawable.icon_add);
		img_right.setOnClickListener(this);
		//==============
		//btnEdit = findViewById(R.id.btnEdit);
        userPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				xiangceClick();
			}
		});
//		back=findViewById(R.id.back_btn);
//		title=findViewById(R.id.title);
//		title.setText("个人信息");


		initPopWindow();
		//titlePopup.show(findViewById(R.id.layout_bar));
		//====================


        //btnEdit = findViewById(R.id.btnEdit);
        //=========================================


//		back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//		userPhoto.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//xiangceClick();
//			}
//		});
        btnDown = (Button)findViewById(R.id.btnDown);
		 extras = this.getIntent().getExtras();
//======================================================
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


			}
		}else {
			userName.setText(extras.getString("userName"));
			studentId.setText(String.valueOf(extras.getInt("studentId")));
		}
		byte [] photo=null;
		photo=extras.getByteArray("photo");
		Bitmap iuserPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);
		// 获取图片数据
		userPhoto.getBigCircleImageView().setImageBitmap(iuserPhoto);
		if(userType.equals("快递员")) {
			btnDown.setVisibility(View.VISIBLE);
			userPhoto.getSmallCircleImageView().setImageResource(R.drawable.v);
		}

        userType1.setText(extras.getString("userType"));
		userPassword.setText(extras.getString("userPassword"));
		userId.setText(String.valueOf(extras.get("userId")));
		regTime.setText(extras.getString("regTime"));
		userPhone.setText(extras.getString("userPhone"));
		userGender.setText(extras.getString("userGender"));
		userEmail.setText(extras.getString("userEmail"));
		userMoney.setText(extras.getString("userMoney"));
		userReputation.setText(String.valueOf(extras.getInt("userReputation")));
		nickName.setText(extras.getString("nickName"));
//========================================================================
		//userType = findViewById(R.id.ET_userType);


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

	}

	//================================
	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, R.string.menu_groupchat,
				R.drawable.icon_menu_group));
		titlePopup.addAction(new ActionItem(this, R.string.menu_addfriend,
				R.drawable.icon_menu_addfriend));
		titlePopup.addAction(new ActionItem(this, R.string.menu_qrcode,
				R.drawable.icon_menu_sao));
		titlePopup.addAction(new ActionItem(this, R.string.menu_money,
				R.drawable.abv));
	}
	//======================================================
	private OnItemOnClickListener onitemClick = new OnItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			// mLoadingDialog.show();
			switch (position) {
				case 0:// 通过审核
					Log.i("ppppp",extras.getString("userPhoto")+"点击1");
					new Thread(new Runnable() {
						@Override
						public void run() {    //
							user.setUserId(extras.getInt("userId"));
							user.setUserName(extras.getString("userName"));
							user.setUserType("快递员");
							user.setUserPhoto(extras.getString("Photo"));
							user.setUserMoney(extras.getString("userMoney"));
							user.setUserGender(extras.getString("userGender"));
							user.setRegTime(extras.getString("regTime"));
							user.setUserEmail(extras.getString("userEmail"));
							user.setUserReputation(extras.getInt("userReputation"));
							user.setUserAuthFile(extras.getString("userAuthFile"));
							user.setUserPassword(extras.getString("userPassword"));
							user.setNickName(extras.getString("nickName"));
							user.setStudentId(extras.getInt("studentId"));
							user.setUserPhone(extras.getString("userPhone"));
							userService.UpdateUserInfo(user);
							Log.i("ppppp",""+"点击1");
							Message msg=new Message();
							Bundle bundle=new Bundle();
							bundle.putSerializable("user", user);
							msg.setData(bundle);
							msg.what=0x123;
							handler.sendMessage(msg);
						}
					}).start();
					break;
				case 1:// 不通过审核
//					Utils.start_Activity(MainActivity.this, PublicActivity.class,
//							new BasicNameValuePair(Constants.NAME, "添加朋友"));
					break;
				case 2:// 查看订单
//					Utils.start_Activity(MainActivity.this, CaptureActivity.class);
					break;
				case 3://编辑
					new Thread(new Runnable() {
						@Override
						public void run() {
							Message msg=new Message();
							if(imagePath!=null) {  //选择了新图片
								upload();
								user.setUserId(Integer.parseInt(userId.getText().toString()));
								user.setUserName(userName.getText().toString());
								user.setUserType(userType1.getText().toString());
								user.setUserPhoto(photoPath);
								user.setUserMoney(userMoney.getText().toString());
								user.setUserGender(userGender.getText().toString());
								user.setRegTime(regTime.getText().toString());
								user.setUserEmail(userEmail.getText().toString());
								user.setUserReputation(Integer.parseInt(userReputation.getText().toString()));
								user.setUserAuthFile(userAuthfile.getText().toString());
								user.setUserPassword(userPassword.getText().toString());
								user.setNickName(nickName.getText().toString());
								user.setStudentId(Integer.parseInt(studentId.getText().toString()));
								user.setUserPhone(userPhone.getText().toString());
								userService.UpdateUserInfo(user);
								Bundle bundle = new Bundle();
								bundle.putSerializable("user", user);
								msg.setData(bundle);
								msg.what = 0x123;
							}
							else{
								user.setUserPhoto(photoPath);
								user.setUserId(Integer.parseInt(userId.getText().toString()));
								user.setUserName(userName.getText().toString());
								user.setUserType(userType1.getText().toString());
								user.setUserMoney(userMoney.getText().toString());
								user.setUserGender(userGender.getText().toString());
								user.setRegTime(regTime.getText().toString());
								user.setUserEmail(userEmail.getText().toString());
								user.setUserReputation(Integer.parseInt(userReputation.getText().toString()));
								user.setUserAuthFile(userAuthfile.getText().toString());
								user.setUserPassword(userPassword.getText().toString());
								user.setNickName(nickName.getText().toString());
								user.setStudentId(Integer.parseInt(studentId.getText().toString()));
								user.setUserPhone(userPhone.getText().toString());
								userService.UpdateUserInfo(user);
								Bundle bundle=new Bundle();
								bundle.putSerializable("user", user);
								msg.setData(bundle);
								msg.what=0x124;
							}
						}
					}).start();




					break;
				default:
					break;
			}
		}
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_right:
//				if (index == 0) {
					titlePopup.show(findViewById(R.id.layout_bar));
//				} else {
////					Utils.start_Activity(UserInfoDetailActivity.this, PublicActivity.class,
////							new BasicNameValuePair(Constants.NAME, "添加朋友"));
//				}
				break;
			default:
				break;
		}
	}

	//=====================

	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0x120) {

				userInfo= (User) msg.getData().getSerializable("user");
				userType=userInfo.getUserType();
				if(userType.equals("普通用户")){
					if(extras.getString("userAuthFile").equals("--")) {
						userName.setText("未认证");
						studentId.setText("未认证");
					}
					else {
						userName.setText("待认证" + user.getUserName());
						studentId.setText("待认证"+user.getStudentId());
						//如果是待审核用户，则出现审核按钮


					}
				}else {
					userName.setText(user.getUserName());
					studentId.setText(String.valueOf(user.getStudentId()));
				}
			    byte [] photo=null;
				photo=msg.getData().getByteArray("userPhoto");
				// 获取图片数据
				Bitmap photo1 = BitmapFactory.decodeByteArray(photo, 0, photo.length);
				userPhoto.getBigCircleImageView().setImageBitmap(photo1);
				if(userType.equals("快递员")) {
					btnDown.setVisibility(View.VISIBLE);
					userPhoto.getSmallCircleImageView().setImageResource(R.drawable.v);
				}


				userId.setText(String.valueOf(userInfo.getUserId()));
				regTime.setText(userInfo.getRegTime());
				userPhone.setText(userInfo.getUserPhone());
				userGender.setText(userInfo.getUserGender());
				userEmail.setText(userInfo.getUserEmail());
				userMoney.setText(userInfo.getUserMoney());
				userReputation.setText(String.valueOf(user.getUserReputation()));
				nickName.setText(user.getNickName());




			}
			if(msg.what==0x123){
				setViews();
			}
			if(msg.what==0x124){
				setViews();
			}


		}
	};

    public void  setViews(){




    	new Thread(new Runnable() {
			@Override
			public void run() {
				user=userService.GetUserInfo(extras.getString("nickName"));
				byte[] userPhoto_data = null;
				try {
					userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + user.getUserPhoto());
					//Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
					Message msg=new Message();
					Bundle bundle=new Bundle();
					bundle.putSerializable("user", user);
					bundle.putByteArray("userPhoto",userPhoto_data);
					msg.setData(bundle);
					msg.what=0x120;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
	/**
	 * 打开相机
	 *
	 *
	 */
	public void xiangjiClick() {
		//checkSelfPermission 检测有没有 权限
//        PackageManager.PERMISSION_GRANTED 有权限
//        PackageManager.PERMISSION_DENIED  拒绝权限
		//一定要先判断权限,再打开相机,否则会报错
		if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
			//权限发生了改变 true  //  false,没有权限时
			if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
				new AlertDialog.Builder(this).setTitle("title")
						.setPositiveButton("ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 请求授权
								ActivityCompat.requestPermissions(UserInfoDetailActivity.this,new String[]{Manifest.permission.CAMERA},1);
							}
						}).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//不请求权限的操作
					}
				}).create().show();
			}else {
				ActivityCompat.requestPermissions(UserInfoDetailActivity.this,new String[]{Manifest.permission.CAMERA},1);
			}
		}else{
			take_photo();//已经授权了就调用打开相机的方法
		}
	}

	/**
	 * 打开相册
	 */
	public void xiangceClick() {
		select_photo();
	}

	/**
	 * 拍照获取图片
	 **/
	public void take_photo() {
		//获取系統版本
		int currentapiVersion = Build.VERSION.SDK_INT;
		// 激活相机
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			SimpleDateFormat timeStampFormat = new SimpleDateFormat(
					"yyyy_MM_dd_HH_mm_ss");
			String filename = timeStampFormat.format(new Date());
			outputImagepath = new File(Environment.getExternalStorageDirectory(),
					filename + ".jpg");
			if (currentapiVersion < 24) {
				// 从文件中创建uri
				Uri uri = Uri.fromFile(outputImagepath);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			} else {
				//兼容android7.0 使用共享文件的形式
				ContentValues contentValues = new ContentValues(1);
				contentValues.put(MediaStore.Images.Media.DATA, outputImagepath.getAbsolutePath());
				Uri uri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			}
		}
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		Log.d(TAG, "图片a " );
		startActivityForResult(intent, TAKE_PHOTO);
	}


	/*
	 * 判断sdcard是否被挂载
	 */
	public static boolean hasSdcard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 从相册中获取图片
	 */
	public void select_photo() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
		} else {
			openAlbum();
		}
	}

	/**
	 * 打开相册的方法
	 */
	private void openAlbum() {
		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		intent.setType("image/*");
		startActivityForResult(intent, SELECT_PHOTO);
	}


	/**
	 * 4.4以下系统处理图片的方法
	 */
	private void handleImageBeforeKitKat(Intent data) {
		Uri uri = data.getData();
		String imagePath = getImagePath(uri, null);
		displayImage(imagePath);
	}

	/**
	 * 4.4及以上系统处理图片的方法
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void handleImgeOnKitKat(Intent data) {

		Uri uri = data.getData();
		Log.d("uri=intent.getData :", "" + uri);
		if (DocumentsContract.isDocumentUri(this, uri)) {
			String docId = DocumentsContract.getDocumentId(uri);        //数据表里指定的行
			Log.d("getDocumentId(uri) :", "" + docId);
			Log.d("uri.getAuthority() :", "" + uri.getAuthority());
			if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
				String id = docId.split(":")[1];
				String selection = MediaStore.Images.Media._ID + "=" + id;
				imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
				Log.d(TAG, "图片d " +imagePath);
			} else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
				imagePath = getImagePath(contentUri, null);
				Log.i(TAG,"图片e"+imagePath);
			}

		} else if ("content".equalsIgnoreCase(uri.getScheme())) {
			imagePath = getImagePath(uri, null);
		}
		displayImage(imagePath);
		Log.i("TAG","图片c"+imagePath);
		//uploadImage(imagePath);

		// Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
		// circleImageView.setImageBitmap(bitmap);
	}
	/**
	 * 通过uri和selection来获取真实的图片路径,从相册获取图片时要用
	 */
	private String getImagePath(Uri uri, String selection) {
		String path = null;
		Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			}
			cursor.close();
		}
		return path;
	}


	/**
	 * 拍完照和从相册获取玩图片都要执行的方法(根据图片路径显示图片)
	 */
	private void displayImage(String imagePath) {
		if (!TextUtils.isEmpty(imagePath)) {
			//orc_bitmap = BitmapFactory.decodeFile(imagePath);//获取图片
			Log.d(TAG, "图片f " +imagePath);
			orc_bitmap = comp(BitmapFactory.decodeFile(imagePath)); //压缩图片
			ImgUpdateDirection(imagePath);//显示图片,并且判断图片显示的方向,如果不正就放正
		} else {
			Toast.makeText(this, "图片获取失败", Toast.LENGTH_LONG).show();
		}
	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			//打开相机后返回
			case TAKE_PHOTO:
				if (resultCode == RESULT_OK) {
					/**
					 * 这种方法是通过内存卡的路径进行读取图片，所以的到的图片是拍摄的原图
					 */
					Log.i(TAG, "图片b " );
					displayImage(outputImagepath.getAbsolutePath());
					Log.i(TAG, "拍照图片路径>>>>" + outputImagepath);
				}
				break;
			//打开相册后返回
			case SELECT_PHOTO:
				if (resultCode == RESULT_OK) {
					//判断手机系统版本号
					if (Build.VERSION.SDK_INT > 19) {
						//4.4及以上系统使用这个方法处理图片
						handleImgeOnKitKat(data);
					} else {
						handleImageBeforeKitKat(data);
					}
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 1:
				//判断是否有权限
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					openAlbum();//打开相册
					take_photo();//打开相机
				} else {
					Toast.makeText(this, "你需要许可", Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (orc_bitmap != null) {
			orc_bitmap.recycle();
		} else {
			orc_bitmap = null;
		}
	}


	/**
	 * 质量压缩方法
	 *
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		if (image != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while (baos.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
				baos.reset();//重置baos即清空baos
				options -= 10;//每次都减少10
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
			return bitmap;
		} else {
			return null;
		}

	}
	//比例压缩
	private Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return bitmap;//压缩好比例大小后再进行质量压缩
	}


	//改变拍完照后图片方向不正的问题
	private void ImgUpdateDirection(String filepath) {
		imagePath=filepath;
		Log.d(TAG, "图片g "+filepath );
		//uploadImage(filepath);
		//user.setUserPhoto(filepath);
		int digree = 0;//图片旋转的角度
		//根据图片的URI获取图片的绝对路径
		Log.i("tag", ">>>>>>>>>>>>>开始");
		//String filepath = ImgUriDoString.getRealFilePath(getApplicationContext(), uri);
		Log.i(TAG, "》》》》》》》》》》》》》》》" + filepath);
		//根据图片的filepath获取到一个ExifInterface的对象
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
			Log.i("tag", "exif》》》》》》》》》》》》》》》" + exif);
			if (exif != null) {

				// 读取图片中相机方向信息
				int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

				// 计算旋转角度
				switch (ori) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						digree = 90;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						digree = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						digree = 270;
						break;
					default:
						digree = 0;
						break;

				}

			}
			//如果图片不为0
			if (digree != 0) {
				// 旋转图片
				Matrix m = new Matrix();
				m.postRotate(digree);
				orc_bitmap = Bitmap.createBitmap(orc_bitmap, 0, 0, orc_bitmap.getWidth(),
						orc_bitmap.getHeight(), m, true);
			}
			if (orc_bitmap != null) {
				userPhoto.getBigCircleImageView().setImageBitmap(orc_bitmap);
				Log.i("eeeee",""+orc_bitmap);
			}
		} catch (IOException e) {
			e.printStackTrace();
			exif = null;
		}
	}
/****
 *
 * 上传图片
 *
 *
 */
	/**
	 * 异步上传文件
	 */
	public void upload() {
//		final ProgressDialog progressDialog = new ProgressDialog(this);
		//progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//progressDialog.show();
		File file = new File(imagePath);
		//构造一个请求体
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentTypeFor = fileNameMap.getContentTypeFor(file.getAbsolutePath());
		if (contentTypeFor == null) {
			contentTypeFor = "application/octet-stream";
		}
		Log.e("WJ", "文件类型=" + contentTypeFor);
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		builder.addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse(contentTypeFor), file));
		//构造一个请求
		Request request = new Request.Builder().url(HttpUtil.url).post(builder.build()).build();
		Log.e("WJ", "请求信息=" + request.toString());
		OkHttpClient okHttpClient=new OkHttpClient();
		try {
			Response response=okHttpClient.newCall(request).execute();
			photoPath=response.body().string();
			//	user.setUserPhoto(photo);
			Log.i("ppppppp","photo"+photoPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
