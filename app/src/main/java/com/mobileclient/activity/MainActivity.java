 
package com.mobileclient.activity;
 

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.cc.testdemo.UserInfoListActivity;
import com.mobileclient.adapter.ExpressOrderAdapter;
import com.mobileclient.util.ActivityUtils;
import com.mobileclient.util.Utils;


public class MainActivity extends MyTabActivity {

	private static final String FIRST_TAB = "first";
	private static final String SECOND_TAB = "second";


	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//去除title   
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		//getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
				
		setContentView(R.layout.main_menu);
		Utils.setStatusBar(this, false, false);
		Utils.setStatusTextColor(false, MainActivity.this);
		tabHost = this.getTabHost();

		/*第一tab页 */
		TabSpec firstSpec = tabHost.newTabSpec(FIRST_TAB).setIndicator(FIRST_TAB)
				.setContent(new Intent(this, UserInfoListActivity.class));
		Button firstBtn = (Button)findViewById(R.id.firstBtn);
		firstBtn.setText("用户列表");
		/*第二tab页*/
		TabSpec secondSpec = tabHost.newTabSpec(SECOND_TAB).setIndicator(SECOND_TAB)
				.setContent(new Intent(this,NoticeListActivity.class));
		Button secondBtn = (Button)findViewById(R.id.secondBtn);
		secondBtn.setText("公告信息管理");

		tabHost.addTab(firstSpec);
		tabHost.addTab(secondSpec);

		//tabHost.addTab(fourthSpec);

		RadioGroup radioGroup = (RadioGroup) this
				.findViewById(R.id.rg_main_btns);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.firstBtn:
					Utils.setStatusTextColor(false, MainActivity.this);
					tabHost.setCurrentTabByTag(FIRST_TAB);
					break;

				case R.id.secondBtn:
					Utils.setStatusTextColor(false, MainActivity.this);
					tabHost.setCurrentTabByTag(SECOND_TAB);
					break;



//				case R.id.fourthBtn:
//					tabHost.setCurrentTabByTag(FOURTH_TAB);
//					break;

				default:
					break;
				}

			}
		});

	}

 
    
    @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
    	if (event.getAction() == KeyEvent.ACTION_DOWN
    			&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
    		ActivityUtils.getInstance().ConfirmExit(this);
            
    	}
    	
    	return super.dispatchKeyEvent(event);
    };
    
 

}
