package com.mobileclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.mobileclient.app.Declare;

public class BaseActivity extends AppCompatActivity {


    public Declare application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (Declare) getApplication();
        startAD();
    }

    private Handler handler = new Handler();
    private long time = 1000 * 5;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(runnable);
                break;
            case MotionEvent.ACTION_UP:
                startAD();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //PrefUtils.setBoolean(BaseActivity.this, "isLogin", false);
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder.setTitle("温馨提示")
                    .setCancelable(false)
                    .setMessage("当前登录已失效，请重新登录")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setClass(BaseActivity.this, LoginActivity.class);
                            startActivity(intent);
                           // I//MMessage.stop();
                            application.exit();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };

    public void startAD() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, time);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
