package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AppointDesignerViewPageAdapter extends PagerAdapter {
    private static final String TAG = "ViewPageAdapter";
    private Context context;
    private List<View> list;

    public AppointDesignerViewPageAdapter(Context context,List<View> viewList) {
        this.context = context;
        this.list = viewList;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.get(position);
    }



    @Override
    public float getPageWidth(int position) {
//		return super.getPageWidth(position);
        if (position == (list.size() - 1)) {//最后一屏，显示屏幕宽
            return 1.0f;
        }
        return 0.8f;
    }



}
