package com.mobileclient.activity;

import com.mobileclient.domain.Notice;
import com.mobileclient.service.NoticeService;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class NoticeDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明公告id控件
	private TextView TV_noticeId;
	// 声明标题控件
	private TextView TV_title;
	// 声明公告内容控件
	private TextView TV_content;
	// 声明发布时间控件
	private TextView TV_publishDate;
	/* 要保存的新闻公告信息 */
	Notice notice = new Notice(); 
	/* 新闻公告管理业务逻辑层 */
	private NoticeService noticeService = new NoticeService();
	private int noticeId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.notice_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看新闻公告详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_noticeId = (TextView) findViewById(R.id.TV_noticeId);
		TV_title = (TextView) findViewById(R.id.TV_title);
		TV_content = (TextView) findViewById(R.id.TV_content);
		TV_publishDate = (TextView) findViewById(R.id.TV_publishDate);
		Bundle extras = this.getIntent().getExtras();
		noticeId = extras.getInt("noticeId");
		btnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NoticeDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what==0x123){
					TV_noticeId.setText(msg.getData().getString("noticeId"));
					TV_title.setText(msg.getData().getString("title"));
					TV_content.setText(msg.getData().getString("content"));
					TV_publishDate.setText(msg.getData().getString("publishDate"));
				}
			}
		};
		new Thread(){
			@Override
			public void run() {
				super.run();
				notice = noticeService.GetNotice(noticeId);
				Bundle bundle=new Bundle();
				Message msg=new Message();
				bundle.putString("noticeId",notice.getNoticeId() + "");
				bundle.putString("title",notice.getNoticeTitle());
				bundle.putString("content",notice.getNoticeContent());
				bundle.putString("publishDate",notice.getPublishDate());
				msg.setData(bundle);
				msg.what=0x123;
				handler.sendMessage(msg);
			}
		};
	   // notice = noticeService.GetNotice(noticeId);

	} 
}
