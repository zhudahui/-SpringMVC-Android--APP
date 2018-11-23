package com.mobileclient.activity;

import java.util.List;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.ExpressTake;
import com.mobileclient.service.ExpressTakeService;
import com.mobileclient.domain.Company;
import com.mobileclient.service.CompanyService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class ExpressTakeUserAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明代拿任务输入框
	private EditText ET_taskTitle;
	// 声明物流公司下拉框
	private Spinner spinner_companyObj;
	private ArrayAdapter<String> companyObj_adapter;
	private static  String[] companyObj_ShowText  = null;
	private List<Company> companyList = null;
	/*物流公司管理业务逻辑层*/
	private CompanyService companyService = new CompanyService();
	// 声明运单号码输入框
	private EditText ET_waybill;
	// 声明收货人输入框
	private EditText ET_receiverName;
	// 声明收货电话输入框
	private EditText ET_telephone;
	// 声明收货备注输入框
	private EditText ET_receiveMemo;
	// 声明送达地址输入框
	private EditText ET_takePlace;
	// 声明代拿报酬输入框
	private EditText ET_giveMoney;
	 
	protected String carmera_path;
	/*要保存的快递代拿信息*/
	ExpressTake expressTake = new ExpressTake();
	/*快递代拿管理业务逻辑层*/
	private ExpressTakeService expressTakeService = new ExpressTakeService();
    Declare declare=new Declare();
    private int companyCount;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.expresstake_user_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加快递代拿");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_taskTitle = (EditText) findViewById(R.id.ET_taskTitle);
		spinner_companyObj = (Spinner) findViewById(R.id.Spinner_companyObj);
		// 获取所有的物流公司


		// 设置默认值
		spinner_companyObj.setVisibility(View.VISIBLE);
		ET_waybill = (EditText) findViewById(R.id.ET_waybill);
		ET_receiverName = (EditText) findViewById(R.id.ET_receiverName);
		ET_telephone = (EditText) findViewById(R.id.ET_telephone);
		ET_receiveMemo = (EditText) findViewById(R.id.ET_receiveMemo);
		ET_takePlace = (EditText) findViewById(R.id.ET_takePlace);
		ET_giveMoney = (EditText) findViewById(R.id.ET_giveMoney);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加快递代拿按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					/*验证获取代拿任务*/
					if(ET_taskTitle.getText().toString().equals("")) {
						Toast.makeText(ExpressTakeUserAddActivity.this, "代拿任务输入不能为空!", Toast.LENGTH_LONG).show();
						ET_taskTitle.setFocusable(true);
						ET_taskTitle.requestFocus();
						return;
					}
					expressTake.setTaskTitle(ET_taskTitle.getText().toString());
					/*验证获取运单号码*/
					if(ET_waybill.getText().toString().equals("")) {
						Toast.makeText(ExpressTakeUserAddActivity.this, "运单号码输入不能为空!", Toast.LENGTH_LONG).show();
						ET_waybill.setFocusable(true);
						ET_waybill.requestFocus();
						return;
					}
					expressTake.setWaybill(ET_waybill.getText().toString());
					/*验证获取收货人*/
					if(ET_receiverName.getText().toString().equals("")) {
						Toast.makeText(ExpressTakeUserAddActivity.this, "收货人输入不能为空!", Toast.LENGTH_LONG).show();
						ET_receiverName.setFocusable(true);
						ET_receiverName.requestFocus();
						return;
					}
					expressTake.setReceiverName(ET_receiverName.getText().toString());
					/*验证获取收货电话*/
					if(ET_telephone.getText().toString().equals("")) {
						Toast.makeText(ExpressTakeUserAddActivity.this, "收货电话输入不能为空!", Toast.LENGTH_LONG).show();
						ET_telephone.setFocusable(true);
						ET_telephone.requestFocus();
						return;
					}
					expressTake.setTelephone(ET_telephone.getText().toString());
					/*验证获取收货备注*/
					if(ET_receiveMemo.getText().toString().equals("")) {
						Toast.makeText(ExpressTakeUserAddActivity.this, "收货备注输入不能为空!", Toast.LENGTH_LONG).show();
						ET_receiveMemo.setFocusable(true);
						ET_receiveMemo.requestFocus();
						return;
					}
					expressTake.setReceiveMemo(ET_receiveMemo.getText().toString());
					/*验证获取送达地址*/
					if(ET_takePlace.getText().toString().equals("")) {
						Toast.makeText(ExpressTakeUserAddActivity.this, "送达地址输入不能为空!", Toast.LENGTH_LONG).show();
						ET_takePlace.setFocusable(true);
						ET_takePlace.requestFocus();
						return;
					}
					expressTake.setTakePlace(ET_takePlace.getText().toString());
					/*验证获取代拿报酬*/
					if(ET_giveMoney.getText().toString().equals("")) {
						Toast.makeText(ExpressTakeUserAddActivity.this, "代拿报酬输入不能为空!", Toast.LENGTH_LONG).show();
						ET_giveMoney.setFocusable(true);
						ET_giveMoney.requestFocus();
						return;
					}
					expressTake.setGiveMoney(Float.parseFloat(ET_giveMoney.getText().toString()));

					expressTake.setTakeStateObj("待接单");

					expressTake.setAddTime("--");
					/*调用业务逻辑层上传快递代拿信息*/
					ExpressTakeUserAddActivity.this.setTitle("正在上传快递代拿信息，稍等...");
					init();
					Intent intent = getIntent();
					intent.setClass(ExpressTakeUserAddActivity.this,MainUserActivity.class);
					startActivity(intent);
					//setResult(RESULT_OK,intent);
					//finish();

				} catch (Exception e) {}
			}
		});
		sendRequest();

	}
	private  void sendRequest() {
		final Handler myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 0x123) {
					companyCount = msg.getData().getInt("size");
					companyObj_ShowText = new String[companyCount];
					companyObj_ShowText = new String[companyCount];

					for (int i = 0; i < companyCount; i++) {
						companyObj_ShowText[i] = companyList.get(i).getCompanyName();
					}
					// 将可选内容与ArrayAdapter连接起来
					companyObj_adapter = new ArrayAdapter<String>(ExpressTakeUserAddActivity.this, android.R.layout.simple_spinner_item, companyObj_ShowText);
					// 设置下拉列表的风格
					companyObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					// 将adapter 添加到spinner中
					spinner_companyObj.setAdapter(companyObj_adapter);
					// 添加事件Spinner事件监听
					spinner_companyObj.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							expressTake.setCompanyObj(companyList.get(arg2).getCompanyId());
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});


					Declare declare = (Declare) ExpressTakeUserAddActivity.this.getApplication();
					expressTake.setUserObj(declare.getUserName());


				}
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					companyList = companyService.QueryCompany(null);
					Log.i("zhuhuz", "" + companyList.size());
					//declare.setCompanyLength(companyList.size());
					//companyCount=companyList.size();
					Bundle bundle = new Bundle();
					Message msg = new Message();
					bundle.putInt("size", companyList.size());
					msg.setData(bundle);
					msg.what = 0x123;
					myHandler.sendMessage(msg);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();


	}
	private  void init() {
		new Thread(new Runnable() {
			@Override
			public void run() {
					String result = expressTakeService.AddExpressTake(expressTake);
//					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
					Log.i("zhu3333",""+result);
			}
		}).start();


	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
