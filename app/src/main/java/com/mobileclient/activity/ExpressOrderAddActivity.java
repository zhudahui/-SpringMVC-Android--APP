package com.mobileclient.activity;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.adapter.ReceiveAddressAdapter;
import com.mobileclient.app.Declare;
import com.mobileclient.domain.Order;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.domain.User;
import com.mobileclient.service.OrderService;
import com.mobileclient.service.ReceiveAddressService;
import com.mobileclient.util.ActivityUtils;

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


    Order order = new Order();
    OrderService orderService = new OrderService();
    ReceiveAddress receiveAddress = new ReceiveAddress();
    ReceiveAddress receiveAddressQuery = new ReceiveAddress();
    ReceiveAddressService receiveAddressService = new ReceiveAddressService();
    User user=new User();
    private int userId;
    private String pay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.expressorder_add);
        ImageView search = (ImageView) this.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("添加快递代拿");
        ImageView back = (ImageView) this.findViewById(R.id.back_btn);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        final Declare declare = (Declare) getApplicationContext();
        pay=declare.getUserMoney();
        userId=declare.getUserId();
        ET_orderName = (EditText) findViewById(R.id.ET_orderName);
        ET_expressCompanyAddress = (EditText) findViewById(R.id.ET_expressCompanyAddress);
        ET_expressCompanyName = (EditText) findViewById(R.id.ET_expressCompanyName);
        ET_remark = (EditText) findViewById(R.id.ET_remark);
        ET_receiveAddressName = findViewById(R.id.receiveAddressName);
        ET_orderPay = (EditText) findViewById(R.id.ET_orderPay);
        ET_receiveCode = findViewById(R.id.ET_receiveCode);
        init(); //获取默认地址，如果没有默认地址，则获取第一个地址,如果还未创建地址，则提示用户添加地址
        //点击选择更换地址
        ET_receiveAddressName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent();
                //传值
                startActivityForResult(new Intent(ExpressOrderAddActivity.this, SecondAddressListActivity.class), 1);
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
                    if (ET_orderName.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "代拿任务输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_orderName.setFocusable(true);
                        ET_orderName.requestFocus();
                        return;
                    }
                    order.setOrderName(ET_orderName.getText().toString());
                    /*验证获取快递公司名*/
                    if (ET_expressCompanyName.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "快递公司输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_expressCompanyName.setFocusable(true);
                        ET_expressCompanyName.requestFocus();
                        return;
                    }
                    order.setExpressCompanyName(ET_expressCompanyName.getText().toString());
                    /*验证获取快递公司地址*/
                    if (ET_expressCompanyAddress.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "取货地址输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_expressCompanyAddress.setFocusable(true);
                        ET_expressCompanyAddress.requestFocus();
                        return;
                    }
                    order.setExpressCompanyAdress(ET_expressCompanyAddress.getText().toString());
                    /*验证获取收货备注*/
                    if (ET_remark.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "收货备注输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_remark.setFocusable(true);
                        ET_remark.requestFocus();
                        return;
                    }
                    order.setRemark(ET_remark.getText().toString());
                    /*验证获取收货备注*/
                    if (ET_receiveCode.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "收货地址输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_receiveCode.setFocusable(true);
                        ET_receiveCode.requestFocus();
                        return;
                    }
                    //   order.setReceiveAdressId();
                    /*验证获取送达地址*/
                    if (ET_orderPay.getText().toString().equals("")) {
                        Toast.makeText(ExpressOrderAddActivity.this, "酬金输入不能为空!", Toast.LENGTH_LONG).show();
                        ET_orderPay.setFocusable(true);
                        ET_orderPay.requestFocus();
                        return;
                    }
                    order.setOrderPay(ET_orderPay.getText().toString());
                    if(Integer.parseInt(order.getOrderPay())>Integer.parseInt(pay)){
                        Toast.makeText(ExpressOrderAddActivity.this, "你的余额不足，请充值!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    /*调用业务逻辑层上传快递代拿信息*/
                    ExpressOrderAddActivity.this.setTitle("正在上传快递代拿信息，稍等...");
                    order.setOrderId(userId);//添加发布者Id
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    order.setAddTime(df.format(new Date()));//添加发布时间
                    order.setOrderState("待接单");
                    order.setUserPhone("123456789");
                    order.setOrderEvaluate("--");
                    order.setTakeUserId(Integer.parseInt("-1"));   //没有快递代取者时，字段默认为-1；
                    Log.i("vvv", "vvvv");
                    initAdd();
                    String money=String.valueOf(Integer.parseInt(pay)-Integer.parseInt(order.getOrderPay()));
                    declare.setUserMoney(money);//  扣除订单金额
                    Intent intent = getIntent();
                    //intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                    //intent.setClass(ExpressOrderAddActivity.this,MainUserActivity.class);
                    //  startActivity(intent);
                    //setResult(RESULT_OK,intent);

                } catch (Exception e) {
                }
            }
        });

    }


    //获取默认收获地址信息,如果用户地址为空则添加地址
    private void init() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x123) {
                    Log.i("ppppppppp","state"+msg.getData().getString("receiveAddressName"));

                    ET_receiveAddressName.setText(msg.getData().getString("receiveAddressName"));   //绑定默认地址
                }

                if (msg.what == 0x122) {
                    ET_receiveAddressName.setText("请添加收货地址！");
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                Message message = new Message();
                int flag = 0;     //判断是否有默认地址
                try {
                   // Log.i("pppppppp","aaa"+declare.getUserId());
                    List<ReceiveAddress> receiveAddressesrList = receiveAddressService.QueryReceiveAdressList(userId);
                    if (receiveAddressesrList.size() > 0) {         //收货地址列表不为空
                        for (int i = 0; i < receiveAddressesrList.size(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            if (receiveAddressesrList.get(i).getReceiveState().equals("1")) {
                                flag = 1;   //有默认地址
                                bundle.putString("receiveAddressName", receiveAddressesrList.get(i).getReceiveAddressName());//获得默认地址

                                Log.i("ppppppppp","address"+receiveAddressesrList.get(i).getReceiveAddressName());
                            }
                        }
                        if (flag == 1) {

                            message.what = 0x123;
                        } else {
                            Log.i("ppppppppp", "state" + receiveAddressesrList.get(1).getReceiveAddressName());
                            //bundle.putString("receiveAddressName", receiveAddressesrList.get(1).getReceiveAddressName());//获得第一个地址
                            message.what = 0x123;
                        }
                    } else {
                        message.what = 0x122;    //收货地址列表为空
                    }
                    message.setData(bundle);

                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void initAdd() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                orderService.AddOrder(order);
            }
        }).start();
    }

    //结果处理函数，当从ReceiveAddressAddActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("ppppppppp",""+data);
            Bundle extras = data.getExtras();
            ET_receiveAddressName.setText(extras.getString("receiveAddressName"));
            order.setReceiveAdressId(extras.getInt("receiveId"));

//
        }
}
