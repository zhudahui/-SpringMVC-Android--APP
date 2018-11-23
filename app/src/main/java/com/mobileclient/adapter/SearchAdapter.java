package com.mobileclient.adapter;

import java.util.List;
import java.util.Map;


import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import android.annotation.SuppressLint;
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

public class SearchAdapter extends SimpleAdapter {
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

    public SearchAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) {
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
        ViewHolder holder = null;
        ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
        if (convertView == null) convertView = mInflater.inflate(R.layout.search_list_item, null);
        convertView.setTag("listViewTAG" + position);
        holder = new SearchAdapter.ViewHolder();
        /*绑定该view各个控件*/
        holder.photo =  convertView.findViewById(R.id.phone);
        holder.userObj=convertView.findViewById(R.id.userObj);
        holder.taskTitle=convertView.findViewById(R.id.orderName);
        holder.companyObj=convertView.findViewById(R.id.companyObj);
        holder.expressCompanyAdress=convertView.findViewById(R.id.expressCompanyAdress);
        holder.takePlace=convertView.findViewById(R.id.receiveAdress);
        holder.addTime=convertView.findViewById(R.id.addTime);
        holder.takeStateObj=convertView.findViewById(R.id.takeStateObj);
        /*设置各个控件的展示内容*/
        String photoPath=mData.get(position).get("userPhoto").toString();
        Log.i("头像路径",""+photoPath);
        byte[] userPhoto_data = null;
        try {
            if(photoPath!=null) {
                // 获取图片数据
                userPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + photoPath);
                Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
                holder.photo.setImageBitmap(userPhoto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         holder.userObj.setText(mData.get(position).get("userObj").toString());
         holder.taskTitle.setText(mData.get(position).get("taskTitle").toString());
         holder.companyObj.setText(mData.get(position).get("companyObj").toString());
         holder.expressCompanyAdress.setText(mData.get(position).get("expressCompanyAdress").toString());
         holder.takePlace.setText(mData.get(position).get("takePlace").toString());
         holder.addTime.setText(mData.get(position).get("addTime").toString());
         holder.takeStateObj.setText(mData.get(position).get("takeStateObj").toString());
        //holder.photo.setImageBitmap("代拿任务：" + mData.get(position).get("taskTitle").toString());
       // Log.i("33334444",""+mData.get(position).get("taskTitle").toString());
       // holder.tv_companyObj.setText("物流公司：" + (mData.get(position).get("companyObj").toString()));
        /*返回修改好的view*/
        return convertView;
    }

    static class ViewHolder{
        CircleImageView photo;
        TextView userObj;
        TextView taskTitle;
        TextView companyObj;
        TextView expressCompanyAdress;
        TextView takePlace;
        TextView addTime;
        TextView takeStateObj;
    }
}
