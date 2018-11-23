package com.mobileclient.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class GuidePageAdapter extends PagerAdapter {
	private Context context;
	private List<View> ar;

	public GuidePageAdapter(Context context, List<View> ar) {
		this.context = context;
		this.ar = ar;
	}

	@Override
	public int getCount() {
		return ar.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(ar.get(position));
		return ar.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(ar.get(position));
	}

}
