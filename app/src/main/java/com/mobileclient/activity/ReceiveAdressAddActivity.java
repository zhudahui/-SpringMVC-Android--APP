package com.mobileclient.activity;

import android.app.Activity;
import android.os.Bundle;;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.service.ReceiveAddressService;

import java.util.ArrayList;
import java.util.List;


public class ReceiveAdressAddActivity extends Activity {
    private EditText et_receiveName;
    private EditText et_receivePhone;
    private EditText et_receiveAdressName;
    private Button btn_save;
    ReceiveAddressService receiveAdressService=new ReceiveAddressService();
    ReceiveAddress receiveAdress=new ReceiveAddress();
    Declare declare=new Declare();
    ReceiveAddress queryReceiveAdress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.receive_adress);
        et_receiveName=findViewById(R.id.et_receiveName);
        et_receivePhone=findViewById(R.id.et_receivePhone);
        et_receiveAdressName=findViewById(R.id.et_receiveAdressName);
        btn_save=findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }
    private void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                receiveAdress.setReceiveName(et_receiveName.getText().toString());
                receiveAdress.setReceivePhone(et_receivePhone.getText().toString());
                receiveAdress.setReceiveAddressName(et_receiveAdressName.getText().toString());
                receiveAdress.setUserId(declare.getUserId());
                List<ReceiveAddress> receiveAdressesList = new ArrayList<ReceiveAddress>();
                try {
                    receiveAdressesList=receiveAdressService.QueryReceiveAdress(queryReceiveAdress);
                    receiveAdress.setReceiveId(receiveAdressesList.size()+1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                receiveAdress.setReceiveState("1");
                receiveAdressService.AddReceiveAdress(receiveAdress);
            }
        }).start();
    }
}