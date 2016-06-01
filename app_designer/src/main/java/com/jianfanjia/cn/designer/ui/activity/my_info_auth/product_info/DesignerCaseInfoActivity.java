package com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductImageInfo;
import com.jianfanjia.api.request.guest.GetProductHomePageRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.ui.activity.common.ShowPicActivity;
import com.jianfanjia.cn.designer.ui.adapter.DesignerCaseAdapter;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description:设计师作品案例详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerCaseInfoActivity extends BaseSwipeBackActivity implements OnClickListener {
    private static final String TAG = DesignerCaseInfoActivity.class.getName();

    private int mScrollY = 0;

    @Bind(R.id.head_back_layout)
    protected RelativeLayout head_back_layout = null;

    @Bind(R.id.tv_title)
    protected TextView tv_title = null;

    @Bind(R.id.designer_case_listview)
    protected RecyclerView designer_case_listview = null;

    private LinearLayoutManager mLayoutManager = null;

    private List<String> imgs = new ArrayList<>();

    private List<ProductImageInfo> productImageInfoList;

    private String productid = null;
    private Product mProduct;

    private DesignerCaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
        this.getDataFromIntent(this.getIntent());
        this.setListener();
    }

    public void initView() {
        mLayoutManager = new LinearLayoutManager(DesignerCaseInfoActivity.this);
        designer_case_listview.setLayoutManager(mLayoutManager);
        designer_case_listview.setItemAnimator(new DefaultItemAnimator());
        designer_case_listview.setHasFixedSize(true);
        designer_case_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getApplicationContext()));

        adapter = new DesignerCaseAdapter(DesignerCaseInfoActivity.this, productImageInfoList, new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogTool.d(TAG, "position:" + position);
                Bundle showPicBundle = new Bundle();
                showPicBundle.putInt(Constant.CURRENT_POSITION, position);
                showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                        (ArrayList<String>) imgs);
                startActivity(ShowPicActivity.class, showPicBundle);
            }
        });
        tv_title.setText(getString(R.string.product_detail));
        designer_case_listview.setAdapter(adapter);
        setListener();
    }

    private void getDataFromIntent(Intent intent) {
        Bundle productBundle = intent.getExtras();
        if (productBundle != null) {
            productid = productBundle.getString(Global.PRODUCT_ID);
            LogTool.d(TAG, "productid=" + productid);
            getProductHomePageInfo(productid);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.d(TAG, "onNewIntent");
        mScrollY = 0;
        getDataFromIntent(intent);
    }

    private void setListener() {
        mScrollY = -TDevice.dip2px(this, 45);
        tv_title.setTranslationY(mScrollY);
        LogTool.d(this.getClass().getName(), "mScrollY =" + mScrollY);
        designer_case_listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LogTool.d(this.getClass().getName(), "dy =" + dy);
                mScrollY += dy;
                if (tv_title.getTranslationY() > 0) {
                    tv_title.setTranslationY(mScrollY);
                }
            }
        });
    }

    @OnClick({R.id.head_back_layout, R.id.head_right_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.head_right_title:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Global.PRODUCT_INFO, mProduct);
                startActivity(UploadProductActivity.class, bundle);
                break;
            default:
                break;
        }
    }

    private void getProductHomePageInfo(String productid) {
        GetProductHomePageRequest request = new GetProductHomePageRequest();
        request.set_id(productid);
        Api.getProductHomePage(request, this.getProductHomePageInfoCallback);
    }

    private ApiCallback<ApiResponse<Product>> getProductHomePageInfoCallback = new ApiCallback<ApiResponse<Product>>() {
        @Override
        public void onPreLoad() {
            showWaitDialog(R.string.loading);
        }

        @Override
        public void onHttpDone() {
            hideWaitDialog();
        }

        @Override
        public void onSuccess(ApiResponse<Product> apiResponse) {
            mProduct = apiResponse.getData();
            LogTool.d(TAG, "designerCaseInfo" + mProduct);
            if (null != mProduct) {
                tv_title.setText(mProduct.getCell());
                List<ProductImageInfo> imgList = mProduct.getImages();
                List<ProductImageInfo> planimgList = mProduct.getPlan_images();
                productImageInfoList = new ArrayList<>();
                imgs.clear();
                for (ProductImageInfo info : planimgList) {
                    imgs.add(info.getImageid());
                    info.setSection("平面设计图");
                    productImageInfoList.add(info);
                }
                for (ProductImageInfo info : imgList) {
                    imgs.add(info.getImageid());
                    productImageInfoList.add(info);
                }
                adapter.setDesignerCaseInfo(mProduct);
                adapter.setList(productImageInfoList);
            }
        }

        @Override
        public void onFailed(ApiResponse<Product> apiResponse) {
            makeTextLong(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_case_info;
    }

}