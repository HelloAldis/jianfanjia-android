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

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.SearchProductAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.DesignerWorksInfo;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchDesignerProductRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

/**
 * @author fengliang
 * @ClassName: SearchProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class SearchProductFragment extends BaseFragment{

    private static final String TAG = SearchProductFragment.class.getName();
    @Bind(R.id.recycleview)
    protected RecyclerView recyclerView;
    @Bind(R.id.empty_include)
    protected RelativeLayout emptyLayout;
    @Bind(R.id.empty_text)
    protected TextView emptyText;
    @Bind(R.id.empty_img)
    protected ImageView emptyImage;
    @Bind(R.id.error_include)
    protected RelativeLayout errorLayout;
    private SearchProductAdapter productAdapter = null;
    private String search = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    public void initView() {
        search = getArguments().getString(Global.SEARCH_TEXT);
        LogTool.d(TAG, "search=" + search);
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
                LogTool.d(TAG, "productid:" + productid);
                Bundle productBundle = new Bundle();
                productBundle.putString(Global.PRODUCT_ID, productid);
                startActivity(DesignerCaseInfoActivity.class, productBundle);
            }

            @Override
            public void OnViewClick(int position) {
                Product product = productAdapter.getData().get(position);
                String designertid = product.getDesignerid();
                LogTool.d(TAG, "designertid=" + designertid);
                Bundle designerBundle = new Bundle();
                designerBundle.putString(Global.DESIGNER_ID, designertid);
                startActivity(DesignerInfoActivity.class, designerBundle);
            }
        });
        productAdapter.setLoadMoreListener(new BaseRecycleAdapter.LoadMoreListener() {
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

    private void searchProduct(int from, String searchText, ApiUiUpdateListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        param.put("from", from);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDesignerProduct(new SearchDesignerProductRequest(getContext(), param), listener, this);
    }

    @OnClick(R.id.error_include)
    protected void errorClick() {
        searchProduct(productAdapter.getData().size(), search, listener);
    }

    private ApiUiUpdateListener listener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data.toString());
            DesignerWorksInfo worksInfo = JsonParser.jsonToBean(data.toString(), DesignerWorksInfo.class);
            LogTool.d(TAG, "worksInfo :" + worksInfo);
            if (null != worksInfo) {
                int total = worksInfo.getTotal();
                if (total > 0) {
                    LogTool.d(this.getClass().getName(), "total size =" + total);
                    LogTool.d(this.getClass().getName(), "searchDesignerAdapter.getData().size() =" + productAdapter
                            .getData().size());
                    productAdapter.addData(worksInfo.getProducts());
                    if (total > productAdapter.getData().size()) {
                        productAdapter.setState(BaseRecycleAdapter.STATE_LOAD_MORE);
                    } else {
                        productAdapter.setState(BaseRecycleAdapter.STATE_NO_MORE);
                    }
                    productAdapter.hideErrorAndEmptyView();
                } else {
                    productAdapter.setEmptyViewShow();
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            productAdapter.setErrorViewShow();
            productAdapter.setState(BaseRecycleAdapter.STATE_NETWORK_ERROR);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_common;
    }
}
