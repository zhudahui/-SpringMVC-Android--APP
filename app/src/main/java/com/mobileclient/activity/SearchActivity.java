package com.mobileclient.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileclient.adapter.CircleImageView;
import com.mobileclient.adapter.SearchAdapter;
import com.mobileclient.domain.ExpressTake;
import com.mobileclient.service.ExpressTakeService;
import com.mobileclient.util.ExpressTakeSimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends Activity {
    private RelativeLayout mSearchBGTxt;
    private TextView mHintTxt;
    private TextView recommandTv,locationTv;
    private TextView mSearchTxt;
    private FrameLayout mContentFrame,frameBg;
    private ImageView mbackIv;
    private TextView tv_search;
    float frameBgHeight = 0;
    float searchBgHeight = 0;
    /*查询过滤条件保存到这个对象中*/
    private ExpressTake queryConditionExpressTake = new ExpressTake();
    ExpressTakeService expressTakeService=new ExpressTakeService();
    private ListView lv;
    private MyProgressDialog dialog; //进度条	@Override
    List<Map<String, Object>> list;
    SearchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        dialog = MyProgressDialog.getInstance(this);
        mSearchBGTxt = findViewById(R.id.tv_search_rlt);
        mHintTxt =  findViewById(R.id.tv_hint);
        mContentFrame = findViewById(R.id.frame_content_bg);
        mSearchTxt =  findViewById(R.id.tv_search);
        mbackIv =  findViewById(R.id.back_iv);
        frameBg =   findViewById(R.id.frame_bg);
       // recommandTv = findViewById(R.id.recommand_tv);
        locationTv =  findViewById(R.id.location_tv);
        tv_search=findViewById(R.id.tv_search);
        mHintTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                  // init();
            }
        });
        //绑定数据


            tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索
                try {
                    /*获取查询参数*/
                    queryConditionExpressTake.setTaskTitle(mHintTxt.getText().toString());
                    //Log.i("eeeee",ET_taskTitle.getText().toString());
                    queryConditionExpressTake.setWaybill(mHintTxt.getText().toString());
                    queryConditionExpressTake.setReceiverName(mHintTxt.getText().toString());
                    queryConditionExpressTake.setTelephone(mHintTxt.getText().toString());
                    queryConditionExpressTake.setTakePlace(mHintTxt.getText().toString());
                    queryConditionExpressTake.setTakeStateObj(mHintTxt.getText().toString());
                    queryConditionExpressTake.setAddTime(mHintTxt.getText().toString());
                    Intent intent = new Intent();
                    //这里使用bundle绷带来传输数据
                    Bundle bundle =new Bundle();
                    //传输的内容仍然是键值对的形式
                    bundle.putSerializable("queryConditionExpressTake", queryConditionExpressTake);
                    intent.putExtras(bundle);
                    intent.putExtra("query","query");
                    //Declare declare=new Declare();
                    intent.putExtra("q",1);
                    //Log.i("zhu8888",""+declare.getQuery());
                    setResult(RESULT_OK,intent);
                    intent.setClass(SearchActivity.this,ReceiveAddressListActivity.class);
                    startActivity(intent);
                } catch (Exception e) {}
            }
        });

        mSearchBGTxt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSearchBGTxt.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                performEnterAnimation();

            }
        });
        mbackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }






    private void performEnterAnimation() {
        float originY = getIntent().getIntExtra("y", 0);

        int location[] = new int[2];
        mSearchBGTxt.getLocationOnScreen(location);

        final float translateY = originY - (float) location[1];


        frameBgHeight = frameBg.getHeight();

        //放到前一个页面的位置
        mSearchBGTxt.setY(mSearchBGTxt.getY() + translateY);
        mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
        mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
        final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGTxt.getY(), mSearchBGTxt.getY() - 100);
        searchBgHeight = mSearchBGTxt.getY();
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
                ViewGroup.LayoutParams linearParams = frameBg.getLayoutParams(); //取控件textView当前的布局参数
                linearParams.height = (int) (frameBgHeight-(searchBgHeight-(Float) valueAnimator.getAnimatedValue())*2);
                frameBg.setLayoutParams(linearParams);
                mbackIv.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mbackIv.getHeight()) / 2);
                mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
                mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
            }
        });

        ValueAnimator scaleVa = ValueAnimator.ofFloat(1, 0.8f);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });

        ValueAnimator alphaVa = ValueAnimator.ofFloat(0, 1f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
                mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
                mbackIv.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        ValueAnimator alphaVa2 = ValueAnimator.ofFloat(1f, 0);
        alphaVa2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                locationTv.setAlpha((Float) valueAnimator.getAnimatedValue());
                recommandTv.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        //给以上动画设置持续时长并启动动画
        alphaVa.setDuration(500);
        alphaVa2.setDuration(300);
        translateVa.setDuration(500);
        scaleVa.setDuration(500);

        alphaVa.start();
        alphaVa2.start();
        translateVa.start();
        scaleVa.start();
    }

    @Override
    public void onBackPressed() {
        performExitAnimation();
    }

    private void performExitAnimation() {
        float originY = getIntent().getIntExtra("y", 0);

        int location[] = new int[2];
        mSearchBGTxt.getLocationOnScreen(location);

        final float translateY = originY - (float) location[1];

        //通过valueAnimator实现渐变的效果
        final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGTxt.getY(), mSearchBGTxt.getY()+translateY);
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
                mbackIv.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mbackIv.getHeight()) / 2);
                mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
                mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
                //组件高度动态改变
                ViewGroup.LayoutParams linearParams = frameBg.getLayoutParams(); //取控件textView当前的布局参数
                linearParams.height = (int) (frameBgHeight-(searchBgHeight-(Float) valueAnimator.getAnimatedValue())*2);
                frameBg.setLayoutParams(linearParams);
            }
        });
        translateVa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        ValueAnimator scaleVa = ValueAnimator.ofFloat(0.8f, 1f);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });

        ValueAnimator alphaVa = ValueAnimator.ofFloat(1, 0f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
                mbackIv.setAlpha((Float) valueAnimator.getAnimatedValue());
                mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        ValueAnimator alphaVa2 = ValueAnimator.ofFloat(0, 1f);
        alphaVa2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                locationTv.setAlpha((Float) valueAnimator.getAnimatedValue());
                recommandTv.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });

        alphaVa.setDuration(500);
        alphaVa2.setDuration(500);
        translateVa.setDuration(500);
        scaleVa.setDuration(500);
        alphaVa.start();
        alphaVa2.start();
        translateVa.start();
        scaleVa.start();
    }
    private void setViews() {
        lv = findViewById(R.id.list_view);
        dialog.show();
        final Handler handler = new Handler();
        new Thread() {
            @Override
            public void run() {
                //在子线程中进行下载数据操作
                list = getDatas();
                //发送消失到handler，通知主线程下载完成
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.cancel();
                        adapter = new SearchAdapter(SearchActivity.this, list,
                                R.layout.search_list_item,
                                new String[] { "userPhoto","userName","orderName","expressCompanyName","expressCompanyAdress","receiveAdress","addTime","orderState" },
                                new int[] { R.id.userPhoto,R.id.userName,R.id.orderName,R.id.expressCompanyName,R.id.expressCompanyAdress,
                                        R.id.addTime,R.id.orderState},lv);
                        lv.setAdapter(adapter);
                    }
                });
            }
        }.start();

    }
    //查询数据
    private List<Map<String, Object>> getDatas () {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Log.i("zhu1010", "查询");
            /* 查询快递代拿信息 */
            List<ExpressTake> expressTakeList = expressTakeService.QueryExpressTake(queryConditionExpressTake);
            //Log.i("zhu1111","查询"+expressTakeList);
            for (int i = 0; i < expressTakeList.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("photo", expressTakeList.get(i).getOrderId());
                Log.i("aaaaaaaa", "" + expressTakeList.get(i).getOrderId());
                map.put("userObj",""+expressTakeList.get(i).getUserObj());
                map.put("taskTitle",""+expressTakeList.get(i).getTaskTitle());
                map.put("companyObj",""+expressTakeList.get(i).getCompanyObj());
                map.put("expressCompanyAdress",expressTakeList.get(i).getExpressCompanyAdress());
                map.put("takePlace", expressTakeList.get(i).getTakePlace());
                map.put("addTime", expressTakeList.get(i).getAddTime());
                map.put("takeStateObj",expressTakeList.get(i).getTakeStateObj());
                list.add(map);
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }
        return list;
    }

}
