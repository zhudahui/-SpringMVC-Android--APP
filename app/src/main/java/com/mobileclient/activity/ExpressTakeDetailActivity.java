package com.mobileclient.activity;
import com.mobileclient.app.Declare;
import com.mobileclient.domain.Order;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.domain.User;
import com.mobileclient.service.OrderService;
import com.mobileclient.service.ReceiveAddressService;
import com.mobileclient.service.TakeOrderService;
import com.mobileclient.service.CompanyService;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.service.UserService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpressTakeDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明订单名
	private TextView tx_orderName;
	// 声明发布者
	private TextView tx_userName;
	// 声明物流公司
	private TextView tx_expressCompanyName;
	// 声明取货地址
	private TextView tx_expressCompanyAddress;
	// 声明收货地址
	private TextView tx_receiveAddressName;
	// 声明收货电话
	private TextView tx_receivePhone;
	// 声明收货备注
	private TextView tx_remark;
	// 声明酬金
	private TextView tx_orderPay;
	// 声明订单状态
	private TextView tx_orderState;
	// 声明取货码
	private TextView tx_receiveCode;
	// 声明发布时间控件
	private TextView tx_addTime;
	/* 要保存的快递代拿信息 */
	Order order = new Order();
	/* 快递代拿管理业务逻辑层 */
	private OrderService orderService = new OrderService();
	/*保存查询参数条件的快递代拿对象*/
	private Order queryConditionExpressTake;
	private int orderId;
	private User user;//订单发布者
	private User user1;//用户
	private int Id;//获取用户Id
	private Button btnGetOrder;
	private Button btnViewOrder;
	private CircleImageView ci_userPhoto;
	UserService userService = new UserService();
	ReceiveAddress receiveAddress = new ReceiveAddress();
	ReceiveAddressService receiveAdressService = new ReceiveAddressService();
	Declare declare;
	private File file;
	String orderState;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.expressmyorder_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("订单信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件

		//orderId = (TextView) findViewById(R.id.orderId);
		tx_orderName = (TextView) findViewById(R.id.orderName);
		ci_userPhoto=findViewById(R.id.userPhoto);
		tx_userName = (TextView) findViewById(R.id.userName);
		tx_expressCompanyName = (TextView) findViewById(R.id.expressCompanyName);
		tx_expressCompanyAddress = (TextView) findViewById(R.id.expressCompanyAddress);
		tx_receiveAddressName = (TextView) findViewById(R.id.receiveAddress);
		tx_receivePhone = (TextView) findViewById(R.id.receivePhone);
		tx_remark = (TextView) findViewById(R.id.remark);
		tx_orderPay = (TextView) findViewById(R.id.orderPay);
		tx_orderState = (TextView) findViewById(R.id.orderState);
		tx_receiveCode = (TextView) findViewById(R.id.receiveCode);
		tx_addTime = (TextView) findViewById(R.id.addTime);
		Bundle extras = this.getIntent().getExtras();
		if(extras!=null) {
			Log.i("34566", "" + extras.getInt("orderId"));
			orderId = extras.getInt("orderId");//订单发布者Id
			Log.i("34566", "" + extras.getString("orderName"));
			tx_orderName.setText(extras.getString("orderName"));
			byte[] photo=extras.getByteArray("photo");
			Bitmap userPhoto = BitmapFactory.decodeByteArray(photo, 0,photo.length);
			ci_userPhoto.setImageBitmap(userPhoto);
			tx_userName.setText(extras.getString("userName"));
			tx_expressCompanyName.setText(extras.getString("expressCompanyName"));
			tx_expressCompanyAddress.setText(extras.getString("expressCompanyAddress"));
			tx_receiveAddressName.setText(extras.getString("receiveAddressName"));
			tx_receivePhone.setText(extras.getString("receivePhone"));
			tx_remark.setText(extras.getString("remark"));
			tx_orderPay.setText(extras.getString("orderPay"));
			tx_orderState.setText(extras.getString("orderState"));
			tx_receiveCode.setText(extras.getString("receiveCode"));
			tx_addTime.setText(extras.getString("addTime"));
			orderState=extras.getString("orderState");
		}

		declare = (Declare) this.getApplication();
		Id = declare.getUserId();


		//btnGetOrder = (Button) findViewById(R.id.btnGetOrder);
//		btnGetOrder.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				order.setOrderId(orderId);
//				update();   //更新订单，把快递代取者添加到订单中
//				Intent intent = getIntent();
//				//点击接单后跳转到送货地图页面
//				intent.setClass(ExpressTakeDetailActivity.this, ExpressRouteActivity.class);
//				intent.putExtra("point",tx_receiveAddressName.getText());
//				startActivity(intent);
//				setResult(RESULT_OK,intent);
//				finish();
//			}
//		});
		btnViewOrder = (Button) findViewById(R.id.btnViewOrder);
		if(orderState.equals("待接单"))
		{
			btnViewOrder.setText("取消订单");
		}
		if(orderState.equals("送货中"))
		{
			btnViewOrder.setText("确认收货");
		}
		if(orderState.equals("已收货"))
		{
			btnViewOrder.setText("评价");
		}
		btnViewOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {

//                    int orderId = expressTake.getOrderId();
//                    TakeOrder queryConditionTakeOrder = new TakeOrder();
//                    queryConditionTakeOrder.setExpressTakeObj(orderId);
//                    queryConditionTakeOrder.setUserObj("");
//                    queryConditionTakeOrder.setTakeTime("");
//                    queryConditionTakeOrder.setOrderStateObj(0);
//                    TakeOrder takeOrder= takeOrderService.QueryTakeOrder(queryConditionTakeOrder).get(0);
//
//                    Intent intent = new Intent();
//                    intent.setClass(ExpressOrderDetailActivity.this, TakeOrderDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("orderId", takeOrder.getOrderId());
//                    intent.putExtras(bundle);
//                    startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 请求数据和UI组件绑定数据
	 */
//
//    private void init() {
//        final Handler myHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (msg.what == 0x123) {
//                    orderName.setText(msg.getData().getString("orderName"));
//                    userName.setText(msg.getData().getString("userName"));
//                    expressCompanyName.setText(msg.getData().getString("expressCompanyName"));
//                    expressCompanyAdress.setText(msg.getData().getString("expressCompanyAdress"));
//                    receiveAdressName.setText(msg.getData().getString("receiveAdressName"));
//                    receivePhone.setText(msg.getData().getString("receivePhone"));
//                    remark.setText(msg.getData().getString("remark"));
//                    orderPay.setText(msg.getData().getString("orderPay"));
//                    receiveCode.setText(msg.getData().getInt("receiveCode"));
//                    TV_addTime.setText(msg.getData().getString("addTime"));
//                    //判断用户类型是否为快递员
//                    //判断订单是否被接单，如果已经被接单则接单按钮变为返回
//                    //如果查看用户同时是发布者，则不可接单
//
//
//                    }
//
//                }
//              }
//            };
//
//        new Thread(new Runnable() {
//                @Override
//                public void run () {
//                    order = orderService.GetOrder(orderId);   //根据orderId查询order信息
//                    Bundle bundle = new Bundle();
//                    bundle.putString("orderName", order.getOrderName());
//                    user = userService.GetUserInfo(order.getUserId());      //订单发布者
//                    bundle.putString("userName", user.getUserName());
//                    bundle.putString("expressCompanyName", order.getExpressCompanyName());
//                   // bundle.putString("expressCompanyAdress", order.getExpressCompanyAdress());
//                  //  receiveAdress = receiveAdressService.GetReceiveAdress(order.getReceiveAdressId());
//                    bundle.putString("receiveAdressName", receiveAdress.getReceiveAddressName());
//                    bundle.putString("receivePhone", receiveAdress.getReceivePhone());
//                    bundle.putString("receiveName", receiveAdress.getReceiveName());
//                    bundle.putString("remark", order.getRemark());
//                    bundle.putString("orderPay", order.getOrderPay());
//                    bundle.putString("orderState", order.getOrderState());
//                    bundle.putInt("receiveCode", order.getReceiveCode());
//                    bundle.putString("addTime", order.getAddTime());
//                    user1 = userService.GetUserInfo(Id);//获取用户信息
//                    bundle.putString("userType", user1.getUserType());
//                    bundle.putString("name",user1.getUserName());
//                    Message msg = new Message();
//                    msg.setData(bundle);
//                    msg.what = 0x123;
//                    myHandler.sendMessage(msg);
//                }
//
//            }).start();
//        }
	//更新订单，添加代取者
	private void update(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				orderService.UpdateOrder(order);
			}
		}).start();
	}
}