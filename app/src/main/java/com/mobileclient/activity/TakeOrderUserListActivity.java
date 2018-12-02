package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.adapter.ExpressOrderAdapter;
import com.mobileclient.app.Declare;
import com.mobileclient.domain.Order;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.domain.TakeOrder;
import com.mobileclient.domain.User;
import com.mobileclient.service.ExpressTakeService;
import com.mobileclient.service.OrderService;
import com.mobileclient.service.ReceiveAddressService;
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
import android.util.Log;
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

public class TakeOrderUserListActivity extends Activity {
	ExpressOrderAdapter adapter;
	ListView lv;
	List<Map<String, Object>> list;
	int orderId;
	ExpressTakeService expressTakeService = new ExpressTakeService();
	/*保存查询参数条件*/
	private Order queryConditionExpressOrder;
	private MyProgressDialog dialog; //进度条	@Override
	OrderService orderService=new OrderService();
	/* 快递代拿操作业务逻辑层对象 */
	Order  order=new Order();
	User user=new User();
	UserService userService=new UserService();
	ReceiveAddress receiveAddress=new ReceiveAddress();
    Declare declare;
    private int userId;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.expresstake_list);
		dialog = MyProgressDialog.getInstance(this);
		declare = (Declare) getApplicationContext();
		userId=declare.getUserId();
		//标题栏控件
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setImageResource(R.drawable.btn_add);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(TakeOrderUserListActivity.this,ExpressOrderAddActivity.class);
				startActivity(intent);
			}
		});
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("我发布的快递代拿");

		if(declare.getUserType().equals("普通用户"))
		{
			Toast.makeText(TakeOrderUserListActivity.this,"请先进行认证！",Toast.LENGTH_SHORT).show();
			return;
		}
		//queryConditionExpressOrder = new Order();
		//queryConditionExpressOrder.setTakeUserId(declare.getUserId());
		setViews();
	}

	//结果处理函数，当从secondActivity中返回时调用此函数
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==ActivityUtils.UPDATE_CODE&&resultCode==RESULT_OK) {    //从Detail页面回来
			Bundle extras = data.getExtras();
			if (extras != null) {
				int p = extras.getInt("p");
				if (p == 1)
					setViews();
			}
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
						adapter = new ExpressOrderAdapter(TakeOrderUserListActivity.this, list,
								R.layout.order_list_item,
								new String[] { "userPhoto","userName","orderName","expressCompanyName","expressCompanyAddress","receiveAddressName","addTime","orderState" },
								new int[] { R.id.userPhoto,R.id.userName,R.id.orderName,R.id.expressCompanyName,R.id.expressCompanyAdress,
										R.id.addTime,R.id.orderState},lv);
						lv.setAdapter(adapter);
					}
				});
			}
		}.start();

		// 添加长按点击
		lv.setOnCreateContextMenuListener(expressTakeListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				int orderId = Integer.parseInt(list.get(arg2).get("orderId").toString());
				Intent intent = new Intent();
				intent.setClass(TakeOrderUserListActivity.this, ExpressTakeDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("orderId", orderId);
				bundle.putString("orderName",list.get(arg2).get("orderName").toString());
				bundle.putString("userName",list.get(arg2).get("userName").toString());
				bundle.putByteArray("photo", (byte[]) list.get(arg2).get("photo"));
				bundle.putString("expressCompanyName",list.get(arg2).get("expressCompanyName").toString());
				bundle.putString("expressCompanyAddress",list.get(arg2).get("expressCompanyAddress").toString());
				bundle.putString("receiveAddressName",list.get(arg2).get("receiveAddressName").toString());
				bundle.putString("receiveName",list.get(arg2).get("receiveName").toString());
				bundle.putString("receivePhone",list.get(arg2).get("receivePhone").toString());
				bundle.putString("remark",list.get(arg2).get("remark").toString());
				bundle.putString("receiveCode",list.get(arg2).get("receiveCode").toString());
				bundle.putString("receiveName",list.get(arg2).get("receiveName").toString());
				bundle.putString("orderPay",list.get(arg2).get("orderPay").toString());
				bundle.putString("orderState",list.get(arg2).get("orderState").toString());
				bundle.putString("addTime",list.get(arg2).get("addTime").toString());
				//if(list.get(arg2).get("evaluate").toString()!=null)
				bundle.putString("evaluate",list.get(arg2).get("evaluate").toString());
				bundle.putInt("takeUserId", Integer.parseInt(list.get(arg2).get("takeUserId").toString()));
				//bundle.putString("userType",declare.getUserType());
				if (list.get(arg2).get("evaluate").toString().equals("--")||list.get(arg2).get("evaluate").toString().equals("请评价")) {//若评价为空
					intent.putExtras(bundle);
					intent.setClass(TakeOrderUserListActivity.this, ExpressOrderDetailActivity.class);   //已评价
					startActivityForResult(intent, ActivityUtils.UPDATE_CODE);
				}
				else {
					Log.i("zhu111", "已评价");
					intent.setClass(TakeOrderUserListActivity.this, SecondOrderDetailActivity.class);   //已评价
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
	}
	private OnCreateContextMenuListener expressTakeListItemListener = new OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			//menu.add(0, 0, 0, "编辑快递代拿信息");
			//menu.add(0, 1, 0, "删除快递代拿信息");

			menu.add(0, 0, 0, "取消快递代拿订单");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {// 删除快递代拿信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取订单id
			orderId = Integer.parseInt(list.get(position).get("orderId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(TakeOrderUserListActivity.this);
		builder.setMessage("确认取消吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String result = expressTakeService.DeleteExpressTake(orderId);
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
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
			/* 查询快递代拿信息 */
			ReceiveAddressService receiveAdressService=new ReceiveAddressService();
			/* 查询快递代拿信息 */
			List<Order> expressOrderList = orderService.takeUserQuery(userId);
			for (int i = 0; i < expressOrderList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("orderId",expressOrderList.get(i).getOrderId());
				map.put("orderName", expressOrderList.get(i).getOrderName());
				map.put("userId", expressOrderList.get(i).getUserId());
				user=userService.GetUserInfo(expressOrderList.get(i).getUserId());
				map.put("userName", user.getUserName());
				byte[] userPhoto_data = null;
				// 获取图片数据
				userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + user.getUserPhoto());
				map.put("photo",userPhoto_data);
				Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
				map.put("userPhoto",userPhoto);
				map.put("expressCompanyName", expressOrderList.get(i).getExpressCompanyName());
				map.put("expressCompanyAddress", expressOrderList.get(i).getExpressCompanyAddress());
				// 根据获取到的地址Id，查询地址名以及收获人姓名
				receiveAddress=receiveAdressService.QueryReceiveAdress(expressOrderList.get(i).getReceiveAddressId());
				Log.i("zhu1111","查询"+receiveAddress.getReceiveAddressName());
				map.put("receiveAddressName", receiveAddress.getReceiveAddressName());
				map.put("receiveName",receiveAddress.getReceiveName());
				map.put("receivePhone",receiveAddress.getReceivePhone());
				map.put("addTime", expressOrderList.get(i).getAddTime());
				map.put("orderState", expressOrderList.get(i).getOrderState());
				map.put("orderPay", expressOrderList.get(i).getOrderPay());
				map.put("remark", expressOrderList.get(i).getRemark());
				map.put("receiveCode", expressOrderList.get(i).getReceiveCode());
				map.put("evaluate", expressOrderList.get(i).getOrderEvaluate());
				map.put("takeUserId",expressOrderList.get(i).getTakeUserId());
				//map.put("userPhone", expressOrderList.get(i).getAddTime());
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
