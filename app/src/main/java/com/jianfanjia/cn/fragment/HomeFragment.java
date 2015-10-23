package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.jianfanjia.cn.activity.DesignerCaseInfoActivity;
import com.jianfanjia.cn.activity.DesignerInfoActivity;
import com.jianfanjia.cn.activity.EditRequirementActivity_;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerListAdapter;
import com.jianfanjia.cn.adapter.MarchDesignerAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerListInfo;
import com.jianfanjia.cn.bean.HomeDesignersInfo;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.bean.Requirement;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ListItemClickListener;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshScrollView;
import com.jianfanjia.cn.tools.JsonParser;
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
        OnRefreshListener2<ScrollView>, ListItemClickListener, ApiUiUpdateListener, OnItemClickListener {
    private static final String TAG = HomeFragment.class.getName();
    private PullToRefreshScrollView mPullRefreshScrollView = null;
    private LinearLayout marchedLayout = null;
    private LinearLayout noMarchedLayout = null;
    private GridView marchDesignerView = null;
    private MarchDesignerAdapter marchDesignerAdapter = null;
    private List<OrderDesignerInfo> designers = new ArrayList<OrderDesignerInfo>();

    private Button addXuQiu = null;
    private ListView designer_listview = null;
    private DesignerListAdapter designerAdapter = null;
    private List<DesignerListInfo> designerList = new ArrayList<DesignerListInfo>();

    private int FROM = 0;

    private static final int BANNER_ICON[] = {R.mipmap.bg_home_banner1,
            R.mipmap.bg_home_banner2, R.mipmap.bg_home_banner3,
            R.mipmap.bg_home_banner4};

    @Override
    public void initView(View view) {
        initBannerView();
        mPullRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setMode(Mode.BOTH);
        mPullRefreshScrollView.setOverScrollMode(PullToRefreshBase.OVER_SCROLL_NEVER);
        marchedLayout = (LinearLayout) view.findViewById(R.id.marched_layout);
        noMarchedLayout = (LinearLayout) view.findViewById(R.id.no_marched_layout);
        addXuQiu = (Button) view.findViewById(R.id.btn_add);
        marchDesignerView = (GridView) view.findViewById(R.id.marchGridview);
        designer_listview = (ListView) view.findViewById(R.id.designer_listview);
        designer_listview.setFocusable(false);
        initHomePage();
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

    private void initHomePage() {
        getHomePageDesigners(FROM, Constant.LIMIT);
        designerAdapter = new DesignerListAdapter(getActivity(), designerList, this);
        designer_listview.setAdapter(designerAdapter);
    }

    @Override
    public void setListener() {
        mPullRefreshScrollView.setOnRefreshListener(this);
        addXuQiu.setOnClickListener(this);
        marchDesignerView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                startActivity(EditRequirementActivity_.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        OrderDesignerInfo orderDesignerInfo = designers.get(position);
        String designertid = orderDesignerInfo.get_id();
        LogTool.d(TAG, "designertid:" + designertid);
        Bundle designerBundle = new Bundle();
        designerBundle.putString(Global.DESIGNER_ID, designertid);
        startActivity(DesignerInfoActivity.class, designerBundle);
    }

    private void getHomePageDesigners(int from, int limit) {
        JianFanJiaClient.getHomePageDesigners(getActivity(), from, limit, this, this);
    }

    @Override
    public void preLoad() {
        marchedLayout.setVisibility(View.GONE);
        noMarchedLayout.setVisibility(View.GONE);
        showWaitDialog(R.string.loding);
    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data=============================" + data);
        hideWaitDialog();
        parseResponse(data.toString());
        mPullRefreshScrollView.onRefreshComplete();
        designerAdapter.notifyDataSetChanged();
    }

    private void parseResponse(String response) {
        HomeDesignersInfo homeDesignersInfo = JsonParser.jsonToBean(response, HomeDesignersInfo.class);
        LogTool.d(TAG, "homeDesignersInfo:" + homeDesignersInfo);
        if (null != homeDesignersInfo) {
            Requirement requirement = homeDesignersInfo.getRequirement();
            LogTool.d(TAG, "requirement=" + requirement);
            if (null != requirement) {
                designers = requirement.getDesigners();
                LogTool.d(TAG, "designers=" + designers);
                if (null != designers && designers.size() > 0) {
                    marchedLayout.setVisibility(View.VISIBLE);
                    noMarchedLayout.setVisibility(View.GONE);
                    marchDesignerAdapter = new MarchDesignerAdapter(getActivity(), designers);
                    marchDesignerView.setAdapter(marchDesignerAdapter);
                } else {
                    marchedLayout.setVisibility(View.GONE);
                    noMarchedLayout.setVisibility(View.GONE);
                }
            } else {
                marchedLayout.setVisibility(View.GONE);
                noMarchedLayout.setVisibility(View.VISIBLE);
            }
            designerList.addAll(homeDesignersInfo.getDesigners());
            LogTool.d(TAG, "designerList:" + designerList.size());
            designerAdapter.notifyDataSetChanged();
        } else {

        }
    }

    @Override
    public void loadFailture(String error_msg) {
        hideWaitDialog();
        mPullRefreshScrollView.onRefreshComplete();
        makeTextLong(error_msg);
    }

    @Override
    public void onMaxClick(int position) {
        DesignerListInfo designerListInfo = designerList.get(position);
        Product product = designerListInfo.getProduct();
        String productid = product.get_id();
        LogTool.d(TAG, "productid:" + productid);
        Bundle productBundle = new Bundle();
        productBundle.putString(Global.DESIGNER_ID, productid);
        startActivity(DesignerCaseInfoActivity.class, productBundle);
    }

    @Override
    public void onMinClick(int position) {
        DesignerListInfo designerListInfo = designerList.get(position);
        String designertid = designerListInfo.get_id();
        LogTool.d(TAG, "designertid:" + designertid);
        Bundle designerBundle = new Bundle();
        designerBundle.putString(Global.DESIGNER_ID, designertid);
        startActivity(DesignerInfoActivity.class, designerBundle);
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
        FROM = 0;
        designerList.clear();
        designerAdapter.notifyDataSetChanged();
        getHomePageDesigners(FROM, Constant.LIMIT);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullRefreshScrollView.onRefreshComplete();
        FROM = designerList.size();
        getHomePageDesigners(FROM, Constant.LIMIT);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }
}
