package com.mobileclient.activity;
import com.mobileclient.app.Declare;
import com.mobileclient.domain.Order;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.domain.User;
import com.mobileclient.service.OrderService;
import com.mobileclient.service.ReceiveAddressService;
import com.mobileclient.service.UserService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpressOrderDetailActivity extends Activity {
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
    private int userId;
    private EditText evaluate;
    private int takeUserId;
    private int  flag;
    private int  p=0; //判断是否发生数据更新，1发生。0否
    private int q=0;//异步抢单
    private  RatingBar mRatingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.expressorder_detail);
        ImageView search = (ImageView) this.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("快递信息");
        ImageView back = (ImageView) this.findViewById(R.id.back_btn);

        // 通过findViewById方法实例化组件
        declare = (Declare) getApplicationContext();
        userId = declare.getUserId();
        Log.i("userType", "" + declare.getUserId());
        Log.i("userType", "" + declare.getUserType());
        //orderId = (TextView) findViewById(R.id.orderId);
        tx_orderName = (TextView) findViewById(R.id.orderName);
        ci_userPhoto = findViewById(R.id.userPhoto);
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
        btnGetOrder = (Button) findViewById(R.id.btnGetOrder);
        mRatingBar = (RatingBar) findViewById(R.id.ratingbar);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(ExpressOrderDetailActivity.this, "评分星级=" + rating, Toast.LENGTH_SHORT).show();
            }
        });





        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                if(p==1){
//                    Intent intent = new Intent();
//                    Bundle bundle =new Bundle();
//                    //传输的内容仍然是键值对的形式
//                    bundle.putInt("p",p);
//                    intent.putExtras(bundle);
//                    setResult(RESULT_OK,intent);
//                    Log.i("zhu34566", "123" );
//                    finish();
//                }
//                else {
//                    Log.i("zhu34566", "456" );
//                    finish();
//                }
                finish();

            }
        });
        evaluate = findViewById(R.id.ET_Evaluate);
        btnViewOrder = (Button) findViewById(R.id.btnViewOrder);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            orderId = extras.getInt("orderId");//订单发布者Id
            Log.i("34566", "" + extras.getString("orderName"));
            tx_orderName.setText(extras.getString("orderName"));
            byte[] photo = extras.getByteArray("photo");
            Bitmap userPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);
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
            takeUserId = extras.getInt("takeUserId");    //获取代取者Id
            order.setTakeUserId(extras.getInt("takeUserId"));

            order.setOrderId(extras.getInt("orderId"));
            order.setOrderName(extras.getString("orderName"));
            Log.i("zhu2222", "" + extras.getInt("userId"));
            order.setUserId(extras.getInt("userId"));
            order.setExpressCompanyName(extras.getString("expressCompanyName"));
            order.setExpressCompanyAdress(extras.getString("expressCompanyAddress"));
            Log.i("zhu2222", "" + extras.getInt("receiveAddressId"));
            order.setReceiveAdressId(extras.getInt("receiveAddressId"));
            order.setAddTime(extras.getString("addTime"));
            order.setOrderState(extras.getString("orderState"));
            order.setOrderPay(extras.getString("orderPay"));
            order.setRemark(extras.getString("remark"));
            order.setReceiveCode(extras.getString("receiveCode"));
            order.setOrderEvaluate(extras.getString("evaluate"));


        }
        if (declare.getUserType().equals("快递员")) {
            if (extras.getString("orderState").equals("待接单")) {
                if (extras.getString("userName").equals(declare.getUserName())) {  //如果是本人
                    Toast.makeText(ExpressOrderDetailActivity.this, "您的订单还未有人接单！", Toast.LENGTH_SHORT).show();
                } else {    //其他人可以接单
                    flag = 1;
                    btnGetOrder.setVisibility(View.VISIBLE);
                }
            } else if (extras.getString("orderState").equals("送单中")) {
                if (extras.getString("userName").equals(declare.getUserName())) {  //如果是本人
                    btnGetOrder.setVisibility(View.GONE);
                    btnViewOrder.setVisibility(View.VISIBLE);
                    btnViewOrder.setText("等待送达");
                }
                if (takeUserId == declare.getUserId()) {  //接单着
                    btnGetOrder.setVisibility(View.VISIBLE);
                    flag = 2;
                    btnGetOrder.setText("确认送达");
                } else {
                    Toast.makeText(ExpressOrderDetailActivity.this, "已有人接单！", Toast.LENGTH_SHORT).show();
                }
            } else if (extras.getString("orderState").equals("已送达")) {
                if (extras.getString("userName").equals(declare.getUserName())) {  //如果是本人
                    btnGetOrder.setVisibility(View.VISIBLE);
                    flag = 3;
                    btnGetOrder.setText("确认收货");

                }
                if (takeUserId == declare.getUserId()) {  //接单者
                    btnGetOrder.setVisibility(View.GONE);
                    btnViewOrder.setVisibility(View.VISIBLE);
                    btnViewOrder.setText("等待确认收货");
                } else {
                    Toast.makeText(ExpressOrderDetailActivity.this, "已有人接单！", Toast.LENGTH_SHORT).show();
                }
            } else if (extras.getString("orderState").equals("交易结束")) {
                if (extras.getString("userName").equals(declare.getUserName())) {  //如果是本人
                    btnGetOrder.setVisibility(View.VISIBLE);
                    mRatingBar.setVisibility(View.VISIBLE);
                    evaluate.setVisibility(View.VISIBLE);
                    btnGetOrder.setText("评价");
                    flag = 4;

                }
                else if (takeUserId == declare.getUserId()) {  //接单者
                    btnGetOrder.setVisibility(View.GONE);
                    btnViewOrder.setVisibility(View.VISIBLE);
                    btnViewOrder.setText("等待对方评价");
                } else {
                    Toast.makeText(ExpressOrderDetailActivity.this, "交易已结束！", Toast.LENGTH_SHORT).show();
                }
            }


        }else {  //如果是普通用户
            if (extras.getString("orderState").equals("已送达")) {
                if (extras.getString("userName").equals(declare.getUserName())) {  //如果是本人
                    btnGetOrder.setVisibility(View.VISIBLE);
                    flag = 3;
                    btnGetOrder.setText("确认收货");

                } else {
                    Toast.makeText(ExpressOrderDetailActivity.this, "订单交易中！", Toast.LENGTH_SHORT).show();
                }
            }
             if (extras.getString("orderState").equals("交易结束")) {
                if (extras.getString("userName").equals(declare.getUserName())) {  //如果是本人
                        btnGetOrder.setVisibility(View.VISIBLE);
                        evaluate.setVisibility(View.VISIBLE);
                        mRatingBar.setVisibility(View.VISIBLE);
                        btnGetOrder.setText("评价");
                        flag = 4;

                    }else
                    Toast.makeText(ExpressOrderDetailActivity.this, "订单交易中！", Toast.LENGTH_SHORT).show();
                }

        }
            // declare = (Declare) this.getApplication();
            //Id = declare.getUserId();

            btnGetOrder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag==1) {
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                if (msg.what == 0x123) {
                                    if (msg.getData().getInt("takeUserId") != -1) {
                                        Toast.makeText(ExpressOrderDetailActivity.this, "已有人接单！", Toast.LENGTH_SHORT).show();
                                        tx_orderState.setText("送单中");
                                    }
                                    else
                                        showDialog();
                                }
                            }
                        };

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    order =  orderService.QueryTake(order.getOrderId());
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("takeUserId", order.getTakeUserId());
                                    Log.i("zhuUser",""+order.getTakeUserId());
                                    Message msg = new Message();
                                    msg.what = 0x123;
                                    msg.setData(bundle);
                                    handler.sendMessage(msg);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        p=1;
                    }
                    else {
                        if (btnGetOrder.getText().toString().equals("等待确认收货")) {
                            Toast.makeText(ExpressOrderDetailActivity.this, "对方还未确认收货！", Toast.LENGTH_SHORT).show();
                        } else
                            showDialog();

                    }

                }
            });


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
//
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
        //更新订单，添加代取者
        private void update(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (flag==0) {
                   double m= Double.parseDouble(order.getScore());

                    Log.i("nnnnnnnn",""+m);

                        if (m >= 4) {
                            user = userService.GetUserInfo(order.getTakeUserId());
                            user.setUserReputation(user.getUserReputation() + 1);
                            userService.UpdateUserInfo(user);
                        } else {
                            user = userService.GetUserInfo(order.getTakeUserId());
                            user.setUserReputation(user.getUserReputation() - 1);
                            userService.UpdateUserInfo(user);
                        }

                }
                orderService.UpdateOrder(order);

            }
        }).start();
        }
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage(btnGetOrder.getText().toString());
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        p=1;
                        if(flag==1){     //接单后，订单状态修改

                            order.setTakeUserId(userId);
                            order.setOrderState("送单中");
                            order.setOrderEvaluate("-+-");
                            Toast.makeText(ExpressOrderDetailActivity.this, "恭喜您抢到订单！", Toast.LENGTH_SHORT).show();
                            btnGetOrder.setText("确认送达");
                            flag=2;
                            //flag=2;
                            //更新订单，把快递代取者添加到订单中
                        }else if(flag==2){//确认送达后，订单状态修改，等待用户确认送达
                            order.setOrderState("已送达");
                            order.setOrderEvaluate("-+-");
                            btnGetOrder.setText("等待确认收货");

                        }else if(flag==3){ //送达后，确认收货，可以评价

                            order.setOrderState("交易结束");
                            order.setOrderEvaluate("-+-");
                            mRatingBar.setVisibility(View.VISIBLE);
                            evaluate.setVisibility(View.VISIBLE);  //出现评价框
                            btnGetOrder.setText("评价");
                            flag=4;
                        }else if(flag==4){
                            if(evaluate.getText().toString().equals(""))
                                return;
                            order.setScore(""+mRatingBar.getRating());
                            order.setOrderEvaluate(evaluate.getText().toString());
                            flag=0;
                        }
                        update();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();

    }


}