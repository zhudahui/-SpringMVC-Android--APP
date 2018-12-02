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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondOrderDetailActivity extends Activity {
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
    private TextView evaluate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.secondexpressorder_detail);
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
        declare = (Declare) getApplicationContext();
        userId=declare.getUserId();
        Log.i("userType",""+declare.getUserType());
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
        btnGetOrder = (Button) findViewById(R.id.btnGetOrder);
        evaluate=findViewById(R.id.TV_Evaluate);
        btnViewOrder = (Button) findViewById(R.id.btnViewOrder);
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
            evaluate.setVisibility(View.VISIBLE);
            evaluate.setText(extras.getString("evaluate"));
        }

        }
        // declare = (Declare) this.getApplication();
        //Id = declare.getUserId();

}