package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ViewPagerManager;
import com.jianfanjia.cn.tools.ViewPagerManager.ShapeType;
import com.jianfanjia.cn.view.MyViewPager;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:首页
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class HomeFragment extends BaseFragment implements
        PullToRefreshBase.OnRefreshListener2<ListView>, ListItemClickListener, OnItemClickListener {
    private static final String TAG = HomeFragment.class.getName();
    private PullToRefreshListView pullToRefreshListView = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private RelativeLayout viewpagerLayout = null;
    private LinearLayout marchedLayout = null;
    private LinearLayout noMarchedLayout = null;
    private LinearLayout headLayout = null;
    private GridView marchDesignerView = null;
    private MarchDesignerAdapter marchDesignerAdapter = null;
    private List<OrderDesignerInfo> designers = new ArrayList<OrderDesignerInfo>();

    private Button addXuQiu = null;
    private DesignerListAdapter designerAdapter = null;
    private List<DesignerListInfo> designerList = new ArrayList<DesignerListInfo>();

    private int FROM = 0;// 当前页的编号，从0开始
    private int total = Constant.LIMIT;

    private static final int BANNER_ICON[] = {R.mipmap.bg_home_banner1,
            R.mipmap.bg_home_banner2, R.mipmap.bg_home_banner3,
            R.mipmap.bg_home_banner4};

    @Override
    public void initView(View view) {
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_scrollview);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        headLayout = (LinearLayout) inflater.inflate(R.layout.list_item_home_head, null, false);
        viewpagerLayout = (RelativeLayout) headLayout.findViewById(R.id.viewpager_layout);
        marchedLayout = (LinearLayout) headLayout.findViewById(R.id.marched_layout);
        noMarchedLayout = (LinearLayout) headLayout.findViewById(R.id.no_marched_layout);
        addXuQiu = (Button) headLayout.findViewById(R.id.btn_add);
        marchDesignerView = (GridView) headLayout.findViewById(R.id.marchGridview);
        pullToRefreshListView.getRefreshableView().addHeaderView(headLayout);
        initBannerView();
        initHomePage();
    }

    private void initBannerView() {
        MyViewPager myViewPager = (MyViewPager) headLayout.findViewById(R.id.viewPager_lib);
        ViewPagerManager contoler = new ViewPagerManager(getActivity(), myViewPager);
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
        getHomePageDesigners(FROM, Constant.LIMIT, pullDownListener);
        designerAdapter = new DesignerListAdapter(getActivity(), designerList, this);
        pullToRefreshListView.setAdapter(designerAdapter);
    }

    @Override
    public void setListener() {
        pullToRefreshListView.setOnRefreshListener(this);
        addXuQiu.setOnClickListener(this);
        marchDesignerView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent(getActivity(), EditRequirementActivity_.class);
                getActivity().startActivityForResult(intent, XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT);
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

    private void getHomePageDesigners(int from, int limit, ApiUiUpdateListener listener) {
        JianFanJiaClient.getHomePageDesigners(getActivity(), from, limit, listener, this);
    }


    @Override
    public void onMaxClick(int position) {
        DesignerListInfo designerListInfo = designerList.get(position);
        Product product = designerListInfo.getProduct();
        String productid = product.get_id();
        LogTool.d(TAG, "productid:" + productid);
        Bundle productBundle = new Bundle();
        productBundle.putString(Global.PRODUCT_ID, productid);
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
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        FROM = 0;
        getHomePageDesigners(FROM, total, pullDownListener);
    }


    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//        FROM = designerList.size();
        getHomePageDesigners(total, Constant.LIMIT, pullUpListener);
    }


    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            HomeDesignersInfo homeDesignersInfo = JsonParser.jsonToBean(data.toString(), HomeDesignersInfo.class);
            LogTool.d(TAG, "homeDesignersInfo:" + homeDesignersInfo);
            if (null != homeDesignersInfo) {
                Requirement requirement = homeDesignersInfo.getRequirement();
                LogTool.d(TAG, "requirement=" + requirement);
                if (null != requirement) {
                    designers = requirement.getDesigners();
                    LogTool.d(TAG, "designers=" + designers);
                    if (null != designers) {
                        if (designers.size() > 0) {
                            marchedLayout.setVisibility(View.VISIBLE);
                            noMarchedLayout.setVisibility(View.GONE);
                            marchDesignerAdapter = new MarchDesignerAdapter(getActivity(), designers);
                            marchDesignerView.setAdapter(marchDesignerAdapter);

                        } else {
                            marchedLayout.setVisibility(View.GONE);
                            noMarchedLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        marchedLayout.setVisibility(View.GONE);
                        noMarchedLayout.setVisibility(View.GONE);
                    }
                } else {
                    marchedLayout.setVisibility(View.GONE);
                    noMarchedLayout.setVisibility(View.VISIBLE);
                }
                designerList.clear();
                designerList.addAll(homeDesignersInfo.getDesigners());
                LogTool.d(TAG, "designerList:" + designerList.size());
                designerAdapter.notifyDataSetChanged();
            }
            pullToRefreshListView.onRefreshComplete();
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            pullToRefreshListView.onRefreshComplete();
            marchedLayout.setVisibility(View.GONE);
            noMarchedLayout.setVisibility(View.GONE);
        }
    };

    private ApiUiUpdateListener pullUpListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            total += Constant.LIMIT;
            LogTool.d(TAG, "homeDesignersInfo=" + data.toString());
            HomeDesignersInfo homeDesignersInfo = JsonParser.jsonToBean(data.toString(), HomeDesignersInfo.class);
            LogTool.d(TAG, "homeDesignersInfo:" + homeDesignersInfo);
            if (null != homeDesignersInfo) {
                designerList.addAll(homeDesignersInfo.getDesigners());
                LogTool.d(TAG, "designerList:" + designerList.size());
                designerAdapter.notifyDataSetChanged();
            }
            pullToRefreshListView.onRefreshComplete();
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            pullToRefreshListView.onRefreshComplete();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT:
                getHomePageDesigners(FROM, total, pullDownListener);
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


}
