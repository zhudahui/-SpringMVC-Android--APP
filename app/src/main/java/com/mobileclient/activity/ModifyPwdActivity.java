package com.mobileclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;

public class ModifyPwdActivity extends Activity {
    private TextView title,save;
    private EditText orgin_et,id_new_et,id_confirm_et;
    private ImageView search;
    private ImageView back_btn;
    private Declare declare;
    User user=new User();
    UserService userService=new UserService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.modify_pwd);
        declare= (Declare) getApplication();
        orgin_et=findViewById(R.id.id_orgin_et);
        id_new_et=findViewById(R.id.id_new_et);
        id_confirm_et=findViewById(R.id.id_confirm_et);

        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("修改密码");
        back_btn = (ImageView)this.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                finish();
            }

        });
        save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Toast.makeText(ModifyPwdActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                };
                if(orgin_et.getText().toString().equals(declare.getUserPassword())){
                    if(id_new_et.getText().toString().equals("")){
                        Toast.makeText(ModifyPwdActivity.this,"新密码输入不能为空！",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(id_new_et.getText().toString().equals(id_confirm_et.getText().toString())){
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
                            user.setUserPassword(id_new_et.getText().toString());
                            user.setUserReputation(declare.getUserReputation());
                            user.setUserType(declare.getUserType());
                            user.setNickName(declare.getNickName());
                            user.setStudentId(declare.getStudentId());
                            user.setPayPwd(declare.getPayPwd());
                            declare.setUserPassword(id_new_et.getText().toString());
                            user.setUserAuthState(declare.getUserAuthState());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    userService.UpdateUserInfo(user);
                                    Message msg=new Message();
                                    handler.sendMessage(msg);
                                }
                            }).start();
                        }
                        else{
                            Toast.makeText(ModifyPwdActivity.this,"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(ModifyPwdActivity.this,"原密码不正确！",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
