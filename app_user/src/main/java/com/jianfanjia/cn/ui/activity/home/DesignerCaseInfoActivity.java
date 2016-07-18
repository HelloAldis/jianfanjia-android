package com.jianfanjia.cn.ui.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductImageInfo;
import com.jianfanjia.api.request.common.AddProductFavoriteRequest;
import com.jianfanjia.api.request.common.DeleteProductFavoriteRequest;
import com.jianfanjia.api.request.guest.GetProductHomePageRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.Event.MessageEvent;
import com.jianfanjia.cn.ui.activity.common.CommonShowPicActivity;
import com.jianfanjia.cn.ui.adapter.DesignerCaseAdapter;
import com.jianfanjia.cn.view.layout.SwipeBackLayout;
import com.jianfanjia.common.tool.LogTool;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;
import de.greenrobot.event.EventBus;
import me.iwf.photopicker.entity.AnimationRect;

/**
 * Description:设计师作品案例详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerCaseInfoActivity extends BaseSwipeBackActivity implements OnClickListener {
    private static final String TAG = DesignerCaseInfoActivity.class.getName();

    public static final String INTENT_FROM_HOME = "intent_from_home";

    private static final int SCROLL_Y = 120;
    private int mScrollY = 0;
    private boolean mHeaderIsShow = false;
    private boolean isIntentFromHome = false;

    @Bind(R.id.head_back_layout)
    protected RelativeLayout head_back_layout = null;

    @Bind(R.id.toolbar_collect_layout)
    protected RelativeLayout toolbar_collect_layout = null;

    @Bind(R.id.tv_title)
    protected TextView tv_title = null;

    @Bind(R.id.toolbar_collect)
    protected ImageView toolbar_collect = null;

    @Bind(R.id.top_info_layout)
    protected LinearLayout activity_case_info_top_layout = null;

    @Bind(R.id.designer_case_listview)
    protected RecyclerView designer_case_listview = null;

    private LinearLayoutManager mLayoutManager = null;

    @Bind(R.id.head_img)
    protected ImageView head_img = null;

    @Bind(R.id.name_text)
    protected TextView nameText = null;

    private List<String> imgs = new ArrayList<>();

    private List<ProductImageInfo> productImageInfoList;

    private DesignerCaseAdapter adapter;

    private String productid = null;
    private String designertid = null;

    private Product mProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initSwipeBack();
        this.initView();
        this.getDataFromIntent(this.getIntent());
        this.setListener();
    }

    private void initSwipeBack() {
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.TOP);
        swipeBackLayout.setScrollChild(designer_case_listview);
    }

    public void initView() {
        mLayoutManager = new LinearLayoutManager(DesignerCaseInfoActivity.this);
        designer_case_listview.setLayoutManager(mLayoutManager);
        designer_case_listview.setItemAnimator(new DefaultItemAnimator());
        designer_case_listview.setHasFixedSize(true);
        designer_case_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getApplicationContext()));

        adapter = new DesignerCaseAdapter(DesignerCaseInfoActivity.this);
        adapter.setOnItemClickListener(new DesignerCaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position,ImageView imageView) {
                LogTool.d("position:" + position);

                AnimationRect animationRect = AnimationRect.buildFromImageView(imageView);
                List<AnimationRect> animationRectList = new ArrayList<>();
                animationRectList.add(animationRect);
                gotoShowBigPic(position,animationRectList);
            }

            @Override
            public void intentToDesignerInfo() {
                startDesignerInfoActivity(designertid);
            }
        });
        designer_case_listview.setAdapter(adapter);
    }

    private void gotoShowBigPic(int position, List<AnimationRect>
            animationRectList) {
        LogTool.d("position:" + position);
        CommonShowPicActivity.intentTo(this,(ArrayList<String>) imgs, (ArrayList<AnimationRect>)
                animationRectList, position);
        overridePendingTransition(0, 0);
    }

    private void getDataFromIntent(Intent intent) {
        Bundle productBundle = intent.getExtras();
        if (productBundle != null) {
            productid = productBundle.getString(IntentConstant.PRODUCT_ID);
            isIntentFromHome = productBundle.getBoolean(INTENT_FROM_HOME, false);
            if (isIntentFromHome) {
                initSwipeBack();
            }
            LogTool.d("productid=" + productid);
            getProductHomePageInfo(productid);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.d("onNewIntent");
        mScrollY = 0;
        getDataFromIntent(intent);
    }

    private void setListener() {
        designer_case_listview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mScrollY += dy;
                if (mScrollY > SCROLL_Y) {
                    showTopHeader();
                } else {
                    hideTopHeader();
                }
            }
        });
    }

    private void showTopHeader() {
        if (!mHeaderIsShow) {
            ViewPropertyAnimator.animate(activity_case_info_top_layout).cancel();
            ViewPropertyAnimator.animate(activity_case_info_top_layout).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    activity_case_info_top_layout.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).translationY(0).setDuration(300).start();
            mHeaderIsShow = true;
        }
    }

    private void hideTopHeader() {
        if (mHeaderIsShow) {
            ViewPropertyAnimator.animate(activity_case_info_top_layout).cancel();
            ViewPropertyAnimator.animate(activity_case_info_top_layout).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    activity_case_info_top_layout.setVisibility(View.GONE);
                    tv_title.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).translationY(-SCROLL_Y).setDuration(300).start();
            mHeaderIsShow = false;
        }
    }

    @OnClick({R.id.head_back_layout, R.id.toolbar_collect_layout, R.id.top_info_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                if (isIntentFromHome) {
                    overridePendingTransition(0, R.anim.slide_out_to_bottom);
                }
                break;
            case R.id.toolbar_collect_layout:
                UiHelper.imageButtonAnim(toolbar_collect, null);
                if (toolbar_collect.isSelected()) {
                    deleteProductDesigner(productid);
                } else {
                    addProductHomePageInfo(productid);
                }
                break;
            case R.id.top_info_layout:
                startDesignerInfoActivity(designertid);
                break;
            default:
                break;
        }
    }

    private void startDesignerInfoActivity(String designertid) {
        Bundle designerInfoBundle = new Bundle();
        designerInfoBundle.putString(IntentConstant.DESIGNER_ID, designertid);
        startActivity(DesignerInfoActivity.class, designerInfoBundle);
    }

    private void getProductHomePageInfo(String productid) {
        GetProductHomePageRequest request = new GetProductHomePageRequest();
        request.set_id(productid);
        Api.getProductHomePage(request, this.getProductHomePageInfoCallback,this);
    }

    private void addProductHomePageInfo(String productid) {
        AddProductFavoriteRequest request = new AddProductFavoriteRequest();
        request.set_id(productid);
        Api.addProductFavorite(request, this.addProductHomePageInfoCallback,this);
    }

    private void deleteProductDesigner(String productid) {
        DeleteProductFavoriteRequest request = new DeleteProductFavoriteRequest();
        request.set_id(productid);
        Api.deleteProductFavorite(request, this.deleteProductCallback,this);
    }

    private ApiCallback<ApiResponse<Product>> getProductHomePageInfoCallback = new ApiCallback<ApiResponse<Product>>() {
        @Override
        public void onPreLoad() {
            Hud.show(getUiContext());
        }

        @Override
        public void onHttpDone() {
            Hud.dismiss();
        }

        @Override
        public void onSuccess(ApiResponse<Product> apiResponse) {
            mProduct = apiResponse.getData();
            LogTool.d("designerCaseInfo" + mProduct);
            if (null != mProduct) {
                toolbar_collect_layout.setVisibility(View.VISIBLE);
                if (mProduct.is_my_favorite()) {
                    toolbar_collect.setSelected(true);
                } else {
                    toolbar_collect.setSelected(false);
                }
                designertid = mProduct.getDesigner().get_id();
                tv_title.setText(mProduct.getCell());
                nameText.setText(mProduct.getDesigner().getUsername());
                imageShow.displayImageHeadWidthThumnailImage(DesignerCaseInfoActivity.this, mProduct
                        .getDesigner().getImageid(), head_img);

                List<ProductImageInfo> imgList = mProduct.getImages();
                List<ProductImageInfo> planimgList = mProduct.getPlan_images();
                productImageInfoList = new ArrayList<>();
                imgs.clear();
                for (ProductImageInfo info : planimgList) {
                    imgs.add(info.getImageid());
                    productImageInfoList.add(info);
                }
                for (ProductImageInfo info : imgList) {
                    imgs.add(info.getImageid());
                    productImageInfoList.add(info);
                }
                adapter.setDesignerCaseInfo(mProduct);
                designer_case_listview.setAdapter(adapter);
            }
        }

        @Override
        public void onFailed(ApiResponse<Product> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
            toolbar_collect.setVisibility(View.GONE);
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
        }
    };

    private ApiCallback<ApiResponse<Object>> addProductHomePageInfoCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            toolbar_collect.setSelected(true);
            makeTextShort(getString(R.string.str_collect_success));
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_PRODUCT_FRAGMENT));
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<Object>> deleteProductCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            toolbar_collect.setSelected(false);
            makeTextShort(getString(R.string.str_cancel_collect_success));
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_PRODUCT_FRAGMENT));
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
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
