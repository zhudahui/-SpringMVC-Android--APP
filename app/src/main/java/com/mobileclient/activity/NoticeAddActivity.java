package com.mobileclient.activity;

import com.mobileclient.domain.Notice;
import com.mobileclient.service.NoticeService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoticeAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明标题输入框
	private EditText ET_title;
	// 声明公告内容输入框
	private EditText ET_content;
	// 声明发布时间输入框
	private EditText ET_publishDate;
	protected String carmera_path;
	/*要保存的新闻公告信息*/
	Notice notice = new Notice();
	/*新闻公告管理业务逻辑层*/
	private NoticeService noticeService = new NoticeService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.notice_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加新闻公告");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_title = (EditText) findViewById(R.id.ET_title);
		ET_content = (EditText) findViewById(R.id.ET_content);
		//ET_publishDate = (EditText) findViewById(R.id.ET_publishDate);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加新闻公告按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					/*验证获取标题*/ 
					if(ET_title.getText().toString().equals("")) {
						Toast.makeText(NoticeAddActivity.this, "标题输入不能为空!", Toast.LENGTH_LONG).show();
						ET_title.setFocusable(true);
						ET_title.requestFocus();
						return;	
					}
					notice.setNoticeTitle(ET_title.getText().toString());
					/*验证获取公告内容*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(NoticeAddActivity.this, "公告内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					notice.setNoticeContent(ET_content.getText().toString());
//					/*验证获取发布时间*/
//					if(ET_publishDate.getText().toString().equals("")) {
//						Toast.makeText(NoticeAddActivity.this, "发布时间输入不能为空!", Toast.LENGTH_LONG).show();
//						ET_publishDate.setFocusable(true);
//						ET_publishDate.requestFocus();
//						return;
//					}
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					//添加发布时间

					notice.setPublishDate(df.format(new Date()));
					notice.setNoticeFile("--");
					/*调用业务逻辑层上传新闻公告信息*/
					NoticeAddActivity.this.setTitle("正在上传新闻公告信息，稍等...");
					final Handler handler=new Handler()
					{
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
							Intent intent = getIntent();
							setResult(RESULT_OK,intent);
							finish();
						}
					};
					new Thread(new Runnable() {
						@Override
						public void run() {
							noticeService.AddNotice(notice);
							Message msg=new Message();
							handler.sendMessage(msg);
						}
					}).start();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
