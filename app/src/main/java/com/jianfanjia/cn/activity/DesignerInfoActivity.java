package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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

/**
 * Description:设计师信息
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerInfoActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = DesignerInfoActivity.class.getName();
    private Toolbar toolbar = null;
    private CollapsingToolbarLayout collapsingToolbar = null;
    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;
    private RatingBar ratingBar = null;
    private ImageView designerinfo_head_img = null;
    private ImageView designerinfo_auth = null;
    private TextView viewCountText = null;
    private TextView productCountText = null;
    private TextView appointCountText = null;
    private Button addBtn = null;
    private DesignerInfoFragment infoFragment = null;
    private DesignerWorksFragment workFragment = null;
    private String designerid = null;


    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle designerBundle = intent.getExtras();
        designerid = designerBundle.getString(Global.DESIGNER_ID);
        LogTool.d(TAG, "designerid=" + designerid);
        //---------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon_register_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.listview_item_text_style_title);
        collapsingToolbar.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL);
        collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbar.setExpandedTitleColor(Color.BLACK);
        designerinfo_head_img = (ImageView) findViewById(R.id.designerinfo_head_img);
        designerinfo_auth = (ImageView) findViewById(R.id.designerinfo_auth);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        viewCountText = (TextView) findViewById(R.id.viewCountText);
        productCountText = (TextView) findViewById(R.id.productCountText);
        appointCountText = (TextView) findViewById(R.id.appointCountText);
        addBtn = (Button) findViewById(R.id.btn_add);

        getDesignerPageInfo(designerid);
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                addFavoriteDesignerToList(designerid);
                break;
            default:
                break;
        }
    }


    private void getDesignerPageInfo(String designerid) {
        JianFanJiaClient.getDesignerHomePage(DesignerInfoActivity.this, designerid, designerHomePage, this);
    }

    private void addFavoriteDesignerToList(String designerid) {
        JianFanJiaClient.addFavoriteDesigner(DesignerInfoActivity.this, designerid, addFavoriteDesigner, this);
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
                collapsingToolbar.setTitle(designerInfo.getUsername());
                String designerid = designerInfo.getImageid();
                if (!TextUtils.isEmpty(designerid)) {
                    imageShow.displayImageHeadWidthThumnailImage(DesignerInfoActivity.this, designerInfo.getImageid(), designerinfo_head_img);
                } else {
                    imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, designerinfo_head_img);
                }
                viewCountText.setText("" + designerInfo.getView_count());
                productCountText.setText("" + designerInfo.getProduct_count());
                appointCountText.setText("" + designerInfo.getOrder_count());
                float respond_speed = designerInfo.getRespond_speed();
                float service_attitude = designerInfo.getService_attitude();
                ratingBar.setRating((int) (respond_speed + service_attitude) / 2);
                if (designerInfo.is_my_favorite()) {
                    addBtn.setEnabled(false);
                    addBtn.setText("已添加意向");
                } else {
                    addBtn.setEnabled(true);
                    addBtn.setText("添加意向");
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
            addBtn.setText("已添加意向");
            addBtn.setEnabled(false);
        }

        @Override
        public void loadFailture(String error_msg) {
            addBtn.setEnabled(true);
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_info;
    }
}