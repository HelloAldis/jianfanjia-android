package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.ui.interf.OnItemClickListener;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-14 11:08
 */
public class CollectPersonViewPageAdapter extends PagerAdapter {

    private static final String TAG = "CollectLoveStyleViewPageAdapter.class";
    private Context context;
    private List<LoveStyleItemInfo> list;
    private OnItemClickListener onItemClickListener;

    public CollectPersonViewPageAdapter(Context context, List<LoveStyleItemInfo> list, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }

    public List<LoveStyleItemInfo> getList() {
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
                R.layout.viewpager_item_collect_req, null, false);
        final LoveStyleItemInfo loveStyleItemInfo = list.get(position);
        ImageView contentView = (ImageView)view.findViewById(R.id.img);
//        imageShow.displayLocalImage(loveStyleItemInfo.getImageid(),contentView);
        contentView.setImageResource(loveStyleItemInfo.getResId());
//        ImageView foreGroundView = (ImageView)view.findViewById(R.id.img);
        if(loveStyleItemInfo.isSelector){
//            foreGroundView.setVisibility(View.VISIBLE);
            contentView.setSelected(true);
        }else{
            contentView.setSelected(false);
//            foreGroundView.setVisibility(View.GONE);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loveStyleItemInfo.setIsSelector(true);
                onItemClickListener.OnItemClick(position);
            }
        });
        container.addView(view,0);
        return view;
    }

    @Override
    public float getPageWidth(int position) {
        if(position == (list.size() - 1)){ //最后一屏，显示屏幕宽
            return 1.0f;
        }
        return 0.75f;
    }

    public static class LoveStyleItemInfo{
        int resId;
        boolean isSelector;

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public boolean isSelector() {
            return isSelector;
        }

        public void setIsSelector(boolean isSelector) {
            this.isSelector = isSelector;
        }
    }
}
