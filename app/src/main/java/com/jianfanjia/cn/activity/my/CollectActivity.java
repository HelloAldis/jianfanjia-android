package com.jianfanjia.cn.activity.my;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.fragment.DecorationImgFragment;
import com.jianfanjia.cn.fragment.MyFavoriteDesignerFragment;
import com.jianfanjia.cn.fragment.ProductFragment;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:我的收藏
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class CollectActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = CollectActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;

    @Override
    public void initView() {
        initMainHeadView();
        tabLayout = (TabLayout) findViewById(R.id.tabLyout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_collect_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.my_favorite));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    private void setupViewPager(ViewPager viewPager) {
        List<SelectItem> listViews = new ArrayList<SelectItem>();
        SelectItem designerItem = new SelectItem(new MyFavoriteDesignerFragment(), getResources().getString(R.string.designerText));
        SelectItem productItem = new SelectItem(new ProductFragment(), getResources().getString(R.string.productText));
        SelectItem imgItem = new SelectItem(new DecorationImgFragment(), getResources().getString(R.string.imgText));
        listViews.add(designerItem);
        listViews.add(productItem);
        listViews.add(imgItem);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }
}
