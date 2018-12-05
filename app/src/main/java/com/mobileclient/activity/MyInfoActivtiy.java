package com.mobileclient.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cc.testdemo.Activity;
import com.mobileclient.app.Declare;
import com.mobileclient.app.IdentityImageView;
import com.mobileclient.util.ActivityUtils;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

public class MyInfoActivtiy extends Activity implements View.OnClickListener {
    private IdentityImageView userPhoto; //头像
    private ImageView right;  //进入个人中心
    private TextView tv_NickName;  //用户名
    private ImageView sex;   //性别
    private TextView userId;  //用户Id
    private TextView reward;   //悬赏
    private TextView auth; //认证
    private TextView money;//消费情况
    private TextView express;//快递通，根据快递单号查找快递
    private TextView notice; //通知
    private TextView setting; //设置
    Declare declare;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置当前Activity界面布局
        //去除title
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.profile);
        declare = (Declare)getApplication();
        userPhoto=findViewById(R.id.iiv_userPhoto);
        right=findViewById(R.id.iv_right);
        tv_NickName=findViewById(R.id.tvname);
        sex=findViewById(R.id.iv_sex);
        userId=findViewById(R.id.tv_userId);
        reward=findViewById(R.id.txt_reward);
        auth=findViewById(R.id.txt_auth);
        money=findViewById(R.id.txt_money);
        express=findViewById(R.id.txt_express);
        notice=findViewById(R.id.txt_notice);
        setting=findViewById(R.id.txt_setting);
        //============
        init();

        //============
        userId.setOnClickListener(this);
        reward.setOnClickListener(this);
        auth.setOnClickListener(this);
        money.setOnClickListener(this);
        express.setOnClickListener(this);
        notice.setOnClickListener(this);
        setting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.iv_right:
                // 获取用户名
                String user_name = declare.getUserName();
                intent.setClass(MyInfoActivtiy.this, UserInfoEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_name", user_name);
                intent.putExtras(bundle);
                startActivityForResult(intent,ActivityUtils.EDIT_CODE);
                break;
            case R.id.txt_reward:
                break;
            case R.id.txt_auth:
                String userAuthFile=declare.getUserAuthFile();
                if(userAuthFile.equals("--")) {
                    intent = new Intent(MyInfoActivtiy.this, UserAuthActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(MyInfoActivtiy.this, SecondAuthActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.txt_money:
                break;
            case R.id.txt_express:
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
        if(declare.getUserType().equals("快递员")){   //快递员头像+V
            userPhoto.getSmallCircleImageView().setImageResource(R.drawable.v);
        }
        if(declare.getUserGender().equals("男")){
            sex.setImageResource(R.drawable.ic_sex_male);
        }

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

}
