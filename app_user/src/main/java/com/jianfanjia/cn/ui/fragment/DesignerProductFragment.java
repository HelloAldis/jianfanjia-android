package com.jianfanjia.cn.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductList;
import com.jianfanjia.api.request.guest.SearchDesignerProductRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.ScrollableHelper;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.ui.adapter.DesignerWorksAdapter;
import com.jianfanjia.common.tool.LogTool;

/**
 * @author fengliang
 * @ClassName: DesignerProductFragment
 * @Description: 设计师作品
 * @date 2015-8-26 下午1:07:52
 */

public class DesignerProductFragment extends BaseFragment implements PullToRefreshBase
        .OnRefreshListener2<RecyclerView>, ScrollableHelper.ScrollableContainer {
    private static final String TAG = DesignerProductFragment.class.getName();

    @Bind(R.id.designer_works_listview)
    PullToRefreshRecycleView mPullToRefreshRecycleView;

    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private DesignerWorksAdapter adapter = null;
    private List<Product> productList = new ArrayList<Product>();
    private String designerid = null;
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

    public static DesignerProductFragment newInstance(String info) {
        Bundle args = new Bundle();
        DesignerProductFragment productFragment = new DesignerProductFragment();
        args.putString(IntentConstant.DESIGNER_ID, info);
        productFragment.setArguments(args);
        return productFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        designerid = bundle.getString(IntentConstant.DESIGNER_ID);
        LogTool.d("designerid:" + designerid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.d("onCreateView()");
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        isPrepared = true;
        load();
        return view;
    }

    private void initView() {
        mPullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullToRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPullToRefreshRecycleView.setItemAnimator(new DefaultItemAnimator());
        mPullToRefreshRecycleView.setHasFixedSize(true);
        mPullToRefreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity()
                .getApplicationContext()));
        mPullToRefreshRecycleView.setFocusable(false);
        mPullToRefreshRecycleView.setOnRefreshListener(this);
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
        getDesignerProduct(designerid, pullUpListener);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDesignerProduct(designerid, pullUpListener);
    }

    private void getDesignerProduct(String designerid, ApiCallback<ApiResponse<ProductList>> listener) {
        SearchDesignerProductRequest request = new SearchDesignerProductRequest();
        Map<String, Object> param = new HashMap<>();
        param.put("designerid", designerid);
        request.setQuery(param);
        request.setFrom(FROM);
        request.setLimit(Constant.HOME_PAGE_LIMIT);
        Api.searchDesignerProduct(request, listener, this);
    }

    private ApiCallback<ApiResponse<ProductList>> pullUpListener = new
            ApiCallback<ApiResponse<ProductList>>() {

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
                    mHasLoadedOnce = true;
                    ProductList worksInfo = apiResponse.getData();
                    LogTool.d("worksInfo :" + worksInfo);
                    if (null != worksInfo) {
                        if (null == adapter) {
                            productList.addAll(worksInfo.getProducts());
                            adapter = new DesignerWorksAdapter(getActivity(), productList, new
                                    BaseRecyclerViewAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            Product product = productList.get(position);
                                            String productid = product.get_id();
                                            LogTool.d("productid:" + productid);
                                            Bundle productBundle = new Bundle();
                                            productBundle.putString(IntentConstant.PRODUCT_ID, productid);
                                            startActivity(DesignerCaseInfoActivity.class, productBundle);
                                        }
                                    });
                            mPullToRefreshRecycleView.setAdapter(adapter);
                            FROM = productList.size();
                            LogTool.d("FROM:" + FROM);
                        } else {
                            List<Product> products = worksInfo.getProducts();
                            if (null != products && products.size() > 0) {
                                adapter.add(FROM, products);
                                FROM += Constant.HOME_PAGE_LIMIT;
                                LogTool.d("FROM=" + FROM);
                            } else {
                                makeTextShort(getResources().getString(R.string.no_more_data));
                            }
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

    @Override
    public View getScrollableView() {
        return mPullToRefreshRecycleView.getRefreshableView();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_designer_works;
    }
}
