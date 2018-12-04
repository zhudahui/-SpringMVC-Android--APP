package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.adapter.UserInfoSimpleAdapter;
import com.mobileclient.app.Declare;
import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;
import com.mobileclient.util.ActivityUtils;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class UserInfoListActivity extends Activity {
	UserInfoSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	String user_name;
	/* 用户操作业务逻辑层对象 */
	UserService userService = new UserService();
	/*保存查询参数条件的用户对象*/
	private User queryConditionUser;

	private MyProgressDialog dialog; //进度条	@Override
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.userinfo_list);
		dialog = MyProgressDialog.getInstance(this);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		//标题栏控件
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				//intent.setClass(UserListActivity.this, UserQueryActivity.class);
				//startActivityForResult(intent,ActivityUtils.QUERY_CODE);//此处的requestCode应与下面结果处理函中调用的requestCode一致
			}
		});
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("用户列表");
		ImageView add_btn = (ImageView) this.findViewById(R.id.add_btn);
		add_btn.setOnClickListener(new android.view.View.OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				//intent.setClass(UserListActivity.this, UserAddActivity.class);
				startActivityForResult(intent,ActivityUtils.ADD_CODE);
			}
		});
		setViews();
	}

	//结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
        	Bundle extras = data.getExtras();
        	if(extras != null)
        		queryConditionUser = (User)extras.getSerializable("queryConditionUser");
        	setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
        	setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
        	queryConditionUser = null;
        	setViews();
        }
    }

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		dialog.show();
		final Handler handler = new Handler();
		new Thread(){
			@Override
			public void run() {
				//在子线程中进行下载数据操作
				list = getDatas();
				//发送消失到handler，通知主线程下载完成
				handler.post(new Runnable() {
					@Override
					public void run() {
						dialog.cancel();
						adapter = new UserInfoSimpleAdapter(UserInfoListActivity.this, list,
	        					R.layout.userinfo_list_item,
	        					new String[] { "userPhoto","userType","nickName" },
	        					new int[] {R.id.iv_userPhoto,R.id.userType,R.id.nickName},lv);
	        			lv.setAdapter(adapter);
					}
				});
			}
		}.start(); 

		// 添加点击
		lv.setOnCreateContextMenuListener(UserListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            @Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	Intent intent = new Intent();
            	intent.setClass(UserInfoListActivity.this, UserInfoDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString("nickName", list.get(arg2).get("nickName").toString());
				bundle.putString("userName", list.get(arg2).get("userName").toString());
				bundle.putInt("userId", Integer.parseInt(list.get(arg2).get("userId").toString()));
				bundle.putInt("studentId", Integer.parseInt(list.get(arg2).get("studentId").toString()));
				bundle.putString("userPassword", list.get(arg2).get("userPassword").toString());
				bundle.putString("userType", list.get(arg2).get("userType").toString());
				bundle.putString("userPhone", list.get(arg2).get("userPhone").toString());
				bundle.putString("userGender", list.get(arg2).get("userGender").toString());
				bundle.putString("userEmail", list.get(arg2).get("userEmail").toString());
				bundle.putString("userMoney", list.get(arg2).get("userMoney").toString());
				bundle.putString("userReputation", list.get(arg2).get("userReputation").toString());
				bundle.putString("regTime", list.get(arg2).get("regTime").toString());
				bundle.putString("userAuthFile", list.get(arg2).get("userAuthFile").toString());
				bundle.putByteArray("photo", (byte[]) list.get(arg2).get("photo"));
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener UserListItemListener = new OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "编辑用户信息"); 
			menu.add(0, 1, 0, "删除用户信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑用户信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取用户名
			user_name = list.get(position).get("user_name").toString();
			Intent intent = new Intent();
			intent.setClass(UserInfoListActivity.this, UserInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("user_name", user_name);
			intent.putExtras(bundle);
			startActivityForResult(intent,ActivityUtils.EDIT_CODE);
		} else if (item.getItemId() == 1) {// 删除用户信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取用户名
			user_name = list.get(position).get("user_name").toString();
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(UserInfoListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//String result = UserService.DeleteUser(user_name);
				//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* 查询用户信息 */
			List<User> userList = userService.QueryUser(queryConditionUser);
			for (int i = 0; i < userList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", userList.get(i).getUserId());
				map.put("userName",userList.get(i).getUserName());
				map.put("userPassword",userList.get(i).getUserPassword());
                byte[] userPhoto_data = null;
                // 获取图片数据
                userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + userList.get(i).getUserPhoto());
                map.put("photo",userPhoto_data);
                Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
				map.put("userPhoto",userPhoto);
				map.put("userType",userList.get(i).getUserType());
				map.put("userPhone",userList.get(i).getUserPhone());
				map.put("userGender",userList.get(i).getUserGender());
				map.put("userEmail",userList.get(i).getUserEmail());
				map.put("userReputation",userList.get(i).getUserReputation());
				map.put("userMoney",userList.get(i).getUserMoney());
				map.put("userAuthFile",userList.get(i).getUserAuthFile());
				map.put("regTime",userList.get(i).getRegTime());
				map.put("studentId",userList.get(i).getStudentId());
				map.put("nickName",userList.get(i).getNickName());
				/*byte[] userPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ UserList.get(i).getUserPhoto());// 获取图片数据
				BitmapFactory.Options userPhoto_opts = new BitmapFactory.Options();  
				userPhoto_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length, userPhoto_opts); 
				userPhoto_opts.inSampleSize = photoListActivity.computeSampleSize(userPhoto_opts, -1, 100*100); 
				userPhoto_opts.inJustDecodeBounds = false; 
				try {
					Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length, userPhoto_opts);
					map.put("userPhoto", userPhoto);
				} catch (OutOfMemoryError err) { }*/
//				map.put("userPhoto", HttpUtil.BASE_URL+ UserList.get(i).getUserPhoto());
//				map.put("telephone", UserList.get(i).getTelephone());
//				map.put("shenHeState", UserList.get(i).getShenHeState());
				list.add(map);
			}
		} catch (Exception e) { 
		//	Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

}
