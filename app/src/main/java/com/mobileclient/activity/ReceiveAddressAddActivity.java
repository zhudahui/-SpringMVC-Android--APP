package com.mobileclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.service.ReceiveAddressService;

import java.util.ArrayList;
import java.util.List;


public class ReceiveAddressAddActivity extends Activity {
    private EditText et_receiveName;
    private EditText et_receivePhone;
    private EditText et_receiveAddressName;
    private Button btn_save;
    ReceiveAddressService receiveAddressService=new ReceiveAddressService();
    ReceiveAddress receiveAddress=new ReceiveAddress();
    Declare declare;
    ReceiveAddress queryReceiveAddress;
    Intent intent = getIntent();
    private int userId;
    private ImageView back;
    private TextView title;
    private TextView save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.receive_adress);
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title=findViewById(R.id.title);
        title.setText("添加收货地址");
        save=findViewById(R.id.save);
        save.setVisibility(View.GONE);
        declare = (Declare) getApplicationContext();
        userId=declare.getUserId();
        et_receiveName=findViewById(R.id.et_receiveName);
        et_receivePhone=findViewById(R.id.et_receivePhone);
        et_receiveAddressName=findViewById(R.id.et_receiveAdressName);
        btn_save=findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                Intent intent = new Intent();  //点击返回地址名和地址Id
                setResult(RESULT_OK,intent);
                finish();

            }
        });
    }
    private void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                receiveAddress.setReceiveName(et_receiveName.getText().toString());
                receiveAddress.setReceivePhone(et_receivePhone.getText().toString());
                receiveAddress.setReceiveAddressName(et_receiveAddressName.getText().toString());
                receiveAddress.setUserId(userId);
                receiveAddress.setReceiveState("0");
                Log.i("Address",""+receiveAddress);
                receiveAddressService.AddReceiveAdress(receiveAddress);
            }
        }).start();
    }
}