package com.jianfanjia.cn.activity.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.ui.interf.ViewPagerClickListener;
import com.jianfanjia.cn.activity.tools.ImageShow;

import java.util.List;

/**
 * Name: PreviewAdapter
 * User: fengliang
 * Date: 2015-10-29
 * Time: 09:16
 */
public class PreviewAdapter extends PagerAdapter {
    private static final String TAG = "PreviewAdapter";
    private ViewPagerClickListener listener;
    private Context context;
    private List<String> mList;
    private ImageShow imageShow;

    public PreviewAdapter(Context context, List<String> mList, ViewPagerClickListener listener) {
        this.context = context;
        this.mList = mList;
        this.listener = listener;
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
                R.layout.list_item_preview_view_item, container, false);
        ImageView imageView = (ImageView) view
                .findViewById(R.id.list_item_plan_img);
        imageShow.displayScreenWidthThumnailImage(context, mList.get(position), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClickItem(position);
                }
            }
        });
        container.addView(view, 0);
        return view;
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }
}