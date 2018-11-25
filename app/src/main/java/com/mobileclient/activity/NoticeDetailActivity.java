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
	private ImageView btnReturn;
	// 声明公告标题控件
	private TextView TV_noticeTitle;
	// 声明附件控件
	private TextView TV_noticeFile;
	// 声明公告内容控件
	private TextView TV_noticeContent;
	// 声明发布时间控件
	private TextView TV_publishDate;
	/* 要保存的新闻公告信息 */
	Notice notice = new Notice(); 
	/* 新闻公告管理业务逻辑层 */
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
		title.setText("公告详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件

		Bundle extras = this.getIntent().getExtras();
		noticeId = extras.getInt("noticeId");
        TV_noticeTitle=findViewById(R.id.TV_noticeTitle);
        TV_noticeContent=findViewById(R.id.TV_noticeContent);
        TV_publishDate=findViewById(R.id.TV_publishDate);
        TV_noticeFile=findViewById(R.id.TV_noticeFile);
        TV_noticeTitle.setText(extras.getString("noticeTitle"));
        TV_noticeContent.setText("noticeContent");
        TV_publishDate.setText("publishDate");
        TV_noticeFile.setText("noticeFile");
        btnReturn=findViewById(R.id.back_btn);
		btnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NoticeDetailActivity.this.finish();
			}
		});
	}
	/* 初始化显示详情界面的数据 */

}
