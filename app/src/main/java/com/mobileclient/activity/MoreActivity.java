package com.mobileclient.activity;
 
 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileclient.app.Declare;

import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.util.ActivityUtils;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.apache.cordova.Config.init;

public class MoreActivity extends Activity {
	private String[] strs = new String[]{"订单状态","代拿订单","新闻公告","修改个人信息","关于"};
	private ListView list = null;
	private TextView mName;
	private CircleImageView photo;
	private Button unlogin;
	/*用户管理业务逻辑层*/
	/*要保存的用户信息*/
	UserInfo userInfo = new UserInfo();
	/*用户管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	Declare declare;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置当前Activity界面布局
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.more);
		// 通过findViewById方法实例化组件
		mName = (TextView) this.findViewById(R.id.username);
		photo =  this.findViewById(R.id.userPhoto);
		list = (ListView) this.findViewById(R.id.list);
		photo.setImageResource(R.drawable.xiaohui);
		photo.setEnabled(true);
		unlogin = (Button) this.findViewById(R.id.unlogin);
		unlogin.setVisibility(View.GONE);
		declare = (Declare) MoreActivity.this.getApplication();
		mName.setText(declare.getUserName());
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("个人");
		init();
        ImageView back = (ImageView) this.findViewById(R.id.back_btn);
        back.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
				finish();

            }
        });
		if(declare.getIdentify().equals("user"))
			strs = new String[]{"通知公告","个人信息","收货地址","我的快递","关于"};
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(declare.getIdentify().equals("admin")) {
					if(arg2 == 0){
						Intent intent  = new Intent(MoreActivity.this, OrderStateListActivity.class);
						startActivity(intent);
					}
					if(arg2 == 1){
						Intent intent  = new Intent(MoreActivity.this, TakeOrderListActivity.class);
						startActivity(intent);
					}
					if(arg2 == 2){
						Intent intent  = new Intent(MoreActivity.this, NoticeListActivity.class);
						startActivity(intent);
					}
					if(arg2 == 3){
						 
					}
					if(arg2 == 4){
						Intent intent  = new Intent(MoreActivity.this, AboutActivity.class);
						startActivity(intent);
					}
				} else {
					 
					if(arg2 == 0){
						Intent intent  = new Intent(MoreActivity.this, NoticeListActivity.class);
						startActivity(intent);
					}
					if(arg2 == 1){
						// 获取用户名
						String user_name = declare.getUserName();
						Intent intent = new Intent();
						intent.setClass(MoreActivity.this, UserInfoEditActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("user_name", user_name);
						intent.putExtras(bundle);
						startActivityForResult(intent,ActivityUtils.EDIT_CODE);
					}
					if(arg2 == 2){
						Intent intent  = new Intent(MoreActivity.this, ReceiveAddressListActivity.class);
						startActivity(intent);
					}
					if(arg2 == 3){
						Intent intent  = new Intent(MoreActivity.this, ExpressTakeMyListActivity.class);
						startActivity(intent);
					}
					if(arg2 == 4){
						Intent intent  = new Intent(MoreActivity.this, AboutActivity.class);
						startActivity(intent);
					}
				}
				


			}
		});
		list.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				ListViewItem item = new ListViewItem(MoreActivity.this, strs[arg0]);
				return item;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return strs.length;
			}
		});
		
		
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MoreActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		}); 
	}
	
	
	public class ListViewItem extends RelativeLayout{
		private TextView mTvColumnName;//栏目名称
		private ImageView mIvColumnImg;//栏目logo
		public ListViewItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		public ListViewItem(Context context,String columnInfo) {
			super(context,null);
			View view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			mTvColumnName = (TextView) view.findViewById(R.id.column_name);
			mIvColumnImg = (ImageView) view.findViewById(R.id.column_img);
			mTvColumnName.setText(columnInfo);
			addView(view);
		}
	}
	public synchronized static Drawable StringToDrawable(String icon) {
		if (icon == null || icon.length() < 10)
			return null;
		byte[] img = Base64.decode(icon.getBytes(), Base64.DEFAULT);
		Bitmap bitmap;
		if (img != null) {
			bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(bitmap);

			return drawable;
		}
		return null;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{// 捕捉返回键
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			setResult(RESULT_CANCELED);
			finish();
		}
		return true;
	}
	/*
	 *
	 *
	 *
	 */
	private void init(){
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what==0x123) {
					byte[] Photo = null;
					Photo=msg.getData().getByteArray("userPhoto_data");
					Log.i("gggggggg","1111"+Photo);
					Bitmap userPhoto = BitmapFactory.decodeByteArray(Photo, 0, Photo.length);
					photo.setImageBitmap(userPhoto);
				}
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte[] userPhoto_data = null;
				try {
					// 获取图片数据
					userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + declare.getUserPhoto());
					Log.i("gggggggg","2222"+userPhoto_data);
					Bundle bundle=new Bundle();
					Message msg=new Message();
					bundle.putByteArray("userPhoto_data",userPhoto_data);
					msg.what=0x123;
					msg.setData(bundle);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


}
