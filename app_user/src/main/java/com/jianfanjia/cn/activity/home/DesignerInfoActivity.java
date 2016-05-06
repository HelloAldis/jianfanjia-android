package com.jianfanjia.cn.activity.home;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.guest.DesignerHomePageRequest;
import com.jianfanjia.api.request.user.AddFavoriteDesignerRequest;
import com.jianfanjia.api.request.user.DeleteFavoriteDesignerRequest;
import com.jianfanjia.cn.Event.CollectDesignerEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.fragment.DesignerInfoFragment;
import com.jianfanjia.cn.fragment.DesignerProductFragment;
import com.jianfanjia.cn.tools.ScrollableHelper;
import com.jianfanjia.cn.view.layout.ScrollableLayout;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;

/**
 * Description:设计师信息
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerInfoActivity extends BaseSwipeBackActivity implements OnClickListener, ViewPager
        .OnPageChangeListener {
    private static final String TAG = DesignerInfoHighPointActivity.class.getName();

    @Bind(R.id.sl_root)
    protected ScrollableLayout sl_root = null;

    @Bind(R.id.head_back_layout)
    protected RelativeLayout head_back_layout = null;

    @Bind(R.id.tv_title)
    protected TextView tv_title = null;

    @Bind(R.id.tabs)
    protected TabLayout tabLayout = null;

    @Bind(R.id.viewpager)
    protected ViewPager viewPager = null;

    @Bind(R.id.ratingBar)
    protected RatingBar ratingBar = null;

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

    @Bind(R.id.viewCountTextTitle)
    protected TextView viewCountTextTitle = null;

    @Bind(R.id.productCountTextTitle)
    protected TextView productCountTextTitle = null;

    @Bind(R.id.appointCountTextTitle)
    protected TextView appointCountTextTitle = null;

    @Bind(R.id.btn_add)
    protected Button addBtn = null;

    @Bind(R.id.designer_info_head_content)
    protected LinearLayout headContentLayout;

    @Bind(R.id.container_layout)
    protected LinearLayout containerLayout;

    @Bind(R.id.designer_info_background)
    ImageView designerInfoBackground;

    @Bind(R.id.designer_info_font)
    ImageView designerInfoFont;

    @Bind(R.id.designer_info_tab_content)
    LinearLayout designerInfoTabLayout;

    @Bind(R.id.merger_button1_layout)
    RelativeLayout buttonResourceLayout;

    @Bind(R.id.merger_button2_layout)
    RelativeLayout buttonCaseLayout;

    @Bind(R.id.designer_info_head_content_wrap)
    FrameLayout designerInfoHeadContentWrap;

    @Bind(R.id.ic_headerbar)
    RelativeLayout headLayout;

    private boolean isHighPoint;

    private String designerid = null;
    private String designer_name = null;

    private float titleMaxScrollHeight;
    private float hearderMaxHeight;
    private float avatarTop;
    private float maxScrollHeight;

    private int initHeight;

    private List<SelectItem> listViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent(this.getIntent());
        this.initView();
        this.setListener();
    }

    public void initView() {
        viewPager.setOffscreenPageLimit(1);
        tv_title.setTranslationY(-1000);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getDataFromIntent(Intent intent) {
        Bundle designerBundle = intent.getExtras();
        if (designerBundle != null) {
            designerid = designerBundle.getString(IntentConstant.DESIGNER_ID);
            LogTool.d(TAG, "designerid =" + designerid);
            getDesignerPageInfo(designerid);
        }
    }

    private void startChangeAni() {
        final int startHeight = designerInfoHeadContentWrap.getHeight();
        int aniHeight = startHeight - initHeight;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0, aniHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) designerInfoHeadContentWrap
                        .getLayoutParams();
                layoutParams.height = startHeight - (Integer) animation.getAnimatedValue();
                designerInfoHeadContentWrap.setLayoutParams(layoutParams);
            }
        });

        Integer fromColor = getResources().getColor(R.color.font_white);
        Integer endColor = getResources().getColor(R.color.light_black_color);
        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, endColor);
        textColorAnimator.setDuration(300);
        textColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (Integer) animation.getAnimatedValue();
                LogTool.d(this.getClass().getName(), "color =" + color);
                setAllTextColor(color);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(designerInfoFont, "alpha",
                0.0f, 1.0f);
        objectAnimator.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimator, objectAnimator, textColorAnimator);
        animatorSet.start();
    }

    private void setAllTextColor(int color) {
        designerName.setTextColor(color);
        viewCountText.setTextColor(color);
        productCountText.setTextColor(color);
        appointCountText.setTextColor(color);
        viewCountTextTitle.setTextColor(color);
        productCountTextTitle.setTextColor(color);
        appointCountTextTitle.setTextColor(color);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.d(TAG, "onNewIntent");
        getDataFromIntent(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        SelectItem resItem = new SelectItem(DesignerInfoFragment.newInstance(designerid),
                getResources().getString(R.string.resourceText));
        SelectItem productItem = new SelectItem(DesignerProductFragment.newInstance(designerid),
                getResources().getString(R.string.str_case));
        listViews.add(resItem);
        listViews.add(productItem);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        sl_root.getHelper().setCurrentScrollableContainer((ScrollableHelper.ScrollableContainer) listViews.get(0)
                .getFragment());
    }

    private void setListener() {
        sl_root.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int translationY, int maxY) {
                translationY = -translationY;
                if (titleMaxScrollHeight == 0) {
                    titleMaxScrollHeight = ((View) tv_title.getParent()).getBottom() - tv_title.getTop();
                    maxScrollHeight = hearderMaxHeight + titleMaxScrollHeight;
                }
                if (hearderMaxHeight == 0) {
                    hearderMaxHeight = designerName.getTop();
                    maxScrollHeight = hearderMaxHeight + titleMaxScrollHeight;
                }
                if (avatarTop == 0) {
                    avatarTop = designerinfo_head_img.getTop();
                }
                int alpha = 0;
                int baseAlpha = 60;
                if (0 > avatarTop + translationY) {
                    alpha = Math.min(255, (int) (Math.abs(avatarTop + translationY) * (255 - baseAlpha) /
                            (hearderMaxHeight -
                                    avatarTop) + baseAlpha));
                    tv_title.setVisibility(View.VISIBLE);
                } else {
                    tv_title.setVisibility(View.GONE);
                }
                tv_title.setAlpha(alpha);
                tv_title.setTranslationY(Math.max(0, maxScrollHeight + translationY));
            }
        });
    }

    @OnClick({R.id.head_back_layout, R.id.btn_add, R.id.merger_button1_layout, R.id
            .merger_button2_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_add:
                if (!addBtn.isSelected()) {
                    addFavoriteDesignerToList(designerid);
                } else {
                    deleteFavoriteDesigner(designerid);
                }
                break;
            case R.id.merger_button1_layout:
                startChangeAni();
                designerInfoTabLayout.setVisibility(View.GONE);
                sl_root.setIsEnableScroll(true);
                viewPager.setCurrentItem(0);
                break;
            case R.id.merger_button2_layout:
                startChangeAni();
                designerInfoTabLayout.setVisibility(View.GONE);
                sl_root.setIsEnableScroll(true);
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        sl_root.getHelper().setCurrentScrollableContainer((ScrollableHelper.ScrollableContainer) listViews.get
                (position).getFragment());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void getDesignerPageInfo(String designerid) {
        DesignerHomePageRequest request = new DesignerHomePageRequest();
        request.set_id(designerid);
        Api.getDesignerHomePage(request, designerHomePageCallback);
    }

    private void addFavoriteDesignerToList(String designerid) {
        AddFavoriteDesignerRequest request = new AddFavoriteDesignerRequest();
        request.set_id(designerid);
        Api.addFavoriteDesigner(request, addFavoriteDesignerCallback);
    }

    private void deleteFavoriteDesigner(String designerid) {
        DeleteFavoriteDesignerRequest request = new DeleteFavoriteDesignerRequest();
        request.set_id(designerid);
        Api.deleteFavoriteDesigner(request, this.deleteMyFavoriteDesignerCallback);
    }

    private ApiCallback<ApiResponse<Designer>> designerHomePageCallback = new ApiCallback<ApiResponse<Designer>>() {
        @Override
        public void onPreLoad() {
            showWaitDialog();
        }

        @Override
        public void onHttpDone() {
            hideWaitDialog();
        }

        @Override
        public void onSuccess(ApiResponse<Designer> apiResponse) {
            Designer designerInfo = apiResponse.getData();
            updateUi(designerInfo);
        }

        @Override
        public void onFailed(ApiResponse<Designer> apiResponse) {
            makeTextLong(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
        }
    };

    private void updateUi(Designer designerInfo) {
        if (null != designerInfo) {
            LogTool.d(TAG, "designerInfo:" + designerInfo);
            designer_name = designerInfo.getUsername();
            tv_title.setText(designer_name);
            designerName.setText(designer_name);
            String designerid = designerInfo.getImageid();
            if (!TextUtils.isEmpty(designerid)) {
                imageShow.displayImageHeadWidthThumnailImage(DesignerInfoActivity.this, designerInfo
                        .getImageid()
                        , designerinfo_head_img);
            } else {
                imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, designerinfo_head_img);
            }
            if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
                designerinfo_auth.setVisibility(View.VISIBLE);
            } else {
                designerinfo_auth.setVisibility(View.GONE);
            }
            viewCountText.setText("" + designerInfo.getView_count());
            productCountText.setText("" + designerInfo.getProduct_count());
            appointCountText.setText("" + designerInfo.getOrder_count());
            float respond_speed = designerInfo.getRespond_speed();
            float service_attitude = designerInfo.getService_attitude();
            ratingBar.setRating((int) (respond_speed + service_attitude) / 2);
            if (!designerInfo.is_my_favorite()) {
                addBtn.setSelected(false);
                addBtn.setTextColor(getResources().getColor(R.color.font_white));
                addBtn.setText(R.string.strl_add_yixiang);
            } else {
                addBtn.setSelected(true);
                addBtn.setTextColor(getResources().getColor(R.color.orange_color));
                addBtn.setText(R.string.strl_delete_yixiang);
            }
            changeUiShow(designerInfo);
        }
    }

    private void changeUiShow(Designer designerInfo) {
        isHighPoint = RequirementBusiness.isHighPointDesigner(designerInfo);
        LogTool.d(this.getClass().getName(), "isHigh =" + isHighPoint);
        designerInfoHeadContentWrap.setVisibility(View.VISIBLE);
        if (isHighPoint) {
            designerInfoHeadContentWrap.post(new Runnable() {
                @Override
                public void run() {

                    int height = containerLayout.getHeight() - tabLayout.getHeight() - headLayout.getHeight();
                    initHeight = headContentLayout.getHeight();
                    designerInfoHeadContentWrap.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                            .MATCH_PARENT, height));
                }
            });
            designerInfoBackground.setImageResource(R.mipmap.bg_fragment_my);
            designerInfoTabLayout.setVisibility(View.VISIBLE);
            sl_root.setIsEnableScroll(false);
            setAllTextColor(getResources().getColor(R.color.font_white));
        } else {
            setAllTextColor(getResources().getColor(R.color.light_black_color));
            designerInfoTabLayout.setVisibility(View.GONE);
            sl_root.setIsEnableScroll(true);
        }
    }

    private ApiCallback<ApiResponse<Object>> addFavoriteDesignerCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            addBtn.setSelected(true);
            addBtn.setTextColor(getResources().getColor(R.color.orange_color));
            addBtn.setText(R.string.strl_delete_yixiang);
//            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_ORDER_DESIGNER_ACTIVITY));
            postFavoriteDesignerEvent(true);
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
            makeTextLong(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
        }
    };

    private void postFavoriteDesignerEvent(boolean isCollect){
        CollectDesignerEvent collectDesignerEvent = new CollectDesignerEvent();
        collectDesignerEvent.setDesignerId(designerid);
        collectDesignerEvent.setIsCollect(isCollect);
        EventBus.getDefault().post(collectDesignerEvent);
    }


    private ApiCallback<ApiResponse<Object>> deleteMyFavoriteDesignerCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            addBtn.setSelected(false);
            addBtn.setTextColor(getResources().getColor(R.color.font_white));
            addBtn.setText(R.string.strl_add_yixiang);
            postFavoriteDesignerEvent(false);
//            EventBus.getDefault().post(new MessageEvent(Constant.DELETE_FAVORITE_DESIGNER_FRAGMENT));
//            EventBus.getDefault().post(new MessageEvent(Constant.DELETE_ORDER_DESIGNER_ACTIVITY));
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
            makeTextLong(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_info_high_point;
    }
}