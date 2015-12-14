package com.jianfanjia.cn.fragment;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.jianfanjia.cn.activity.DesignerCaseInfoActivity;
import com.jianfanjia.cn.activity.DesignerInfoActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.ProductAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.bean.ProductInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: ProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class ProductFragment extends BaseFragment implements ApiUiUpdateListener, RecyclerViewOnItemClickListener {
    private static final String TAG = DecorationImgFragment.class.getName();
    private RecyclerView prodtct_listview = null;
    private ProductAdapter productAdapter = null;
    private List<Product> products = new ArrayList<Product>();
    private String productid = null;
    private int itemPosition = -1;

    @Override
    public void initView(View view) {
        prodtct_listview = (RecyclerView) view.findViewById(R.id.prodtct_listview);
        prodtct_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        prodtct_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        prodtct_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        JianFanJiaClient.getCollectListByUser(getActivity(), 0, 100, this, this);
    }

    @Override
    public void setListener() {

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
            productAdapter = new ProductAdapter(getActivity(), products, this);
            prodtct_listview.setAdapter(productAdapter);
        }
    }

    @Override
    public void loadFailture(String error_msg) {

    }

    @Override
    public void OnItemClick(View view, int position) {
        Product product = products.get(position);
        productid = product.get_id();
        LogTool.d(TAG, "productid:" + productid);
        Bundle productBundle = new Bundle();
        productBundle.putString(Global.PRODUCT_ID, productid);
        startActivity(DesignerCaseInfoActivity.class, productBundle);
    }

    @Override
    public void OnLongItemClick(View view, int position) {
        itemPosition = position;
        Product product = products.get(position);
        productid = product.get_id();
        LogTool.d(TAG, "productid=" + productid);
        deleteProductDialog();
    }

    @Override
    public void OnViewClick(int position) {
        Product product = products.get(position);
        String designertid = product.getDesignerid();
        LogTool.d(TAG, "designertid:" + designertid);
        Bundle designerBundle = new Bundle();
        designerBundle.putString(Global.DESIGNER_ID, designertid);
        startActivity(DesignerInfoActivity.class, designerBundle);
    }

    private void deleteProductDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        dialog.setTitle("移出作品");
        dialog.setMessage("确定把该作品移出列表吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteProductDesigner(productid);
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    private void deleteProductDesigner(String productid) {
        JianFanJiaClient.deleteCollectionByUser(getActivity(), productid, deleteProductListener, this);
    }

    private ApiUiUpdateListener deleteProductListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data.toString());
            products.remove(itemPosition);
            productAdapter.notifyItemRemoved(itemPosition);
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_product;
    }
}
