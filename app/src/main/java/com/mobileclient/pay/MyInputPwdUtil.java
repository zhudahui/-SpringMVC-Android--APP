package com.mobileclient.pay;

import android.content.Context;
import android.view.Gravity;

/**
 * Created by limengjie on 2016/12/9.
 */

public class MyInputPwdUtil {
    private InputPwdView inputView;
    private Context context;
    private InputPwdView.InputPwdListener inputListener;
    private MyInputDialogBuilder myInputDialogBuilder;

    public MyInputPwdUtil(Context tcontext) {
        this.context = tcontext;
        myInputDialogBuilder = new MyInputDialogBuilder(context);
        inputView =     new InputPwdView(context);
        myInputDialogBuilder.setContentView(inputView) // 设置显示视图
                .setWidthMatchParent() // 让dialog宽全屏
                .setGravity(Gravity.BOTTOM); // 让dialog在屏幕最底下
        init();
    }
    public void init(){
        inputView.setListener(new InputPwdView.InputPwdListener() {
            @Override
            public void hide() {
                inputListener.hide();
            }
            @Override
            public void forgetPwd() {
                inputListener.forgetPwd();
            }

            @Override
            public void finishPwd(String pwd) {
                inputListener.finishPwd(pwd);
            }
        });
    }

    public void setListener(InputPwdView.InputPwdListener inputListener) {
        this.inputListener = inputListener;
    }
    public MyInputDialogBuilder getMyInputDialogBuilder() {
        return myInputDialogBuilder;
    }

    public void show() {
        inputView.reSetView();
        myInputDialogBuilder.show();
    }

    public void hide() {
        myInputDialogBuilder.dismiss();
    }


}
