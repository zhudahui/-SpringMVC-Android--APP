package com.mobileclient.activity;
import com.mobileclient.app.Declare;
import com.mobileclient.domain.Order;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.domain.User;
import com.mobileclient.pay.MyInputPwdUtil;
import com.mobileclient.service.OrderService;
import com.mobileclient.service.ReceiveAddressService;
import com.mobileclient.service.UserService;
import com.mobileclient.util.ActivityUtils;

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

public class AdminExpressDetailActivity extends Activity {
    // 声明返回按钮
    private Button btnReturn;
    // 声明订单名
    private EditText tx_orderName;
    // 声明发布者
    private EditText tx_nickName;
    // 声明物流公司
    private EditText tx_expressCompanyName;
    // 声明取货地址
    private EditText tx_expressCompanyAddress;
    // 声明收货地址
    private EditText tx_receiveAddressName;
    // 声明收货电话
    private EditText tx_receivePhone;
    // 声明收货备注
    private EditText tx_remark;
    // 声明酬金
    private EditText tx_orderPay;
    // 声明订单状态
    private EditText tx_orderState;
    // 声明取货码
    private EditText tx_receiveCode;
    // 声明发布时间控件
    private EditText tx_addTime;
    //声明收货吗 收货电话
    private EditText tx_orderId,tx_takeUserId,tx_receiveName;

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
    private ImageView ci_userPhoto;
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
    String tt_receiveCode; //暂记住收货码
    Float t;
    /**
     * 模拟支付
     */
    private MyInputPwdUtil myInputPwdUtil;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.admin_expressdetail);
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
//        TV_receiveCode=findViewById(R.id.TV_receiveCode);
//        TV_receivePhone=findViewById(R.id.TV_receivePhone);
        tx_orderName =  findViewById(R.id.orderName);
        ci_userPhoto = findViewById(R.id.userPhoto);
        tx_nickName =  findViewById(R.id.nickName);
        tx_expressCompanyName = findViewById(R.id.expressCompanyName);
        tx_expressCompanyAddress = findViewById(R.id.expressCompanyAddress);
        tx_receiveAddressName = findViewById(R.id.receiveAddress);
        tx_receivePhone =  findViewById(R.id.receivePhone);    //收货电话
        tx_remark = findViewById(R.id.remark);
        tx_orderPay = findViewById(R.id.orderPay);
        tx_orderState = findViewById(R.id.orderState);
        tx_receiveCode = findViewById(R.id.receiveCode);    //收货吗
        tx_addTime = findViewById(R.id.addTime);
        btnGetOrder = (Button) findViewById(R.id.btnGetOrder);
        mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        tx_takeUserId=findViewById(R.id.takeUserId);
        tx_orderId=findViewById(R.id.orderId);
        tx_receiveName=findViewById(R.id.receiveName);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                t=rating;
                order.setScore(String.valueOf(t));
                Toast.makeText(AdminExpressDetailActivity.this, "评分星级=" + rating, Toast.LENGTH_SHORT).show();
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
       // btnViewOrder = (Button) findViewById(R.id.btnViewOrder);
        final Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            orderId = extras.getInt("orderId");//订单发布者Id
            tx_orderId.setText(String.valueOf(orderId));
            Log.i("34566", "" + extras.getString("orderName"));
            tx_orderName.setText(extras.getString("orderName"));
            byte[] photo = extras.getByteArray("photo");
            Bitmap userPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            ci_userPhoto.setImageBitmap(userPhoto);
            tx_nickName.setText(extras.getString("nickName"));
            tx_expressCompanyName.setText(extras.getString("expressCompanyName"));
            tx_expressCompanyAddress.setText(extras.getString("expressCompanyAddress"));
            tx_receiveAddressName.setText(extras.getString("receiveAddressName"));
            tx_receivePhone.setText(extras.getString("receivePhone"));
            tx_remark.setText(extras.getString("remark"));
            tx_orderPay.setText(extras.getString("orderPay"));
            tx_orderState.setText(extras.getString("orderState"));
            //tx_receiveCode.setText(extras.getString("receiveCode"));
            tt_receiveCode=extras.getString("receiveCode");
            tx_receiveCode.setText(tt_receiveCode);
            tx_addTime.setText(extras.getString("addTime"));
            tx_takeUserId.setText(String.valueOf(extras.getInt("takeUserId")));    //获取代取者Id
             evaluate.setText(extras.getString("evaluate"));
             if(extras.getString("score").equals("--"))
                 order.setScore("--");
             else
                 mRatingBar.setRating(Float.parseFloat(extras.getString("score")));
             tx_receiveName.setText(extras.getString("receiveName"));
            }
              //17




        // declare = (Declare) this.getApplication();
        //Id = declare.getUserId();

        btnGetOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setUserId(extras.getInt("userId"));
                order.setTakeUserId(Integer.parseInt(tx_takeUserId.getText().toString()));     //1
                order.setOrderId(Integer.parseInt(tx_orderId.getText().toString()));      //2
                order.setOrderName(tx_orderName.getText().toString());   //3
                //Log.i("zhu2222", "" + extras.getInt("userId"));
               // order.setUserId();   //4
                order.setExpressCompanyName(tx_expressCompanyName.getText().toString());   //5
                order.setExpressCompanyAdress(tx_expressCompanyAddress.getText().toString());  //6
                order.setReceiveAdressId(extras.getInt("receiveAddressId"));   //7
                order.setReceiveName(tx_receiveName.getText().toString());   //8
                order.setReceivePhone(tx_receivePhone.getText().toString());   //9
                order.setReceiveState(tx_orderState.getText().toString());    //10
                order.setReceiveAddressName(tx_receiveAddressName.getText().toString());   //11
                order.setAddTime(tx_addTime.getText().toString());  //12
                order.setOrderState(tx_orderState.getText().toString());   //13
                order.setOrderPay(tx_orderPay.getText().toString());   //14
                order.setRemark(tx_remark.getText().toString());   //15
                order.setReceiveCode(tx_receiveCode.getText().toString());  //16
                order.setOrderEvaluate(evaluate.getText().toString());  //17
                order.setOrderPic(extras.getString("orderPic"));

                showDialog();
                }


        });



    }

    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("是否修改？");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final Handler handler1 = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                if (msg.what == 0x120) {
                                    Toast.makeText(AdminExpressDetailActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        };

                        new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    orderService.UpdateOrder(order);
                                    Message msg=new Message();
                                    msg.what=0x120;
                                    handler1.sendMessage(msg);
                                }
                            }).start();

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


    //结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            if(extras != null){

            }

        }
        if(requestCode==1001 && resultCode==RESULT_OK){
            tx_orderState.setText("交易结束");
            btnGetOrder.setVisibility(View.VISIBLE);
            evaluate.setVisibility(View.VISIBLE);
            mRatingBar.setVisibility(View.VISIBLE);
            btnGetOrder.setText("评价");
            flag = 4;

        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {

        }

    }
    /***
     *
     * 检测订单状态
     *
     *
     */

}