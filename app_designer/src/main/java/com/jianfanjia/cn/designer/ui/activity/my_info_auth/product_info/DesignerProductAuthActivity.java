package com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductList;
import com.jianfanjia.api.request.designer.DeleteOneProductRequest;
import com.jianfanjia.api.request.designer.GetAllProductRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.ui.adapter.DesignerWorksAdapter;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 11:26
 */
public class DesignerProductAuthActivity extends BaseSwipeBackActivity {

    @Bind(R.id.pullrefresh_recycleview)
    PullToRefreshRecycleView mRecyclerView;

    @Bind(R.id.designer_product_head_layout)
    MainHeadView mMainHeadView;

    DesignerWorksAdapter mDesignerWorksAdapter;

    private List<Product> mProductList;

    private boolean isEditStatus = false;//判断当前页面是否是编辑页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initMainView();
        initRecycleView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllProduct();
    }

    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getAllProduct();
            }
        });
        mRecyclerView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(this));
        mDesignerWorksAdapter = new DesignerWorksAdapter(this, mProductList, new BaseRecyclerViewAdapter
                .OnItemEditListener() {
            @Override
            public void onItemClick(int position) {
                Product product = mProductList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString(Global.PRODUCT_ID, product.get_id());
                startActivity(DesignerCaseInfoActivity.class, bundle);
            }

            @Override
            public void onItemAdd() {
                startActivity(UploadProductActivity.class);
            }

            @Override
            public void onItemDelete(int position) {
                Product product = mProductList.get(position);
                deleteOneProduct(product.get_id(), position);
            }
        });
        mRecyclerView.setAdapter(mDesignerWorksAdapter);
    }

    private void deleteOneProduct(String productId, final int position) {
        DeleteOneProductRequest deleteOneProductRequest = new DeleteOneProductRequest();
        deleteOneProductRequest.set_id(productId);

        Api.deleteOneProduct(deleteOneProductRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                mDesignerWorksAdapter.remove(position);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }


    private void getAllProduct() {
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
                mRecyclerView.onRefreshComplete();
            }

            @Override
            public void onSuccess(ApiResponse<ProductList> apiResponse) {
                ProductList productList = apiResponse.getData();
                if (productList != null) {
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

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.my_product));
        mMainHeadView.setRightTitle(getString(R.string.edit));
        mMainHeadView.setRightTitleColor(R.color.grey_color);
        mMainHeadView.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditStatus = !isEditStatus;
                mDesignerWorksAdapter.setIsEdit(isEditStatus);
                mDesignerWorksAdapter.notifyDataSetChanged();
                if (isEditStatus) {
                    mMainHeadView.setRightTitle(getString(R.string.finish));
                } else {
                    mMainHeadView.setRightTitle(getString(R.string.edit));
                }
            }
        });

    }

    @OnClick({R.id.head_back_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_product_auth;
    }
}
