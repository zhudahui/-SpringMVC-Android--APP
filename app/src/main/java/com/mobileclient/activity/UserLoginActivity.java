package com.mobileclient.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;

public class UserLoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private MyProgressDialog dialog;
    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.VIBRATE
    };

    private static final int PERMISSON_REQUESTCODE = 0;
    private MessageThread messageThread=null;
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    UserService userService = new UserService();
    User user = new User();
    private FloatingActionButton fab;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    // 获取消息线程
    private TextView adminlogin;
    Declare declare;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        declare = (Declare) UserLoginActivity.this.getApplication();
        declare.setIdentify("user");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        dialog = MyProgressDialog.getInstance(this);
        _loginButton = findViewById(R.id.btn_login);
        _passwordText = findViewById(R.id.input_password);
        _emailText = findViewById(R.id.input_email);
        _signupLink = findViewById(R.id.link_signup);
        _emailText.setText(pref.getString("nickName", ""));
        _passwordText.setText(pref.getString("userPwd", ""));
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        adminlogin = findViewById(R.id.adminlogin);
        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        /**
         *
         * 测试网络
         *
         */
        // 开启线程
        messageThread =new UserLoginActivity.MessageThread();
        messageThread.isRunning = true;
        messageThread.start();
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {

            return;
        }

        //_loginButton.setEnabled(false);
        dialog.show();
        ExecutorService e = Executors.newCachedThreadPool();
        e.execute(new Runnable() {
            @Override
            public void run() {
                Declare declare = (Declare) UserLoginActivity.this.getApplication();
                try {
                    user = userService.GetUserInfo(_emailText.getText().toString());  //验证登录名是否存在

                    if (user != null) { //验证用户是否存在
                        Log.i("pppppppp", "cccc" + user.getUserPassword() + "" + _passwordText.getText().toString());
                        if (user.getUserPassword().equals(_passwordText.getText().toString()))   //用户账号存在时验证密码是否正确
                        {
                            //declare.setUserId(Integer.parseInt(et_nickName.getText().toString()));
                            Log.i("pppppppp", "cccc" + declare.getUserId());
                            declare.setUserId(user.getUserId());  //将系统Id存储为全局可用
                            declare.setUserName(user.getUserName());
                            declare.setUserType(user.getUserType());
                            declare.setUserPhoto(user.getUserPhoto());
                            declare.setUserMoney(user.getUserMoney());  //
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
                            declare.setNetWork("网络正确");

                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(2);
                        }
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    Log.i("LoginActivity", e.toString());
                }
            }
        });


//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();

//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:           //账号密码正确时登陆跳转到主页
                    Toast.makeText(UserLoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(UserLoginActivity.this, MainUserActivity.class);
                    editor = pref.edit();
                    editor.putString("nickName", _emailText.getText().toString());
                    editor.putString("userPwd", _passwordText.getText().toString());
                    editor.apply();
                    startActivity(intent);
                    UserLoginActivity.this.finish();
                    dialog.cancel();
                    break;
                case 0:
                    Toast.makeText(UserLoginActivity.this, "账号不存在!", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    break;
                case 2:
                    Toast.makeText(UserLoginActivity.this, "密码不正确，请重新输入！", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    break;
                case 3:
                    declare.setNetWork("网络错误");
                    break;
                case 4:
                   // Toast.makeText(UserLoginActivity.this, "网络已连接！", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }



    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _emailText.setError("输入不能为空");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
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




    /*
     * 从服务器端获取消息
     *
     */
    class MessageThread extends Thread {
        // 设置是否循环推送
        public boolean isRunning = true;

        public void run() {
            while (isRunning) {
// 间隔时间
                // 间隔时间
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process p = runtime.exec("ping -c 2 www.baidu.com");
                    int ret = p.waitFor();
                    System.out.println("查询到的网络访问码是：：" + ret);
                    if (ret != 0) {
                        //网络不通，停止加载M

                        //Toast.makeText(UserLoginActivity.this, "网络已断开", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessage(3);

                    }else{
                        //Toast.makeText(UserLoginActivity.this, "网络已连接", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessage(4);

                    }
                } catch (Exception e) {

                }

                {

                }
            }
        }

    }
}