package com.jianfanjia.cn.fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DecStrategyActivity_;
import com.jianfanjia.cn.activity.home.DesignerCaseListActivity;
import com.jianfanjia.cn.activity.home.DesignerListActivity;
import com.jianfanjia.cn.activity.home.SearchActivity_;
import com.jianfanjia.cn.activity.requirement.PublishRequirementActivity_;
import com.jianfanjia.cn.adapter.HomeProductPagerAdapter;
import com.jianfanjia.cn.adapter.ViewPageAdapter;
import com.jianfanjia.cn.base.BaseAnnotationFragment;
import com.jianfanjia.cn.bean.ProductNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.view.MyViewPager;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshScrollView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: com.jianfanjia.cn.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-02-19 09:54
 */
@EFragment(R.layout.fragment_new_home)
public class HomeNewFragment extends BaseAnnotationFragment {
    private static final String TAG = HomeNewFragment.class.getName();
    public static final int TOTAL_COUNT = 20;
    private int BANNER_ICON[] = {R.mipmap.bg_home_banner1,
            R.mipmap.bg_home_banner2, R.mipmap.bg_home_banner3,
            R.mipmap.bg_home_banner4};

    @ViewById(R.id.viewPager_lib)
    protected MyViewPager scrollViewPager;

    @ViewById(R.id.indicatorGroup_lib)
    protected LinearLayout dotLinearLayout;

    @ViewById(R.id.content_layout)
    protected RelativeLayout coordinatorLayout;

    @ViewById(R.id.content_viewpager)
    protected ViewPager contentViewPager;

    @ViewById(R.id.pullrefresh_scrollview)
    protected PullToRefreshScrollView pullToRefreshScrollView;

    private List<View> bannerList;
    private HomeProductPagerAdapter mPagerAdapter;
    private List<ProductNew> productNews;

    @AfterViews
    protected void initAnnotationView() {
        initBannerView(scrollViewPager, dotLinearLayout);
        ViewTreeObserver vto2 = pullToRefreshScrollView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pullToRefreshScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //此处需要动态传宽高给viewpager的imageview的宽高
                mPagerAdapter = new HomeProductPagerAdapter(getContext(), productNews, null, pullToRefreshScrollView.getWidth(), pullToRefreshScrollView.getHeight());
                contentViewPager.setAdapter(mPagerAdapter);
                getProduct(TOTAL_COUNT);
            }
        });
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getProduct(TOTAL_COUNT);
            }
        });
    }

    private void getProduct(int limit) {
        JianFanJiaClient.getTopProducts(getContext(), limit, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                List<ProductNew> productNews = JsonParser.jsonToList(data.toString(), new TypeToken<List<ProductNew>>() {
                }.getType());
                mPagerAdapter.setPaths(productNews);
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
                pullToRefreshScrollView.onRefreshComplete();
            }
        }, this);
    }

    @Click({R.id.ltm_home_layout0, R.id.ltm_home_layout1, R.id.ltm_home_layout2, R.id.ltm_home_layout3, R.id.home_search, R.id.list_item_more_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.ltm_home_layout0:
                Intent intent = new Intent(getContext(), PublishRequirementActivity_.class);
                startActivityForResult(intent, XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT);
                break;
            case R.id.ltm_home_layout1:
                startActivity(DecStrategyActivity_.class);
                break;
            case R.id.ltm_home_layout2:
                ((MainActivity) getActivity()).switchTab(Constant.DECORATE);
                break;
            case R.id.ltm_home_layout3:
                startActivity(DesignerListActivity.class);
                break;
            case R.id.list_item_more_layout:
                startActivity(DesignerCaseListActivity.class);
                break;
            case R.id.home_search:
                Intent searchIntent = new Intent(getContext(), SearchActivity_.class);
                startActivity(searchIntent);
                break;
            default:
                break;
        }
    }

    private void initBannerView(MyViewPager viewPager, LinearLayout indicatorGroup_lib) {
        indicatorGroup_lib.removeAllViews();
        List<View> bannerList = new ArrayList<View>();
        for (int i = 0; i < BANNER_ICON.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(BANNER_ICON[i]);
            bannerList.add(imageView);
        }
        final View[] indicators = new View[bannerList.size()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new ViewGroup.LayoutParams(20, 20));
        params.setMargins(0, 0, 15, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new View(getContext());
            if (i == 0) {
                indicators[i].setBackgroundResource(R.drawable.shape_indicator_selected_oval);
            } else {
                indicators[i].setBackgroundResource(R.drawable.shape_indicator_unselected_oval);
            }
            indicators[i].setLayoutParams(params);
            indicatorGroup_lib.addView(indicators[i]);
        }
        ViewPageAdapter mPagerAdapter = new ViewPageAdapter(getContext(), bannerList);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    }

}
