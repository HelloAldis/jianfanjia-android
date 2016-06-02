package com.jianfanjia.cn.activity.ui.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.DesignerCanOrderList;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductImageInfo;
import com.jianfanjia.api.request.user.GetCanOrderDesignerListRequest;
import com.jianfanjia.api.request.user.OrderDesignerRequest;
import com.jianfanjia.api.request.user.ReplaceOrderedDesignerRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.ui.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.activity.ui.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.activity.ui.adapter.AppointDesignerViewPageAdapter;
import com.jianfanjia.cn.activity.api.Api;
import com.jianfanjia.cn.activity.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.activity.config.Constant;
import com.jianfanjia.cn.activity.constant.IntentConstant;
import com.jianfanjia.cn.activity.tools.ImageShow;
import com.jianfanjia.cn.activity.view.MainHeadView;
import com.jianfanjia.cn.activity.view.viewpager.transfrom.ScaleInOutTransfromer;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.viewpager_indicator.CircleIndicator;

/**
 * Description: com.jianfanjia.cn.activity.requirement
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-03 14:38
 */
public class AppointHighPointDesignerActivity extends BaseSwipeBackActivity {

    private static final String TAG = AppointHighPointDesignerActivity.class.getName();

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @Bind(R.id.appoint_head_layout)
    MainHeadView mMainHeadView;

    @Bind(R.id.dot_indicator)
    CircleIndicator mCircleIndicator;

    @Bind(R.id.appoint_tip)
    TextView appointTipView;

    AppointDesignerViewPageAdapter mViewPageAdapter;

    private String requirementid;
    private String oldDesignerid;

    List<View> mViews = new ArrayList<>();

