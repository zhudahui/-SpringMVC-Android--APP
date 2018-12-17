package com.mobileclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.service.ReceiveAddressService;

public class ReceiveAddressEditActivity extends Activity {
    private EditText ET_receiveName;
    private EditText ET_reivePhone;
    private EditText ET_receiveAddressName;
    private Switch receiveState;
    private TextView save;
    private Declare declare;
    private Button btnDelete;
    private ImageView back;
    ReceiveAddress receiveAddress=new ReceiveAddress();
    ReceiveAddress receiveAddress1=new ReceiveAddress();
    ReceiveAddressService receiveAddressService=new ReceiveAddressService();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置当前Activity界面布局
        setContentView(R.layout.receiveaddress_edit);
        save=findViewById(R.id.save);
        declare = (Declare) getApplicationContext();
        ET_receiveAddressName=findViewById(R.id.et_receiveAdressName);
        ET_receiveName=findViewById(R.id.et_receiveName);
        ET_reivePhone=findViewById(R.id.et_receivePhone);
        receiveState=findViewById(R.id.receiveState);
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDelete=findViewById(R.id.btnDelete);

        Bundle extras = this.getIntent().getExtras();
        ET_receiveAddressName.setText(extras.getString("receiveAddressName"));
        ET_receiveName.setText(extras.getString("receiveName"));
        ET_reivePhone.setText(extras.getString("receivePhone"));
        String state=extras.getString("receiveState");
        final int receiveId=extras.getInt("receiveId");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        receiveAddressService.DeleteReceiveAddress(receiveId);
                        Intent intent=new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }).start();
            }
        });
        if(state.equals("1"))
            receiveState.setChecked(true);
            receiveState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    receiveAddress.setReceiveState("1");
                    receiveAddress1.setReceiveAddressName(declare.getReceiveAddressName());
                    receiveAddress1.setReceiveName(declare.getReceiveName());
                    receiveAddress1.setReceiveId(declare.getReceiveId());
                    receiveAddress1.setUserId(declare.getUserId());
                    receiveAddress1.setReceiveState("0");
                    receiveAddress1.setReceivePhone(declare.getReceivePhone());
                }else {
                    //Todo
                    receiveAddress.setReceiveState("0");
                }
            }
        });
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Intent intent = new Intent();  //点击返回地址名和地址Id
                setResult(RESULT_OK,intent);
                finish();
            }
        };

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (declare.getReceiveId() != -1 && receiveId != declare.getReceiveId()) {   //有默认地址，且换另一个默认地址
                    receiveAddress.setReceiveName(ET_receiveName.getText().toString());
                    receiveAddress.setReceivePhone(ET_reivePhone.getText().toString());
                    receiveAddress.setReceiveAddressName(ET_receiveAddressName.getText().toString());
                    receiveAddress.setUserId(declare.getUserId());
                    receiveAddress.setReceiveId(receiveId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            receiveAddressService.UpdateReceiveAddress(receiveAddress1);
                            receiveAddressService.UpdateReceiveAddress(receiveAddress);
                            Message msg=new Message();
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
                if (declare.getReceiveId() == -1) {   // 无默认地址
                    receiveAddress.setReceiveName(ET_receiveName.getText().toString());
                    receiveAddress.setReceivePhone(ET_reivePhone.getText().toString());
                    receiveAddress.setReceiveAddressName(ET_receiveAddressName.getText().toString());
                    receiveAddress.setUserId(declare.getUserId());
                    receiveAddress.setReceiveId(receiveId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            receiveAddressService.UpdateReceiveAddress(receiveAddress);
                            Message msg=new Message();
                            handler.sendMessage(msg);
                        }
                    }).start();
                }



            }

        });

    }

}
