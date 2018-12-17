package com.mobileclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mobileclient.activity.myorder.ExpressTakeMyListActivity;
import com.mobileclient.activity.takeOrder.TakeOrderListActivity;
import com.mobileclient.app.Declare;
import com.mobileclient.app.IdentityImageView;
import com.mobileclient.util.ActivityUtils;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import me.wcy.express.activity.SearchActivity;

public class MyInfoActivtiy extends Activity implements View.OnClickListener {
    private IdentityImageView userPhoto; //头像
    private ImageView right;  //进入个人中心
    private TextView tv_NickName;  //用户名
    private ImageView sex;   //性别
    private TextView userId;  //用户Id
    private TextView myorder;   //悬赏
    private TextView auth; //认证
    private TextView address;//地址管理
    private TextView express;//快递通，根据快递单号查找快递
    private TextView notice; //通知
    private TextView setting; //设置
    private TextView payPwd; //我的代取
    private TextView title;
    Declare declare;
    private RelativeLayout view_user1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置当前Activity界面布局
        //去除title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //去掉Activity上面的状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.profile);
        declare = (Declare)getApplication();
        title=findViewById(R.id.title);
        title.setText("我的");
        userPhoto=findViewById(R.id.iiv_userPhoto);
        right=findViewById(R.id.iv_right);
        tv_NickName=findViewById(R.id.tvname);
        sex=findViewById(R.id.iv_sex);
        userId=findViewById(R.id.tv_userId);
        myorder=findViewById(R.id.txt_modPwd);
        auth=findViewById(R.id.txt_auth);
        address=findViewById(R.id.txt_address);
        express=findViewById(R.id.txt_express);
        notice=findViewById(R.id.txt_notice);
        setting=findViewById(R.id.txt_setting);
        payPwd=findViewById(R.id.txt_payPwd);
        view_user1=findViewById(R.id.view_user);
        //============
        init();

        //============
        right.setOnClickListener(this);
        userId.setOnClickListener(this);
        myorder.setOnClickListener(this);
        auth.setOnClickListener(this);
        address.setOnClickListener(this);
        express.setOnClickListener(this);
        notice.setOnClickListener(this);
        setting.setOnClickListener(this);
        payPwd.setOnClickListener(this);
        view_user1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.view_user:
                // 获取用户名
                String user_name = declare.getUserName();
                intent.setClass(MyInfoActivtiy.this, UserInfoEditActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("user_name", user_name);
                //intent.putExtras(bundle);
                startActivityForResult(intent,ActivityUtils.EDIT_CODE);
                break;
            case R.id.txt_modPwd:
                intent = new Intent(MyInfoActivtiy.this, ModifyPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_payPwd:
                    intent = new Intent(MyInfoActivtiy.this, PayPwdActivity.class);
                    startActivity(intent);

                break;
            case R.id.txt_auth:
                String userAuthState=declare.getUserAuthState();
                if(userAuthState.equals("未认证")) {

                    intent = new Intent(MyInfoActivtiy.this, UserAuthActivity.class);
                    startActivityForResult(intent,ActivityUtils.UPDATE_CODE);


                }else if(userAuthState.equals("待认证")){
                    intent = new Intent(MyInfoActivtiy.this, SecondAuthActivity.class);
                    startActivity(intent);
                }
                else{   //已认证
                    Toast.makeText(MyInfoActivtiy.this,"您已认证",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_address:
                intent = new Intent(MyInfoActivtiy.this, ReceiveAddressListActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_express:
                intent = new Intent(MyInfoActivtiy.this,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_notice:
                intent  = new Intent(MyInfoActivtiy.this, NoticeListActivity.class);
                startActivity(intent);
                break;
            case R.id. txt_setting:
                break;
        }
    }
    private void init(){


        userId.setText(String.valueOf(declare.getUserId()));
        tv_NickName.setText(declare.getNickName());
        if(declare.getUserType().equals("快递员")){   //快递员头像+V
            userPhoto.getSmallCircleImageView().setImageResource(R.drawable.xueli);
        }
        if(declare.getUserGender().equals("男")){
            sex.setImageResource(R.drawable.nan);
        }
        else
            sex.setImageResource(R.drawable.nv);

        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x123) {
                    byte[] Photo = null;
                    Photo=msg.getData().getByteArray("userPhoto_data");
                    Log.i("gggggggg","1111"+Photo);
                    Bitmap iuserPhoto = BitmapFactory.decodeByteArray(Photo, 0, Photo.length);
                    userPhoto.getBigCircleImageView().setImageBitmap(iuserPhoto);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] userPhoto_data = null;
                try {
                    // 获取图片数据
                    userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + declare.getUserPhoto());
                    Log.i("gggggggg","2222"+userPhoto_data);
                    Bundle bundle=new Bundle();
                    Message msg=new Message();
                    bundle.putByteArray("userPhoto_data",userPhoto_data);
                    msg.what=0x123;
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){

        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
            Log.i("mmmmm",""+requestCode+resultCode);
           init();
        }
        if(requestCode == ActivityUtils.UPDATE_CODE && resultCode == RESULT_OK) {


        }
    }

}
