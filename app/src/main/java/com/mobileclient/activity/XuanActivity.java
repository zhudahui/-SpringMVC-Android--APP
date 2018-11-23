package com.mobileclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class XuanActivity extends Activity {
 private ImageView img1;
 private  ImageView img2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_xuan);
        img1=findViewById(R.id.user);
        img2=findViewById(R.id.admin);
        img1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(XuanActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        img2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(XuanActivity.this,AdminLoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
