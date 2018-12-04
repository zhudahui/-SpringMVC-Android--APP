package com.mobileclient.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import com.sun.jna.platform.win32.Netapi32Util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SecondAuthActivity extends Activity {
    private static final String TAG = "uploadImage";
    // 声明登录、取消按钮
    private Button btnUpload;
    // 声明用户名、密码输入框
    private TextView TV_userId,TV_userName;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.secondauth);
        TV_userId = findViewById(R.id.TV_userId);
        TV_userName = findViewById(R.id.TV_userName);
        authFile = findViewById(R.id.authFile);
        //userType = findViewById(R.id.ET_userType);
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);
        back = findViewById(R.id.back_btn);
        title = findViewById(R.id.title);
        btnUpload = findViewById(R.id.btnUpload);
        title.setText("个人信息");
        declare = (Declare) getApplicationContext();

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
