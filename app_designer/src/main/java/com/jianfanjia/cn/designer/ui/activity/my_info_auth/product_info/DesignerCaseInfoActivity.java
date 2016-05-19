package com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info;

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
import com.jianfanjia.cn.designer.view.SwipeBackLayout;
import com.jianfanjia.common.tool.LogTool;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;

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


    @Bind(R.id.tv_title)
    protected TextView tv_title = null;

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

    private String productid = null;

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
        setListener();
    }

    private void getDataFromIntent(Intent intent) {
        Bundle productBundle = intent.getExtras();
        if (productBundle != null) {
            productid = productBundle.getString(Global.PRODUCT_ID);
            isIntentFromHome = productBundle.getBoolean(INTENT_FROM_HOME, false);
            if (isIntentFromHome) {
                initSwipeBack();
            }
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

    @OnClick({R.id.head_back_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                if (isIntentFromHome) {
                    overridePendingTransition(0, R.anim.slide_out_to_bottom);
                }
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
            Product designerCaseInfo = apiResponse.getData();
            LogTool.d(TAG, "designerCaseInfo" + designerCaseInfo);
            if (null != designerCaseInfo) {
                tv_title.setText(designerCaseInfo.getCell());
                nameText.setText(designerCaseInfo.getDesigner().getUsername());
                imageShow.displayImageHeadWidthThumnailImage(DesignerCaseInfoActivity.this, designerCaseInfo
                        .getDesigner().getImageid(), head_img);
                List<ProductImageInfo> imgList = designerCaseInfo.getImages();
                imgs.clear();
                for (ProductImageInfo info : imgList) {
                    imgs.add(info.getImageid());
                }
                DesignerCaseAdapter adapter = new DesignerCaseAdapter(DesignerCaseInfoActivity.this, imgList,
                        designerCaseInfo, new BaseRecyclerViewAdapter.OnItemClickListener() {
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
                designer_case_listview.setAdapter(adapter);
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
