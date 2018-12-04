package com.mobileclient.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;

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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoEditActivity extends Activity {
	private static final String TAG = "uploadImage";
	// 声明登录、取消按钮
	private Button btnEdit;
	// 声明用户名、密码输入框
	private EditText userPhone,userGender,userEmail;
	private TextView studentId,nickName,tv_studentId,userName,TV_userName;
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
	Declare declare;
	private User userInfo;
	private ImageView back,search;
	private TextView title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.userinfo_edit);
		declare = (Declare)this.getApplication();
		studentId = findViewById(R.id.TV_studentId);
		userName = findViewById(R.id.tv_userName);
		TV_userName=findViewById(R.id.TV_userName);
		userPhoto=findViewById(R.id.userPhoto);
		//userType = findViewById(R.id.ET_userType);
		userPhone = findViewById(R.id.ET_userPhone);
		userGender = findViewById(R.id.ET_userGender);
		userEmail = findViewById(R.id.ET_userEmail);
		btnEdit = findViewById(R.id.btnEdit);
		search=findViewById(R.id.search);
		search.setVisibility(View.GONE);
		back=findViewById(R.id.back_btn);
		title=findViewById(R.id.title);
		title.setText("个人信息");
		nickName=findViewById(R.id.tv_nickName);
		tv_studentId=findViewById(R.id.tv_studentId);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		userPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xiangceClick();
			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte[] userPhoto_data = null;
				try {
					userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + declare.getUserPhoto());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Bundle bundle=new Bundle();
				bundle.putByteArray("userPhoto_data",userPhoto_data);
				Message msg=new Message();
				msg.setData(bundle);
				msg.what=0x120;
				handler.sendMessage(msg);
			}
		}).start();

         if(declare.getUserType().equals("快递员")) {    //若已认证，则显示出认证studentId
         	 tv_studentId.setVisibility(View.VISIBLE);
			 studentId.setVisibility(View.VISIBLE);
			 studentId.setText(String.valueOf(declare.getUserId()));
			 TV_userName.setVisibility(View.VISIBLE);
			 userName.setVisibility(View.VISIBLE);
			 userName.setText(declare.getUserName());
		 }
		nickName.setText(declare.getNickName());
		userName.setText(declare.getUserName());
		//userPassword = findViewById(R.id.ET_userPwd);
		userPhone.setText(declare.getUserPhone());
		userGender.setText(declare.getUserGender());
		userEmail.setText(declare.getUserEmail());
		btnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(userName.getText().toString().equals(""))
				{
					Toast.makeText(UserInfoEditActivity.this, "姓名输入不能为空!", Toast.LENGTH_LONG).show();
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
//				if(nickName.getText().toString().equals(""))
//				{
//					Toast.makeText(UserInfoEditActivity.this, "用户名输入不能为空!", Toast.LENGTH_LONG).show();
//					nickName.setFocusable(true);
//					nickName.requestFocus();
//					return;
//				}

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
				new Thread(new Runnable() {
					@Override
					public void run() {
						Message msg=new Message();
						if(imagePath!=null){  //选择了新图片
							upload();
							user.setUserId(declare.getUserId());
							user.setUserName(declare.getUserName());
							user.setUserAuthFile(declare.getUserAuthFile());
							Log.i("ooooo",""+photo);
							user.setUserPhoto(photo);
							user.setUserPassword(declare.getUserPassword());
							user.setUserMoney(declare.getUserMoney());
							user.setRegTime(declare.getRegTime());
							user.setUserEmail(declare.getUserEmail());
							user.setUserGender(declare.getUserGender());
							user.setUserPhone(declare.getUserPhone());
							user.setUserPassword(declare.getUserPassword());
							user.setUserReputation(declare.getUserReputation());
							user.setUserType(declare.getUserType());
							user.setNickName(declare.getNickName());
							user.setStudentId(declare.getStudentId());
							userService.UpdateUserInfo(user);
							user=userService.GetUserInfo(declare.getUserId());
							Bundle bundle=new Bundle();
							bundle.putSerializable("user", user);
							msg.setData(bundle);
							msg.what=0x123;
						}
						else{
							user.setUserId(declare.getUserId());
							user.setUserName(declare.getUserName());
							user.setUserAuthFile(declare.getUserAuthFile());
							Log.i("ooooo",""+declare.getUserPhoto());
							user.setUserPhoto(declare.getUserPhoto());
							user.setUserPassword(declare.getUserPassword());
							user.setUserMoney(declare.getUserMoney());
							user.setRegTime(declare.getRegTime());
							user.setUserEmail(declare.getUserEmail());
							user.setUserGender(declare.getUserGender());
							user.setUserPhone(declare.getUserPhone());
							user.setUserPassword(declare.getUserPassword());
							user.setUserReputation(declare.getUserReputation());
							user.setUserType(declare.getUserType());
							userService.UpdateUserInfo(user);
							user.setNickName(declare.getNickName());
							user=userService.GetUserInfo(declare.getUserId());
							Bundle bundle=new Bundle();
							bundle.putSerializable("user", user);
							msg.setData(bundle);
							msg.what=0x125;
						}
						handler.sendMessage(msg);
					}

				}).start();
			}
		});
}


	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what==0x123){
				userInfo= (User) msg.getData().getSerializable("user");
				declare.setUserId(userInfo.getUserId());
				declare.setUserName(userInfo.getUserName());
				declare.setUserType(userInfo.getUserType());
				declare.setUserPhoto(userInfo.getUserPhoto());
				declare.setUserMoney(userInfo.getUserMoney());
				declare.setUserGender(userInfo.getUserGender());
				declare.setRegTime(userInfo.getRegTime());
				declare.setUserEmail(userInfo.getUserEmail());
				declare.setUserReputation(userInfo.getUserReputation());
				declare.setUserAuthFile(userInfo.getUserAuthFile());
				declare.setUserPassword(userInfo.getUserPassword());
				declare.setNickName(userInfo.getNickName());
				declare.setStudentId(userInfo.getStudentId());
				Toast.makeText(UserInfoEditActivity.this, "更新成功!", Toast.LENGTH_LONG).show();
			}
			if(msg.what==0x124){
				userInfo= (User) msg.getData().getSerializable("user");
				declare.setUserId(userInfo.getUserId());
				declare.setUserName(userInfo.getUserName());
				declare.setUserType(userInfo.getUserType());
				declare.setUserPhoto(userInfo.getUserPhoto());
				declare.setUserMoney(userInfo.getUserMoney());
				declare.setUserGender(userInfo.getUserGender());
				declare.setRegTime(userInfo.getRegTime());
				declare.setUserEmail(userInfo.getUserEmail());
				declare.setUserReputation(userInfo.getUserReputation());
				declare.setUserAuthFile(userInfo.getUserAuthFile());
				declare.setUserPassword(userInfo.getUserPassword());
				declare.setNickName(userInfo.getNickName());
				declare.setStudentId(userInfo.getStudentId());
				Toast.makeText(UserInfoEditActivity.this, "更新成功!", Toast.LENGTH_LONG).show();
			}
            if(msg.what==0x120){
				byte []path=null;
				path=msg.getData().getByteArray("userPhoto_data");
				Bitmap Photo = BitmapFactory.decodeByteArray(path, 0, path.length);
				userPhoto.setImageBitmap(Photo);
			}

			//dialog.cancel();
		}
	};
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
								ActivityCompat.requestPermissions(UserInfoEditActivity.this,new String[]{Manifest.permission.CAMERA},1);
							}
						}).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//不请求权限的操作
					}
				}).create().show();
			}else {
				ActivityCompat.requestPermissions(UserInfoEditActivity.this,new String[]{Manifest.permission.CAMERA},1);
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
				userPhoto.setImageBitmap(orc_bitmap);
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
			photo=response.body().string();
			//	user.setUserPhoto(photo);
			Log.i("ppppppp","photo"+photo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
