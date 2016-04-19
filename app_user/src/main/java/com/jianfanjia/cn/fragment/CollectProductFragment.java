package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductList;
import com.jianfanjia.api.request.common.GetCollectionRequest;
import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.adapter.CollectProductAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;

/**
 * @author fengliang
 * @ClassName: CollectProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class CollectProductFragment extends BaseFragment implements PullToRefreshBase
        .OnRefreshListener2<RecyclerView> {
    private static final String TAG = CollectProductFragment.class.getName();

    @Bind(R.id.prodtct_listview)
    PullToRefreshRecycleView prodtct_listview = null;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout = null;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout = null;

    private CollectProductAdapter productAdapter = null;
    private List<Product> products = new ArrayList<>();
    private boolean isFirst = true;
    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private int currentPos = -1;
    private int FROM = 0;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    public static CollectProductFragment newInstance() {
        CollectProductFragment productFragment = new CollectProductFragment();
        return productFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        isPrepared = true;
        load();
        return view;
    }

    private void initView() {
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.empty_view_no_product_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_product);
        prodtct_listview.setMode(PullToRefreshBase.Mode.BOTH);
        prodtct_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        prodtct_listview.setHasFixedSize(true);
        prodtct_listview.setItemAnimator(new DefaultItemAnimator());
        prodtct_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity().getApplicationContext
                ()));
        prodtct_listview.setOnRefreshListener(this);
    }

    private void onVisible() {
        load();
    }

    private void onInvisible() {

    }

    private void load() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getProductList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    private void getProductList(int from, int limit, ApiCallback<ApiResponse<ProductList>> listener) {
        GetCollectionRequest request = new GetCollectionRequest();
        request.setFrom(from);
        request.setLimit(limit);
        Api.getCollectListByUser(request, listener);
    }

    @OnClick(R.id.error_include)
    public void onClick() {
        getProductList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        getProductList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getProductList(FROM, Constant.HOME_PAGE_LIMIT, pullUpListener);
    }


    private ApiCallback<ApiResponse<ProductList>> pullDownListener = new ApiCallback<ApiResponse<ProductList>>() {
        @Override
        public void onPreLoad() {
            if (isFirst) {
                showWaitDialog();
            }
        }

        @Override
        public void onHttpDone() {
            hideWaitDialog();
            prodtct_listview.onRefreshComplete();
        }

        @Override
        public void onSuccess(ApiResponse<ProductList> apiResponse) {
            mHasLoadedOnce = true;
            ProductList ProductList = apiResponse.getData();
            LogTool.d(TAG, "ProductList=" + ProductList);
            if (ProductList != null) {
                products.clear();
                products.addAll(ProductList.getProducts());
                if (null != products && products.size() > 0) {
                    productAdapter = new CollectProductAdapter(getActivity(), products, new
                            RecyclerViewOnItemClickListener() {
                                @Override
                                public void OnItemClick(View view, int position) {
                                    LogTool.d(TAG, "position:" + position);
                                    currentPos = position;
                                    LogTool.d(TAG, "currentPos-------" + currentPos);
                                    String productid = products.get(currentPos).get_id();
                                    LogTool.d(TAG, "productid:" + productid);
                                    Bundle productBundle = new Bundle();
                                    productBundle.putString(IntentConstant.PRODUCT_ID, productid);
                                    startActivity(DesignerCaseInfoActivity.class, productBundle);
                                }

                                @Override
                                public void OnViewClick(int position) {

                                }
                            });
                    prodtct_listview.setAdapter(productAdapter);
                    prodtct_listview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    isFirst = false;
                } else {
                    prodtct_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                }
                FROM = products.size();
                LogTool.d(TAG, "FROM:" + FROM);
            }
        }

        @Override
        public void onFailed(ApiResponse<ProductList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            prodtct_listview.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }

    };

    private ApiCallback<ApiResponse<ProductList>> pullUpListener = new ApiCallback<ApiResponse<ProductList>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {
            prodtct_listview.onRefreshComplete();
        }

        @Override
        public void onSuccess(ApiResponse<ProductList> apiResponse) {

            ProductList ProductList = apiResponse.getData();
            LogTool.d(TAG, "ProductList=" + ProductList);
            if (ProductList != null) {
                List<Product> productList = ProductList.getProducts();
                if (null != productList && productList.size() > 0) {
                    productAdapter.add(FROM, productList);
                    FROM += Constant.HOME_PAGE_LIMIT;
                } else {
                    makeTextShort(getResources().getString(R.string.no_more_data));
                }
            }
        }

        @Override
        public void onFailed(ApiResponse<ProductList> apiResponse) {
        }

        @Override
        public void onNetworkError(int code) {

        }

    };

    public void onEventMainThread(MessageEvent event) {
        switch (event.getEventType()) {
            case Constant.UPDATE_PRODUCT_FRAGMENT:
                productAdapter.remove(currentPos);
                if (products.size() == 0) {
                    prodtct_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collect_product;
    }
}
