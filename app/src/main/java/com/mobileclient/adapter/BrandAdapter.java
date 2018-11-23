package com.mobileclient.adapter;

import java.util.List;
import java.util.Map;;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileclient.activity.R;

public class BrandAdapter extends BaseAdapter {
	private Context context;
	private List ar;
	private LayoutInflater inflater;

	public BrandAdapter(Context context, List ar) {
		this.context = context;
		this.ar = ar;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ar.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return ar.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewHolder holder;
		if (arg1 == null) {
			holder = new viewHolder();
			arg1 = inflater.inflate(R.layout.brand_item, null);
			holder.mIvBimage = (ImageView) arg1.findViewById(R.id.iv_bimage);
			holder.mTvBname = (TextView) arg1.findViewById(R.id.tv_bname);

			arg1.setTag(holder);
		} else {
			holder = (viewHolder) arg1.getTag();
		}
		Map map = (Map) ar.get(arg0);
		holder.mIvBimage.setImageResource(Integer.parseInt(map.get("bimage")
				.toString()));
		holder.mIvBimage.setBackgroundColor(color.transparent);
		holder.mTvBname.setText(map.get("bname").toString());

		return arg1;
	}

	private class viewHolder {
		private ImageView mIvBimage;
		private TextView mTvBname;
	}

}
