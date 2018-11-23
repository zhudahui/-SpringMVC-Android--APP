package com.mobileclient.adapter;

import java.util.List;
import java.util.Map;

import com.mobileclient.service.CompanyService;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ExpressOrderAdapter extends SimpleAdapter {
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

    public ExpressOrderAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) {
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
        com.mobileclient.adapter.ExpressOrderAdapter.ViewHolder holder = null;
        ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
        if (convertView == null) convertView = mInflater.inflate(R.layout.order_list_item, null);
        convertView.setTag("listViewTAG" + position);
        holder = new com.mobileclient.adapter.ExpressOrderAdapter.ViewHolder();
        /*绑定该view各个控件*/
        holder.userPhoto=convertView.findViewById(R.id.userPhoto);
        holder.userName=convertView.findViewById(R.id.userName);
        holder.orderName=convertView.findViewById(R.id.orderName);
        holder.expressCompanyName=convertView.findViewById(R.id.expressCompanyName);
        holder.expressCompanyAdress=convertView.findViewById(R.id.expressCompanyAdress);
        holder.receiveAdressName=convertView.findViewById(R.id.receiveAdressName);
        holder.addTime = (TextView)convertView.findViewById(R.id.addTime);
        holder.orderState=convertView.findViewById(R.id.orderState);
        /*设置各个控件的展示内容*/
        /*设置各个控件的展示内容*/
        String photoPath=mData.get(position).get("userPhoto").toString();
        Log.i("头像路径",""+photoPath);
        byte[] userPhoto_data = null;
        try {
            if(photoPath!=null) {
                // 获取图片数据
                userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + photoPath);
                Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
                holder.userPhoto.setImageBitmap(userPhoto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.userName.setText( mData.get(position).get("userName").toString());
        Log.i("33334444",""+mData.get(position).get("userName").toString());
        holder.orderName.setText( (mData.get(position).get("orderName").toString()));
        holder.expressCompanyName.setText( mData.get(position).get("expressCompanyName").toString());
        holder.expressCompanyAdress.setText( mData.get(position).get("expressCompanyAdress").toString());
        holder.receiveAdressName.setText( mData.get(position).get("receiveAdressName").toString());
        holder.addTime.setText(mData.get(position).get("addTime").toString());
        holder.orderState.setText( mData.get(position).get("orderState").toString());
        /*返回修改好的view*/
        return convertView;
    }

    static class ViewHolder{
        CircleImageView userPhoto;
        TextView userName;
        TextView orderName;
        TextView expressCompanyName;
        TextView expressCompanyAdress;
        TextView receiveAdressName;
        TextView addTime;
        TextView orderState;
    }
}
