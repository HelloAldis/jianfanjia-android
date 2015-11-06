package com.jianfanjia.cn.activity;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:帮助
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class HelpActivity extends BaseActivity {
    private static final String TAG = HelpActivity.class.getName();
    private ViewPager viewPager;
    private ViewGroup group = null;
    private ImageView[] tips;
    private List<View> bannerList = new ArrayList<View>();
    private static final int IMG_ID[] = {R.mipmap.p1, R.mipmap.p2,
            R.mipmap.p3, R.mipmap.p4};

    @Override
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        group = (ViewGroup) findViewById(R.id.viewGroup);
        for (int i = 0; i < IMG_ID.length; i++) {
            ImageView view = new ImageView(this);
            view.setImageResource(IMG_ID[i]);
            bannerList.add(view);
        }
        // 将点点加入到ViewGroup中
        tips = new ImageView[bannerList.size()];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(HelpActivity.this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(10, 10));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.shape_indicator_selected_oval);
            } else {
                tips[i].setBackgroundResource(R.drawable.shape_indicator_unselected_oval);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(20, 20));
            layoutParams.leftMargin = 15;
            layoutParams.rightMargin = 15;
            group.addView(imageView, layoutParams);
        }
        ViewPageAdapter pageAdapter = new ViewPageAdapter(HelpActivity.this,
                bannerList);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageSelected(int arg0) {
                setImageBackground(arg0 % bannerList.size());
            }
        });
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
    }


    @Override
    public void setListener() {

    }

    /**
     * 设置选中的索引的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.shape_indicator_selected_oval);
            } else {
                tips[i].setBackgroundResource(R.drawable.shape_indicator_unselected_oval);
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }
}