    private List<Designer> rec_designer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
        initDate();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            requirementid = intent.getStringExtra(IntentConstant.REQUIREMENT_ID);
            oldDesignerid = intent.getStringExtra(IntentConstant.DESIGNER_ID);
        }
    }

    private void initView() {
        initMainView();
        changeTipColor();
    }

    private void changeTipColor() {
        appointTipView.setText(Html.fromHtml("您可立即预约<font color=\"#fe7003\"> 1 </font>名匠心定制设计师"));
    }

    private void initDate() {
        if (requirementid != null) {
            getOrderDesignerList(requirementid);
        }
    }

    private void initViewPager() {

        mViewPageAdapter = new AppointDesignerViewPageAdapter(this, mViews);

        mViewPager.setAdapter(mViewPageAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new ScaleInOutTransfromer());
        mCircleIndicator.setViewPager(mViewPager);
    }

    //获取自己可以预约的设计师
    private void getOrderDesignerList(String requestmentid) {
        GetCanOrderDesignerListRequest getCanOrderDesignerListRequest = new GetCanOrderDesignerListRequest();
        getCanOrderDesignerListRequest.setRequirementid(requestmentid);
        Api.getCanOrderDesigner(getCanOrderDesignerListRequest, new
                ApiCallback<ApiResponse<DesignerCanOrderList>>() {

                    @Override
                    public void onPreLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void onHttpDone() {
                        hideWaitDialog();
                    }

                    @Override
                    public void onSuccess(ApiResponse<DesignerCanOrderList> apiResponse) {
                        DesignerCanOrderList designerCanOrderListInfo = apiResponse.getData();
                        if (null != designerCanOrderListInfo) {
                            rec_designer = designerCanOrderListInfo.getRec_designer();
                            resetViews(rec_designer);
                        }
                    }

                    @Override
                    public void onFailed(ApiResponse<DesignerCanOrderList> apiResponse) {
                        makeTextShort(apiResponse.getErr_msg());
                    }

                    @Override
                    public void onNetworkError(int code) {
                        makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                    }
                });
    }

    //业主预约设计师
    private void orderDesignerByUser(String requestmentid, List<String> designerids) {
        OrderDesignerRequest orderDesignerRequest = new OrderDesignerRequest();
        orderDesignerRequest.setRequirementid(requestmentid);
        orderDesignerRequest.setDesignerids(designerids);
        Api.orderDesigner(orderDesignerRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                appManager.finishActivity(AppointHighPointDesignerActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }


    private void resetViews(List<Designer> designerList) {
        mViews.clear();
        int i = 0;
        for (final Designer designerInfo : designerList) {
            View view = inflater.inflate(R.layout.viewpager_item_appoint_designer, null, true);
            ViewHolder viewHolder = new ViewHolder(view);
            String username = designerInfo.getUsername();
            String imageid = designerInfo.getImageid();
            if (!TextUtils.isEmpty(imageid)) {
                ImageShow.getImageShow().displayImageHeadWidthThumnailImage(this, imageid, viewHolder
                        .designerinfo_head_img);
            } else {
                viewHolder.designerinfo_head_img.setImageResource(R.mipmap.icon_default_head);
            }
            if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
                viewHolder.designerinfo_auth.setVisibility(View.VISIBLE);
            } else {
                viewHolder.designerinfo_auth.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(username)) {
                viewHolder.designerName.setText(username);
            } else {
                viewHolder.designerName.setText(getResources().getString(R.string.designer));
            }

            final List<Product> productList = designerInfo.getProducts();
            if (productList.size() > 0) {
                Product product = productList.get(0);
                List<ProductImageInfo> productImageInfoList = product.getImages();
                if (productImageInfoList.size() > 0) {
                    String productImageid = productImageInfoList.get(0).getImageid();
                    ImageShow.getImageShow().displayThumbnailImage(productImageid, viewHolder
                            .designerCaseBackgroundView, viewHolder.designerCaseBackgroundView.getWidth(), false);
                }
            }

            viewHolder.viewCountText.setText("" + designerInfo.getView_count());
            viewHolder.productCountText.setText("" + designerInfo.getAuthed_product_count());
            viewHolder.appointCountText.setText("" + designerInfo.getOrder_count());
            float respond_speed = designerInfo.getRespond_speed();
            float service_attitude = designerInfo.getService_attitude();
            viewHolder.ratingBar.setRating((int) (respond_speed + service_attitude) / 2);
            viewHolder.appointView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> designerIdList = new ArrayList<>();
                    designerIdList.add(designerInfo.get_id());
                    if (oldDesignerid != null) {
                        replaceDesignerByUser(requirementid, oldDesignerid, designerInfo.get_id());
                    } else {
                        orderDesignerByUser(requirementid, designerIdList);
                    }
                }
            });
            viewHolder.mFrameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentToDesignerInfo(designerInfo);
                }
            });
            viewHolder.designerCaseBackgroundView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentToProduct(productList);
                }
            });
            mViews.add(view);
            i++;
        }
        initViewPager();
    }

    //替换设计师
    private void replaceDesignerByUser(String requirementid, String old_designerid, String new_designerid) {
        ReplaceOrderedDesignerRequest replaceOrderedDesignerRequest = new ReplaceOrderedDesignerRequest();
        replaceOrderedDesignerRequest.setRequirementid(requirementid);
        replaceOrderedDesignerRequest.setOld_designerid(old_designerid);
        replaceOrderedDesignerRequest.setNew_designerid(new_designerid);
        Api.replaceOrderedDesigner(replaceOrderedDesignerRequest, new ApiCallback<ApiResponse<String>>() {
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
                appManager.finishActivity(AppointHighPointDesignerActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void intentToDesignerInfo(Designer designerInfo) {
        Bundle designerBundle = new Bundle();
        designerBundle.putString(IntentConstant.DESIGNER_ID, designerInfo.get_id());
        startActivity(DesignerInfoActivity.class, designerBundle);
    }

    private void intentToProduct(List<Product> productNews) {
        if (productNews.size() > 0) {
            Product product = productNews.get(0);
            String productid = product.get_id();
            LogTool.d(TAG, "productid:" + productid);
            Bundle productBundle = new Bundle();
            productBundle.putString(IntentConstant.PRODUCT_ID, productid);
            startActivity(DesignerCaseInfoActivity.class, productBundle);
            overridePendingTransition(R.anim.slide_and_fade_in_from_bottom, 0);
        }
    }

    @OnClick({R.id.head_back_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(AppointHighPointDesignerActivity.this);
                break;
        }
    }


    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.high_point));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_appoint_highpoint_designer;
    }

    static class ViewHolder {

        @Bind(R.id.ratingBar)
        protected RatingBar ratingBar = null;

        @Bind(R.id.designer_case_background)
        protected ImageView designerCaseBackgroundView = null;

        @Bind(R.id.imageLayout)
        protected FrameLayout mFrameLayout;

        @Bind(R.id.designerinfo_head_img)
        protected ImageView designerinfo_head_img = null;

        @Bind(R.id.designerinfo_auth)
        protected ImageView designerinfo_auth = null;

        @Bind(R.id.designer_name)
        protected TextView designerName = null;

        @Bind(R.id.viewCountText)
        protected TextView viewCountText = null;

        @Bind(R.id.productCountText)
        protected TextView productCountText = null;

        @Bind(R.id.appointCountText)
        protected TextView appointCountText = null;

        @Bind(R.id.btn_apponit)
        protected TextView appointView = null;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
