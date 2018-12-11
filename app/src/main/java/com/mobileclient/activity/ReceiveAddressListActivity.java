package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.adapter.ReceiveAddressAdapter;
import com.mobileclient.app.Declare;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.service.ReceiveAddressService;
import com.mobileclient.util.ActivityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class ReceiveAddressListActivity extends Activity  {
    ReceiveAddressAdapter adapter;
    ListView lv;
    List<Map<String, Object>> list;
    private ReceiveAddress queryConditionReceiveAddress;
    Declare declare;
    private MyProgressDialog dialog; //进度条	@Override
    ReceiveAddressService receiveAddressService = new ReceiveAddressService();
    private ImageView back;
    Bundle extras;
    private TextView add;
    private int j;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.receiveaddress_list);
        lv = findViewById(R.id.address_list_view);
        dialog = MyProgressDialog.getInstance(this);
        declare = (Declare) getApplicationContext();
       // extras = this.getIntent().getExtras();
        String username = declare.getUserName();
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("收货地址");
        add=findViewById(R.id.save); //地址添加
        add.setText("添加");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ReceiveAddressListActivity.this, ReceiveAddressAddActivity.class), ActivityUtils.ADD_CODE);
            }
        });
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();       //从地址列表过来
            }
        });
        Log.i("pppppppppppppppp", "11111" + declare.getUserId());
        //ListView item 中的删除按钮的点击事件
        setViews();

    }

    //结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityUtils.QUERY_CODE && resultCode == RESULT_OK) {

        }
        if (requestCode == ActivityUtils.EDIT_CODE && resultCode == RESULT_OK) {
            setViews();
        }
        if (requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
            setViews();
        }
    }

    private void setViews() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("3333","33333");
            }
        });
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
                        adapter = new ReceiveAddressAdapter(ReceiveAddressListActivity.this, list,
                                R.layout.receiveadress_list_item,
                                new String[]{"receiveAddressName", "receiveName", "receivePhone"},
                                new int[]{R.id.receiveAddressName, R.id.receiveName, R.id.receivePhone,}, lv);
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.i("222","222");
                            }
                        });
                        adapter.setOnItemEditClickListener(new ReceiveAddressAdapter.onItemEditListener() {
                            @Override
                            public void onEditClick(int i) {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                // bundle.putInt("noticeId", noticeId);
                                intent.setClass(ReceiveAddressListActivity.this, ReceiveAddressEditActivity.class);
                                bundle.putInt("receiveId", Integer.parseInt(list.get(i).get("receiveId").toString()));
                                bundle.putString("receiveAddressName", list.get(i).get("receiveAddressName").toString());
                                bundle.putString("receiveName", list.get(i).get("receiveName").toString());
                                bundle.putString("receivePhone", list.get(i).get("receivePhone").toString());
                                bundle.putString("receiveState", list.get(i).get("receiveState").toString());
                                intent.putExtras(bundle);
                                startActivityForResult(intent,ActivityUtils.EDIT_CODE);

                            }
                        });
                        adapter.setOnItemDeleteClickListener(new ReceiveAddressAdapter.onItemDeleteListener() {
                            @Override
                            public void onDeleteClick(final int i) {
                                j=i;
                               showDialog();
                            }
                        });

                    }
                });
            }
        }.start();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("11111","1111");
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
            //  noticeId = Integer.parseInt(list.get(position).get("noticeId").toString());
            Intent intent = new Intent();
            intent.setClass(ReceiveAddressListActivity.this, NoticeEditActivity.class);
            Bundle bundle = new Bundle();
            // bundle.putInt("noticeId", noticeId);
            intent.putExtras(bundle);
            startActivityForResult(intent, ActivityUtils.EDIT_CODE);
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
        Builder builder = new Builder(ReceiveAddressListActivity.this);
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
            Log.i("Address", "11111" + declare.getUserId());

            List<ReceiveAddress> receiveAddressList = receiveAddressService.QueryReceiveAdressList(declare.getUserId());
            Log.i("Address", "11111" + receiveAddressList);
            for (int i = 0; i < receiveAddressList.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("receiveId", receiveAddressList.get(i).getReceiveId());
                map.put("userId", receiveAddressList.get(i).getUserId());
                map.put("receiveAddressName", receiveAddressList.get(i).getReceiveAddressName());
                map.put("receiveName", receiveAddressList.get(i).getReceiveName());
                map.put("receivePhone", receiveAddressList.get(i).getReceivePhone());
                map.put("receiveState", receiveAddressList.get(i).getReceiveState());
                if(receiveAddressList.get(i).getReceiveState().equals("1")){

                    declare.setReceiveId(receiveAddressList.get(i).getReceiveId());
                    declare.setReceiveName(receiveAddressList.get(i).getReceiveName());
                    declare.setReceiveAddressName(receiveAddressList.get(i).getReceiveAddressName());
                    declare.setReceiveUserId(receiveAddressList.get(i).getUserId());
                    declare.setReceivePhone(receiveAddressList.get(i).getReceivePhone());
                }
                list.add(map);
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }
        return list;
    }
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(ReceiveAddressListActivity.this);
        builder.setTitle("温馨提示");
        builder.setMessage("是否删除");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,final int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                receiveAddressService.DeleteReceiveAddress(Integer.parseInt(list.get(j).get("receiveId").toString()));

                            }
                        }).start();

                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();

    }
}
