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

import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.DesignerInfoFragment;
import com.jianfanjia.cn.fragment.DesignerWorksFragment;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ScrollableHelper;
import com.jianfanjia.cn.view.layout.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Description:设计师信息
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerInfoActivity extends SwipeBackActivity implements OnClickListener, ViewPager.OnPageChangeListener, ScrollableLayout.OnScrollListener {
    private static final String TAG = DesignerInfoActivity.class.getName();
    private ScrollableLayout sl_root = null;
    private RelativeLayout head_back_layout = null;
    private TextView tv_title = null;
    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;
    private RatingBar ratingBar = null;
    private ImageView designerinfo_head_img = null;
    private ImageView designerinfo_auth = null;
    private TextView designerName = null;
    private TextView viewCountText = null;
    private TextView productCountText = null;
    private TextView appointCountText = null;
    private Button addBtn = null;
    private Button deleteBtn = null;

    private String designerid = null;
    private String designer_name = null;

    private float titleMaxScrollHeight;
    private float hearderMaxHeight;
    private float avatarTop;
    private float maxScrollHeight;

    private List<SelectItem> listViews = new ArrayList<SelectItem>();

    @Override
    public void initView() {
        sl_root = (ScrollableLayout) findViewById(R.id.sl_root);
        head_back_layout = (RelativeLayout) findViewById(R.id.head_back_layout);
        tv_title = (TextView) findViewById(R.id.tv_title);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        designerinfo_head_img = (ImageView) findViewById(R.id.designerinfo_head_img);
        designerinfo_auth = (ImageView) findViewById(R.id.designerinfo_auth);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        designerName = (TextView) findViewById(R.id.designer_name);
        viewCountText = (TextView) findViewById(R.id.viewCountText);
        productCountText = (TextView) findViewById(R.id.productCountText);
        appointCountText = (TextView) findViewById(R.id.appointCountText);
        addBtn = (Button) findViewById(R.id.btn_add);
        deleteBtn = (Button) findViewById(R.id.btn_delete);
        tv_title.setTranslationY(-1000);
        initData(this.getIntent());
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initData(Intent intent) {
        Bundle designerBundle = intent.getExtras();
        designerid = designerBundle.getString(Global.DESIGNER_ID);
        getDesignerPageInfo(designerid);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.d(TAG, "onNewIntent");
        initData(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        SelectItem resItem = new SelectItem(DesignerInfoFragment.newInstance(designerid),
                getResources().getString(R.string.resourceText));
        SelectItem productItem = new SelectItem(DesignerWorksFragment.newInstance(designerid),
                getResources().getString(R.string.productText));
        listViews.add(resItem);
        listViews.add(productItem);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        sl_root.getHelper().setCurrentScrollableContainer((ScrollableHelper.ScrollableContainer) listViews.get(0).getFragment());
    }

    @Override
    public void setListener() {
        head_back_layout.setOnClickListener(this);
        sl_root.setOnScrollListener(this);
        addBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
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
        sl_root.getHelper().setCurrentScrollableContainer((ScrollableHelper.ScrollableContainer) listViews.get(position).getFragment());
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
            alpha = Math.min(255, (int) (Math.abs(avatarTop + translationY) * (255 - baseAlpha) / (hearderMaxHeight - avatarTop) + baseAlpha));
            tv_title.setVisibility(View.VISIBLE);
        } else {
            tv_title.setVisibility(View.GONE);
        }
        tv_title.setAlpha(alpha);
        tv_title.setTranslationY(Math.max(0, maxScrollHeight + translationY));
    }

    private void getDesignerPageInfo(String designerid) {
        JianFanJiaClient.getDesignerHomePage(DesignerInfoActivity.this, designerid, designerHomePage, this);
    }

    private void addFavoriteDesignerToList(String designerid) {
        JianFanJiaClient.addFavoriteDesigner(DesignerInfoActivity.this, designerid, addFavoriteDesigner, this);
    }

    private void deleteFavoriteDesigner(String designerid) {
        JianFanJiaClient.deleteFavoriteDesigner(DesignerInfoActivity.this, designerid, deleteMyFavoriteDesignerListener, this);
    }

    private ApiUiUpdateListener designerHomePage = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data);
            DesignerInfo designerInfo = JsonParser.jsonToBean(data.toString(), DesignerInfo.class);
            LogTool.d(TAG, "designerInfo:" + designerInfo);
            if (null != designerInfo) {
                designer_name = designerInfo.getUsername();
                tv_title.setText(designer_name);
                designerName.setText(designer_name);
                String designerid = designerInfo.getImageid();
                if (!TextUtils.isEmpty(designerid)) {
                    imageShow.displayImageHeadWidthThumnailImage(DesignerInfoActivity.this, designerInfo.getImageid(), designerinfo_head_img);
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
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    private ApiUiUpdateListener addFavoriteDesigner = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            addBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.VISIBLE);
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_ORDER_DESIGNER_ACTIVITY));
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    private ApiUiUpdateListener deleteMyFavoriteDesignerListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data.toString());
            addBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.GONE);
            EventBus.getDefault().post(new MessageEvent(Constant.DELETE_FAVORITE_DESIGNER_FRAGMENT));
            EventBus.getDefault().post(new MessageEvent(Constant.DELETE_ORDER_DESIGNER_ACTIVITY));
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
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