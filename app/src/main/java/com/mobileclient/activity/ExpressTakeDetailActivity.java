package com.mobileclient.activity;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.ExpressTake;
import com.mobileclient.domain.TakeOrder;
import com.mobileclient.service.ExpressTakeService;
import com.mobileclient.service.TakeOrderService;
import com.mobileclient.domain.Company;
import com.mobileclient.service.CompanyService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.util.ActivityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;

import okhttp3.Response;

public class ExpressTakeDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明订单id控件
	private TextView TV_orderId;
	// 声明代拿任务控件
	private TextView TV_taskTitle;
	// 声明物流公司控件
	private TextView TV_companyObj;
	// 声明运单号码控件
	private TextView TV_waybill;
	// 声明收货人控件
	private TextView TV_receiverName;
	// 声明收货电话控件
	private TextView TV_telephone;
	// 声明收货备注控件
	private TextView TV_receiveMemo;
	// 声明送达地址控件
	private TextView TV_takePlace;
	// 声明代拿报酬控件
	private TextView TV_giveMoney;
	// 声明代拿状态控件
	private TextView TV_takeStateObj;
	// 声明任务发布人控件
	private TextView TV_userObj;
	// 声明发布时间控件
	private TextView TV_addTime;
	/* 要保存的快递代拿信息 */
	ExpressTake expressTake = new ExpressTake(); 
	/* 快递代拿管理业务逻辑层 */
	private ExpressTakeService expressTakeService = new ExpressTakeService();
	private CompanyService companyService = new CompanyService();
	private UserInfoService userInfoService = new UserInfoService(); 
	private TakeOrderService takeOrderService = new TakeOrderService();
	/*保存查询参数条件的快递代拿对象*/
	private ExpressTake queryConditionExpressTake;
	private int orderId;
	private  UserInfo userInfo;
	private  String userName;
	 private  Button btnGetOrder;
	private  Button btnViewOrder;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.expresstake_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看快递代拿详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_orderId = (TextView) findViewById(R.id.TV_orderId);
		TV_taskTitle = (TextView) findViewById(R.id.TV_taskTitle);
		TV_companyObj = (TextView) findViewById(R.id.TV_companyObj);
		TV_waybill = (TextView) findViewById(R.id.TV_waybill);
		TV_receiverName = (TextView) findViewById(R.id.TV_receiverName);
		TV_telephone = (TextView) findViewById(R.id.TV_telephone);
		TV_receiveMemo = (TextView) findViewById(R.id.TV_receiveMemo);
		TV_takePlace = (TextView) findViewById(R.id.TV_takePlace);
		TV_giveMoney = (TextView) findViewById(R.id.TV_giveMoney);
		TV_takeStateObj = (TextView) findViewById(R.id.TV_takeStateObj);
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		TV_addTime = (TextView) findViewById(R.id.TV_addTime);
		Bundle extras = this.getIntent().getExtras();
		orderId = extras.getInt("orderId");
		btnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ExpressTakeDetailActivity.this.finish();
			}
		});
		Declare declare = (Declare) this.getApplication();
		userName = declare.getUserName();
		sendRequest();
		Log.i("111111111111111111111", "32"+userName);
		Request();
		 btnGetOrder = (Button) findViewById(R.id.btnGetOrder);
		btnGetOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TakeOrder takeOrder = new TakeOrder();
				takeOrder.setEvaluate("--");
				takeOrder.setExpressTakeObj(expressTake.getOrderId());
				takeOrder.setOrderStateObj(1);
				takeOrder.setSsdt("--");
				takeOrder.setTakeTime("--");
				takeOrder.setUserObj(userName);
				String result = takeOrderService.AddTakeOrder(takeOrder);
				//Toast.makeText(getApplicationContext(), result, 1).show();
				Intent intent = getIntent();
				intent.setClass(ExpressTakeDetailActivity.this, ExpressRouteActivity.class);
				intent.putExtra("point",TV_takePlace .getText());
				startActivity(intent);
				setResult(RESULT_OK,intent);
				finish();
			}
		});




		 btnViewOrder = (Button) findViewById(R.id.btnViewOrder);
		btnViewOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				try {
					int orderId = expressTake.getOrderId();
					TakeOrder queryConditionTakeOrder = new TakeOrder();
					queryConditionTakeOrder.setExpressTakeObj(orderId);
					queryConditionTakeOrder.setUserObj("");
					queryConditionTakeOrder.setTakeTime("");
					queryConditionTakeOrder.setOrderStateObj(0); 
					TakeOrder takeOrder= takeOrderService.QueryTakeOrder(queryConditionTakeOrder).get(0);
 
	            	Intent intent = new Intent();
	            	intent.setClass(ExpressTakeDetailActivity.this, TakeOrderDetailActivity.class);
	            	Bundle bundle = new Bundle();
	            	bundle.putInt("orderId", takeOrder.getOrderId());
	            	intent.putExtras(bundle);
	            	startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}); 
		
		

		
	}
private  void init(){
	userInfo = userInfoService.GetUserInfo(userName);
	Log.i("zhu4444",""+userInfo.getUserType());
	if(userInfo.getUserType().equals("快递员") && expressTake.getTakeStateObj().equals("待接单")) {
		btnGetOrder.setVisibility(View.VISIBLE);
		if(expressTake.getTakeStateObj().equals("已接单") && expressTake.getUserObj().equals(userName) ) {
			btnViewOrder.setVisibility(View.VISIBLE);
		}

	}
}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
		this.TV_orderId.setText(expressTake.getOrderId() + "");

		this.TV_taskTitle.setText(expressTake.getTaskTitle());
		Company companyObj = companyService.GetCompany(expressTake.getCompanyObj());
		this.TV_companyObj.setText(companyObj.getCompanyName());
		this.TV_waybill.setText(expressTake.getWaybill());
		this.TV_receiverName.setText(expressTake.getReceiverName());
		this.TV_telephone.setText(expressTake.getTelephone());
		this.TV_receiveMemo.setText(expressTake.getReceiveMemo());
		this.TV_takePlace.setText(expressTake.getTakePlace());

		this.TV_giveMoney.setText(expressTake.getGiveMoney() + "");
		this.TV_takeStateObj.setText(expressTake.getTakeStateObj());
		UserInfo userObj = userInfoService.GetUserInfo(expressTake.getUserObj());
		this.TV_userObj.setText(userObj.getName());
		this.TV_addTime.setText(expressTake.getAddTime());
	}
    private  void sendRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                expressTake = expressTakeService.SearchExpressTake(orderId);
               initViewData();

            }
        }).start();
    }
	private  void Request(){
		new Thread(new Runnable() {
			@Override
			public void run() {

				init();

			}
		}).start();
	}
}
