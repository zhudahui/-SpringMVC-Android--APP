package com.mobileclient.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileclient.adapter.SearchAdapter;
import com.mobileclient.domain.ExpressTake;
import com.mobileclient.domain.Order;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.domain.User;
import com.mobileclient.service.OrderService;
import com.mobileclient.service.ReceiveAddressService;
import com.mobileclient.service.UserService;
import com.mobileclient.util.ActivityUtils;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

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
    private ListView lv;
    private MyProgressDialog dialog; //进度条	@Override
    List<Map<String, Object>> list;
    SearchAdapter adapter;
    User user=new User();
    UserService userService=new UserService();
    Order order;
    Order queryConditionExpressOrder=null;
    OrderService orderService=new OrderService();
    ReceiveAddress receiveAddress=new ReceiveAddress();
    ReceiveAddressService receiveAddressService=new ReceiveAddressService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.search);
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
       setViews();
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
               // recommandTv.setAlpha((Float) valueAnimator.getAnimatedValue());
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
               // recommandTv.setAlpha((Float) valueAnimator.getAnimatedValue());
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
                        Log.i("ppppppp",""+list.get(0).get("nickName"));
                        adapter = new SearchAdapter(SearchActivity.this,list);
                        lv.setAdapter(adapter);



                        mHintTxt.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                Log.i("ppppppp","333"+adapter);

                                adapter.getFilter().filter(s);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // init();
                            }
                        });

                    }
                });

            }


        }.start();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                Log.i("pppppppp","arg"+arg2);
                Intent intent = new Intent();
                //intent.setClass(ExpressTakeMyListActivity.this, ExpressTakeDetailActivity.class);
                Bundle bundle = new Bundle();
                int orderId = Integer.parseInt(list.get(arg2).get("orderId").toString());
                arg2= Integer.parseInt(list.get(arg2).get("position").toString());
                bundle.putInt("orderId", orderId);
                bundle.putInt("userId",Integer.parseInt(list.get(arg2).get("userId").toString()));
                bundle.putString("orderName",list.get(arg2).get("orderName").toString());
                bundle.putString("nickName",list.get(arg2).get("nickName").toString());
                bundle.putByteArray("photo", (byte[]) list.get(arg2).get("photo"));
                bundle.putString("expressCompanyName",list.get(arg2).get("expressCompanyName").toString());
                bundle.putString("expressCompanyAddress",list.get(arg2).get("expressCompanyAddress").toString());
                bundle.putString("receiveAddressName",list.get(arg2).get("receiveAddressName").toString());
                bundle.putString("receiveName",list.get(arg2).get("receiveName").toString());
                bundle.putString("receivePhone",list.get(arg2).get("receivePhone").toString());
                bundle.putString("receiveState",list.get(arg2).get("receiveState").toString());
                bundle.putString("remark",list.get(arg2).get("remark").toString());
                bundle.putString("receiveCode",list.get(arg2).get("receiveCode").toString());
                bundle.putString("receiveName",list.get(arg2).get("receiveName").toString());
                bundle.putString("orderPay",list.get(arg2).get("orderPay").toString());
                bundle.putString("orderState",list.get(arg2).get("orderState").toString());
                bundle.putString("addTime",list.get(arg2).get("addTime").toString());
                bundle.putInt("receiveAddressId",Integer.parseInt(list.get(arg2).get("receiveAddressId").toString()));
                bundle.putString("evaluate",list.get(arg2).get("evaluate").toString());
                bundle.putInt("takeUserId", Integer.parseInt(list.get(arg2).get("takeUserId").toString()));
                //bundle.putString("score",list.get(arg2).get("score").toString());
                if (list.get(arg2).get("evaluate").toString().equals("-+-")||list.get(arg2).get("evaluate").toString().equals("请评价")) {//若评价为空
                    intent.putExtras(bundle);
                    intent.setClass(SearchActivity.this, ExpressOrderDetailActivity.class);   //已评价
                    startActivityForResult(intent, ActivityUtils.UPDATE_CODE);
                }
                else {
                    Log.i("zhu111", "已评价");
                    intent.setClass(SearchActivity.this, SecondOrderDetailActivity.class);   //已评价
                    intent.putExtras(bundle);
                    startActivity(intent);
                }


            }
        });
    }
    private List<Map<String, Object>> getDatas() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            /* 查询快递代拿信息 */
            ReceiveAddressService receiveAdressService=new ReceiveAddressService();
            /* 查询快递代拿信息 */
            List<Order> expressOrderList = orderService.OrderStateQuery("待接单");
            for (int i = 0; i < expressOrderList.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                    map.put("position",i);
                    map.put("orderId", expressOrderList.get(i).getOrderId());
                    map.put("orderName", expressOrderList.get(i).getOrderName());
                    map.put("userId", expressOrderList.get(i).getUserId());
                    user = userService.GetUserInfo(expressOrderList.get(i).getUserId());
                    map.put("userName", user.getUserName());
                    map.put("nickName", user.getNickName());
                    byte[] userPhoto_data = null;
                    // 获取图片数据
                    userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + user.getUserPhoto());
                    map.put("photo", userPhoto_data);
                    Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
                    map.put("userPhoto", userPhoto);
                    map.put("expressCompanyName", expressOrderList.get(i).getExpressCompanyName());
                    map.put("expressCompanyAddress", expressOrderList.get(i).getExpressCompanyAddress());
                    map.put("receiveAddressId", expressOrderList.get(i).getReceiveAddressId());
                    // 根据获取到的地址Id，查询地址名以及收获人姓名
                   //receiveAddress = receiveAdressService.QueryReceiveAdress(expressOrderList.get(i).getReceiveAddressId());
                    Log.i("zhu1111", "查询ttt" + expressOrderList.get(i).getReceiveAddressName());
                    map.put("receiveAddressName", expressOrderList.get(i).getReceiveAddressName());
                    map.put("receiveName", expressOrderList.get(i).getReceiveName());
                    map.put("receivePhone", expressOrderList.get(i).getReceivePhone());
                    map.put("receiveState", expressOrderList.get(i).getReceiveState());
                    map.put("addTime", expressOrderList.get(i).getAddTime());
                    map.put("orderState", expressOrderList.get(i).getOrderState());
                    map.put("orderPay", expressOrderList.get(i).getOrderPay());
                    map.put("remark", expressOrderList.get(i).getRemark());
                    map.put("receiveCode", expressOrderList.get(i).getReceiveCode());
                    map.put("evaluate", expressOrderList.get(i).getOrderEvaluate());
                    map.put("takeUserId", expressOrderList.get(i).getTakeUserId());
                    map.put("orderType", expressOrderList.get(i).getOrderType());
                    byte[] orderpic = null;
                    // 获取图片数据
                    if(expressOrderList.get(i).getOrderPic().equals("--")){
                        map.put("orderPic", expressOrderList.get(i).getOrderPic());
                    }else {
                       orderpic = ImageService.getImage(HttpUtil.DOWNURL + expressOrderList.get(i).getOrderPic());
                       Bitmap pic = BitmapFactory.decodeByteArray(orderpic, 0, orderpic.length);
                       map.put("orderPic", pic);
                    }
                map.put("score", expressOrderList.get(i).getScore());
                    //map.put("userPhone", expressOrderList.get(i).getAddTime());
                    list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}