package com.jianfanjia.cn.activity.home;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.ProductAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerWorksInfo;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchDesignerProductRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.EndlessRecyclerViewScrollListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:全部案例
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerCaseListActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = DesignerCaseListActivity.class.getName();
    private static final int DEC_TYPE = 1;
    private static final int DESIGN_STYLE = 2;
    private static final int HOUSE_TYPE = 3;
    private static final int DEC_AREA = 4;
    private static final int NOT = 5;
    private MainHeadView mainHeadView = null;
    private PullToRefreshRecycleView pullToRefreshRecyclerView = null;
    private RelativeLayout errorLayout = null;
    private boolean isFirst = true;
    private ProductAdapter productAdapter = null;
    private List<Product> productList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private int FROM = 0;
    private String decType = null;
    private String designStyle = null;
    private String houseType = null;
    private String decArea = null;

    @Override
    public void initView() {
        initMainHeadView();
        errorLayout = (RelativeLayout) findViewById(R.id.error_include);
        pullToRefreshRecyclerView = (PullToRefreshRecycleView) findViewById(R.id.pull_refresh_scrollview);
        linearLayoutManager = new LinearLayoutManager(DesignerCaseListActivity.this);
        pullToRefreshRecyclerView.setLayoutManager(linearLayoutManager);
        pullToRefreshRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getDesignerProductList(decType, designStyle, houseType, decArea, FROM, pullUpListener);
            }
        });
        pullToRefreshRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        pullToRefreshRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(DesignerCaseListActivity.this).paint(paint).showLastDivider().build());
        getDesignerProductList(decType, designStyle, houseType, decArea, FROM, pullDownListener);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.designer_case_head);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.all_designer_case));
        mainHeadView.setBackgroundTransparent();
    }

    @Override
    public void setListener() {
        pullToRefreshRecyclerView.setOnRefreshListener(this);
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.error_include:
                getDesignerProductList(decType, designStyle, houseType, decArea, FROM, pullDownListener);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        getDesignerProductList(decType, designStyle, houseType, decArea, FROM, pullDownListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDesignerProductList(decType, designStyle, houseType, decArea, FROM, pullUpListener);
    }

    private void getDesignerProductList(String decType, String designStyle, String houseType, String decArea, int from, ApiUiUpdateListener listener) {
        Map<String, Object> conditionParam = new HashMap<>();
        conditionParam.put("dec_type", decType);
        conditionParam.put("house_type", houseType);
        conditionParam.put("dec_style", designStyle);
        conditionParam.put("house_area", decArea);
        Map<String, Object> param = new HashMap<>();
        param.put("query", conditionParam);
        param.put("from", from);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDesignerProduct(new SearchDesignerProductRequest(DesignerCaseListActivity.this, param), listener, this);
    }


    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            LogTool.d(TAG, "isFirst = " + isFirst);
            if (isFirst) {
                showWaitDialog();
            }
        }

        @Override
        public void loadSuccess(Object data) {
            hideWaitDialog();
            DesignerWorksInfo worksInfo = JsonParser.jsonToBean(data.toString(), DesignerWorksInfo.class);
            LogTool.d(TAG, "worksInfo :" + worksInfo);
            if (null != worksInfo) {
                isFirst = false;
                productList.clear();
                productList.addAll(worksInfo.getProducts());
                productAdapter = new ProductAdapter(DesignerCaseListActivity.this, productList, new RecyclerViewOnItemClickListener() {

                    @Override
                    public void OnItemClick(View view, int position) {
                        Product product = productList.get(position);
                        String productid = product.get_id();
                        LogTool.d(TAG, "productid:" + productid);
                        Intent productIntent = new Intent(DesignerCaseListActivity.this, DesignerCaseInfoActivity.class);
                        Bundle productBundle = new Bundle();
                        productBundle.putString(Global.PRODUCT_ID, productid);
                        productIntent.putExtras(productBundle);
                        startActivity(productIntent);
                    }

                    @Override
                    public void OnViewClick(int position) {
                        Product product = productList.get(position);
                        String designertid = product.getDesignerid();
                        LogTool.d(TAG, "designertid=" + designertid);
                        Intent designerIntent = new Intent(DesignerCaseListActivity.this, DesignerInfoActivity.class);
                        Bundle designerBundle = new Bundle();
                        designerBundle.putString(Global.DESIGNER_ID, designertid);
                        designerIntent.putExtras(designerBundle);
                        startActivity(designerIntent);
                    }
                });
                pullToRefreshRecyclerView.setAdapter(productAdapter);
                FROM = productList.size();
                LogTool.d(TAG, "FROM:" + FROM);
                errorLayout.setVisibility(View.GONE);
            }
            pullToRefreshRecyclerView.onRefreshComplete();
        }

        @Override
        public void loadFailture(String error_msg) {
            hideWaitDialog();
            makeTextShort(error_msg);
            if (isFirst) {
                errorLayout.setVisibility(View.VISIBLE);
            }
            pullToRefreshRecyclerView.onRefreshComplete();
        }
    };

    private ApiUiUpdateListener pullUpListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            DesignerWorksInfo worksInfo = JsonParser.jsonToBean(data.toString(), DesignerWorksInfo.class);
            LogTool.d(TAG, "worksInfo :" + worksInfo);
            if (null != worksInfo) {
                List<Product> products = worksInfo.getProducts();
                if (null != products && products.size() > 0) {
                    productAdapter.add(FROM + 1, products);
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_case_list;
    }
}
