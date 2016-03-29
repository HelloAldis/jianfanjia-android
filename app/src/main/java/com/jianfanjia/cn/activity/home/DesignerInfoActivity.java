package com.jianfanjia.cn.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.user.AddFavoriteDesignerRequest;
import com.jianfanjia.api.request.user.DeleteFavoriteDesignerRequest;
import com.jianfanjia.api.request.guest.DesignerHomePageRequest;
import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.DesignerInfoFragment;
import com.jianfanjia.cn.fragment.DesignerProductFragment;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ScrollableHelper;
import com.jianfanjia.cn.view.layout.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Description:设计师信息
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerInfoActivity extends SwipeBackActivity implements OnClickListener, ViewPager
        .OnPageChangeListener, ScrollableLayout.OnScrollListener {
    private static final String TAG = DesignerInfoActivity.class.getName();

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

    @Bind(R.id.btn_add)
    protected Button addBtn = null;

    @Bind(R.id.btn_delete)
    protected Button deleteBtn = null;

    private String designerid = null;
    private String designer_name = null;

    private float titleMaxScrollHeight;
    private float hearderMaxHeight;
    private float avatarTop;
    private float maxScrollHeight;

    private List<SelectItem> listViews = new ArrayList<SelectItem>();


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
        designerid = designerBundle.getString(Global.DESIGNER_ID);
        getDesignerPageInfo(designerid);
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
        sl_root.setOnScrollListener(this);
    }

    @OnClick({R.id.head_back_layout, R.id.btn_add, R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_add:
                addFavoriteDesignerToList(designerid);
                break;
            case R.id.btn_delete:
                deleteFavoriteDesigner(designerid);
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
            alpha = Math.min(255, (int) (Math.abs(avatarTop + translationY) * (255 - baseAlpha) / (hearderMaxHeight -
                    avatarTop) + baseAlpha));
            tv_title.setVisibility(View.VISIBLE);
        } else {
            tv_title.setVisibility(View.GONE);
        }
        tv_title.setAlpha(alpha);
        tv_title.setTranslationY(Math.max(0, maxScrollHeight + translationY));
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

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Designer> apiResponse) {
            Designer designerInfo = apiResponse.getData();
            LogTool.d(TAG, "designerInfo:" + designerInfo);
            if (null != designerInfo) {
                designer_name = designerInfo.getUsername();
                tv_title.setText(designer_name);
                designerName.setText(designer_name);
                String designerid = designerInfo.getImageid();
                if (!TextUtils.isEmpty(designerid)) {
                    imageShow.displayImageHeadWidthThumnailImage(DesignerInfoActivity.this, designerInfo.getImageid()
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
                if (designerInfo.is_my_favorite()) {
                    addBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.VISIBLE);
                } else {
                    addBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailed(ApiResponse<Designer> apiResponse) {
            makeTextLong(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {

        }
    };

    private ApiCallback<ApiResponse<Object>> addFavoriteDesignerCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            addBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.VISIBLE);
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_ORDER_DESIGNER_ACTIVITY));
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
            makeTextLong(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {

        }
    };


    private ApiCallback<ApiResponse<Object>> deleteMyFavoriteDesignerCallback = new ApiCallback<ApiResponse<Object>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<Object> apiResponse) {
            addBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.GONE);
            EventBus.getDefault().post(new MessageEvent(Constant.DELETE_FAVORITE_DESIGNER_FRAGMENT));
            EventBus.getDefault().post(new MessageEvent(Constant.DELETE_ORDER_DESIGNER_ACTIVITY));
        }

        @Override
        public void onFailed(ApiResponse<Object> apiResponse) {
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
        return R.layout.activity_designer_info;
    }
}