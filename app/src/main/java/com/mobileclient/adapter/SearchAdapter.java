package com.mobileclient.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.mobileclient.activity.R;
import com.mobileclient.domain.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：EditSearch
 * 类描述：
 * 创建人：tonycheng
 * 创建时间：2016/4/25 17:31
 * 邮箱：tonycheng93@outlook.com
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class SearchAdapter extends BaseAdapter implements Filterable {

    private Context mContext;

    /**
     * Contains the list of objects that represent the data of this Adapter.
     * Adapter数据源
     */
    private List<? extends Map<String, ?>> mData;;

    private LayoutInflater mInflater;

    //过滤相关
    /**
     * This lock is also used by the filter
     * (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     * 过滤器上的锁可以同步复制原始数据。
     */
    private final Object mLock = new Object();

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    //对象数组的备份，当调用ArrayFilter的时候初始化和使用。此时，对象数组只包含已经过滤的数据。
    private ArrayList<? extends Map<String, ?>> mOriginalValues;
    private ArrayFilter mFilter;

    public SearchAdapter(Context context, List<? extends Map<String, ?>> mDatas) {
        mContext = context;
        mData = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        Log.i("pppppooo",""+mData.size());
        return mData.size() > 0 ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.order_list_item, parent, false);
            holder = new SearchAdapter.ViewHolder();
            holder.userPhoto= (CircleImageView) convertView.findViewById(R.id.userPhoto);
            // holder.userName=convertView.findViewById(R.id.userName);
            holder.orderName= (TextView) convertView.findViewById(R.id.orderName);
            holder.expressCompanyName= (TextView) convertView.findViewById(R.id.expressCompanyName);
            holder.expressCompanyAddress= (TextView) convertView.findViewById(R.id.expressCompanyAddress);
            // holder.receiveAddressName=convertView.findViewById(R.id.receiveAddressName);
            holder.receiveAddressName= (TextView) convertView.findViewById(R.id.tv_receiveAddressName);
            holder.addTime = (TextView)convertView.findViewById(R.id.addTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.userPhoto.setImageBitmap((Bitmap) mData.get(position).get("userPhoto"));
        // holder.userName.setText( mData.get(position).get("userName").toString());
        holder.orderName.setText( (mData.get(position).get("orderName").toString()));
        holder.expressCompanyName.setText( mData.get(position).get("expressCompanyName").toString());
        holder.expressCompanyAddress.setText( mData.get(position).get("expressCompanyAddress").toString());
        holder.receiveAddressName.setText( mData.get(position).get("receiveAddressName").toString());
        holder.addTime.setText(mData.get(position).get("addTime").toString());
        return convertView;
    }

    public class ViewHolder {
        CircleImageView userPhoto;
        //TextView userName;
        TextView orderName;
        TextView expressCompanyName;
        TextView expressCompanyAddress;
        TextView receiveAddressName;
        TextView addTime;
        TextView orderState;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    /**
     * 过滤数据的类
     */
    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     * <p/>
     * 一个带有首字母约束的数组过滤器，每一项不是以该首字母开头的都会被移除该list。
     */
    private class ArrayFilter extends Filter {
        //执行刷选
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();//过滤的结果
            //原始数据备份为空时，上锁，同步复制原始数据
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mData);
                }
            }
            //当首字母为空时
            if (prefix == null || prefix.length() == 0) {
                ArrayList<? extends Map<String, ?>> list;
                synchronized (mLock) {//同步复制一个原始备份数据
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();//此时返回的results就是原始的数据，不进行过滤
            } else {
                String prefixString = prefix.toString().toLowerCase();//转化为小写

                ArrayList<? extends Map<String, ?>> values;
                synchronized (mLock) {//同步复制一个原始备份数据
                    values = new ArrayList<>(mOriginalValues);
                }
                final int count = values.size();
                final ArrayList<Map<String, ?>> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                   //从List<Order>中拿到Order对象
//                    final String valueText = value.toString().toLowerCase();
                    final String valueText1 = values.get(i).get("nickName").toString().toLowerCase();//Order对象的用户名属性作为过滤的参数
                    final String valueText2=values.get(i).get("orderName").toString().toLowerCase();//Order对象的订单名属性也作为过滤参数
                    final String valueText3=values.get(i).get("orderPay").toString().toLowerCase();//Order对象的酬劳属性也作为过滤参数
                    final String valueText4=values.get(i).get("addTime").toString().toLowerCase();//Order对象的创建时间属性也作为过滤参数
                    final String valueText5=values.get(i).get("orderState").toString().toLowerCase();//Order对象的订单状态属性也作为过滤参数
                    final String valueText6=values.get(i).get("expressCompanyName").toString().toLowerCase();//Order对象的快递公司属性也作为过滤参数
                    final String valueText7=values.get(i).get("remark").toString().toLowerCase();//Order对象的备注属性也作为过滤参数
                    final String valueText8=values.get(i).get("receiveAddressName").toString().toLowerCase();//Order对象的收货地址属性也作为过滤参数
                    final String valueText9=values.get(i).get("expressCompanyAddress").toString().toLowerCase();//Order对象的快递公司属性也作为过滤参
                    // First match against the whole, non-splitted value
                    if (valueText1.startsWith(prefixString) || valueText1.indexOf(prefixString.toString()) != -1
                            ||valueText2.startsWith(prefixString) || valueText2.indexOf(prefixString.toString()) != -1
                            ||valueText3.startsWith(prefixString) || valueText3.indexOf(prefixString.toString()) != -1
                            ||valueText4.startsWith(prefixString) || valueText4.indexOf(prefixString.toString()) != -1
                            ||valueText5.startsWith(prefixString) || valueText5.indexOf(prefixString.toString()) != -1
                            ||valueText6.startsWith(prefixString) || valueText6.indexOf(prefixString.toString()) != -1
                            ||valueText7.startsWith(prefixString) || valueText7.indexOf(prefixString.toString()) != -1
                            ||valueText8.startsWith(prefixString) || valueText8.indexOf(prefixString.toString()) != -1
                            ||valueText9.startsWith(prefixString) || valueText9.indexOf(prefixString.toString()) != -1) {//第一个字符是否匹配
                        newValues.add(values.get(i));//将这个item加入到数组对象中
                    } else {//处理首字符是空格
                        final String[] words = valueText1.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {//一旦找到匹配的就break，跳出for循环
                                newValues.add(values.get(i));
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;//此时的results就是过滤后的List<Order>数组
                results.count = newValues.size();
            }
            return results;
        }

        //刷选结果
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            //noinspection unchecked
            mData = (List<? extends Map<String, ?>>) results.values;//此时，Adapter数据源就是过滤后的Results
            if (results.count > 0) {
                notifyDataSetChanged();//这个相当于从mDatas中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
            } else {
                /**
                 * 数据容器变化 ----> notifyDataSetInValidated

                 容器中的数据变化  ---->  notifyDataSetChanged
                 */
                notifyDataSetInvalidated();//当results.count<=0时，此时数据源就是重新new出来的，说明原始的数据源已经失效了
            }
        }
    }
}
