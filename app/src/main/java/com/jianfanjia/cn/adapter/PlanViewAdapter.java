package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.tools.ScreenUtil;

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
    private List<String> mList;
    private ViewPagerClickListener itemClickListener;
    private ImageShow imageShow;

    public PlanViewAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
        imageShow = ImageShow.getImageShow();
    }

    public PlanViewAdapter(Context context, List<String> mList, ViewPagerClickListener itemClickListener) {
        this.context = context;
        this.mList = mList;
        this.itemClickListener = itemClickListener;
        imageShow = ImageShow.getImageShow();
    }


    public List<String> getList() {
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
                R.layout.list_item_plan_view_item, container, false);
        ImageView imageView = (ImageView) view
                .findViewById(R.id.list_item_plan_img);
        String imgid = mList.get(position);
        imageShow.displayThumbnailImage(imgid,imageView, ScreenUtil.getScreenWidth(context)/3);
        container.addView(view, 0);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onClickItem(position);
                }
            }
        });
        return view;
    }

    @Override
    public float getPageWidth(int position) {
        if (null != itemClickListener) {
            return 0.39f;
        }
        return super.getPageWidth(position);
    }
}