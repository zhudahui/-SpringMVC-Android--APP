package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.ExpressTake;
import com.mobileclient.service.ExpressTakeService;
import com.mobileclient.util.ActivityUtils;import com.mobileclient.util.ExpressTakeSimpleAdapter;
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

public class ExpressTakeMyListActivity extends Activity {
	ExpressTakeSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int orderId;
	/* 快递代拿操作业务逻辑层对象 */
	ExpressTakeService expressTakeService = new ExpressTakeService();
	/*保存查询参数条件的快递代拿对象*/
	private ExpressTake queryConditionExpressTake;

	private MyProgressDialog dialog; //进度条	@Override
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.expresstake_list);
		dialog = MyProgressDialog.getInstance(this);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		//标题栏控件
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ExpressTakeMyListActivity.this, ExpressTakeQueryActivity.class);
				startActivityForResult(intent,ActivityUtils.QUERY_CODE);//此处的requestCode应与下面结果处理函中调用的requestCode一致
			}
		});
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("我发布的快递代拿");
		Log.i("zhu7777","我发布的快递代拿");
		ImageView add_btn = (ImageView) this.findViewById(R.id.add_btn);

		add_btn.setImageResource(R.drawable.btn_left_normal);
		add_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		queryConditionExpressTake = new ExpressTake();
		queryConditionExpressTake.setTaskTitle("");
		queryConditionExpressTake.setCompanyObj(0);
		queryConditionExpressTake.setWaybill("");
		queryConditionExpressTake.setReceiverName("");
		queryConditionExpressTake.setTelephone("");
		queryConditionExpressTake.setTakePlace("");
		queryConditionExpressTake.setTakeStateObj("");
		queryConditionExpressTake.setUserObj(username);
		queryConditionExpressTake.setAddTime("");
		 
		setViews();
	}

	//结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
        	Bundle extras = data.getExtras();
        	if(extras != null)
        		queryConditionExpressTake = (ExpressTake)extras.getSerializable("queryConditionExpressTake");
        	setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
        	setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
        	queryConditionExpressTake = null;
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
						adapter = new ExpressTakeSimpleAdapter(ExpressTakeMyListActivity.this, list,
	        					R.layout.expresstake_list_item,
	        					new String[] { "taskTitle","companyObj","waybill","receiverName","telephone","receiveMemo","takePlace","giveMoney","takeStateObj","userObj","addTime" },
	        					new int[] { R.id.tv_taskTitle,R.id.tv_companyObj,R.id.tv_waybill,R.id.tv_receiverName,R.id.tv_telephone,R.id.tv_receiveMemo,R.id.tv_takePlace,R.id.tv_giveMoney,R.id.tv_takeStateObj,R.id.tv_userObj,R.id.tv_addTime,},lv);
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
            	intent.setClass(ExpressTakeMyListActivity.this, ExpressTakeDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("orderId", orderId);
            	intent.putExtras(bundle);
            	startActivity(intent);
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
		Builder builder = new Builder(ExpressTakeMyListActivity.this);
		builder.setMessage("确认取消吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String result = expressTakeService.DeleteExpressTake(orderId);
				Toast.makeText(getApplicationContext(), result, 1).show();
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
			List<ExpressTake> expressTakeList = expressTakeService.QueryExpressTake(queryConditionExpressTake);
			for (int i = 0; i < expressTakeList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("orderId",expressTakeList.get(i).getOrderId());
				map.put("taskTitle", expressTakeList.get(i).getTaskTitle());
				map.put("companyObj", expressTakeList.get(i).getCompanyObj());
				map.put("waybill", expressTakeList.get(i).getWaybill());
				map.put("receiverName", expressTakeList.get(i).getReceiverName());
				map.put("telephone", expressTakeList.get(i).getTelephone());
				map.put("receiveMemo", expressTakeList.get(i).getReceiveMemo());
				map.put("takePlace", expressTakeList.get(i).getTakePlace());
				map.put("giveMoney", expressTakeList.get(i).getGiveMoney());
				map.put("takeStateObj", expressTakeList.get(i).getTakeStateObj());
				map.put("userObj", expressTakeList.get(i).getUserObj());
				map.put("addTime", expressTakeList.get(i).getAddTime());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

}
