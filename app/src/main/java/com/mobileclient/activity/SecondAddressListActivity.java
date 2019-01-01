package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.adapter.AddressAdapter;
import com.mobileclient.adapter.ReceiveAddressAdapter;
import com.mobileclient.app.Declare;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.service.ReceiveAddressService;
import com.mobileclient.util.ActivityUtils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class SecondAddressListActivity extends Activity  {
    AddressAdapter adapter;
    ListView lv;
    List<Map<String, Object>> list;
    private ReceiveAddress queryConditionReceiveAddress;
    Declare declare;
    private MyProgressDialog dialog; //进度条	@Override
    ReceiveAddressService receiveAddressService = new ReceiveAddressService();
    private ImageView back,add;
    Bundle extras;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.receiveaddress_list);
        dialog = MyProgressDialog.getInstance(this);
        declare = (Declare) getApplicationContext();
        // extras = this.getIntent().getExtras();
        String username = declare.getUserName();
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("收货地址");
        add=findViewById(R.id.save); //地址添加
        add.setImageResource(R.drawable.plusplus);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SecondAddressListActivity.this, ReceiveAddressAddActivity.class), ActivityUtils.ADD_CODE);
            }
        });
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Log.i("pppppppppppppppp", "11111" + declare.getUserId());
        //ListView item 中的删除按钮的点击事件

        setViews();

    }

    //结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityUtils.EDIT_CODE && resultCode == RESULT_OK) {
            setViews();
        }
        if (requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
            setViews();
        }
    }

    private void setViews() {
        lv = findViewById(R.id.address_list_view);
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
                        adapter = new AddressAdapter(SecondAddressListActivity.this, list,
                                R.layout.query,
                                new String[]{"receiveAddressName", "receiveName", "receivePhone"},
                                new int[]{R.id.receiveAddressName, R.id.receiveName, R.id.receivePhone,}, lv);
                        lv.setAdapter(adapter);
                        adapter.setOnItemEditClickListener(new AddressAdapter.onItemEditListener() {
                            @Override
                            public void onEditClick(int i) {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                // bundle.putInt("noticeId", noticeId);
                                intent.setClass(SecondAddressListActivity.this, ReceiveAddressEditActivity.class);
                                bundle.putInt("receiveId", Integer.parseInt(list.get(i).get("receiveId").toString()));
                                bundle.putString("receiveAddressName", list.get(i).get("receiveAddressName").toString());
                                bundle.putString("receiveName", list.get(i).get("receiveName").toString());
                                bundle.putString("receivePhone", list.get(i).get("receivePhone").toString());
                                bundle.putString("receiveState", list.get(i).get("receiveState").toString());
                                intent.putExtras(bundle);
                                startActivityForResult(intent,ActivityUtils.EDIT_CODE);

                            }
                        });
                    }
                });
            }
        }.start();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // int orderId = Integer.parseInt(list.get(arg2).get("orderId").toString());
                Intent intent = new Intent();  //点击返回地址名和地址Id
                Bundle bundle = new Bundle();
                bundle.putInt("receiveId",Integer.parseInt(list.get(position).get("receiveId").toString()));
                bundle.putString("receiveAddressName",list.get(position).get("receiveAddressName").toString());
                bundle.putString("receiveName",list.get(position).get("receiveAddressName").toString());
                bundle.putString("receivePhone",list.get(position).get("receivePhone").toString());
                bundle.putString("receiveState",list.get(position).get("receiveState").toString());
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
                // finish();

            }
        });
    }



    // 长按菜单响应函数
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {  //编辑地址信息
            ContextMenuInfo info = item.getMenuInfo();
            AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
            // 获取选中行位置
            int position = contextMenuInfo.position;
            // 获取公告id

            Intent intent = new Intent();  //点击返回地址名和地址Id
            Bundle bundle = new Bundle();
            bundle.putInt("receiveId",Integer.parseInt(list.get(position).get("noticeId").toString()));
            bundle.putString("receiveAddressName",list.get(position).get("receiveAddressName").toString());
            bundle.putString("","");
            intent.putExtras(bundle);
            intent.setClass(SecondAddressListActivity.this, ExpressOrderAddActivity.class);//点击返回订单添加页面
            intent.putExtras(bundle);
            startActivityForResult(intent,RESULT_OK);
            //finish();
        } else if (item.getItemId() == 1) {// 删除地址公告信息
            ContextMenuInfo info = item.getMenuInfo();
            AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
            // 获取选中行位置
            int position = contextMenuInfo.position;
            // 获取公告id
            //  noticeId = Integer.parseInt(list.get(position).get("noticeId").toString());
            dialog();
        }
        return super.onContextItemSelected(item);
    }

    // 删除
    protected void dialog() {
        Builder builder = new Builder(SecondAddressListActivity.this);
        builder.setMessage("确认删除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // String result = noticeService.DeleteNotice(noticeId);
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
            /* 查询新闻公告信息 */
            //  Log.i("Address", "11111" + declare.getUserId());

            List<ReceiveAddress> receiveAddressList = receiveAddressService.QueryReceiveAdressList(declare.getUserId());
            // Log.i("Address", "11111" + receiveAddressList);
            for (int i = 0; i < receiveAddressList.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("receiveId", receiveAddressList.get(i).getReceiveId());
                map.put("userId", receiveAddressList.get(i).getUserId());
                map.put("receiveAddressName", receiveAddressList.get(i).getReceiveAddressName());
                map.put("receiveName", receiveAddressList.get(i).getReceiveName());
                map.put("receivePhone", receiveAddressList.get(i).getReceivePhone());
                map.put("receiveState", receiveAddressList.get(i).getReceiveState());
                list.add(map);
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }
        return list;
    }



}