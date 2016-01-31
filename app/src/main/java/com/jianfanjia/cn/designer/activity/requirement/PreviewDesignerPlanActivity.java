package com.jianfanjia.cn.designer.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.common.ShowPicActivity;
import com.jianfanjia.cn.designer.adapter.PreviewAdapter;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.PlanInfo;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.cache.BusinessManager;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.interf.ViewPagerClickListener;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:预览设计师方案
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PreviewDesignerPlanActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = PreviewDesignerPlanActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private LinearLayout houseTypeLayout = null;
    private LinearLayout houseAreaLayout = null;
    private LinearLayout decorateTypeLayout = null;
    private LinearLayout totalDateLayout = null;
    private LinearLayout priceLayout = null;
    private LinearLayout designTextLayout = null;
    private ViewPager viewPager = null;
    private LinearLayout indicatorGroup_lib = null;
    private TextView cellName = null;
    private TextView houseType = null;
    private TextView houseArea = null;
    private TextView decorateType = null;
    private TextView totalDate = null;
    private TextView price = null;
    private TextView designText = null;

    private Button btnDetail = null;
    private PlanInfo plan = null;
    private RequirementInfo requirement = null;
    private int itemPosition = -1;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle planBundle = intent.getExtras();
        plan = (PlanInfo) planBundle.getSerializable(Global.PLAN);
        requirement = (RequirementInfo) planBundle.getSerializable(Global.REQUIRE);
        itemPosition = planBundle.getInt(Global.POSITION);
        LogTool.d(TAG, "plan=" + plan + " requirement=" + requirement + " itemPosition=" + itemPosition);
        initMainHeadView();
        houseTypeLayout = (LinearLayout) findViewById(R.id.houseTypeLayout);
        houseAreaLayout = (LinearLayout) findViewById(R.id.houseAreaLayout);
        decorateTypeLayout = (LinearLayout) findViewById(R.id.decorateTypeLayout);
        totalDateLayout = (LinearLayout) findViewById(R.id.totalDateLayout);
        priceLayout = (LinearLayout) findViewById(R.id.priceLayout);
        designTextLayout = (LinearLayout) findViewById(R.id.designTextLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        indicatorGroup_lib = (LinearLayout) findViewById(R.id.indicatorGroup_lib);
        cellName = (TextView) findViewById(R.id.cellName);
        houseType = (TextView) findViewById(R.id.houseType);
        houseArea = (TextView) findViewById(R.id.houseArea);
        decorateType = (TextView) findViewById(R.id.decorateType);
        totalDate = (TextView) findViewById(R.id.totalDate);
        price = (TextView) findViewById(R.id.price);
        designText = (TextView) findViewById(R.id.designText);
        btnDetail = (Button) findViewById(R.id.btnDetail);
        if (null != plan && null != requirement) {
            totalDateLayout.setVisibility(View.VISIBLE);
            priceLayout.setVisibility(View.VISIBLE);
            designTextLayout.setVisibility(View.VISIBLE);
            mainHeadView.setRigthTitleEnable(true);
            String dec_type = requirement.getDec_type();
            LogTool.d(TAG, "dec_type=" + dec_type + "    requirement.getHouse_type()=" + requirement.getHouse_type());
            if (!TextUtils.isEmpty(requirement.getCell())) {
                cellName.setVisibility(View.VISIBLE);
                cellName.setText(requirement.getCell());
            }
            if (!TextUtils.isEmpty(requirement.getHouse_type()) && dec_type.equals(Global.DEC_TYPE_HOME)) {
                houseTypeLayout.setVisibility(View.VISIBLE);
                houseType.setText(BusinessManager.convertHouseTypeToShow(requirement.getHouse_type()));
            }
            houseAreaLayout.setVisibility(View.VISIBLE);
            houseArea.setText(requirement.getHouse_area() + getString(R.string.str_sq_unit));
            if (!TextUtils.isEmpty(requirement.getWork_type())) {
                decorateTypeLayout.setVisibility(View.VISIBLE);
                decorateType.setText(BusinessManager.getWorkType(requirement.getWork_type()));
            }
            totalDate.setText(plan.getDuration() + "天");
            price.setText(plan.getTotal_price() + "元");
            designText.setText(plan.getDescription());
            initViewPager(viewPager, indicatorGroup_lib, plan.getImages());
        }
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_prieview_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView.setMianTitle(itemPosition != 0 ? getResources().getString(R.string.designerPlanText) + itemPosition
                : getResources().getString(R.string.designerPlan));
        mainHeadView.setRightTitle(getResources().getString(R.string.detailPrice));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
        mainHeadView.setRigthTitleEnable(false);
    }

    private void initViewPager(ViewPager viewPager, LinearLayout indicatorGroup_lib, final List<String> imgList) {
        final View[] indicators = new View[imgList.size()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new ViewGroup.LayoutParams(20, 20));
        params.setMargins(0, 0, 15, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new View(this);
            if (i == 0) {
                indicators[i].setBackgroundResource(R.drawable.shape_indicator_selected_oval);
            } else {
                indicators[i].setBackgroundResource(R.drawable.shape_indicator_unselected_oval);
            }
            indicators[i].setLayoutParams(params);
            indicatorGroup_lib.addView(indicators[i]);
        }
        PreviewAdapter adapter = new PreviewAdapter(PreviewDesignerPlanActivity.this, imgList, new ViewPagerClickListener() {
            @Override
            public void onClickItem(int pos) {
                LogTool.d(TAG, "pos:" + pos);
                Intent showPicIntent = new Intent(PreviewDesignerPlanActivity.this, ShowPicActivity.class);
                Bundle showPicBundle = new Bundle();
                showPicBundle.putInt(Constant.CURRENT_POSITION, pos);
                showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                        (ArrayList<String>) imgList);
                showPicIntent.putExtras(showPicBundle);
                startActivity(showPicIntent);
            }
        });
        viewPager.setAdapter(adapter);
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

    @Override
    public void setListener() {
        btnDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.head_right_title:
                startToActivity(plan);
                break;
            case R.id.btnDetail:
                startToActivity(plan);
                break;
            default:
                break;
        }
    }

    private void startToActivity(PlanInfo planInfo) {
        Bundle priceBundle = new Bundle();
        priceBundle.putSerializable(Global.PLAN_DETAIL, planInfo);
        startActivity(DetailPriceActivity.class, priceBundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_designer_plan;
    }
}
