package com.mobileclient.adapter;



import java.util.List;
import java.util.Map;

import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ReceiveAddressAdapter extends SimpleAdapter implements View.OnClickListener {
    /*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
    /*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData;

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //图片异步缓存加载类,带内存缓存和文件缓存
    private SyncImageLoader syncImageLoader;
    private MyClickListener mListener;
    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener{
        public void clickListener(View v);
    }
    public ReceiveAddressAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, ListView listView) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ReceiveAddressAdapter.ViewHolder holder = null;
        ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
        if (convertView == null) convertView = mInflater.inflate(R.layout.receiveadress_list_item, null);
        convertView.setTag("listViewTAG" + position);
        holder = new ReceiveAddressAdapter.ViewHolder();
        /*绑定该view各个控件*/
        holder.receiveAddressName = (TextView)convertView.findViewById(R.id.receiveAddressName);
        holder.receiveName = (TextView)convertView.findViewById(R.id.receiveName);
        holder.receivePhone = (TextView)convertView.findViewById(R.id.receivePhone);
        holder.edit=convertView.findViewById(R.id.edit);
        holder.edit.setOnClickListener(this);
        holder.edit.setTag(position);
        /*设置各个控件的展示内容*/
        holder.receiveAddressName.setText( mData.get(position).get("receiveAddressName").toString());
        holder.receiveName.setText(mData.get(position).get("receiveName").toString());
        holder.receivePhone.setText(mData.get(position).get("receivePhone").toString());
        /*返回修改好的view*/
        return convertView;
    }

    static class ViewHolder{
        TextView receiveAddressName;
        TextView receiveName;
        TextView receivePhone;
        ImageView edit;
    }
    //响应按钮点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {
        mListener.clickListener(v);
    }



}
