package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.DesignerByMarchedInfo;
import com.jianfanjia.cn.interf.ViewPagerClickListener;

import java.util.List;

public class MyViewPageAdapter extends PagerAdapter {
    private static final String TAG = "MyViewPageAdapter";
    private Context context;
    private List<DesignerByMarchedInfo> list;
    private ViewPagerClickListener itemClickListener;

    public MyViewPageAdapter(Context context, List<DesignerByMarchedInfo> list) {
        this.context = context;
        this.list = list;
    }

    public MyViewPageAdapter(Context context, List<DesignerByMarchedInfo> list,
                             ViewPagerClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    public List<DesignerByMarchedInfo> getList() {
        return list;
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
        imageView.setImageResource(R.mipmap.pix_default);
        titleView.setText(list.get(position).getDesignerName());
        container.addView(view, 0);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                itemClickListener.onClickItem(position);
            }
        });
        return view;
    }

    @Override
    public float getPageWidth(int position) {
        return 0.25f;
    }

}
