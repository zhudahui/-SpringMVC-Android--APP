package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.TakeOrder;
import com.mobileclient.service.TakeOrderService;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.util.ActivityUtils;import com.mobileclient.util.TakeOrderSimpleAdapter;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
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
	TakeOrderSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int orderId;
	/* 代拿订单操作业务逻辑层对象 */
	TakeOrderService takeOrderService = new TakeOrderService();
	UserInfoService userInfoService = new UserInfoService();
	/*保存查询参数条件的代拿订单对象*/
	private TakeOrder queryConditionTakeOrder;
	private String username;
    private  String userType;
	private MyProgressDialog dialog; //进度条	@Override
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.takeorder_list);
		dialog = MyProgressDialog.getInstance(this);
		Declare declare = (Declare) getApplicationContext();
		username = declare.getUserName();
		sendRequest();			//**判断用户权限

		//标题栏控件
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(TakeOrderUserListActivity.this, TakeOrderQueryActivity.class);
				startActivityForResult(intent,ActivityUtils.QUERY_CODE);//此处的requestCode应与下面结果处理函中调用的requestCode一致
			}
		});
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("我抢的代拿订单");
		ImageView add_btn = (ImageView) this.findViewById(R.id.add_btn);

		add_btn.setImageResource(R.drawable.btn_left_normal);
		add_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		queryConditionTakeOrder = new TakeOrder();
		queryConditionTakeOrder.setExpressTakeObj(0);
		queryConditionTakeOrder.setUserObj(username);
		queryConditionTakeOrder.setTakeTime("");
		queryConditionTakeOrder.setOrderStateObj(0);
		setViews();
	}

	//结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
        	Bundle extras = data.getExtras();
        	if(extras != null)
        		queryConditionTakeOrder = (TakeOrder)extras.getSerializable("queryConditionTakeOrder");
        	setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
        	setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
        	queryConditionTakeOrder = null;
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
						adapter = new TakeOrderSimpleAdapter(TakeOrderUserListActivity.this, list,
	        					R.layout.takeorder_list_item,
	        					new String[] { "expressTakeObj","userObj","takeTime","orderStateObj","ssdt","evaluate" },
	        					new int[] { R.id.tv_expressTakeObj,R.id.tv_userObj,R.id.tv_takeTime,R.id.tv_orderStateObj,R.id.tv_ssdt,R.id.tv_evaluate,},lv);
	        			lv.setAdapter(adapter);
					}
				});
			}
		}.start(); 

		// 添加长按点击
		lv.setOnCreateContextMenuListener(takeOrderListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            @Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int orderId = Integer.parseInt(list.get(arg2).get("orderId").toString());
            	Intent intent = new Intent();
            	intent.setClass(TakeOrderUserListActivity.this, TakeOrderDetail2Activity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("orderId", orderId);
            	//bundle.putString("tel",);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener takeOrderListItemListener = new OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "更新订单物流信息"); 
			//menu.add(0, 1, 0, "删除代拿订单信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑代拿订单信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取订单id
			orderId = Integer.parseInt(list.get(position).get("orderId").toString());
			Intent intent = new Intent();
			intent.setClass(TakeOrderUserListActivity.this, TakeOrderUserEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("orderId", orderId);
			intent.putExtras(bundle);
			startActivityForResult(intent,ActivityUtils.EDIT_CODE);
		} else if (item.getItemId() == 1) {// 删除代拿订单信息
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
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String result = takeOrderService.DeleteTakeOrder(orderId);
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
			/* 查询代拿订单信息 */
			List<TakeOrder> takeOrderList = takeOrderService.QueryTakeOrder(queryConditionTakeOrder);
			for (int i = 0; i < takeOrderList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("orderId",takeOrderList.get(i).getOrderId());
				map.put("expressTakeObj", takeOrderList.get(i).getExpressTakeObj());
				map.put("userObj", takeOrderList.get(i).getUserObj());
				map.put("takeTime", takeOrderList.get(i).getTakeTime());
				map.put("orderStateObj", takeOrderList.get(i).getOrderStateObj());
				map.put("ssdt", takeOrderList.get(i).getSsdt());
				map.put("evaluate", takeOrderList.get(i).getEvaluate());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
		}
		return list;
	}

	private  void sendRequest(){
		new Thread(new Runnable() {
			@Override
			public void run() {

                init();
                Log.i("gggg",""+userType);
			}
		}).start();
	}
	public void init(){
		userType = userInfoService.GetUserInfo(username).getUserType();
		Log.i("rrrrr","ww"+userType);
		if(userType.equals("普通用户")) {
			Toast.makeText(getApplicationContext(), "普通用户无法使用此功能！", Toast.LENGTH_SHORT).show();
			Log.i("ooooooooo",""+userType);
			return;
		}
	}
}
