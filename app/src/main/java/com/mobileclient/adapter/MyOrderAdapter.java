package com.mobileclient.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MyOrderAdapter extends SimpleAdapter {
    /*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
    /*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData;
    private static final String TAG = "uploadImage";
    private Bitmap orc_bitmap;//获取下载后的图片
    private LayoutInflater mInflater;
    Context context = null;
    String  photoPath;
    private ListView mListView;
    //图片异步缓存加载类,带内存缓存和文件缓存
    private SyncImageLoader syncImageLoader;
    File file;
    private ReceiveAddressAdapter.MyClickListener mListener;
    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener{
        public void clickListener(View v);
    }
    public MyOrderAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) {
        super(context, data, resource, from, to);
        mTo = to;
        mFrom = from;
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
        mListView = listView;
        syncImageLoader = SyncImageLoader.getInstance();
        ListViewOnScrollListener onScrollListener = new ListViewOnScrollListener(syncImageLoader,listView,getCount());
        mListView.setOnScrollListener(onScrollListener);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        com.mobileclient.adapter.MyOrderAdapter.ViewHolder holder = null;
        ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
        if (convertView == null) convertView = mInflater.inflate(R.layout.my_order_two_item, null);
        convertView.setTag("listViewTAG" + position);
        holder = new com.mobileclient.adapter.MyOrderAdapter.ViewHolder();
        /*绑定该view各个控件*/
        holder.orderPic=convertView.findViewById(R.id.img_takeUserPhoto);
        // holder.userName=convertView.findViewById(R.id.userName);
        holder.tv_orderName=convertView.findViewById(R.id.tv_orderName);
        holder.tv_orderState=convertView.findViewById(R.id.tv_orderState);
        holder.tv_orderPay=convertView.findViewById(R.id.tv_orderPay);
        // holder.receiveAddressName=convertView.findViewById(R.id.receiveAddressName);
        holder.addTime=convertView.findViewById(R.id.tv_addTime);
        holder.tv_button=convertView.findViewById(R.id.tv_button);
        //holder.orderState=convertView.findViewById(R.id.orderState);
        /*设置各个控件的展示内容*/
        /*设置各个控件的展示内容*/
        if(mData.get(position).get("orderPic").toString().equals("--")) {
            holder.orderPic.setImageBitmap((Bitmap) mData.get(position).get("userPhoto"));
        }
        else
            holder.orderPic.setImageBitmap((Bitmap) mData.get(position).get("orderPic"));
        //holder.orderPic.setImageBitmap((Bitmap) mData.get(position).get("orderPic"));
        // holder.userName.setText( mData.get(position).get("userName").toString());
        holder.tv_orderName.setText("代取物品："+(mData.get(position).get("orderName").toString()));
        holder.tv_orderState.setText( mData.get(position).get("orderState").toString());
        holder.tv_orderPay.setText("费用：" +mData.get(position).get("orderPay").toString());
        holder.addTime.setText("下单时间："+mData.get(position).get("addTime").toString());
        holder.tv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemEditListener.onDeleteClick(position);
            }
        });
        if(mData.get(position).get("orderState").toString().equals("待接单")){
            holder.tv_button.setText("取消订单");
        }else if(mData.get(position).get("orderState").toString().equals("送单中")){
            holder.tv_button.setText("确认收货");
        }
        else if(mData.get(position).get("orderState").toString().equals("已送达")){
            holder.tv_button.setText("确认收货");
        }else if(mData.get(position).get("orderState").toString().equals("交易结束")&&mData.get(position).get("evaluate").toString().equals("-+-")){
            holder.tv_button.setText("评价");
        }else
            holder.tv_button.setVisibility(View.GONE);

        // holder.orderState.setText( mData.get(position).get("orderState").toString());
        /*返回修改好的view*/
        return convertView;
    }

    static class ViewHolder{
        ImageView orderPic;      //实物图
        TextView tv_orderName;
        TextView tv_orderState;
        TextView tv_orderPay;
        TextView addTime;
        TextView tv_button;   //根据订单状态分为取消订单、消失、确认收货、评价四个

    }
    //响应按钮点击事件,调用子定义接口，并传入View

    /**
     * 编辑按钮的监听接口
     */
    public interface onItemEditListener {
        void onDeleteClick(int i);
    }
    private MyOrderAdapter.onItemEditListener mOnItemEditListener;

    public void setOnItemEditClickListener(MyOrderAdapter.onItemEditListener mOnItemEditListener) {
        this.mOnItemEditListener = mOnItemEditListener;
    }

}
