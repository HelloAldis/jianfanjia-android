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

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductList;
import com.jianfanjia.api.request.guest.SearchDesignerProductRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.ui.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.ui.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.ui.adapter.SearchProductAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.common.tool.LogTool;

/**
 * @author fengliang
 * @ClassName: SearchProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class SearchProductFragment extends BaseFragment {
    private static final String TAG = SearchProductFragment.class.getName();

    @Bind(R.id.recycleview)
    RecyclerView recyclerView;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.empty_text)
    TextView emptyText;

    @Bind(R.id.empty_img)
    ImageView emptyImage;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private SearchProductAdapter productAdapter = null;
    private String search = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search = getArguments().getString(IntentConstant.SEARCH_TEXT);
        LogTool.d("search=" + search);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        emptyText.setText(getString(R.string.search_no_product));
        emptyImage.setImageResource(R.mipmap.icon_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        productAdapter = new SearchProductAdapter(getContext(), recyclerView, new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Product product = productAdapter.getData().get(position);
                String productid = product.get_id();
                LogTool.d("productid:" + productid);
                Bundle productBundle = new Bundle();
                productBundle.putString(IntentConstant.PRODUCT_ID, productid);
                startActivity(DesignerCaseInfoActivity.class, productBundle);
            }

            @Override
            public void OnViewClick(int position) {
                Product product = productAdapter.getData().get(position);
                String designertid = product.getDesignerid();
                LogTool.d("designertid=" + designertid);
                Bundle designerBundle = new Bundle();
                designerBundle.putString(IntentConstant.DESIGNER_ID, designertid);
                startActivity(DesignerInfoActivity.class, designerBundle);
            }
        });
        productAdapter.setLoadMoreListener(new BaseLoadMoreRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                searchProduct(productAdapter.getData().size(), search, listener);
            }
        });
        productAdapter.setErrorView(errorLayout);
        productAdapter.setEmptyView(emptyLayout);
        recyclerView.setAdapter(productAdapter);
        searchProduct(productAdapter.getData().size(), search, listener);
    }

    private void searchProduct(int from, String searchText, ApiCallback<ApiResponse<ProductList>> listener) {
        SearchDesignerProductRequest request = new SearchDesignerProductRequest();
        request.setSearch_word(searchText);
        request.setFrom(from);
        request.setLimit(Constant.HOME_PAGE_LIMIT);
        Api.searchDesignerProduct(request, listener,this);
    }

    @OnClick(R.id.error_include)
    protected void errorClick() {
        searchProduct(productAdapter.getData().size(), search, listener);
    }

    private ApiCallback<ApiResponse<ProductList>> listener = new ApiCallback<ApiResponse<ProductList>>() {

        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override

        public void onSuccess(ApiResponse<ProductList> apiResponse) {
            ProductList worksInfo = apiResponse.getData();
            LogTool.d("worksInfo :" + worksInfo);
            if (null != worksInfo) {
                int total = worksInfo.getTotal();
                if (total > 0) {
                    LogTool.d("total size =" + total);
                    LogTool.d("searchDesignerAdapter.getData().size() =" + productAdapter
                            .getData().size());
                    productAdapter.addData(worksInfo.getProducts());
                    if (total > productAdapter.getData().size()) {
                        productAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_LOAD_MORE);
                    } else {
                        productAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NO_MORE);
                    }
                    productAdapter.hideErrorAndEmptyView();
                } else {
                    productAdapter.setEmptyViewShow();
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
            productAdapter.setErrorViewShow();
            productAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_common;
    }
}
