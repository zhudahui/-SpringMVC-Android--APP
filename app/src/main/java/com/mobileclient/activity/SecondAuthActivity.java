package com.mobileclient.activity;

import java.io.File;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

public class SecondAuthActivity extends Activity {
    private static final String TAG = "uploadImage";
    // 声明登录、取消按钮
    private Button btnUpload;
    // 声明用户名、密码输入框
    private TextView TV_studentId,TV_userName;
    public static final int TO_SELECT_PHOTO = 3;
    User user1=new User();
    User user=new User();
    UserService userService=new UserService();
    Intent intent=new Intent();
    private String picPath = null;
    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;//启动相机标识
    public static final int SELECT_PHOTO = 2;//启动相册标识
    private Bitmap orc_bitmap;//拍照和相册获取图片的Bitmap
    private File outputImagepath;//存储拍完照后的图片
    String imagePath=null;//存储路径
    String reuslt;
    String photo=null;
    private String user_name;
    Declare declare;
    User userInfo=new User();
    private ImageView back,search;
    private TextView title;
    private ImageView authFile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.secondauth);
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(false, SecondAuthActivity.this);
        declare = (Declare) getApplicationContext();
        TV_studentId = findViewById(R.id.tv_studentId);
        TV_userName = findViewById(R.id.tv_userName);
        authFile = findViewById(R.id.authFile);
        //userType = findViewById(R.id.ET_userType);
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);
        back = findViewById(R.id.back_btn);
        title = findViewById(R.id.title);
        btnUpload = findViewById(R.id.btnUpload);
        title.setText("认证信息");
        TV_studentId.setText(String.valueOf(declare.getStudentId()));
        TV_userName.setText(declare.getUserName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] userPhoto_data = null;
                try {
                    // 获取图片数据
                    userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + declare.getUserAuthFile());
                    Log.i("gggggggg","2222"+userPhoto_data);
                    Bundle bundle=new Bundle();
                    Message msg=new Message();
                    bundle.putByteArray("userAuthFile_data",userPhoto_data);
                    msg.what=0x123;
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        declare = (Declare) getApplicationContext();

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    final Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x123) {
                byte[] File = null;
                File=msg.getData().getByteArray("userAuthFile_data");
                Log.i("gggggggg","1111"+File);
                Bitmap iuserPhoto = BitmapFactory.decodeByteArray(File, 0, File.length);
                authFile.setImageBitmap(iuserPhoto);
            }
        }
    };

}
