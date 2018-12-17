package com.mobileclient.pay;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.mobileclient.activity.R;


public class MyInputDialogBuilder {
	private Dialog dialog;
	private Window window;
	
	public MyInputDialogBuilder(Context context) {
		super();
		dialog = new Dialog(context, R.style.myinputpwd_dialog);

	}
	public MyInputDialogBuilder(Context context, boolean cancel) {
		super();
		dialog = new Dialog(context,R.style.myinputpwd_dialog);
		dialog.setCancelable(cancel);

	}
	/**
	 * 设置dialog的显示视图
	 * @param dialogView
	 * @return
	 */
	public MyInputDialogBuilder setContentView(View dialogView){
		dialog.setContentView(dialogView);
		return this;
	}
	/**
	 * 设置dialog的显示位置
	 * @param gravity
	 * @return
	 */
	public MyInputDialogBuilder setGravity(int gravity){
		if(window == null){
			window = dialog.getWindow();
		}
		window.setGravity(gravity);
		return this;
	}
	/**
	 * 设置dialog的宽是全屏的
	 * @return
	 */
	public MyInputDialogBuilder setWidthMatchParent(){
		if(window == null){
			window = dialog.getWindow();
		}
		LayoutParams params = window.getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		return this;
	}
	/**
	 * 设置dialog弹出或消失的动画
	 * @param animStyle
	 * @return
	 */
	public MyInputDialogBuilder setAnimStyle(int animStyle){
		if(window == null){
			window = dialog.getWindow();
		}
		window.setWindowAnimations(animStyle);
		return this;
	}
	/**
	 * 显示dialog
	 */
	public void show(){
		dialog.show();
	}
	/**
	 * 隐藏dialog
	 */
	public void dismiss(){
		dialog.dismiss();
	}
}
