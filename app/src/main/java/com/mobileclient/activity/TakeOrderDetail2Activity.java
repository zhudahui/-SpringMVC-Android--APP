package com.mobileclient.activity;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.TakeOrder;
import com.mobileclient.service.TakeOrderService;
import com.mobileclient.domain.ExpressTake;
import com.mobileclient.service.ExpressTakeService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.domain.OrderState;
import com.mobileclient.service.OrderStateService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
public class TakeOrderDetail2Activity extends Activity {
    // 声明返回按钮
    private Button btnReturn;
    // 声明订单id控件
    private TextView TV_orderId;
    // 声明代拿的快递控件
    private TextView TV_expressTakeObj;
    // 声明接任务人控件
    private TextView TV_userObj;
    // 声明接单时间控件
    private TextView TV_takeTime;
    // 声明订单状态控件
    private TextView TV_orderStateObj;
    // 声明实时动态控件
    private TextView TV_ssdt;
    // 声明用户评价控件
    private TextView TV_evaluate;
    /* 要保存的代拿订单信息 */
    TakeOrder takeOrder = new TakeOrder();
    /* 代拿订单管理业务逻辑层 */
    private TakeOrderService takeOrderService = new TakeOrderService();
    private ExpressTakeService expressTakeService = new ExpressTakeService();
    private UserInfoService userInfoService = new UserInfoService();
    private OrderStateService orderStateService = new OrderStateService();
    private int orderId;
    private String tel;
    private ImageView telphone;
    private ImageView ems;
    ExpressTake expressTake = new ExpressTake();
    private TextView TV_userObj_Label;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.takeorder_detail);
        ImageView search = (ImageView) this.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("查看代拿订单详情");
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
        TV_expressTakeObj = (TextView) findViewById(R.id.TV_expressTakeObj);
        TV_userObj = (TextView) findViewById(R.id.TV_userObj);
        TV_takeTime = (TextView) findViewById(R.id.TV_takeTime);
        TV_orderStateObj = (TextView) findViewById(R.id.TV_orderStateObj);
        TV_ssdt = (TextView) findViewById(R.id.TV_ssdt);
        TV_evaluate = (TextView) findViewById(R.id.TV_evaluate);
        TV_userObj_Label=findViewById(R.id.TV_userObj_Label);
        Bundle extras = this.getIntent().getExtras();
        telphone=findViewById(R.id.telphone);
        ems=findViewById(R.id.ems);
        orderId = extras.getInt("orderId");
        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TakeOrderDetail2Activity.this.finish();
            }
        });
        initViewData();
        telphone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:"+tel);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                startActivity(intent);
            }
        });
        ems.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri2 = Uri.parse("smsto:"+tel);
                Intent intentMessage = new Intent(Intent.ACTION_VIEW,uri2);
                startActivity(intentMessage);
            }
        });
        Button btnEvaluate = (Button) findViewById(R.id.btnEvaluate);
        btnEvaluate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(takeOrder.getOrderStateObj() == 1) {
                    Toast.makeText(getApplicationContext(), "订单派送完毕才能评价", Toast.LENGTH_SHORT).show();
                    return;
                }

                final EditText inputServer = new EditText(TakeOrderDetail2Activity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(TakeOrderDetail2Activity.this);
                builder.setTitle("请输入评价").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String evaluate = inputServer.getText().toString();
                        takeOrder.setEvaluate(evaluate);
                        String result = takeOrderService.UpdateTakeOrder(takeOrder);
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        TV_evaluate.setText(evaluate);

                    }
                });
                builder.show();
            }
        });
        int expressOderId = takeOrder.getExpressTakeObj();
        String takeUserName = expressTakeService.SearchExpressTake(expressOderId).getUserObj();
        Declare declare = (Declare) this.getApplication();
        if(takeUserName.equals(declare.getUserName())) {
            btnEvaluate.setVisibility(View.VISIBLE);
        }
    }
    /* 初始化显示详情界面的数据 */
    private void initViewData() {
        takeOrder = takeOrderService.GetTakeOrder(orderId);
        this.TV_orderId.setText(takeOrder.getOrderId() + "");
        ExpressTake expressTakeObj = expressTakeService.SearchExpressTake(takeOrder.getExpressTakeObj());
        this.TV_expressTakeObj.setText(expressTakeObj.getTaskTitle());
        expressTake = expressTakeService.SearchExpressTake(orderId);
        UserInfo userObj = userInfoService.GetUserInfo(takeOrder.getUserObj());

        this.TV_userObj.setText(expressTake.getReceiverName());
        TV_userObj_Label.setText("发布快递者");
        tel=expressTake.getTelephone();
        OrderState orderStateObj = orderStateService.GetOrderState(takeOrder.getOrderStateObj());
        this.TV_orderStateObj.setText(orderStateObj.getOrderStateName());
        this.TV_ssdt.setText(takeOrder.getSsdt());
        this.TV_evaluate.setText(takeOrder.getEvaluate());
    }
}
