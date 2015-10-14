package com.jianfanjia.cn.fragment;

import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerListAdapter;
import com.jianfanjia.cn.adapter.MyViewPageAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerByMarchedInfo;
import com.jianfanjia.cn.bean.DesignerListInfo;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshScrollView;
import com.jianfanjia.cn.tools.ViewPagerManager;
import com.jianfanjia.cn.tools.ViewPagerManager.ShapeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:首页
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class HomeFragment extends BaseFragment implements
        OnRefreshListener2<ScrollView> {
    private static final String TAG = HomeFragment.class.getName();
    private PullToRefreshScrollView mPullRefreshScrollView = null;
    private ListView designer_listview = null;
    private DesignerListAdapter adapter = null;
    private List<DesignerListInfo> designerList = new ArrayList<DesignerListInfo>();
    private List<DesignerByMarchedInfo> designerByMarchedList = new ArrayList<DesignerByMarchedInfo>();

    private static final int BANNER_ICON[] = {R.mipmap.bg_home_banner1,
            R.mipmap.bg_home_banner2, R.mipmap.bg_home_banner3,
            R.mipmap.bg_home_banner4};

    @Override
    public void initView(View view) {
        initBannerView();
        mPullRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setMode(Mode.BOTH);
        designer_listview = (ListView) view.findViewById(R.id.designer_listview);
        designer_listview.setFocusable(false);
        initDesignerByMarchedList(view);
        initDesignerList();
    }

    private void initBannerView() {
        ViewPagerManager contoler = new ViewPagerManager(getActivity());
        contoler.setmShapeType(ShapeType.OVAL);// 设置指示器的形状为矩形，默认是圆形
        List<View> bannerList = new ArrayList<View>();
        for (int i = 0; i < BANNER_ICON.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(BANNER_ICON[i]);
            bannerList.add(imageView);
        }
        contoler.init(bannerList);
        contoler.setAutoSroll(true);
    }

    private void initDesignerByMarchedList(View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        for (int i = 0; i < 3; i++) {
            DesignerByMarchedInfo info = new DesignerByMarchedInfo();
            info.setDesignerName("设计师" + i);
            designerByMarchedList.add(info);
        }
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getActivity(), designerByMarchedList, new ViewPagerClickListener() {
            @Override
            public void onClickItem(int potition) {
                makeTextLong("设计师");
            }
        });
        viewPager.setAdapter(myViewPageAdapter);
    }

    private void initDesignerList() {
        for (int i = 0; i < 5; i++) {
            DesignerListInfo info = new DesignerListInfo();
            info.setXiaoquInfo("小区名称" + 1);
            info.setProduceInfo("100平米,三室二厅,现代简约");
            designerList.add(info);
        }
        adapter = new DesignerListAdapter(getActivity(), designerList);
        designer_listview.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        mPullRefreshScrollView.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        // 下拉刷新(从第一页开始装载数据)
        String label = DateUtils.formatDateTime(getActivity(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        // Update the LastUpdatedLabel
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        mPullRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullRefreshScrollView.onRefreshComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }
}
