package com.mobileclient.pay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobileclient.activity.R;


/**
 * Created by limengjie on 2016/12/8.
 */

public class InputPwdView extends LinearLayout {
    private Context context;
    private ImageView img_close;
    private TextView tv_forget;
    private GridView listView;
    private InputPwdView_Pwd inputPwdView_Pwd;
    private InputPwdListener inputPwdListener;

    public InputPwdView(Context context) {
        super(context);
        init();
    }

    public InputPwdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputPwdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.inputpwd_layout, this);
        img_close = (ImageView) view.findViewById(R.id.MyPwdInput_close);
        tv_forget = (TextView) view.findViewById(R.id.MyPwdInput_forgetpwd);
        listView = (GridView) view.findViewById(R.id.MyPwdInput_list);
        inputPwdView_Pwd = (InputPwdView_Pwd) findViewById(R.id.MyPwdInput_inputpwd);

        listView.setAdapter(new MAdapter());

        initListener();
    }

    public void initListener() {
        img_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != inputPwdListener) {
                    inputPwdListener.hide();
                }
            }
        });
        tv_forget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != inputPwdListener) {
                    inputPwdListener.forgetPwd();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(context,""+i,Toast.LENGTH_SHORT).show();
                if (i == 11) {
                    inputPwdView_Pwd.deletePwd();
                } else if (i == 10) {
                    inputPwdView_Pwd.addPwd("0");
                } else if (i == 9) {

                } else {
                    inputPwdView_Pwd.addPwd(String.valueOf(++i));
                }

            }
        });
        inputPwdView_Pwd.setOnFinishListener(new InputPwdView_Pwd.OnFinishListener() {
            @Override
            public void setOnPasswordFinished(String pwd) {
                if (null != inputPwdListener) {
                    inputPwdListener.finishPwd(pwd);
                }
            }
        });
    }

    public void setListener(InputPwdListener inputPwdListener) {
        this.inputPwdListener = inputPwdListener;
    }
    public void reSetView(){
        if(null!=inputPwdView_Pwd){
            inputPwdView_Pwd.reSetView();
        }
    }

    class MAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View cview = LayoutInflater.from(context).inflate(R.layout.myinputviewlist_item, viewGroup, false);
            TextView tv = (TextView) cview.findViewById(R.id.myInputPwdListItem_tv);

            if (i == 9) {
                tv.setVisibility(View.GONE);
                cview.findViewById(R.id.myInputPwdListItem_delete).setVisibility(View.GONE);
            } else if (i == 10) {
                cview.findViewById(R.id.myInputPwdListItem_delete).setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                tv.setText("0");
            } else if (i == 11) {
                cview.findViewById(R.id.myInputPwdListItem_delete).setVisibility(View.VISIBLE);
                tv.setVisibility(View.GONE);
            } else {
                tv.setText((i + 1) + "");
                cview.findViewById(R.id.myInputPwdListItem_delete).setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
            }

            return cview;
        }
    }

    public interface InputPwdListener {
        void hide();

        void forgetPwd();

        void finishPwd(String pwd);
    }


}
