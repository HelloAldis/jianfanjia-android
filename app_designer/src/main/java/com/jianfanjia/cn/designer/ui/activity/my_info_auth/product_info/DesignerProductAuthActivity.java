package com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductList;
import com.jianfanjia.api.request.designer.GetAllProductRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.ui.adapter.DesignerWorksAdapter;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 11:26
 */
public class DesignerProductAuthActivity extends BaseSwipeBackActivity {

    @Bind(R.id.recycleview)
    RecyclerView mRecyclerView;

    @Bind(R.id.designer_product_head_layout)
    MainHeadView mMainHeadView;

    DesignerWorksAdapter mDesignerWorksAdapter;

    private List<Product> mProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        initMainView();
        initRecycleView();
        getAllProduct();
    }

    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(this));
        mDesignerWorksAdapter = new DesignerWorksAdapter(this,mProductList,new BaseRecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(int position) {

            }
        });
        mRecyclerView.setAdapter(mDesignerWorksAdapter);
    }


    private void getAllProduct(){
        GetAllProductRequest getAllProductRequest = new GetAllProductRequest();
        getAllProductRequest.setFrom(0);
        getAllProductRequest.setLimit(1000);
        Api.getAllProduct(getAllProductRequest, new ApiCallback<ApiResponse<ProductList>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<ProductList> apiResponse) {
                ProductList productList = apiResponse.getData();
                if(productList != null){
                    mProductList = productList.getProducts();
                    mDesignerWorksAdapter.setList(mProductList);
                }

            }

            @Override
            public void onFailed(ApiResponse<ProductList> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void initMainView(){
        mMainHeadView.setMianTitle(getString(R.string.my_product));

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_product_auth;
    }
}
