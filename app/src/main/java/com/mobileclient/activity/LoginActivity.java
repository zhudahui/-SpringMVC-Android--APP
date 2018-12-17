package com.mobileclient.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;
import com.mobileclient.util.HttpUtil;

import butterknife.OnClick;


/**
 *
 * @author
 *登录
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private TextView register;
    private Button btn_login;
    private EditText et_nickName;
    private EditText et_userPwd;
    private MyProgressDialog dialog;
    private Button btnGo;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
    };

    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    UserService userService=new UserService();
    User user=new User();
    private FloatingActionButton fab;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        dialog = MyProgressDialog.getInstance(this);
        et_nickName= findViewById(R.id.et_nickName);    //用户学号
        et_userPwd = findViewById(R.id.et_userPwd);  //用户密码
        register = (TextView)findViewById(R.id.register);
        et_nickName.setText(pref.getString("nickName",""));
        et_userPwd.setText(pref.getString("userPwd",""));
        fab=findViewById(R.id.fab);
        register.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        Declare declare = (Declare)LoginActivity.this.getApplication();

         declare.setIdentify("user");
         fab.setOnClickListener(this);

        btn_login = (Button) findViewById(R.id.bt_go);
        btn_login.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if("".equals(et_nickName.getText().toString())){
                    Toast.makeText(LoginActivity.this, "用户名必填", Toast.LENGTH_SHORT).show();
                    return;
                }
                if("".equals(et_userPwd.getText().toString())){
                   Toast.makeText(LoginActivity.this, "密码必填", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();
                ExecutorService e = Executors.newCachedThreadPool();
                e.execute(new Runnable() {
                    @Override
                    public void run() {
                        Declare declare = (Declare) LoginActivity.this.getApplication();
                        try {
                            user=userService.GetUserInfo(et_nickName.getText().toString());  //验证登录名是否存在

                            if(user!=null){ //验证用户是否存在
                                Log.i("pppppppp","cccc"+user.getUserPassword()+""+et_userPwd.getText().toString());
                                if(user.getUserPassword().equals(et_userPwd.getText().toString()))   //用户账号存在时验证密码是否正确
                                {
                                    //declare.setUserId(Integer.parseInt(et_nickName.getText().toString()));
                                    Log.i("pppppppp","cccc"+declare.getUserId());
                                    declare.setUserId(user.getUserId());  //将系统Id存储为全局可用
                                    declare.setUserName(user.getUserName());
                                    declare.setUserType(user.getUserType());
                                    declare.setUserPhoto(user.getUserPhoto());
                                    declare.setUserMoney(user.getUserMoney());
                                    declare.setUserGender(user.getUserGender());
                                    declare.setRegTime(user.getRegTime());
                                    declare.setUserEmail(user.getUserEmail());
                                    declare.setUserReputation(user.getUserReputation());
                                    declare.setUserAuthFile(user.getUserAuthFile());
                                    declare.setUserPhone(user.getUserPhone());
                                    declare.setUserPassword(user.getUserPassword());
                                    declare.setNickName(user.getNickName());
                                    declare.setStudentId(user.getStudentId());
                                    declare.setUserAuthState(user.getUserAuthState());
                                    declare.setPayPwd(user.getPayPwd());

                                    declare.setReceiveId(-1);
                                    declare.setReceiveName("--");
                                    declare.setReceiveAddressName("--");
                                    declare.setReceiveUserId(-1);
                                    declare.setReceivePhone("--");

                                    handler.sendEmptyMessage(1);
                                }
                                else{
                                    handler.sendEmptyMessage(2);
                                }
                            }else{
                                handler.sendEmptyMessage(0);
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.i("LoginActivity",e.toString());
                        }
                    }
                });
            }

        });

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:           //账号密码正确时登陆跳转到主页
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,MainUserActivity.class);
                    editor=pref.edit();
                    editor.putString("nickName",et_nickName.getText().toString());
                    editor.putString("userPwd",et_userPwd.getText().toString());
                    editor.apply();
                    startActivity(intent);
                    LoginActivity.this.finish();
                    break;
                case 0:
                   Toast.makeText(LoginActivity.this, "账号不存在!", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "密码不正确，请重新输入！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            dialog.cancel();
        }
    };

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, AdminLoginActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, AdminLoginActivity.class));
                }
                break;
            case R.id.bt_go:
                Explode explode = new Explode();
                explode.setDuration(500);
                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
                //Intent i2 = new Intent(this,LoginSuccessActivity.class);
                //startActivity(i2, oc2.toBundle());
                break;
        }
    }

    //----------以下动态获取权限---------
    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
    }


    /**
     * 检查权限
     *
     * @param
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        //获取权限列表
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            //list.toarray将集合转化为数组
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }


    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        //for (循环变量类型 循环变量名称 : 要被遍历的对象)
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {      //没有授权
                showMissingPermissionDialog();              //显示提示信息
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("是否开启定位");

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("同意",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }


    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}


