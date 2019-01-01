package com.mobileclient.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cc.testdemo.UserInfoListActivity;
import com.mobileclient.app.Declare;
import com.mobileclient.domain.UserInfo;


public class AdminLoginActivity extends AppCompatActivity {
    private FloatingActionButton fab;;
    private CardView cvAdd;
    private EditText adminName,adminPassword;
    private Button login;
    private Declare declare;
    private TextView userlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_login);

        userlogin=findViewById(R.id.userlogin);
        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoginActivity.this,UserLoginActivity.class);
                startActivity(intent);
            }
        });
        adminName=findViewById(R.id.et_adminName);
        adminPassword=findViewById(R.id.et_adminPassword);
        login=findViewById(R.id.bt_go);
        declare = (Declare)getApplication();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminName.getText().toString().equals("a")&&adminPassword.getText().toString().equals("a")){
                    declare.setIdentify("admin");
                    Intent intent=new Intent();
                    intent.setClass(AdminLoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });


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
