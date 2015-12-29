package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.activity.requirement.PublishRequirementActivity;
import com.jianfanjia.cn.adapter.DesignerListAdapter;
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
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Description:首页
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class HomeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = HomeFragment.class.getName();
    private PullToRefreshRecycleView pullToRefreshRecyclerView = null;
    private RelativeLayout errorLayout = null;
    private boolean isFirst = true;

    private DesignerListAdapter designerAdapter = null;
    private List<DesignerListInfo> designerList = new ArrayList<DesignerListInfo>();

    private int FROM = 0;// 当前页的编号，从0开始

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView(View view) {
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        pullToRefreshRecyclerView = (PullToRefreshRecycleView) view.findViewById(R.id.pull_refresh_scrollview);
        pullToRefreshRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pullToRefreshRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        pullToRefreshRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        initHomePage();
    }

    private void initHomePage() {
        getHomePageDesigners(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @Override
    public void setListener() {
        pullToRefreshRecyclerView.setOnRefreshListener(this);
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent(getActivity(), PublishRequirementActivity.class);
                startActivityForResult(intent, XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT);
                break;
            case R.id.error_include:
                initHomePage();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getHomePageDesigners(int from, int limit, ApiUiUpdateListener listener) {
        LogTool.d(TAG, "from=" + from + " limit=" + limit);
        JianFanJiaClient.getHomePageDesigners(getActivity(), from, limit, listener, this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        getHomePageDesigners(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getHomePageDesigners(FROM, Constant.HOME_PAGE_LIMIT, pullUpListener);
    }

    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            LogTool.d(TAG,"isFirst = " + isFirst);
            if (isFirst) {
                showWaitDialog();
            }
        }

        @Override
        public void loadSuccess(Object data) {
            hideWaitDialog();
            HomeDesignersInfo homeDesignersInfo = JsonParser.jsonToBean(data.toString(), HomeDesignersInfo.class);
            LogTool.d(TAG, "homeDesignersInfo:" + homeDesignersInfo);
            if (null != homeDesignersInfo) {
                isFirst = false;
                Requirement requirement = homeDesignersInfo.getRequirement();
                LogTool.d(TAG, "requirement=" + requirement);
                designerList.clear();
                designerList.addAll(homeDesignersInfo.getDesigners());
                designerAdapter = new DesignerListAdapter(getActivity(), designerList, requirement, new ListItemClickListener() {
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
                    public void onItemClick(int itemPosition, OrderDesignerInfo orderDesignerInfo) {
                        LogTool.d(TAG, "itemPosition:" + itemPosition + " orderDesignerInfo:" + orderDesignerInfo);
                        String designertid = orderDesignerInfo.get_id();
                        LogTool.d(TAG, "designertid:" + designertid);
                        Bundle designerBundle = new Bundle();
                        designerBundle.putString(Global.DESIGNER_ID, designertid);
                        startActivity(DesignerInfoActivity.class, designerBundle);
                    }

                    @Override
                    public void onClick() {
                        Intent intent = new Intent(getActivity(), PublishRequirementActivity.class);
                        getActivity().startActivityForResult(intent, XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT);
                    }
                });
                pullToRefreshRecyclerView.setAdapter(designerAdapter);
                FROM = designerList.size();
                LogTool.d(TAG, "FROM:" + FROM);
                errorLayout.setVisibility(View.GONE);
            }
            pullToRefreshRecyclerView.onRefreshComplete();
        }

        @Override
        public void loadFailture(String error_msg) {
            hideWaitDialog();
            makeTextShort(error_msg);
            errorLayout.setVisibility(View.VISIBLE);
            pullToRefreshRecyclerView.onRefreshComplete();
        }
    };

    private ApiUiUpdateListener pullUpListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            HomeDesignersInfo homeDesignersInfo = JsonParser.jsonToBean(data.toString(), HomeDesignersInfo.class);
            LogTool.d(TAG, "homeDesignersInfo:" + homeDesignersInfo);
            if (null != homeDesignersInfo) {
                List<DesignerListInfo> designers = homeDesignersInfo.getDesigners();
                if (null != designers && designers.size() > 0) {
                    designerAdapter.add(FROM + 1, designers);
                    FROM += Constant.HOME_PAGE_LIMIT;
                    LogTool.d(TAG, "FROM=" + FROM);
                }
            }
            pullToRefreshRecyclerView.onRefreshComplete();
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            pullToRefreshRecyclerView.onRefreshComplete();
        }
    };

    public void onEventMainThread(MessageEvent event) {
        LogTool.d(TAG, "event:" + event.getEventType());
        switch (event.getEventType()) {
            case Constant.UPDATE_HOME_FRAGMENT:
                FROM = 0;
                getHomePageDesigners(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogTool.d(TAG, "onActivityResult = " + requestCode + " resultCode=" + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT:
                getHomePageDesigners(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
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
