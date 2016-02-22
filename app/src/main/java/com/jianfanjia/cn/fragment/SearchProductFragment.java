package com.jianfanjia.cn.fragment;

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
import com.jianfanjia.cn.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.SearchProductAdapter;
import com.jianfanjia.cn.adapter.base.BaseLoadingAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.bean.ProductInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchDesignerProductRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengliang
 * @ClassName: ProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class SearchProductFragment extends BaseFragment implements ApiUiUpdateListener, RecyclerViewOnItemClickListener {
    private static final String TAG = SearchProductFragment.class.getName();
    private RecyclerView prodtct_listview = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private SearchProductAdapter productAdapter = null;
    private List<Product> products = new ArrayList<Product>();
    public static final int PAGE_COUNT = 10;

    private int currentPos = 0;
    private int FROM = 0;
    private String decType = null;
    private String designStyle = null;
    private String houseType = null;
    private String decArea = null;

    @Override
    public void initView(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        prodtct_listview = (RecyclerView) view.findViewById(R.id.recycleview);
        prodtct_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        prodtct_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        prodtct_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());

        searchProduct(currentPos,"水岸国际");
    }

    private void searchProduct(int from, String searchText) {
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        param.put("limit", PAGE_COUNT);
        param.put("from", from);
        JianFanJiaClient.searchDesignerProduct(new SearchDesignerProductRequest(getContext(), param), this, this);
    }

    @Override
    public void setListener() {
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                break;
            default:
                break;
        }
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data=" + data.toString());
        ProductInfo productInfo = JsonParser.jsonToBean(data.toString(), ProductInfo.class);
        LogTool.d(TAG, "productInfo=" + productInfo);
        if (productInfo != null) {
            products = productInfo.getProducts();
            if (null != products && products.size() > 0) {
                currentPos += products.size();
                if (productAdapter == null) {
                    productAdapter = new SearchProductAdapter(getActivity(), prodtct_listview, products, this ,PAGE_COUNT);
                    productAdapter.setOnLoadingListener(new BaseLoadingAdapter.OnLoadingListener() {
                        @Override
                        public void loading() {
                            searchProduct(currentPos,"水岸国际");
                        }
                    });
                    prodtct_listview.setAdapter(productAdapter);
                } else {
                    productAdapter.addAll(products);
                }
                prodtct_listview.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.GONE);
            } else {
                prodtct_listview.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextShort(error_msg);
        prodtct_listview.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnItemClick(View view, int position) {
        LogTool.d(TAG, "position:" + position);
        currentPos = position;
        Product product = products.get(position);
        String productid = product.get_id();
        LogTool.d(TAG, "productid:" + productid);
        Intent productIntent = new Intent(getActivity(), DesignerCaseInfoActivity.class);
        Bundle productBundle = new Bundle();
        productBundle.putString(Global.PRODUCT_ID, productid);
        productIntent.putExtras(productBundle);
        startActivity(productIntent);
    }

    @Override
    public void OnViewClick(int position) {
        Product product = products.get(position);
        String designertid = product.getDesignerid();
        LogTool.d(TAG, "designertid=" + designertid);
        Intent designerIntent = new Intent(getActivity(), DesignerInfoActivity.class);
        Bundle designerBundle = new Bundle();
        designerBundle.putString(Global.DESIGNER_ID, designertid);
        designerIntent.putExtras(designerBundle);
        startActivity(designerIntent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_product;
    }
}
