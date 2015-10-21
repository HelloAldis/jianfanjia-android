package com.jianfanjia.cn.fragment;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.jianfanjia.cn.activity.DesignerCaseInfoActivity;
import com.jianfanjia.cn.activity.DesignerInfoActivity;
import com.jianfanjia.cn.activity.EditRequirementActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerListAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerListInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ListItemClickListener;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshScrollView;
import com.jianfanjia.cn.tools.LogTool;
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
        OnRefreshListener2<ScrollView>, ListItemClickListener, ApiUiUpdateListener {
    private static final String TAG = HomeFragment.class.getName();
    private PullToRefreshScrollView mPullRefreshScrollView = null;
    private LinearLayout marchedLayout = null;
    private LinearLayout noMarchedLayout = null;
    private LinearLayout designerLayout_1 = null;
    private LinearLayout designerLayout_2 = null;
    private LinearLayout designerLayout_3 = null;
    private Button addXuQiu = null;
    private ListView designer_listview = null;
    private DesignerListAdapter adapter = null;
    private List<DesignerListInfo> designerList = new ArrayList<DesignerListInfo>();


    private static final int BANNER_ICON[] = {R.mipmap.bg_home_banner1,
            R.mipmap.bg_home_banner2, R.mipmap.bg_home_banner3,
            R.mipmap.bg_home_banner4};

    @Override
    public void initView(View view) {
        initBannerView();
        mPullRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
        marchedLayout = (LinearLayout) view.findViewById(R.id.marched_layout);
        noMarchedLayout = (LinearLayout) view.findViewById(R.id.no_marched_layout);
        noMarchedLayout.setVisibility(View.GONE);
        addXuQiu = (Button) view.findViewById(R.id.btn_add);
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
        /*designerLayout_1 = (LinearLayout) view.findViewById(R.id.designerLayout_1);
        designerLayout_2 = (LinearLayout) view.findViewById(R.id.designerLayout_2);
        designerLayout_3 = (LinearLayout) view.findViewById(R.id.designerLayout_3);*/
    }

    private void initDesignerList() {
        for (int i = 0; i < 5; i++) {
            DesignerListInfo info = new DesignerListInfo();
            /*info.setXiaoquInfo("小区名称" + 1);
            info.setProduceInfo("100平米,三室二厅,现代简约");*/
            designerList.add(info);
        }
//        adapter = new DesignerListAdapter(getActivity(), designerList, this);
//        designer_listview.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        mPullRefreshScrollView.setOnRefreshListener(this);
        /*designerLayout_1.setOnClickListener(this);
        designerLayout_2.setOnClickListener(this);
        designerLayout_3.setOnClickListener(this);*/
        addXuQiu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.designerLayout_1:
                startActivity(DesignerInfoActivity.class);
                break;
            case R.id.designerLayout_2:
                break;
            case R.id.designerLayout_3:
                break;*/
            case R.id.btn_add:
                startActivity(EditRequirementActivity.class);
                break;
            default:
                break;
        }
    }

    private void getHomePageDesigners(int from, int limit) {
        JianFanJiaClient.getHomePageDesigners(getActivity(), from, limit, this, this);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data);
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
    }

    @Override
    public void onMaxClick(int position) {
        makeTextLong("点击案例图" + position);
        startActivity(DesignerCaseInfoActivity.class);
    }

    @Override
    public void onMinClick(int position) {
        makeTextLong("点击设计师头像" + position);
        startActivity(DesignerInfoActivity.class);
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
