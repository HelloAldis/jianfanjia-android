package com.jianfanjia.cn.activity.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.jianfanjia.cn.activity.ui.interf.ViewPagerClickListener;

public class ViewPageAdapter extends PagerAdapter {
	private static final String TAG = "ViewPageAdapter";
	private Context context;
	private List<View> list;
	private ViewPagerClickListener viewPagerClickListener;

	public ViewPageAdapter(Context context, List<View> list) {
		this.context = context;
		this.list = list;
	}

	public void setViewPagerClickListener(ViewPagerClickListener viewPagerClickListener){
		this.viewPagerClickListener = viewPagerClickListener;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(list.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		try {
			if (list.get(position).getParent() == null) {
				container.addView(list.get(position));
			} else {
				((ViewGroup) list.get(position).getParent()).removeView(list
						.get(position));
				container.addView(list.get(position));
			}
			list.get(position).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(viewPagerClickListener != null){
						viewPagerClickListener.onClickItem(position);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list.get(position);
	}

	@Override
	public float getPageWidth(int position) {
		return super.getPageWidth(position);
	}

}
