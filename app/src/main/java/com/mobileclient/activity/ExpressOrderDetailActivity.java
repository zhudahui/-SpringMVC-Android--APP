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
public class ExpressOrderDetailActivity extends Activity {
    // 声明返回按钮
    private Button btnReturn;
    // 声明订单名
    private TextView orderName;
    // 声明发布者
    private TextView userName;
    // 声明物流公司
    private TextView expressCompanyName;
    // 声明取货地址
    private TextView expressCompanyAdress;
    // 声明收货地址
    private TextView receiveAdressName;
    // 声明收货电话
    private TextView receivePhone;
    // 声明收货备注
    private TextView remark;
    // 声明酬金
    private TextView orderPay;
    // 声明订单状态
    private TextView orderState;
    // 声明取货码
    private TextView receiveCode;
    // 声明发布时间控件
    private TextView TV_addTime;
    /* 要保存的快递代拿信息 */
    Order order = new Order();
    /* 快递代拿管理业务逻辑层 */
    private OrderService orderService = new OrderService();
    private CompanyService companyService = new CompanyService();
    private UserInfoService userInfoService = new UserInfoService();
    private TakeOrderService takeOrderService = new TakeOrderService();
    /*保存查询参数条件的快递代拿对象*/
    private Order queryConditionExpressTake;
    private int orderId;
    private User user;//订单发布者
    private User user1;//用户
    private int Id;//获取用户Id
    private Button btnGetOrder;
    private Button btnViewOrder;
    UserService userService = new UserService();
    ReceiveAddress receiveAdress = new ReceiveAddress();
    ReceiveAddressService receiveAdressService = new ReceiveAddressService();
    Declare declare;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.expresstake_detail);
        ImageView search = (ImageView) this.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("查看快递代拿详情");
        ImageView back = (ImageView) this.findViewById(R.id.back_btn);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        // 通过findViewById方法实例化组件
        btnReturn = (Button) findViewById(R.id.btnReturn);
        //orderId = (TextView) findViewById(R.id.orderId);
        orderName = (TextView) findViewById(R.id.orderName);
        userName = (TextView) findViewById(R.id.userName);
        expressCompanyName = (TextView) findViewById(R.id.expressCompanyName);
        expressCompanyAdress = (TextView) findViewById(R.id.expressCompanyAdress);
        receiveAdressName = (TextView) findViewById(R.id.receiveAdress);
        receivePhone = (TextView) findViewById(R.id.receivePhone);
        remark = (TextView) findViewById(R.id.remark);
        orderPay = (TextView) findViewById(R.id.orderPay);
        orderState = (TextView) findViewById(R.id.orderState);
        receiveCode = (TextView) findViewById(R.id.receiveCode);
        TV_addTime = (TextView) findViewById(R.id.addTime);
        Bundle extras = this.getIntent().getExtras();
        orderId = extras.getInt("orderId");//订单发布者Id
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpressOrderDetailActivity.this.finish();
            }
        });
        declare = (Declare) this.getApplication();
        Id = declare.getUserId();
        Log.i("111111111111111111111", "32" + Id);
        init();

        btnGetOrder = (Button) findViewById(R.id.btnGetOrder);
        btnGetOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                  order.setOrderId(orderId);
                  update();   //更新订单，把快递代取者添加到订单中
                  Intent intent = getIntent();
                  //点击接单后跳转到送货地图页面
                  intent.setClass(ExpressOrderDetailActivity.this, ExpressRouteActivity.class);
                  intent.putExtra("point",receiveAdressName.getText());
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

    private void init() {
        final Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x123) {
                    orderName.setText(msg.getData().getString("orderName"));
                    userName.setText(msg.getData().getString("userName"));
                    expressCompanyName.setText(msg.getData().getString("expressCompanyName"));
                    expressCompanyAdress.setText(msg.getData().getString("expressCompanyAdress"));
                    receiveAdressName.setText(msg.getData().getString("receiveAdressName"));
                    receivePhone.setText(msg.getData().getString("receivePhone"));
                    remark.setText(msg.getData().getString("remark"));
                    orderPay.setText(msg.getData().getString("orderPay"));
                    receiveCode.setText(msg.getData().getInt("receiveCode"));
                    TV_addTime.setText(msg.getData().getString("addTime"));
                    //判断用户类型是否为快递员
                    //判断订单是否被接单，如果已经被接单则接单按钮变为返回
                    //如果查看用户同时是发布者，则不可接单
                    if (msg.getData().getString("userType").equals("快递员") && msg.getData().getString("orderState").equals("待接单")) {
                        btnGetOrder.setVisibility(View.VISIBLE);
                        if (msg.getData().getString("orderState").equals("待接单") && msg.getData().getString("name").equals(userName)) {
                            btnViewOrder.setVisibility(View.VISIBLE);
                        }


                    }

                }
              }
            };

        new Thread(new Runnable() {
                @Override
                public void run () {
                    order = orderService.GetOrder(orderId);
                    Bundle bundle = new Bundle();
                    bundle.putString("orderName", order.getOrderName());
                    user = userService.GetUserInfo(order.getUserId());      //订单发布者
                    bundle.putString("userName", user.getUserName());
                    bundle.putString("expressCompanyName", order.getExpressCompanyName());
                   // bundle.putString("expressCompanyAdress", order.getExpressCompanyAdress());
                  //  receiveAdress = receiveAdressService.GetReceiveAdress(order.getReceiveAdressId());
                    bundle.putString("receiveAdressName", receiveAdress.getReceiveAddressName());
                    bundle.putString("receivePhone", receiveAdress.getReceivePhone());
                    bundle.putString("receiveName", receiveAdress.getReceiveName());
                    bundle.putString("remark", order.getRemark());
                    bundle.putString("orderPay", order.getOrderPay());
                    bundle.putString("orderState", order.getOrderState());
                    bundle.putInt("receiveCode", order.getReceiveCode());
                    bundle.putString("addTime", order.getAddTime());
                    user1 = userService.GetUserInfo(Id);//获取用户信息
                    bundle.putString("userType", user1.getUserType());
                    bundle.putString("name",user1.getUserName());
                    Message msg = new Message();
                    msg.setData(bundle);
                    msg.what = 0x123;
                    myHandler.sendMessage(msg);
                }

            }).start();
        }
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