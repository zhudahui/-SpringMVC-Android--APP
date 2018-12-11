package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.AlarmClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import android.widget.TextView;
import android.widget.Toast;

import com.mobileclient.activity.myorder.ExpressTakeMyListActivity;
import com.mobileclient.app.Declare;



public class IndexActivity extends Activity {
	//TabHost tabhost;
	GridView gridview;
	//GridView gridview2;
	private boolean quit = false;
	Intent intent = new Intent();

	private ViewPager advPager1 = null;
    private ViewPager viewPager;
	private View view1, view2, view3;
	//private ViewPager viewPager;  //对应的viewPager

	private List<View> viewList;//view数组


	private ViewPager advPager2 = null;
	private AtomicInteger what2 = new AtomicInteger(0);
	private boolean isContinue2 = true;
	private AtomicInteger what1 = new AtomicInteger(0);
	private boolean isContinue1 = true;
	private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6,imageView7, imageView8, imageView9
			,iv1,iv2,iv3,iv4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.index1);
		final Declare declare = (Declare) IndexActivity.this.getApplication();
		imageView1 = findViewById(R.id.imageView1);
		imageView2 = findViewById(R.id.imageView2);
		imageView3 = findViewById(R.id.imageView3);
		imageView4 = findViewById(R.id.imageView4);
		imageView5 = findViewById(R.id.imageView5);
		imageView6 = findViewById(R.id.imageView6);
		imageView7 = findViewById(R.id.imageView7);
		imageView8 = findViewById(R.id.imageView8);
		imageView9 = findViewById(R.id.imageView9);
		iv1=findViewById(R.id.iv1);
		iv2=findViewById(R.id.iv2);
		iv3=findViewById(R.id.iv3);
		iv4=findViewById(R.id.iv4);
       // viewPager = findViewById(R.id.vp_advertise);
      //  initViewPager();
		imageView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this, TjpuActivity.class);
				startActivity(intent);
			}
		});
		imageView2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		imageView3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this, RouteActivity.class);
				startActivity(intent);
			}
		});
		imageView4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this, NoticeListActivity.class);
				startActivity(intent);
			}
		});
		imageView5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this, MainUserActivity.class);
				startActivity(intent);
			}
		});
//		imageView6.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(IndexActivity.this, TakeOrderUserListActivity.class);
//				startActivity(intent);
//			}
//		});
//		imageView7.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(IndexActivity.this, ExpressTakeMyListActivity.class);
//				startActivity(intent);
//			}
//		});
//		imageView8.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(IndexActivity.this,MoreActivity.class);
//				startActivity(intent);
//			}
//		});
		iv1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent alarms = new Intent(AlarmClock.ACTION_SET_ALARM);
				startActivity(alarms);
			}
		});

		iv3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("tel:"+8470388);
				Intent intent = new Intent(Intent.ACTION_CALL, uri);
				//startActivity(intent);
			}
		});
		iv2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.baidu.com");
				Intent it  = new Intent(Intent.ACTION_VIEW,uri);
				startActivity(it);
			}
		});
		iv4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					//利用Intent打开微信
					Uri uri = Uri.parse("weixin://");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				} catch (Exception e) {
					//若无法正常跳转，在此进行错误处理
					//Toast.makeText(DinpayWeChatActivity.this, "无法跳转到微信，请检查您是否安装了微信！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		imageView9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this, AboutActivity.class);
				startActivity(intent);
			}
		});
//===================================================
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		LayoutInflater inflater=getLayoutInflater();
		view1 = inflater.inflate(R.layout.layout1, null);
		view2 = inflater.inflate(R.layout.layout2,null);
		view3 = inflater.inflate(R.layout.layout3, null);

		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);


		PagerAdapter pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return viewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
									Object object) {
				// TODO Auto-generated method stub
				container.removeView(viewList.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				container.addView(viewList.get(position));


				return viewList.get(position);
			}
		};


		viewPager.setAdapter(pagerAdapter);

        //===============================================
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}



	private void whatOption() {
		what1.incrementAndGet();
		//	if (what1.get() > imageViews1.length - 1) {
		//	what1.getAndAdd(-4);
		//	}
		//try {
		//	Thread.sleep(2000);
		//} catch (InterruptedException e) {

		//	}
	}

}