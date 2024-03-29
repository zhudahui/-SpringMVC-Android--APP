package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.app.RefreshListView;
import com.mobileclient.domain.Notice;
import com.mobileclient.service.NoticeService;
import com.mobileclient.util.ActivityUtils;import com.mobileclient.adapter.NoticeSimpleAdapter;
import com.mobileclient.util.Utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class NoticeListActivity extends Activity {
	NoticeSimpleAdapter adapter;
	RefreshListView lv;
	List<Map<String, Object>> list;
	int noticeId;
	/* 新闻公告操作业务逻辑层对象 */
	NoticeService noticeService = new NoticeService();
	/*保存查询参数条件的新闻公告对象*/
	private Notice queryConditionNotice;

	private MyProgressDialog dialog; //进度条	@Override
	Declare declare;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.notice_list);
		Utils.setStatusBar(this, false, false);
		Utils.setStatusTextColor(false, NoticeListActivity.this);
		dialog = MyProgressDialog.getInstance(this);
		declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		//标题栏控件
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setImageResource(R.drawable.btn_add);
		if(declare.getIdentify().equals("user"))
		    search.setVisibility(View.GONE);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(NoticeListActivity.this, NoticeAddActivity.class);
				startActivityForResult(intent, ActivityUtils.QUERY_CODE);//此处的requestCode应与下面结果处理函中调用的requestCode一致
			}
		});
		ImageView back=findViewById(R.id.back_btn);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("通知公告");
		setViews();
	}

	//结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
        	Bundle extras = data.getExtras();
        	if(extras != null)
        		queryConditionNotice = (Notice)extras.getSerializable("queryConditionNotice");
        	setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
        	setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
        	queryConditionNotice = null;
        	setViews();
        }
    }

	private void setViews() {
		lv =  findViewById(R.id.h_list_view);
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
						adapter = new NoticeSimpleAdapter(NoticeListActivity.this, list,
	        					R.layout.notice_list_item,
	        					new String[] { "noticeTitle","publishDate", },
	        					new int[] { R.id.tv_noticeTitle,R.id.tv_publishDate},lv);
	        			lv.setAdapter(adapter);
					}
				});
			}
		}.start();
		lv.setonRefreshListener(new RefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				setViews();
				adapter.notifyDataSetChanged();
				lv.onRefreshComplete();

			}
		});
		// 添加长按点击
		lv.setOnCreateContextMenuListener(noticeListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            @Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	arg2=arg2-1;
            	int noticeId = Integer.parseInt(list.get(arg2).get("noticeId").toString());
            	Intent intent = new Intent();
            	intent.setClass(NoticeListActivity.this, NoticeDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("noticeId", noticeId);
            	bundle.putString("noticeTitle",list.get(arg2).get("noticeTitle").toString());
				bundle.putString("noticeContent",list.get(arg2).get("noticeContent").toString());
				bundle.putString("noticeTitle",list.get(arg2).get("noticeTitle").toString());
				bundle.putString("publishDate",list.get(arg2).get("publishDate").toString());
				bundle.putString("noticeFile",list.get(arg2).get("noticeFile").toString());
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener noticeListItemListener = new OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			Declare declare = (Declare) NoticeListActivity.this.getApplication();
			if(declare.getIdentify().equals("admin")) {
				menu.add(0, 0, 0, "编辑新闻公告信息"); 
				menu.add(0, 1, 0, "删除新闻公告信息");
			}
			
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑新闻公告信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取公告id
			position=position-1;
			noticeId = Integer.parseInt(list.get(position).get("noticeId").toString());
			Intent intent = new Intent();
			intent.setClass(NoticeListActivity.this, NoticeEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("noticeId", noticeId);
			bundle.putString("noticeTitle",list.get(position).get("noticeTitle").toString());
			bundle.putString("noticeContent",list.get(position).get("noticeContent").toString());
			bundle.putString("noticeTitle",list.get(position).get("noticeTitle").toString());
			bundle.putString("publishDate",list.get(position).get("publishDate").toString());
			//bundle.putString("noticeFile",list.get(position).get("noticeFile").toString());
			intent.putExtras(bundle);
			startActivityForResult(intent,ActivityUtils.EDIT_CODE);
		} else if (item.getItemId() == 1) {// 删除新闻公告信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取公告id
			noticeId = Integer.parseInt(list.get(position).get("noticeId").toString());

			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(NoticeListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, int which) {
				final Handler handler=new Handler()
				{
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
						setViews();
						dialog.dismiss();
					}
				};
				new Thread(new Runnable() {
					@Override
					public void run() {
						noticeService.DeleteNotice(noticeId);
					}
				}).start();


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
			/* 查询新闻公告信息 */
			List<Notice> noticeList = noticeService.QueryNotice(queryConditionNotice);
			for (int i = 0; i < noticeList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("noticeId",noticeList.get(i).getNoticeId());
				map.put("noticeTitle", noticeList.get(i).getNoticeTitle());
				map.put("noticeContent", noticeList.get(i).getNoticeContent());
				map.put("publishDate", noticeList.get(i).getPublishDate());
				map.put("noticeFile",noticeList.get(i).getNoticeFile());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
		}
		return list;
	}

}
