package com.mobileclient.activity.rewardOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mobileclient.activity.ExpressOrderDetailActivity;
import com.mobileclient.activity.MyProgressDialog;
import com.mobileclient.activity.R;
import com.mobileclient.activity.SecondOrderDetailActivity;
import com.mobileclient.adapter.ExpressOrderAdapter;
import com.mobileclient.app.Declare;
import com.mobileclient.app.RefreshListView;
import com.mobileclient.domain.Order;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.domain.User;
import com.mobileclient.service.OrderService;
import com.mobileclient.service.ReceiveAddressService;
import com.mobileclient.service.UserService;
import com.mobileclient.util.ActivityUtils;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class RewardActivity extends Activity implements View.OnClickListener{
    ExpressOrderAdapter adapter;
    RefreshListView lv;
    List<Map<String, Object>> list;
    int orderId;
    /*保存查询参数条件*/
    private Order queryConditionExpressOrder;
    private MyProgressDialog dialog; //进度条	@Override
    OrderService orderService=new OrderService();
    /* 快递代拿操作业务逻辑层对象 */
    Order  order=new Order();
    User user=new User();
    UserService userService=new UserService();
    ReceiveAddress receiveAddress=new ReceiveAddress();
    Declare declare;
    private int userId;
    private String orderType;  //订单类型
    //============前三个推荐位===============
    private ImageView orderPic1,orderPic2,orderPic3;   //推荐位   物品图
    private TextView orderName1,orderName2,orderName3;  //推荐位置  订单名
    private TextView userName1,userName2,userName3;    //推荐位  姓名

    //==========================


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.reward_order);
        dialog = MyProgressDialog.getInstance(this);
        declare = (Declare) getApplicationContext();
        //标题栏控件
        //=========推荐位实例化======
        orderPic1=findViewById(R.id.orderPic1);
        orderPic2=findViewById(R.id.orderPic2);
        orderPic3=findViewById(R.id.orderPic3);
        orderName1=findViewById(R.id.orderName1);
        orderName2=findViewById(R.id.orderName2);
        orderName3=findViewById(R.id.orderName3);
        userName1=findViewById(R.id.userName1);
        userName2=findViewById(R.id.userName2);
        userName3=findViewById(R.id.userName3);
        //=============
        orderPic1.setOnClickListener(this);
        orderPic2.setOnClickListener(this);
        orderPic3.setOnClickListener(this);
        orderName1.setOnClickListener(this);
        orderName2.setOnClickListener(this);
        orderName3.setOnClickListener(this);
        userName1.setOnClickListener(this);
        userName2.setOnClickListener(this);
        userName3.setOnClickListener(this);

        //queryConditionExpressOrder = new Order();
        //queryConditionExpressOrder.setTakeUserId(declare.getUserId());
        setViews();
    }

    //结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.UPDATE_CODE&&resultCode==RESULT_OK) {    //从Detail页面回来
            Bundle extras = data.getExtras();
            if (extras != null) {
                int p = extras.getInt("p");
                if (p == 1)
                    setViews();
            }
        }
    }

    private void setViews() {
        lv = findViewById(R.id.list_view);
        dialog.show();
        final Handler handler = new Handler();
        new Thread(){
            @Override
            public void run() {
                //在子线程中进行下载数据操作
                list = getDatas();
                //发送消失到handler，通知主线程下载完成
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.cancel();
                        Log.i("pppppp","fff"+list.size());
                        if(list.size()==0){         //如果暂未有推荐，则虚位以待
                            orderPic1.setImageResource(R.drawable.xuwei);
                            orderPic2.setImageResource(R.drawable.xuwei);
                            orderPic3.setImageResource(R.drawable.xuwei);
                            orderName1.setText("虚位以待");
                            orderName2.setText("虚位以待");
                            orderName3.setText("虚位以待");
                            userName1.setText("未有发布者");
                            userName2.setText("未有发布者");
                            userName3.setText("未有发布者");
                        }
                        if(list.size()==1){         //如果暂未有推荐，则虚位以待
                            orderPic1.setImageBitmap((Bitmap) list.get(0).get("orderPic"));
                            orderPic2.setImageResource(R.drawable.xuwei);
                            orderPic3.setImageResource(R.drawable.xuwei);
                            orderName1.setText(list.get(0).get("orderName").toString());
                            orderName2.setText("虚位以待");
                            orderName3.setText("虚位以待");
                            userName1.setText(list.get(0).get("userName").toString());
                            userName2.setText("未有发布者");
                            userName3.setText("未有发布者");
                        } if(list.size()==2){         //如果暂未有推荐，则虚位以待
                            orderPic1.setImageBitmap((Bitmap) list.get(0).get("orderPic"));
                            orderPic2.setImageBitmap((Bitmap) list.get(1).get("orderPic"));
                            orderPic3.setImageResource(R.drawable.xuwei);
                            orderName1.setText(list.get(0).get("orderName").toString());
                            orderName2.setText(list.get(1).get("orderName").toString());
                            orderName3.setText("虚位以待");
                            userName1.setText(list.get(0).get("userName").toString());
                            userName2.setText(list.get(1).get("userName").toString());
                            userName3.setText("未有发布者");
                        }
                        if(list.size()==3){         //如果暂未有推荐，则虚位以待
                            orderPic1.setImageBitmap((Bitmap) list.get(0).get("orderPic"));
                            orderPic2.setImageBitmap((Bitmap) list.get(1).get("orderPic"));
                            orderPic3.setImageBitmap((Bitmap) list.get(2).get("orderPic"));
                            orderName1.setText(list.get(0).get("orderName").toString());
                            orderName2.setText(list.get(1).get("orderName").toString());
                            orderName3.setText(list.get(2).get("orderName").toString());
                            userName1.setText(list.get(0).get("userName").toString());
                            userName2.setText(list.get(1).get("userName").toString());
                            userName3.setText(list.get(2).get("userName").toString());
                        }


                            adapter = new ExpressOrderAdapter(RewardActivity.this, list,
                                    R.layout.order_list_item,
                                    new String[]{"userPhoto", "userName", "orderName", "expressCompanyName", "expressCompanyAddress", "receiveAddressName", "addTime", "orderState"},
                                    new int[]{R.id.userPhoto, R.id.userName, R.id.orderName, R.id.expressCompanyName, R.id.expressCompanyAdress,
                                            R.id.addTime, R.id.orderState}, lv);
                            lv.setAdapter(adapter);

                    }
                });
            }
        }.start();
        //=================
        lv.setonRefreshListener(new RefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Message msg = new Message();
                        try {
                            setViews();
                            Thread.sleep(1000);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(0);
                    }
                }).start();

            }
        });
        // 添加长按点击
        lv.setOnCreateContextMenuListener(expressTakeListItemListener);
        lv.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
            {
                arg2=arg2-1;
                int orderId = Integer.parseInt(list.get(arg2).get("orderId").toString());
                Intent intent = new Intent();
                intent.setClass(RewardActivity.this,ExpressOrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("orderId", orderId);
                bundle.putString("orderName",list.get(arg2).get("orderName").toString());
                bundle.putString("userName",list.get(arg2).get("userName").toString());
                bundle.putByteArray("photo", (byte[]) list.get(arg2).get("photo"));
                bundle.putString("expressCompanyName",list.get(arg2).get("expressCompanyName").toString());
                bundle.putString("expressCompanyAddress",list.get(arg2).get("expressCompanyAddress").toString());
                bundle.putString("receiveAddressName",list.get(arg2).get("receiveAddressName").toString());
                bundle.putString("receiveName",list.get(arg2).get("receiveName").toString());
                bundle.putString("receivePhone",list.get(arg2).get("receivePhone").toString());
                bundle.putString("remark",list.get(arg2).get("remark").toString());
                bundle.putString("receiveCode",list.get(arg2).get("receiveCode").toString());
                bundle.putString("receiveName",list.get(arg2).get("receiveName").toString());
                bundle.putString("orderPay",list.get(arg2).get("orderPay").toString());
                bundle.putString("orderState",list.get(arg2).get("orderState").toString());
                bundle.putString("addTime",list.get(arg2).get("addTime").toString());
                //if(list.get(arg2).get("evaluate").toString()!=null)
                bundle.putString("evaluate",list.get(arg2).get("evaluate").toString());
                bundle.putInt("takeUserId", Integer.parseInt(list.get(arg2).get("takeUserId").toString()));
                //bundle.putString("userType",declare.getUserType());
                if (list.get(arg2).get("evaluate").toString().equals("--")||list.get(arg2).get("evaluate").toString().equals("请评价")) {//若评价为空
                    intent.putExtras(bundle);
                    intent.setClass(RewardActivity.this, ExpressOrderDetailActivity.class);   //已评价
                    startActivityForResult(intent, ActivityUtils.UPDATE_CODE);
                }
                else {
                    Log.i("zhu111", "已评价");
                    intent.setClass(RewardActivity.this, SecondOrderDetailActivity.class);   //已评价
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }
    private OnCreateContextMenuListener expressTakeListItemListener = new OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
            //menu.add(0, 0, 0, "编辑快递代拿信息");
            //menu.add(0, 1, 0, "删除快递代拿信息");

            menu.add(0, 0, 0, "取消快递代拿订单");
        }
    };

    // 长按菜单响应函数
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {// 删除快递代拿信息
            ContextMenuInfo info = item.getMenuInfo();
            AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
            // 获取选中行位置
            int position = contextMenuInfo.position;
            // 获取订单id
            orderId = Integer.parseInt(list.get(position).get("orderId").toString());
            dialog();
        }
        return super.onContextItemSelected(item);
    }

    // 删除
    protected void dialog() {
        Builder builder = new Builder(RewardActivity.this);
        builder.setMessage("确认取消吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // //String result = expressTakeService.DeleteExpressTake(orderId);
               // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                setViews();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private List<Map<String, Object>> getDatas() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            /* 查询快递代拿信息 */
            ReceiveAddressService receiveAdressService=new ReceiveAddressService();
            /* 查询快递代拿信息 */
            List<Order> expressOrderList = orderService.OrderTypeQuery("悬赏");  //根据订单类型(悬赏)搜索出需要的订单信息
            for (int i = 0; i < expressOrderList.size(); i++) {
                if(expressOrderList.get(i).getOrderState().equals("待接单")) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("orderId", expressOrderList.get(i).getOrderId());
                    Log.i("pppppp","order"+ expressOrderList.get(i).getOrderId());
                    map.put("orderName", expressOrderList.get(i).getOrderName());
                    map.put("userId", expressOrderList.get(i).getUserId());
                    user = userService.GetUserInfo(expressOrderList.get(i).getUserId());
                    map.put("userName", user.getUserName());
                    byte[] userPhoto_data = null;
                    // 获取图片数据
                    userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + user.getUserPhoto());
                    map.put("photo", userPhoto_data);
                    Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
                    map.put("userPhoto", userPhoto);
                    map.put("expressCompanyName", expressOrderList.get(i).getExpressCompanyName());
                    map.put("expressCompanyAddress", expressOrderList.get(i).getExpressCompanyAddress());
                    // 根据获取到的地址Id，查询地址名以及收获人姓名
                    receiveAddress = receiveAdressService.QueryReceiveAdress(expressOrderList.get(i).getReceiveAddressId());
                    Log.i("zhu1111", "查询" + receiveAddress.getReceiveAddressName());
                    map.put("receiveAddressName", receiveAddress.getReceiveAddressName());
                    map.put("receiveName", receiveAddress.getReceiveName());
                    map.put("receivePhone", receiveAddress.getReceivePhone());
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
                    orderpic = ImageService.getImage(HttpUtil.DOWNURL + expressOrderList.get(i).getOrderPic());
                    Bitmap pic = BitmapFactory.decodeByteArray(orderpic, 0, orderpic.length);
                    map.put("orderPic", pic);
                    map.put("score", expressOrderList.get(i).getScore());
                    //map.put("userPhone", expressOrderList.get(i).getAddTime());
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }


    /**
     * 定义一个handler处理请求返回来的信息
     */
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            System.out.println("这是刷新返回来的信息");
            adapter.notifyDataSetChanged();
            lv.onRefreshComplete();
        }

    };
}
