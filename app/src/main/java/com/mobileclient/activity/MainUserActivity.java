 
package com.mobileclient.activity;
 

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.mobileclient.activity.myorder.ExpressTakeMyListActivity;
import com.mobileclient.activity.rewardOrder.RewardActivity;
import com.mobileclient.activity.takeOrder.TakeOrderListActivity;
import com.mobileclient.app.Declare;
import com.mobileclient.util.Utils;


public class MainUserActivity extends MyTabActivity {

	private static final String FIRST_TAB = "first";
	private static final String SECOND_TAB = "second";
	private static final String THIRD_TAB = "third";
	private static final String FOURTH_TAB = "fourth";

	private TabHost tabHost;
    private Button firstBtn,secondBtn,thirdBtn,fourthBtn;
    private Declare declare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//去除title   
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		//getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		declare= (Declare) getApplication();
		Utils.setStatusBar(MainUserActivity.this, false, false);
		Utils.setStatusTextColor(true, MainUserActivity.this);
		tabHost = this.getTabHost();

		/*第一tab页 */
		TabSpec firstSpec = tabHost.newTabSpec(FIRST_TAB).setIndicator(FIRST_TAB)
				.setContent(new Intent(this, ExpressOrderListActivity.class));
		firstBtn = (Button)findViewById(R.id.firstBtn);
		firstBtn.setText("订单");
		/*第二tab页*/
		final TabSpec secondSpec = tabHost.newTabSpec(SECOND_TAB).setIndicator(SECOND_TAB)
				.setContent(new Intent(this,TakeOrderListActivity.class));
		secondBtn = (Button)findViewById(R.id.secondBtn);
		secondBtn.setText("我的代取");
		/*第三tab页*/
		TabSpec thirdSpec = tabHost.newTabSpec(THIRD_TAB).setIndicator(THIRD_TAB)
				.setContent(new Intent(this,ExpressTakeMyListActivity.class));
		thirdBtn = (Button)findViewById(R.id.thirdBtn);
		thirdBtn.setText("我的发布");
		/*第四tab页*/
		TabSpec fourthSpec = tabHost.newTabSpec(FOURTH_TAB).setIndicator(FOURTH_TAB)
				.setContent(new Intent(this, MyInfoActivtiy.class));
		fourthBtn = (Button)findViewById(R.id.fourthBtn);
		fourthBtn.setText("我的");
		tabHost.addTab(firstSpec);
		tabHost.addTab(secondSpec);
		tabHost.addTab(thirdSpec);
		tabHost.addTab(fourthSpec);

		RadioGroup radioGroup = (RadioGroup) this
				.findViewById(R.id.rg_main_btns);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.firstBtn:

					Utils.setStatusTextColor(true, MainUserActivity.this);
					tabHost.setCurrentTabByTag(FIRST_TAB);
//					firstBtn.setBackgroundResource(R.drawable.zhuyeone);
//					secondBtn.setBackgroundResource(R.drawable.daiqu_one);
//					thirdBtn.setBackgroundResource(R.drawable.fabu_one);
//					fourthBtn.setBackgroundResource(R.drawable.my_one);
					break;

				case R.id.secondBtn:

					Utils.setStatusTextColor(false, MainUserActivity.this);
					tabHost.setCurrentTabByTag(SECOND_TAB);
//					firstBtn.setBackgroundResource(R.drawable.zhuye_two);
//					secondBtn.setBackgroundResource(R.drawable.daiqu_two);
//					thirdBtn.setBackgroundResource(R.drawable.fabu_one);
//					fourthBtn.setBackgroundResource(R.drawable.my_one);
					break;

				case R.id.thirdBtn:

					Utils.setStatusTextColor(false, MainUserActivity.this);
					tabHost.setCurrentTabByTag(THIRD_TAB);
//					firstBtn.setBackgroundResource(R.drawable.zhuye_two);
//					secondBtn.setBackgroundResource(R.drawable.daiqu_one);
//					thirdBtn.setBackgroundResource(R.drawable.fabu_two);
//					fourthBtn.setBackgroundResource(R.drawable.my_one);
					break;

				case R.id.fourthBtn:

					Utils.setStatusTextColor(false, MainUserActivity.this);
					tabHost.setCurrentTabByTag(FOURTH_TAB);
//					firstBtn.setBackgroundResource(R.drawable.zhuye_two);
//					secondBtn.setBackgroundResource(R.drawable.daiqu_one);
//					thirdBtn.setBackgroundResource(R.drawable.fabu_one);
//					fourthBtn.setBackgroundResource(R.drawable.my_two);
					break;

				default:
					break;
				}

			}
		});

	}

 
    

}
