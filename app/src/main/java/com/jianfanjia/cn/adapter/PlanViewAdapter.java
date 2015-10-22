package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;

import java.util.HashMap;
import java.util.List;

/**
 * Name: PlanViewAdapter
 * User: fengliang
 * Date: 2015-10-22
 * Time: 17:14
 */
public class PlanViewAdapter extends PagerAdapter {
    private static final String TAG = "PlanViewAdapter";
    private Context context;
    private List<HashMap<String, Object>> mList;

    public PlanViewAdapter(Context context, List<HashMap<String, Object>> mList) {
        this.context = context;
        this.mList = mList;
    }


    public List<HashMap<String, Object>> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.fragment_home_designerinfo_item, container, false);
        ImageView imageView = (ImageView) view
                .findViewById(R.id.list_item_head_img);
        TextView titleView = (TextView) view
                .findViewById(R.id.list_item_designer_text);
        HashMap<String, Object> hashMap = this.mList.get(position);
        imageView.setImageResource((int) hashMap.get("content"));
        titleView.setText("hello");
        container.addView(view, 0);
        return view;
    }

    @Override
    public float getPageWidth(int position) {
        return 0.29f;
    }
}