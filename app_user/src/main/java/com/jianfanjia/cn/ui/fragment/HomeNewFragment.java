package com.jianfanjia.cn.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.request.guest.GetHomeProductRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.activity.home.DecorateLiveActivity;
import com.jianfanjia.cn.ui.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.ui.activity.home.DesignerCaseListActivity;
import com.jianfanjia.cn.ui.activity.home.DesignerListActivity;
import com.jianfanjia.cn.ui.activity.home.SearchActivity;
import com.jianfanjia.cn.ui.activity.home.WebViewActivity;
import com.jianfanjia.cn.ui.activity.home.WebViewPackage365Activity;
import com.jianfanjia.cn.ui.activity.requirement.GetFreePlanActivity;
import com.jianfanjia.cn.ui.adapter.HomeProductPagerAdapter;
import com.jianfanjia.cn.ui.adapter.ViewPageAdapter;
import com.jianfanjia.cn.ui.interf.ViewPagerClickListener;
import com.jianfanjia.cn.view.pullrefresh.PullToRefreshHomeScrollView;
import com.jianfanjia.cn.view.scrollview.HomeScrollView;
import com.jianfanjia.cn.view.viewpager.auto_view_pager.AutoScrollViewPager;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description: com.jianfanjia.cn.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-02-19 09:54
 */
public class HomeNewFragment extends BaseFragment {
    private static final String TAG = HomeNewFragment.class.getName();
    public static final int TOTAL_COUNT = 20;
    private int BANNER_ICON[] = {R.mipmap.banner_home_loans, R.mipmap.bg_home_banner1, R.mipmap.bg_home_banner2};

    @Bind(R.id.viewPager_lib)
    protected AutoScrollViewPager scrollViewPager;

    @Bind(R.id.indicatorGroup_lib)
    protected LinearLayout dotLinearLayout;

    @Bind(R.id.content_layout)
    protected RelativeLayout coordinatorLayout;

    @Bind(R.id.content_viewpager)
    protected ViewPager contentViewPager;

    @Bind(R.id.pullrefresh_scrollview)
    protected PullToRefreshHomeScrollView mPullToRefreshHomeScrollView;

    @Bind(R.id.content_intent_to)
    protected ImageButton contentIntent;

    @Bind(R.id.content_next)
    protected ImageButton contentNext;

    @Bind(R.id.rootview)
    protected LinearLayout rootView;

