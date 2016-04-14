package com.jianfanjia.cn.activity.home;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductList;
import com.jianfanjia.api.request.guest.SearchDesignerProductRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.ProductAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.interf.EndlessRecyclerViewScrollListener;
import com.jianfanjia.cn.interf.GetItemCallback;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.view.FilterPopWindow;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description:全部作品案例
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerCaseListActivity extends BaseSwipeBackActivity implements View.OnClickListener, PullToRefreshBase
        .OnRefreshListener2<RecyclerView> {
    private static final String TAG = DesignerCaseListActivity.class.getName();
    private static final int DEC_TYPE = 1;
    private static final int DESIGN_STYLE = 2;
    private static final int HOUSE_TYPE = 3;
    private static final int DEC_AREA = 4;
    private static final int NOT = 5;

    @Bind(R.id.topLayout)
    protected LinearLayout topLayout = null;

    @Bind(R.id.decTypeLayout)
    protected RelativeLayout decTypeLayout = null;

    @Bind(R.id.designStyleLayout)
    protected RelativeLayout designStyleLayout = null;

    @Bind(R.id.houseTypeLayout)
    protected RelativeLayout houseTypeLayout = null;

    @Bind(R.id.decAreaLayout)
    protected RelativeLayout decAreaLayout = null;

    @Bind(R.id.decType_item)
    protected TextView decType_item = null;

    @Bind(R.id.designStyle_item)
    protected TextView designStyle_item = null;

    @Bind(R.id.houseType_item)
    protected TextView houseType_item = null;

    @Bind(R.id.decArea_item)
    protected TextView decArea_item = null;

    @Bind(R.id.designer_case_head)
    protected MainHeadView mainHeadView = null;

    @Bind(R.id.pull_refresh_scrollview)
    protected PullToRefreshRecycleView pullToRefreshRecyclerView = null;

    @Bind(R.id.error_include)
    protected RelativeLayout errorLayout = null;

    @Bind(R.id.empty_include)
    protected RelativeLayout emptyLayout = null;

    private FilterPopWindow window = null;
    private boolean isFirst = true;
    private ProductAdapter productAdapter = null;
    private List<Product> productList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private int FROM = 0;
    private String decType = null;
    private String designStyle = null;
    private String houseType = null;
    private Map<String, Object> decArea = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
    }

    public void initView() {
        initMainHeadView();
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string
                .empty_view_no_product_list_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_product);
        linearLayoutManager = new LinearLayoutManager(DesignerCaseListActivity.this);
        pullToRefreshRecyclerView.setLayoutManager(linearLayoutManager);
        pullToRefreshRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getDesignerProductList(FROM, DesignerCaseListActivity.this.pullUpCallback);
            }
        });
        pullToRefreshRecyclerView.setHasFixedSize(true);
        pullToRefreshRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pullToRefreshRecyclerView.setOnRefreshListener(this);
        getDesignerProductList(FROM, this.pullDownCallback);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.all_good_product));
        mainHeadView.setBackgroundTransparent();
    }

    @OnClick({R.id.head_back_layout, R.id.decTypeLayout, R.id.designStyleLayout, R.id.houseTypeLayout, R.id
            .decAreaLayout, R.id.error_include})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.decTypeLayout:
                setSelectState(DEC_TYPE);
                break;
            case R.id.designStyleLayout:
                setSelectState(DESIGN_STYLE);
                break;
            case R.id.houseTypeLayout:
                setSelectState(HOUSE_TYPE);
                break;
            case R.id.decAreaLayout:
                setSelectState(DEC_AREA);
                break;
            case R.id.error_include:
                getDesignerProductList(FROM, this.pullDownCallback);
                break;
            default:
                break;
        }
    }

    private void setSelectState(int type) {
        switch (type) {
            case DEC_TYPE:
                showWindow(R.array.arr_dectype, DEC_TYPE);
                decTypeLayout.setSelected(true);
                designStyleLayout.setSelected(false);
                houseTypeLayout.setSelected(false);
                decAreaLayout.setSelected(false);
                break;
            case DESIGN_STYLE:
                showWindow(R.array.arr_decstyle, DESIGN_STYLE);
                decTypeLayout.setSelected(false);
                designStyleLayout.setSelected(true);
                houseTypeLayout.setSelected(false);
                decAreaLayout.setSelected(false);
                break;
            case HOUSE_TYPE:
                showWindow(R.array.arr_housetype, HOUSE_TYPE);
                decTypeLayout.setSelected(false);
                designStyleLayout.setSelected(false);
                houseTypeLayout.setSelected(true);
                decAreaLayout.setSelected(false);
                break;
            case DEC_AREA:
                showWindow(R.array.arr_area, DEC_AREA);
                decTypeLayout.setSelected(false);
                designStyleLayout.setSelected(false);
                houseTypeLayout.setSelected(false);
                decAreaLayout.setSelected(true);
                break;
            case NOT:
                decTypeLayout.setSelected(false);
                designStyleLayout.setSelected(false);
                houseTypeLayout.setSelected(false);
                decAreaLayout.setSelected(false);
            default:
                break;
        }
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        getDesignerProductList(FROM, this.pullDownCallback);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDesignerProductList(FROM, this.pullUpCallback);
    }

    private void getDesignerProductList(int from, ApiCallback<ApiResponse<ProductList>> apiCallback) {
        Map<String, Object> query = new HashMap<>();
        query.put("dec_type", decType);
        query.put("house_type", houseType);
        query.put("dec_style", designStyle);
        query.put("house_area", decArea);
        SearchDesignerProductRequest request = new SearchDesignerProductRequest();
        request.setQuery(query);
        request.setFrom(from);
        request.setLimit(Constant.HOME_PAGE_LIMIT);
        Api.searchDesignerProduct(request, apiCallback);
    }

    private ApiCallback<ApiResponse<ProductList>> pullDownCallback = new ApiCallback<ApiResponse<ProductList>>() {
        @Override
        public void onPreLoad() {
            if (isFirst) {
                showWaitDialog();
            }
        }

        @Override
        public void onHttpDone() {
            hideWaitDialog();
            pullToRefreshRecyclerView.onRefreshComplete();
        }

        @Override
        public void onSuccess(ApiResponse<ProductList> apiResponse) {
            ProductList worksInfo = apiResponse.getData();
            LogTool.d(TAG, "worksInfo :" + worksInfo);
            if (null != worksInfo) {
                isFirst = false;
                productList.clear();
                productList.addAll(worksInfo.getProducts());
                if (null != productList && productList.size() > 0) {
                    if (null == productAdapter) {
                        productAdapter = new ProductAdapter(DesignerCaseListActivity.this, productList, new
                                RecyclerViewOnItemClickListener() {

                                    @Override
                                    public void OnItemClick(View view, int position) {
                                        Product product = productList.get(position);
                                        String productid = product.get_id();
                                        LogTool.d(TAG, "productid:" + productid);
                                        Bundle productBundle = new Bundle();
                                        productBundle.putString(IntentConstant.PRODUCT_ID, productid);
                                        startActivity(DesignerCaseInfoActivity.class, productBundle);
                                    }

                                    @Override
                                    public void OnViewClick(int position) {
                                        Product product = productList.get(position);
                                        String designertid = product.getDesignerid();
                                        LogTool.d(TAG, "designertid=" + designertid);
                                        Bundle designerBundle = new Bundle();
                                        designerBundle.putString(IntentConstant.DESIGNER_ID, designertid);
                                        startActivity(DesignerInfoActivity.class, designerBundle);
                                    }
                                });
                        pullToRefreshRecyclerView.setAdapter(productAdapter);
                    } else {
                        pullToRefreshRecyclerView.scrollToPosition(0);
                        productAdapter.notifyDataSetChanged();
                    }
                    FROM = productList.size();
                    LogTool.d(TAG, "FROM:" + FROM);
                    pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                } else {
                    pullToRefreshRecyclerView.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onFailed(ApiResponse<ProductList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            if (isFirst) {
                pullToRefreshRecyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            }

        }
    };

    private ApiCallback<ApiResponse<ProductList>> pullUpCallback = new ApiCallback<ApiResponse<ProductList>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {
            pullToRefreshRecyclerView.onRefreshComplete();
        }

        @Override
        public void onSuccess(ApiResponse<ProductList> apiResponse) {
            ProductList worksInfo = apiResponse.getData();
            LogTool.d(TAG, "worksInfo :" + worksInfo);
            if (null != worksInfo) {
                List<Product> products = worksInfo.getProducts();
                if (null != products && products.size() > 0) {
                    productAdapter.add(FROM + 1, products);
                    FROM += Constant.HOME_PAGE_LIMIT;
                    LogTool.d(TAG, "FROM=" + FROM);
                }
            }
        }

        @Override
        public void onFailed(ApiResponse<ProductList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
        }
    };

    private void showWindow(int resId, int type) {
        switch (type) {
            case DEC_TYPE:
                window = new FilterPopWindow(DesignerCaseListActivity.this, resId, getDecTypeCallback, Global
                        .DEC_TYPE_POSITION);
                break;
            case DESIGN_STYLE:
                window = new FilterPopWindow(DesignerCaseListActivity.this, resId, getDesignStyleCallback, Global
                        .STYLE_POSITION);
                break;
            case HOUSE_TYPE:
                window = new FilterPopWindow(DesignerCaseListActivity.this, resId, getHouseTypeCallback, Global
                        .DEC_HOUSE_TYPE_POSITION);
                break;
            case DEC_AREA:
                window = new FilterPopWindow(DesignerCaseListActivity.this, resId, getHouseAreaCallback, Global
                        .DEC_AREA_POSITION);
                break;
            default:
                break;
        }
        window.show(topLayout);
    }

    private GetItemCallback getDecTypeCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            isFirst = true;
            Global.DEC_TYPE_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                decType_item.setText(title);
            } else {
                decType_item.setText(getResources().getString(R.string.dec_type_str));
            }
            FROM = 0;
            decType = BusinessCovertUtil.getDecTypeByText(title);
            getDesignerProductList(FROM, DesignerCaseListActivity.this.pullDownCallback);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }
    };

    private GetItemCallback getDesignStyleCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            isFirst = true;
            Global.STYLE_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                designStyle_item.setText(title);
            } else {
                designStyle_item.setText(getResources().getString(R.string.dec_style_str));
            }
            FROM = 0;
            designStyle = BusinessCovertUtil.getDecStyleByText(title);
            getDesignerProductList(FROM, DesignerCaseListActivity.this.pullDownCallback);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }
    };

    private GetItemCallback getHouseTypeCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            isFirst = true;
            Global.DEC_HOUSE_TYPE_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                houseType_item.setText(title);
            } else {
                houseType_item.setText(getResources().getString(R.string.dec_house_type_str));
            }
            FROM = 0;
            houseType = BusinessCovertUtil.getHouseTypeByText(title);
            getDesignerProductList(FROM, DesignerCaseListActivity.this.pullDownCallback);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }
    };

    private GetItemCallback getHouseAreaCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            isFirst = true;
            Global.DEC_AREA_POSITION = position;
            if (!TextUtils.isEmpty(title) && !title.equals(Constant.KEY_WORD)) {
                decArea_item.setText(title);
            } else {
                decArea_item.setText(getResources().getString(R.string.dec_area_str));
            }
            FROM = 0;
            decArea = BusinessCovertUtil.convertDecAreaValueByText(title);
            LogTool.d(TAG, "decArea=" + decArea);
            getDesignerProductList(FROM, DesignerCaseListActivity.this.pullDownCallback);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Global.DEC_TYPE_POSITION = 0;
        Global.DEC_HOUSE_TYPE_POSITION = 0;
        Global.STYLE_POSITION = 0;
        Global.DEC_AREA_POSITION = 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_case_list;
    }
}
