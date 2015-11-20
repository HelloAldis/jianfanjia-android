package com.jianfanjia.cn.activity;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;

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
    private ViewPager viewPager = null;
    private List<View> bannerList = new ArrayList<View>();
    private static final int IMG_ID[] = {R.mipmap.p1, R.mipmap.p2,
            R.mipmap.p3, R.mipmap.p4};

    @Override
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        for (int i = 0; i < IMG_ID.length; i++) {
            ImageView view = new ImageView(this);
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            view.setBackgroundResource(IMG_ID[i]);
            bannerList.add(view);
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

            }
        });
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
    }


    @Override
    public void setListener() {

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }
}