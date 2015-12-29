package com.jianfanjia.cn.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.base.BaseActivity;
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

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Description:设计师信息
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerInfoActivity extends BaseActivity implements OnClickListener, AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = DesignerInfoActivity.class.getName();
    private Toolbar toolbar = null;
    private TextView toolbar_title = null;
    private CollapsingToolbarLayout collapsingToolbar = null;
    private AppBarLayout appBarLayout = null;
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
    private DesignerInfoFragment infoFragment = null;
    private DesignerWorksFragment workFragment = null;
    private String designerid = null;
    private String designer_name = null;

    @Override
    public void initView() {
        LogTool.d(TAG, "designerid=" + designerid);
        //---------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(R.mipmap.icon_register_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        designerinfo_head_img = (ImageView) findViewById(R.id.designerinfo_head_img);
        designerinfo_auth = (ImageView) findViewById(R.id.designerinfo_auth);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        designerName = (TextView) findViewById(R.id.designer_name);
        viewCountText = (TextView) findViewById(R.id.viewCountText);
        productCountText = (TextView) findViewById(R.id.productCountText);
        appointCountText = (TextView) findViewById(R.id.appointCountText);
        addBtn = (Button) findViewById(R.id.btn_add);
        deleteBtn = (Button) findViewById(R.id.btn_delete);
        initData(this.getIntent());
    }

    private void initData(Intent intent) {
        Bundle designerBundle = intent.getExtras();
        designerid = designerBundle.getString(Global.DESIGNER_ID);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        getDesignerPageInfo(designerid);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogTool.d(TAG, "onNewIntent");
        initData(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        List<SelectItem> listViews = new ArrayList<SelectItem>();
        SelectItem caigouItem = new SelectItem(DesignerInfoFragment.newInstance(designerid),
                "资料");
        SelectItem fukuanItem = new SelectItem(DesignerWorksFragment.newInstance(designerid),
                "作品");
        listViews.add(caigouItem);
        listViews.add(fukuanItem);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        addBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.finishActivity(DesignerInfoActivity.this);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            toolbar_title.setText("");
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            toolbar_title.setText(designer_name);
        } else {
            toolbar_title.setText("");
        }
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
                designerName.setText(designer_name);
                toolbar.setTitle("");
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
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_FAVORITE_FRAGMENT));
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
            EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_FAVORITE_FRAGMENT));
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_info;
    }
}