    private HomeProductPagerAdapter mPagerAdapter;
    private List<Product> productNews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    protected void initView() {
        initBannerView(scrollViewPager, dotLinearLayout, new ViewPagerClickListener() {
            @Override
            public void onClickItem(int pos) {
                LogTool.d("position =" + pos);
                if (pos == 1) {
                    WebViewActivity.intentToWebView(getUiContext(), Global.WEB_VIEW_URL_SUPERVISION);
                } else if (pos == 0) {
                    WebViewActivity.intentToWebView(getUiContext(), Global.WEB_VIEW_URL_CHENGDAITONG);
                } else if (pos == 2) {
                    WebViewActivity.intentToWebView(getUiContext(), Global.WEB_VIEW_URL_SAFEGUARD);
                }
            }
        });


        mPullToRefreshHomeScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getProduct(TOTAL_COUNT);
            }
        });
        mPullToRefreshHomeScrollView.setScrollPullUpListener(new HomeScrollView.ScrollPullUpListener() {
            @Override
            public void scrollPullUp() {
                intentToProduct();
            }
        });

        /*mPullToRefreshScrollViewNew.setShowGuideListener(new HomeScrollView.ShowGuideListener() {
            @Override
            public void showGuideView() {
                if (dataManager.isShowGuide()) {
                    int[] location = new int[2];
                    contentIntent.getLocationInWindow(location);
                    showGuide(location[0], location[1], contentIntent.getWidth() / 2);
                }
            }
        });*/
        contentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    dataManager.setShowNext(false);
                    contentNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getProduct(TOTAL_COUNT);
    }

    private void intentToProduct() {
        if (productNews != null) {
            Product product = productNews.get(contentViewPager.getCurrentItem());
            String productid = product.get_id();
            LogTool.d("productid:" + productid);
            Bundle productBundle = new Bundle();
            productBundle.putString(IntentConstant.PRODUCT_ID, productid);
            productBundle.putBoolean(DesignerCaseInfoActivity.INTENT_FROM_HOME, true);
            startActivity(DesignerCaseInfoActivity.class, productBundle);
            getActivity().overridePendingTransition(R.anim.slide_and_fade_in_from_bottom, 0);
        }
    }

    private void getProduct(int limit) {
        GetHomeProductRequest request = new GetHomeProductRequest();
        request.setLimit(limit);
        Api.getTopProducts(request, new ApiCallback<ApiResponse<List<Product>>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {
                if (mPullToRefreshHomeScrollView != null) {
                    mPullToRefreshHomeScrollView.onRefreshComplete();
                }
            }

            @Override
            public void onSuccess(ApiResponse<List<Product>> apiResponse) {
                if (getView() == null) return;
                productNews = apiResponse.getData();
                if (mPagerAdapter == null) {
                    mPagerAdapter = new HomeProductPagerAdapter(getContext(), productNews);
                    contentViewPager.setAdapter(mPagerAdapter);
                } else {
                    mPagerAdapter.setProductList(productNews);
                }
                contentIntent.setVisibility(View.VISIBLE);
                if (dataManager.isShowNext()) {
                    contentNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(ApiResponse<List<Product>> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.getMsg(code));
                contentNext.setVisibility(View.GONE);
                contentIntent.setVisibility(View.GONE);
            }
        }, this);
    }

    @OnClick({R.id.ltm_home_layout0, R.id.ltm_home_layout1, R.id.ltm_home_layout2, R.id.ltm_home_layout3, R.id
            .home_search, R.id.list_item_more_layout, R.id.content_intent_to, R.id.riv_jianjia, R.id.riv_365packget,
            R.id.home_phone})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.ltm_home_layout0:
                publish_requirement();
                break;
            case R.id.ltm_home_layout1:
                intentToDecStrategy();
                break;
            case R.id.ltm_home_layout2:
                startActivity(DecorateLiveActivity.class);
                break;
            case R.id.ltm_home_layout3:
                startActivity(DesignerListActivity.class);
                break;
            case R.id.list_item_more_layout:
                startActivity(DesignerCaseListActivity.class);
                break;
            case R.id.home_search:
                startActivity(SearchActivity.class);
                break;
            case R.id.content_intent_to:
                intentToProduct();
                break;
            case R.id.riv_365packget:
                intentTo365pACKGET();
                break;
            case R.id.riv_jianjia:
                intentToJianJia();
                break;
            case R.id.home_phone:
                UiHelper.callPhoneIntent(getUiContext(), getString(R.string.app_phone));
                break;
            default:
                break;
        }
    }

    private void intentToDecStrategy() {
        Bundle bundle = new Bundle();
        bundle.putString(Global.WEB_VIEW_URL, Global.WEB_VIEW_URL_DEC_STRATEGY);
        startActivity(WebViewActivity.class, bundle);
    }

    private void intentTo365pACKGET() {
        Bundle bundle = new Bundle();
        bundle.putString(Global.WEB_VIEW_URL, Url_New.getInstance().PACKGET365_DETAIL_URL);
        startActivity(WebViewPackage365Activity.class, bundle);
    }

    private void intentToJianJia() {
        Bundle bundle = new Bundle();
        bundle.putString(Global.WEB_VIEW_URL, Global.WEB_VIEW_URL_YIJIA);
        startActivity(WebViewActivity.class, bundle);
    }

    protected void publish_requirement() {
        startActivity(GetFreePlanActivity.class);
    }

    private void initBannerView(AutoScrollViewPager viewPager, LinearLayout indicatorGroup_lib, final
    ViewPagerClickListener viewPagerClickListener) {
        indicatorGroup_lib.removeAllViews();
        List<View> bannerList = new ArrayList<>();
        for (int i = 0; i < BANNER_ICON.length; i++) {
            LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT));
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            imageView.setImageResource(BANNER_ICON[i]);
            imageView.setLayoutParams(new LinearLayout.LayoutParams((int) TDevice.getScreenWidth(), (int) (520 /
                    (1242 / TDevice.getScreenWidth()))));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            linearLayout.addView(imageView);
            bannerList.add(linearLayout);
        }
        final View[] indicators = new View[bannerList.size()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new ViewGroup.LayoutParams(TDevice.dip2px(getContext(), 8), TDevice.dip2px(getContext(),
                        8)));
        params.setMargins(0, 0, TDevice.dip2px(getContext(), 8), TDevice.dip2px(getContext(), 8));
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new View(getActivity().getApplicationContext());
            if (i == 0) {
                indicators[i].setBackgroundResource(R.drawable.shape_indicator_selected_oval);
            } else {
                indicators[i].setBackgroundResource(R.drawable.shape_indicator_unselected_oval);
            }
            indicators[i].setLayoutParams(params);
            indicatorGroup_lib.addView(indicators[i]);
        }
        ViewPageAdapter mPagerAdapter = new ViewPageAdapter(getContext(), bannerList);
        mPagerAdapter.setViewPagerClickListener(viewPagerClickListener);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < indicators.length; i++) {
                    if (i == arg0) {
                        indicators[i]
                                .setBackgroundResource(R.drawable.shape_indicator_selected_oval);
                    } else {
                        indicators[i]
                                .setBackgroundResource(R.drawable.shape_indicator_unselected_oval);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {


            }
        });
        viewPager.setInterval(5000);
        viewPager.startAutoScroll();

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_new_home;
    }
}
