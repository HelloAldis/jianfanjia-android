package com.jianfanjia.cn.designer.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.designer.Event.MessageEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.designer.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.designer.adapter.ProductAdapter;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.bean.Product;
import com.jianfanjia.cn.designer.bean.ProductInfo;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author fengliang
 * @ClassName: ProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class ProductFragment extends BaseFragment implements ApiUiUpdateListener, RecyclerViewOnItemClickListener {
    private static final String TAG = DecorationImgFragment.class.getName();
    private RecyclerView prodtct_listview = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private ProductAdapter productAdapter = null;
    private List<Product> products = new ArrayList<Product>();
    private int currentPos = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        prodtct_listview = (RecyclerView) view.findViewById(R.id.prodtct_listview);
        prodtct_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        prodtct_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        prodtct_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogTool.d(TAG, "ProductFragment 可见");
            getProductList();
        } else {
            LogTool.d(TAG, "ProductFragment 不可见");
        }
    }

    private void getProductList() {
        JianFanJiaClient.getCollectListByUser(getActivity(), 0, 100, this, this);
    }

    @Override
    public void setListener() {
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getProductList();
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
                productAdapter = new ProductAdapter(getActivity(), products, this);
                prodtct_listview.setAdapter(productAdapter);
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
        makeTextLong(error_msg);
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
        return R.layout.fragment_product;
    }
}
