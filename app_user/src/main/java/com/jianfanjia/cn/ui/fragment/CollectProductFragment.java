package com.jianfanjia.cn.ui.fragment;

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
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductList;
import com.jianfanjia.api.request.common.DeleteProductFavoriteRequest;
import com.jianfanjia.api.request.common.GetProductFavoriteListRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.Event.MessageEvent;
import com.jianfanjia.cn.ui.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.ui.adapter.CollectProductAdapter;
import com.jianfanjia.cn.ui.interf.RecyclerViewOnItemClickListener;
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
    PullToRefreshRecycleView mPullToRefreshRecycleView = null;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout = null;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout = null;

    private CollectProductAdapter productAdapter = null;
    private List<Product> products = new ArrayList<>();
    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private int currentPos = -1;

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
        mPullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPullToRefreshRecycleView.setHasFixedSize(true);
        mPullToRefreshRecycleView.setItemAnimator(new DefaultItemAnimator());
        mPullToRefreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity()
                .getApplicationContext
                        ()));
        mPullToRefreshRecycleView.setOnRefreshListener(this);
    }

    private void onVisible() {
        load();
    }

    private void onInvisible() {

    }

    private void load() {
        if (!isPrepared || !isVisible) {
            return;
        }
        getProductList(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    private void getProductList(int from, int limit, ApiCallback<ApiResponse<ProductList>> listener) {
        GetProductFavoriteListRequest request = new GetProductFavoriteListRequest();
        request.setFrom(from);
        request.setLimit(limit);
        Api.getCollectListByUser(request, listener, this);
    }

    @OnClick(R.id.error_include)
    public void onClick() {
        getProductList(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getProductList(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getProductList(products.size(), Constant.HOME_PAGE_LIMIT, pullUpListener);
    }


    private ApiCallback<ApiResponse<ProductList>> pullDownListener = new ApiCallback<ApiResponse<ProductList>>() {
        @Override
        public void onPreLoad() {
            if (!mHasLoadedOnce) {
                Hud.show(getUiContext());
            }
        }

        @Override
        public void onHttpDone() {
            Hud.dismiss();
            if (mPullToRefreshRecycleView != null) {
                mPullToRefreshRecycleView.onRefreshComplete();
            }
        }

        @Override
        public void onSuccess(ApiResponse<ProductList> apiResponse) {
            mHasLoadedOnce = true;
            ProductList ProductList = apiResponse.getData();
            LogTool.d("ProductList=" + ProductList);
            if (ProductList != null) {
                products.clear();
                products.addAll(ProductList.getProducts());
                if (null != products && products.size() > 0) {
                    productAdapter = new CollectProductAdapter(getActivity(), products, new
                            RecyclerViewOnItemClickListener() {
                                @Override
                                public void OnItemClick(View view, int position) {
                                    LogTool.d("position:" + position);
                                    currentPos = position;
                                    LogTool.d("currentPos-------" + currentPos);
                                    String productid = products.get(currentPos).get_id();
                                    LogTool.d("productid:" + productid);
                                    Bundle productBundle = new Bundle();
                                    productBundle.putString(IntentConstant.PRODUCT_ID, productid);
                                    startActivity(DesignerCaseInfoActivity.class, productBundle);
                                }

                                @Override
                                public void OnViewClick(int position) {
                                    cancelCollectProduct(position);
                                }
                            });
                    mPullToRefreshRecycleView.setAdapter(productAdapter);
                    mPullToRefreshRecycleView.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                } else {
                    mPullToRefreshRecycleView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailed(ApiResponse<ProductList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.getMsg(code));
            mPullToRefreshRecycleView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }

    };

    private void cancelCollectProduct(int position) {
        final Product product = products.get(position);

        DeleteProductFavoriteRequest deleteProductFavoriteRequest = new DeleteProductFavoriteRequest();
        deleteProductFavoriteRequest.set_id(product.get_id());

        Api.deleteProductFavorite(deleteProductFavoriteRequest, new ApiCallback<ApiResponse<Object>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<Object> apiResponse) {
                products.remove(product);
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(ApiResponse<Object> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        }, this);

    }

    private ApiCallback<ApiResponse<ProductList>> pullUpListener = new ApiCallback<ApiResponse<ProductList>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {
            if (mPullToRefreshRecycleView != null) {
                mPullToRefreshRecycleView.onRefreshComplete();
            }
        }

        @Override
        public void onSuccess(ApiResponse<ProductList> apiResponse) {

            ProductList ProductList = apiResponse.getData();
            LogTool.d("ProductList=" + ProductList);
            if (ProductList != null) {
                List<Product> productList = ProductList.getProducts();
                if (null != productList && productList.size() > 0) {
                    productAdapter.add(products.size(), productList);
                } else {
                    makeTextShort(getResources().getString(R.string.no_more_data));
                }
            }
        }

        @Override
        public void onFailed(ApiResponse<ProductList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.getMsg(code));
        }

    };

    public void onEventMainThread(MessageEvent event) {
        switch (event.getEventType()) {
            case Constant.UPDATE_PRODUCT_FRAGMENT:
                productAdapter.remove(currentPos);
                if (products.size() == 0) {
                    mPullToRefreshRecycleView.setVisibility(View.GONE);
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
