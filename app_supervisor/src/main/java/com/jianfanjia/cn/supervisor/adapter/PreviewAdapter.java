package com.jianfanjia.cn.supervisor.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.activity.common.CommonShowPicActivity;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.common.tool.LogTool;
import me.iwf.photopicker.entity.AnimationRect;

/**
 * Name: PreviewAdapter
 * User: fengliang
 * Date: 2015-10-29
 * Time: 09:16
 */
public class PreviewAdapter extends PagerAdapter {
    private static final String TAG = "PreviewAdapter";
    private Context context;
    private List<String> mList;
    private ImageShow imageShow;

    public PreviewAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
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
        final ImageView imageView = (ImageView) view
                .findViewById(R.id.list_item_plan_img);
        imageShow.displayScreenWidthThumnailImage(context, mList.get(position), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationRect animationRect = AnimationRect.buildFromImageView(imageView);
                List<AnimationRect> animationRectList = new ArrayList<>();
                animationRectList.add(animationRect);
                gotoShowBigPic(position, animationRectList);
            }
        });
        container.addView(view, 0);
        return view;
    }

    private void gotoShowBigPic(int position, List<AnimationRect>
            animationRectList) {
        LogTool.d("position:" + position);
        CommonShowPicActivity.intentTo(context, (ArrayList<String>) mList, (ArrayList<AnimationRect>)
                animationRectList, position);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }
}