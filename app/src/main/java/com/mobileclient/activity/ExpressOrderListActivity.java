package com.mobileclient.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Order;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.service.OrderService;
import com.mobileclient.service.ReceiveAddressService;
import com.mobileclient.util.ActivityUtils;import com.mobileclient.util.ExpressTakeSimpleAdapter;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ExpressOrderListActivity extends Activity {
    ExpressTakeSimpleAdapter adapter;
    ListView lv;
    List<Map<String, Object>> list;
    int orderId;
    /* 快递代拿操作业务逻辑层对象 */
   OrderService orderService=new OrderService();
    /*保存查询参数条件的快递代拿对象*/
    private Order queryConditionExpressOrder;
    private ImageView back_btn;
    private MyProgressDialog dialog; //进度条	@Override
    RelativeLayout tvSearchRlt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.list_layout);
        dialog = MyProgressDialog.getInstance(this);
        Declare declare = (Declare) getApplicationContext();
        tvSearchRlt = (RelativeLayout) findViewById(R.id.tv_search_rlt);
        tvSearchRlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpressOrderListActivity.this,SearchActivity.class);
                int location[] = new int[2];
                tvSearchRlt.getLocationOnScreen(location);
                intent.putExtra("x",location[0]);
                intent.putExtra("y",location[1]);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
        //TextView title = (TextView) this.findViewById(R.id.title);
        //title.setText("快递代拿查询列表");
//		back_btn = this.findViewById(R.id.back_btn);
//		back_btn.setOnClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//
//		});
        Intent intent=getIntent();
        Log.i("zhu777",""+intent.getIntExtra("q",0));
        if(intent.getIntExtra("q",0)==1) {
            Log.i("zhu666","查询");
            queryConditionExpressOrder = (Order) intent.getExtras().getSerializable("queryConditionExpressTake");
            if (intent.getExtras().get("query").equals("query")) {
                Log.i("zhu666",""+intent.getExtras().get("query"));
                Bundle extras = intent.getExtras();
                if (extras != null)
                    queryConditionExpressOrder = (Order) extras.getSerializable("queryConditionExpressTake");
               // Log.i("zhu999",""+queryConditionExpressOrder.);
                setViews();
            }
        }
        //if(){
        //setViews();
        //}
        //if() {
        //	queryConditionExpressTake = null;
        //	setViews();
        //}
        setViews();
    }


    //结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            if(extras != null)
                queryConditionExpressOrder = (Order)extras.getSerializable("queryConditionExpressTake");

            setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
            setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
            queryConditionExpressOrder = null;
            setViews();
        }
    }

    private void setViews() {
        lv = findViewById(R.id.h_list_view);
        dialog.show();
        final Handler handler = new Handler();
        new Thread(){
            @Override
            public void run() {
                //在子线程中进行下载数据操作
                Log.i("zhuppp","ppp");
                list = getDatas();
                Log.i("zhuttt","ttt");
                //发送消失到handler，通知主线程下载完成
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.cancel();
                        adapter = new ExpressTakeSimpleAdapter(ExpressOrderListActivity.this, list,
                                R.layout.order_list_item,
                                new String[] { "userPhoto","userName","orderName","expressCompanyName","expressCompanyAdress","receiveAdressName","addTime","orderState" },
                                new int[] { R.id.userPhoto,R.id.userName,R.id.orderName,R.id.expressCompanyName,R.id.expressCompanyAdress,
                                        R.id.addTime,R.id.orderState},lv);
                        lv.setAdapter(adapter);
                    }
                });
            }
        }.start();

        // 添加长按点击
        lv.setOnCreateContextMenuListener(expressTakeListItemListener);
        lv.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                int orderId = Integer.parseInt(list.get(arg2).get("orderId").toString());
                Intent intent = new Intent();
                intent.setClass(ExpressOrderListActivity.this, ExpressTakeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("orderId", orderId);
                Log.i("34566",""+orderId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private OnCreateContextMenuListener expressTakeListItemListener = new OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
            //menu.add(0, 0, 0, "编辑快递代拿信息");
            //menu.add(0, 1, 0, "删除快递代拿信息");
        }
    };

    // 长按菜单响应函数
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {  //编辑快递代拿信息
            ContextMenuInfo info = item.getMenuInfo();
            AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
            // 获取选中行位置
            int position = contextMenuInfo.position;
            // 获取订单id
            orderId = Integer.parseInt(list.get(position).get("orderId").toString());
            Intent intent = new Intent();
            intent.setClass(ExpressOrderListActivity.this, ExpressTakeEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("orderId", orderId);

            intent.putExtras(bundle);

            startActivityForResult(intent,ActivityUtils.EDIT_CODE);
        } else if (item.getItemId() == 1) {// 删除快递代拿信息
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
        Builder builder = new Builder(ExpressOrderListActivity.this);
        builder.setMessage("确认删除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result = orderService.DeleteOrder(orderId);
                Toast.makeText(getApplicationContext(), result,Toast.LENGTH_SHORT).show();
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
            Log.i("zhu1010","查询");
            ReceiveAddressService receiveAdressService=new ReceiveAddressService();
            ReceiveAddress receiveAdress=new ReceiveAddress();
            /* 查询快递代拿信息 */
            List<Order> expressOrderList = orderService.QueryOrder(queryConditionExpressOrder);
           // Log.i("zhu1111","查询"+expressTakeList);
            for (int i = 0; i < expressOrderList.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orderId",expressOrderList.get(i).getOrderId());
                map.put("orderName", expressOrderList.get(i).getOrderName());
                map.put("userId", expressOrderList.get(i).getUserId());
                map.put("expressCompanyName", expressOrderList.get(i).getExpressCompanyName());
                map.put("expressCompanyAdress", expressOrderList.get(i).getExpressCompanyAddress());
               // 根据获取到的地址Id，查询地址名以及收获人姓名
                receiveAdress=receiveAdressService.GetReceiveAdress(expressOrderList.get(i).getReceiveAddressId());
                map.put("receiveAdressName", receiveAdress.getReceiveAddressName());
                map.put("receiveName",receiveAdress.getReceiveName());
                map.put("addTime", expressOrderList.get(i).getAddTime());
                map.put("orderState", expressOrderList.get(i).getOrderState());
                map.put("orderPay", expressOrderList.get(i).getOrderPay());
                map.put("remark", expressOrderList.get(i).getRemark());
                map.put("receiveCode", expressOrderList.get(i).getReceiveCode());
                map.put("userPhone", expressOrderList.get(i).getAddTime());
                list.add(map);
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }
        return list;
    }
}
