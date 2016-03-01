package com.jianfanjia.cn.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengliang
 * @ClassName: SearchProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class SearchProductFragment extends BaseFragment {
    private static final String TAG = SearchProductFragment.class.getName();
    private RecyclerView recyclerView = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private SearchProductAdapter productAdapter = null;
    private String search = null;

    @Override
    public void initView(View view) {
        search = getArguments().getString(Global.SEARCH_TEXT);
        LogTool.d(TAG, "search=" + search);
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_product));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_product);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        productAdapter = new SearchProductAdapter(getContext(), recyclerView, new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Product product = productAdapter.getData().get(position);
                String productid = product.get_id();
                LogTool.d(TAG, "productid:" + productid);
                Intent productIntent = new Intent(getActivity().getApplicationContext(), DesignerCaseInfoActivity.class);
                Bundle productBundle = new Bundle();
                productBundle.putString(Global.PRODUCT_ID, productid);
                productIntent.putExtras(productBundle);
                startActivity(productIntent);
            }

            @Override
            public void OnViewClick(int position) {
                Product product = productAdapter.getData().get(position);
                String designertid = product.getDesignerid();
                LogTool.d(TAG, "designertid=" + designertid);
                Intent designerIntent = new Intent(getActivity().getApplicationContext(), DesignerInfoActivity.class);
                Bundle designerBundle = new Bundle();
                designerBundle.putString(Global.DESIGNER_ID, designertid);
                designerIntent.putExtras(designerBundle);
                startActivity(designerIntent);
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
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        searchProduct(productAdapter.getData().size(), search, listener);
    }

    private void searchProduct(int from, String searchText, ApiUiUpdateListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        param.put("from", from);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDesignerProduct(new SearchDesignerProductRequest(getContext(), param), listener, this);
    }

    @Override
    public void setListener() {
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                searchProduct(productAdapter.getData().size(), search, listener);
                break;
            default:
                break;
        }
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
                    LogTool.d(this.getClass().getName(), "searchDesignerAdapter.getData().size() =" + productAdapter.getData().size());
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
        return R.layout.fragment_search_designer;
    }
}
