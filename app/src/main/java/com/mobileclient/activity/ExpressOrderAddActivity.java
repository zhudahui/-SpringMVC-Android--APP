package com.mobileclient.activity;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Order;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.service.OrderService;
import com.mobileclient.service.ReceiveAddressService;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class ExpressOrderAddActivity extends Activity {
    // 声明确定添加按钮
    private Button btnAdd;
    // 声明代拿任务输入框
    private EditText ET_orderName;
    // 声明物流公司
    private EditText ET_expressCompanyName;
    // 声明物流公司地址
    private EditText ET_expressCompanyAddress;
    // 声明收货备注输入框
    private EditText ET_remark;
    // 声明送达地址
    private TextView ET_receiveAddressName;
    //取货码
    private EditText ET_receiveCode;
    // 声明代拿报酬输入框
    private EditText ET_orderPay;

    Declare declare=new Declare();
    Order order=new Order();
    OrderService orderService=new OrderService();
    ReceiveAddress receiveAddress=new ReceiveAddress();
    ReceiveAddress receiveAddressQuery=new ReceiveAddress();
    ReceiveAddressService receiveAddressService=new ReceiveAddressService();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.expressorder_add);
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
        ET_orderName = (EditText) findViewById(R.id.ET_orderName);
        ET_expressCompanyAddress = (EditText) findViewById(R.id.ET_expressCompanyAddress);
        ET_expressCompanyName = (EditText) findViewById(R.id.ET_expressCompanyName);
        ET_remark = (EditText) findViewById(R.id.ET_remark);
        ET_receiveAddressName = findViewById(R.id.receiveAddressName);
        ET_orderPay = (EditText) findViewById(R.id.ET_orderPay);
        ET_receiveCode=findViewById(R.id.ET_receiveCode);
        ET_receiveAddressName.setText("111");
        //点击选择更换地址
        ET_receiveAddressName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnAdd = (Button) findViewById(R.id.BtnAdd);
        //init();//添加默认地址
        /*单击添加快递代拿按钮*/
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /*验证获取代拿任务*/
                    if(ET_orderName.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "代拿任务输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_orderName.setFocusable(true);
                        ET_orderName.requestFocus();
                        return;
                    }
                   order.setOrderName(ET_orderName.getText().toString());
                    /*验证获取快递公司名*/
                    if(ET_expressCompanyName.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "快递公司输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_expressCompanyName.setFocusable(true);
                        ET_expressCompanyName.requestFocus();
                        return;
                    }
                    order.setExpressCompanyName(ET_expressCompanyName.getText().toString());
                    /*验证获取快递公司地址*/
                    if(ET_expressCompanyAddress.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "取货地址输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_expressCompanyAddress.setFocusable(true);
                        ET_expressCompanyAddress.requestFocus();
                        return;
                    }
                    order.setExpressCompanyAdress(ET_expressCompanyAddress.getText().toString());
                    /*验证获取收货备注*/
                    if(ET_remark.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "收货备注输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_remark.setFocusable(true);
                        ET_remark.requestFocus();
                        return;
                    }
                    order.setRemark(ET_remark.getText().toString());
                    /*验证获取收货备注*/
                    if(ET_receiveCode.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "收货地址输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_receiveCode.setFocusable(true);
                        ET_receiveCode.requestFocus();
                        return;
                    }
                 //   order.setReceiveAdressId();
                    /*验证获取送达地址*/
                    if(ET_orderPay.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "酬金输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_orderPay.setFocusable(true);
                        ET_orderPay.requestFocus();
                        return;
                    }
                    order.setOrderPay(ET_orderPay.getText().toString());

                    /*调用业务逻辑层上传快递代拿信息*/
                    ExpressOrderAddActivity.this.setTitle("正在上传快递代拿信息，稍等...");
                    order.setOrderId(declare.getUserId());//添加发布者Id
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    Log.i("vvv","vvvv"+Log.i("vvv","vvvv"));
                    order.setAddTime(df.format(new Date()));//添加发布时间
                    order.setOrderState("待接单");
                    order.setUserPhone("123456789");
                    order.setOrderEvaluate("--");
                    order.setTakeUserId(Integer.parseInt("1"));
                    Log.i("vvv","vvvv");
                    initAdd();
                    Intent intent = getIntent();
                    //intent.setClass(ExpressOrderAddActivity.this,MainUserActivity.class);
                  //  startActivity(intent);
                    //setResult(RESULT_OK,intent);
                    //finish();

                } catch (Exception e) {}
            }
        });

    }


    //获取默认收获地址信息,如果用户地址为空则添加地址
    private  void init() {
    final Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x123){
                ET_receiveAddressName.setText(msg.getData().getString("receiveAddressName"));
            }
            else
                ET_receiveAddressName.setText("请添加收货地址！");
        }
    };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle=new Bundle();
                Message message=new Message();
                try {
                    List<ReceiveAddress> receiveAddressesrList=receiveAddressService.QueryReceiveAdressList(declare.getUserId());
                    if(receiveAddressesrList.size()>0){
                        for (int i = 0; i < receiveAddressesrList.size(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            if(receiveAddressesrList.get(i).getReceiveState().equals("1"))
                            {
                                bundle.putString("receiveAddressName",receiveAddressesrList.get(i).getReceiveAddressName());
                            }
                        }
                    }
                    message.setData(bundle);
                    message.what=0x123;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
    private  void initAdd() {

        new Thread(new Runnable() {
            @Override
            public void run() {
             orderService.AddOrder(order);
            }
        }).start();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
