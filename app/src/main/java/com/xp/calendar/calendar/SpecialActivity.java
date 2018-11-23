package com.xp.calendar.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobileclient.activity.R;

public class SpecialActivity extends Activity {
private String data;
private TextView text,text2;
private LinearLayout ba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_special);
        text=findViewById(R.id.text1);
        text2=findViewById(R.id.title);
        ba=findViewById(R.id.ba);
        text2.setText("今日安排");
        Intent intent = getIntent();
        if (intent != null) {

            data=  intent .getStringExtra("data");

            Log.i("22222222222222222222", String.valueOf(data));

           if(data.equals("20180606"))
           {
             // text.setText("今天是计算机科学与软件学院计算机专业2018届毕业生答辩！预祝各位同学答辩顺利");
               ba.setBackgroundResource(R.drawable.bizhi_meitu);
           }
        }
        ImageView back = (ImageView) this.findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

}
