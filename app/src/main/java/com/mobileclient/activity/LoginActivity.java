package com.mobileclient.activity;

import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


/**
 *
 * @author
 *登录
 */
public class LoginActivity extends Activity {
    private TextView register;
    private Button btn_login;
    private EditText et_nickName;
    private EditText et_userPwd;
    private MyProgressDialog dialog;
    UserService userService=new UserService();
    User user=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        dialog = MyProgressDialog.getInstance(this);
        et_nickName= findViewById(R.id.et_nickName);    //用户学号
        et_userPwd = findViewById(R.id.et_userPwd);  //用户密码
        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        Declare declare = (Declare)LoginActivity.this.getApplication();

         declare.setIdentify("user");



        btn_login = (Button) findViewById(R.id.btn_login);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {// 捕捉返回键
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            setResult(RESULT_CANCELED);
            finish();
        }
        return true;
    }
}
