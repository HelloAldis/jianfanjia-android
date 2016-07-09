package com.jianfanjia.cn.ui.activity.my;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.ui.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.ui.fragment.CollectDecorationImgFragment;
import com.jianfanjia.cn.ui.fragment.CollectDesignerFragment;
import com.jianfanjia.cn.ui.fragment.CollectProductFragment;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:我的收藏
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class CollectActivity extends BaseSwipeBackActivity {
    private static final String TAG = CollectActivity.class.getName();

    @Bind(R.id.my_collect_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.tabLyout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initMainHeadView();
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.my_favorite));
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setDividerVisable(View.GONE);
    }

    private void setupViewPager(ViewPager viewPager) {
        List<SelectItem> listViews = new ArrayList<>();
        SelectItem designerItem = new SelectItem(CollectDesignerFragment.newInstance(), getResources().getString(R
                .string.designerText));
        SelectItem productItem = new SelectItem(CollectProductFragment.newInstance(), getResources().getString(R
                .string.str_case));
        SelectItem imgItem = new SelectItem(CollectDecorationImgFragment.newInstance(), getResources().getString(R
                .string.imgText));
        SelectItem diarySetItem = new SelectItem(CollectDecorationImgFragment.newInstance(), getResources().getString(R
                .string.diaryset));
        listViews.add(designerItem);
        listViews.add(productItem);
        listViews.add(imgItem);
        listViews.add(diarySetItem);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
    }

    @OnClick(R.id.head_back_layout)
    public void onClick() {
        appManager.finishActivity(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }
}
