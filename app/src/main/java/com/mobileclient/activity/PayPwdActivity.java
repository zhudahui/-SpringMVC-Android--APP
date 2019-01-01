package com.mobileclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.mobileclient.app.Declare;
import com.mobileclient.app.PsdEditText;
import com.mobileclient.domain.User;

import com.mobileclient.pay.MyInputPwdUtil;
import com.mobileclient.service.UserService;
import com.mobileclient.util.Utils;


public class PayPwdActivity extends Activity {
    private MyInputPwdUtil myInputPwdUtil;
    private String pwd1=null;
    private String pwd2=null,pwd3=null;
    private ImageView savepwd;
    UserService userService=new UserService();
    User user=new User();
    Declare declare;
    private ImageView back;
    private TextView title,tv1,tv2,tv3;
    private View v3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_pwd);
        Utils.setStatusBar(this, false, false);
        Utils.setStatusTextColor(false, PayPwdActivity.this);
        title=findViewById(R.id.title);
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        tv3=findViewById(R.id.tv3);
        v3=findViewById(R.id.v3);
        PsdEditText psdEditText3 = (PsdEditText) findViewById(R.id.ppe_pwd3);
        declare= (Declare) getApplication();
        if (declare.getPayPwd().equals("--")){   //  尚未设置支付密码
            title.setText("设置支付密码");
            tv1.setText("请输入密码");


        }
        else{
            tv1.setText("请输入原密码");
            title.setText("修改支付密码");
            tv2.setText("请输入新密码");
            tv3.setVisibility(View.VISIBLE);
            v3.setVisibility(View.VISIBLE);
            psdEditText3.setVisibility(View.VISIBLE);
            tv3.setText("请再次输入新密码");

        }

        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        PsdEditText psdEditText1 = (PsdEditText) findViewById(R.id.ppe_pwd1);
        psdEditText1.initStyle(R.drawable.edit_bg, 6, 0.33f, R.color.gary, R.color.gray, 20);
        psdEditText1.setOnTextFinishListener(new PsdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                if(declare.getPayPwd().equals("--")) {
                    pwd1 = str;
                }
                else {
                    if(str.equals(declare.getPayPwd())){
                        pwd1 = str;
                    }
                    else{
                        Toast.makeText(PayPwdActivity.this,"原密码输入错误！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
        PsdEditText psdEditText2 = (PsdEditText) findViewById(R.id.ppe_pwd2);
        psdEditText2.initStyle(R.drawable.edit_bg, 6, 0.33f, R.color.gary, R.color.gray, 20);
        psdEditText2.setOnTextFinishListener(new PsdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str1) {
                if (declare.getPayPwd().equals("--")) {
                    if (str1.equals(pwd1)) {
                        pwd2 = str1;
                    } else {
                        Toast.makeText(PayPwdActivity.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                     pwd2=str1;

                }
            }
        });
        psdEditText3.initStyle(R.drawable.edit_bg, 6, 0.33f, R.color.gary, R.color.gray, 20);
        psdEditText3.setOnTextFinishListener(new PsdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str3) {
                     pwd3=str3;
                    if (str3.equals(pwd1)) {

                    } else {
                        Toast.makeText(PayPwdActivity.this, "两次新密码输入不一致！", Toast.LENGTH_SHORT).show();
                    }
                }

        });
        savepwd=findViewById(R.id.save);
        savepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if(msg.what==0x122) {
                            Toast.makeText(PayPwdActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(PayPwdActivity.this, "密码设置成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                };
                if(declare.getPayPwd().equals("--")) {   //设置密码
                    if (pwd1==null){
                        Toast.makeText(PayPwdActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pwd2==null){
                        Toast.makeText(PayPwdActivity.this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pwd1.equals(pwd2)) {
                        user.setUserId(declare.getUserId());
                        user.setUserName(declare.getUserName());
                        user.setUserAuthFile(declare.getUserAuthFile());
                        user.setUserPhoto(declare.getUserPhoto());
                        user.setUserPassword(declare.getUserPassword());
                        user.setUserMoney(declare.getUserMoney());
                        user.setRegTime(declare.getRegTime());
                        user.setUserEmail(declare.getUserEmail());
                        user.setUserGender(declare.getUserGender());
                        user.setUserPhone(declare.getUserPhone());
                        user.setUserPassword(declare.getUserPassword());
                        user.setUserReputation(declare.getUserReputation());
                        user.setUserType(declare.getUserType());
                        user.setNickName(declare.getNickName());
                        user.setStudentId(declare.getStudentId());
                        user.setPayPwd(pwd1);
                        user.setUserAuthState(declare.getUserAuthState());
                        declare.setPayPwd(pwd1);  //支付密码全局可用
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                userService.UpdateUserInfo(user);
                                Message msg=new Message();
                                msg.what=0x123;
                                handler.sendMessage(msg);
                            }
                        }).start();
                    } else {
                        Toast.makeText(PayPwdActivity.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                    }
                }else{      //修改密码
                    if (pwd1==null){
                        Toast.makeText(PayPwdActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pwd2==null){
                        Toast.makeText(PayPwdActivity.this, "新密码不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pwd3==null){
                        Toast.makeText(PayPwdActivity.this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pwd2.equals(pwd3)) {
                        user.setUserId(declare.getUserId());
                        user.setUserName(declare.getUserName());
                        user.setUserAuthFile(declare.getUserAuthFile());
                        user.setUserPhoto(declare.getUserPhoto());
                        user.setUserPassword(declare.getUserPassword());
                        user.setUserMoney(declare.getUserMoney());
                        user.setRegTime(declare.getRegTime());
                        user.setUserEmail(declare.getUserEmail());
                        user.setUserGender(declare.getUserGender());
                        user.setUserPhone(declare.getUserPhone());
                        user.setUserPassword(declare.getUserPassword());
                        user.setUserReputation(declare.getUserReputation());
                        user.setUserType(declare.getUserType());
                        user.setNickName(declare.getNickName());
                        user.setStudentId(declare.getStudentId());
                        user.setPayPwd(pwd1);
                        user.setUserAuthState(declare.getUserAuthState());
                        declare.setPayPwd(pwd1);  //支付密码全局可用
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                userService.UpdateUserInfo(user);
                                Message msg=new Message();
                                msg.what=0x122;
                                handler.sendMessage(msg);
                            }
                        }).start();
                    } else {
                        Toast.makeText(PayPwdActivity.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
