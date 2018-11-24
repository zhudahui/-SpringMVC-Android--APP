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
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ReceiveAddressListActivity extends Activity {
    ReceiveAddressAdapter adapter;
    ListView lv;
    List<Map<String, Object>> list;

    private ReceiveAddress queryConditionReceiveAddress;

    private MyProgressDialog dialog; //进度条	@Override
    ReceiveAddressService receiveAddressService=new ReceiveAddressService();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.notice_list);
        dialog = MyProgressDialog.getInstance(this);
        Declare declare = (Declare) getApplicationContext();
        String username = declare.getUserName();
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("收货地址");
        setViews();
    }

    //结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            if(extras != null)
                queryConditionReceiveAddress = (ReceiveAddress) extras.getSerializable("queryConditionReceiveAddress");
            setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
            setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
            queryConditionReceiveAddress = null;
            setViews();
        }
    }

    private void setViews() {
        lv = findViewById(R.id.address_list_view);
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
                        adapter = new ReceiveAddressAdapter(ReceiveAddressListActivity.this, list,
                                R.layout.notice_list_item,
                                new String[] { "receiveAddressName","receiveName","receivePhone" },
                                new int[] { R.id.receiveAddressName,R.id.receiveName,R.id.receivePhone,},lv);
                        lv.setAdapter(adapter);
                    }
                });
            }
        }.start();

    }


    // 长按菜单响应函数
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {  //编辑新闻公告信息
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
            startActivityForResult(intent,ActivityUtils.EDIT_CODE);
        } else if (item.getItemId() == 1) {// 删除新闻公告信息
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
            List<ReceiveAddress> receiveAddressList= receiveAddressService.QueryReceiveAdress(queryConditionReceiveAddress);
            for (int i = 0; i < receiveAddressList.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("receiveAddressName", receiveAddressList.get(i).getReceiveAddressName());
                map.put("receiveName", receiveAddressList.get(i).getReceiveName());
                map.put("receivePhone",receiveAddressList.get(i).getReceivePhone());
                list.add(map);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }
        return list;
    }

}
