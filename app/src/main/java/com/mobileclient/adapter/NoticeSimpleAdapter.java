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
import android.widget.ListView;
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class NoticeSimpleAdapter extends SimpleAdapter { 
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

    public NoticeSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
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
	  if (convertView == null) convertView = mInflater.inflate(R.layout.notice_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_noticeTitle = (TextView)convertView.findViewById(R.id.tv_noticeTitle);
	 // holder.tv_noticeContent = (TextView)convertView.findViewById(R.id.tv_noticeContent);
	  holder.tv_publishDate = (TextView)convertView.findViewById(R.id.tv_publishDate);
	  //holder.tv_noticeFile=convertView.findViewById(R.id.tv_noticeFile);
	  /*设置各个控件的展示内容*/
	  holder.tv_noticeTitle.setText("标题：" + mData.get(position).get("noticeTitle").toString());
	  //holder.tv_noticeContent.setText("内容：" + mData.get(position).get("noticeContent").toString());
	  holder.tv_publishDate.setText("发布时间：" + mData.get(position).get("publishDate").toString());
	  //holder.tv_noticeFile.setText("附件"+mData.get(position).get("noticeFile").toString());
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_noticeTitle;
    	//TextView tv_noticeContent;
    	TextView tv_publishDate;
    	//TextView tv_noticeFile;
    }
} 