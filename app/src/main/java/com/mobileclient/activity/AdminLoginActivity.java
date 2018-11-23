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
import com.mobileclient.util.HttpUtil;


/**
 *
 * @author
 *登录
 */
public class AdminLoginActivity extends Activity {
    private TextView title,register;
    private Button btn_login;
    private ImageView back_btn;
    private EditText et_username;
    private EditText et_pwd;
    //用户身份选择下拉框
    private Spinner Spinner_identify;
    private ArrayAdapter<String> identify_adapter;
    private static  String[] identify_ShowText  = null;

    private ImageView search;
    private MyProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin_login);
        dialog = MyProgressDialog.getInstance(this);
        //title = (TextView) findViewById(R.id.title);
        et_username = (EditText)findViewById(R.id.et_username);
        et_pwd = (EditText)findViewById(R.id.et_pwd);
        //title.setText("用户登录");
        //	search = (ImageView)findViewById(R.id.search);
//		search.setVisibility(View.GONE);
        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(AdminLoginActivity.this,UserInfoRegisterActivity.class);
                startActivity(intent);
            }
        });
        //back_btn = (ImageView) findViewById(R.id.back_btn);
        //back_btn.setOnClickListener(new OnClickListener(){
        //	@Override
        //	public void onClick(View arg0) {
        //		finish();
        //	}
        //	});

       // this.Spinner_identify = (Spinner) findViewById(R.id.Spinner_identify);
       // identify_ShowText = new String[] {"用户或快递员","管理员"};
        // 将可选内容与ArrayAdapter连接起来
      //  identify_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, identify_ShowText);
        // 设置下拉列表的风格
      //  identify_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将adapter 添加到spinner中
     //   Spinner_identify.setAdapter(identify_adapter);
        // 添加事件Spinner事件监听

                Declare declare = (Declare)AdminLoginActivity.this.getApplication();

                        declare.setIdentify("admin");


        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if("".equals(et_username.getText().toString())){
                    Toast.makeText(AdminLoginActivity.this, "用户名必填", Toast.LENGTH_LONG).show();
                    return;
                }
                if("".equals(et_pwd.getText().toString())){
                    Toast.makeText(AdminLoginActivity.this, "密码必填", Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.show();
                ExecutorService e = Executors.newCachedThreadPool();
                e.execute(new Runnable() {
                    @Override
                    public void run() {
                        Declare declare = (Declare) AdminLoginActivity.this.getApplication();
                        try {
                            String url = HttpUtil.BASE_URL
                                    + "LoginServlet?userName="
                                    + URLEncoder.encode(
                                    URLEncoder.encode(et_username.getText().toString(), "UTF-8"), "UTF-8")+"&password="
                                    + URLEncoder.encode(
                                    URLEncoder.encode(et_pwd.getText().toString(), "UTF-8"), "UTF-8")+ "&identify=" + declare.getIdentify();
                            // 查询返回结果
                            String result = HttpUtil.queryStringForPost(url);
                            System.out.println("=========================  "+result);
                            if(!result.equals("0")){
                                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@");
                                declare.setUserName(et_username.getText().toString());
                                handler.sendEmptyMessage(1);
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
                case 1:
                    Toast.makeText(AdminLoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    Declare declare = (Declare) AdminLoginActivity.this.getApplication();
                    if(declare.getIdentify().equals("admin"))
                        intent.setClass(AdminLoginActivity.this,MainActivity.class);
                    else
                        intent.setClass(AdminLoginActivity.this,IndexActivity.class);

                    startActivity(intent);
                    AdminLoginActivity.this.finish();
                    break;
                case 0:
                    Toast.makeText(AdminLoginActivity.this, "登录失败，用户名密码不对或未审核", Toast.LENGTH_LONG).show();
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
