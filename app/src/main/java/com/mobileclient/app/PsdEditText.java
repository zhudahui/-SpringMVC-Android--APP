package com.mobileclient.app;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by zr on 2018/7/2.
 */

public class PsdEditText extends RelativeLayout {
    private Context context;
    private EditText editText;
    private int pwdlength = 6;
    private LinearLayout linearLayout;
    private TextView[] textViews;
    private OnTextFinishListener onTextFinishListener;

    public PsdEditText(Context context) {
        this(context, null);
    }

    public PsdEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PsdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    /**
     * @param bgdrawable 背景drawable
     * @param pwdlength 密码长度
     * @param splilinewidth 分割线宽度
     * @param splilinecolor 分割线颜色
     * @param pwdcolor 密码字体颜色
     * @param pwdsize 密码字体大小
     */
    public void initStyle(int bgdrawable, int pwdlength, float splilinewidth, int splilinecolor, int pwdcolor, int pwdsize)
    {
        this.pwdlength = pwdlength;
        initEdit(bgdrawable);
        initTextview(bgdrawable, pwdlength, splilinewidth, splilinecolor, pwdcolor, pwdsize);
    }

    //初始化edittext
    public void initEdit(int bg)
    {
        editText = new EditText(context);
        editText.setBackgroundResource(bg);
        editText.setCursorVisible(false);
//        editText.setTextSize(0);
        editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
//        输入框EditText限制条件setFilters
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(pwdlength)});
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Editable etext = editText.getText();
//                把光标放在EditText中文本的末尾处
                Selection.setSelection(etext, etext.length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initDatas(s);
                if(s.length() == pwdlength)
                {
                    if(onTextFinishListener != null)
                    {
                        onTextFinishListener.onFinish(s.toString().trim());
                    }
                }
            }
        });
        //添加EditText
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(editText, lp);

    }

    /**
     * 初始化textview
     * @param bg 背景drawable
     * @param pwdlength 密码长度
     * @param slpilinewidth 分割线宽度
     * @param splilinecolor 分割线颜色
     * @param pwdcolor 密码字体颜色
     * @param pwdsize 密码字体大小
     */
    public void initTextview(int bg, int pwdlength, float slpilinewidth, int splilinecolor, int pwdcolor, int pwdsize){
        //密码框的父布局
        linearLayout = new LinearLayout(context);
        linearLayout.setBackgroundResource(bg);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(linearLayout);

        //添加密码框
        textViews = new TextView[pwdlength];
        textViews = new TextView[pwdlength];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER;

        //添加分割线
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(dip2px(context, slpilinewidth),LayoutParams.MATCH_PARENT);

        for(int i = 0; i < textViews.length; i++)
        {
            final int index = i;
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textViews[i] = textView;
            textViews[i].setTextSize(pwdsize);
            textViews[i].setTextColor(context.getResources().getColor(pwdcolor));
            textViews[i].setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
            linearLayout.addView(textView, params);


            if(i < textViews.length - 1)
            {
                View view = new View(context);
                view.setBackgroundColor(context.getResources().getColor(splilinecolor));
                linearLayout.addView(view, params2);
            }
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //根据Edittext的输入内容设置Textview的值

    public void initDatas(Editable s)
    {
        if(s.length() > 0)
        {
            int length = s.length();
            for(int i = 0; i < pwdlength; i++)
            {
                if(i < length)
                {
                    for(int j = 0; j < length; j++)
                    {
                        char ch = s.charAt(j);
                        textViews[j].setText(String.valueOf(ch));
                    }
                }
                else
                {
                    textViews[i].setText("");
                }
            }
        }
        else
        {
            for(int i = 0; i < pwdlength; i++)
            {
                textViews[i].setText("");
            }
        }
    }

    public void setOnTextFinishListener(OnTextFinishListener onTextFinishListener) {
        this.onTextFinishListener = onTextFinishListener;
    }

    public interface OnTextFinishListener
    {
        void onFinish(String str);
    }
}
