package com.jianfanjia.cn.supervisor.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.activity.common.ShowPicActivity;
import com.jianfanjia.cn.supervisor.adapter.PreviewAdapter;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.cn.supervisor.config.Global;
import com.jianfanjia.cn.supervisor.interf.ViewPagerClickListener;
import com.jianfanjia.cn.supervisor.tools.BusinessCovertUtil;
import com.jianfanjia.cn.supervisor.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description:预览设计师方案
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PreviewDesignerPlanActivity extends BaseSwipeBackActivity {
    private static final String TAG = PreviewDesignerPlanActivity.class.getName();

    @Bind(R.id.my_prieview_head_layout)
    protected MainHeadView mainHeadView = null;
    @Bind(R.id.houseTypeLayout)
    protected LinearLayout houseTypeLayout;
    @Bind(R.id.houseAreaLayout)
    protected LinearLayout houseAreaLayout;
    @Bind(R.id.decorateTypeLayout)
    protected LinearLayout decorateTypeLayout;
    @Bind(R.id.totalDateLayout)
    protected LinearLayout totalDateLayout;
    @Bind(R.id.priceLayout)
    protected LinearLayout priceLayout;
    @Bind(R.id.designTextLayout)
    protected LinearLayout designTextLayout;
    @Bind(R.id.viewpager)
    protected ViewPager viewPager;
    @Bind(R.id.indicatorGroup_lib)
    protected LinearLayout indicatorGroup_lib;
    @Bind(R.id.cellName)
    protected TextView cellName;
    @Bind(R.id.houseType)
    protected TextView houseType;
    @Bind(R.id.houseArea)
    protected TextView houseArea;
    @Bind(R.id.decorateType)
    protected TextView decorateType;
    @Bind(R.id.totalDate)
    protected TextView totalDate;
    @Bind(R.id.price)
    protected TextView price;
    @Bind(R.id.designText)
    protected TextView designText;
    @Bind(R.id.btnDetail)
    protected Button btnDetail;

    private Plan plan = null;
    private Requirement requirement = null;
    private String itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
        initData();
    }

    public void initView() {
        initMainHeadView();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle planBundle = intent.getExtras();
        plan = (Plan) planBundle.getSerializable(Global.PLAN_DETAIL);
        requirement = (Requirement) planBundle.getSerializable(Global.REQUIREMENT_INFO);
        itemPosition = plan.getName() == null ? "null" : plan.getName();
        LogTool.d(TAG, "plan=" + plan + " requirement=" + requirement + " itemPosition=" + itemPosition);
    }

    public void initData() {
        if (null != plan && null != requirement) {
            totalDateLayout.setVisibility(View.VISIBLE);
            priceLayout.setVisibility(View.VISIBLE);
            designTextLayout.setVisibility(View.VISIBLE);
            mainHeadView.setRigthTitleEnable(true);
            String dec_type = requirement.getDec_type();
            LogTool.d(TAG, "dec_type=" + dec_type + "    requirement.getHouse_type()=" + requirement.getHouse_type());
            if (!TextUtils.isEmpty(requirement.getBasic_address())) {
                cellName.setVisibility(View.VISIBLE);
                cellName.setText(requirement.getBasic_address());
            }
            if (!TextUtils.isEmpty(requirement.getHouse_type()) && dec_type.equals(Global.DEC_TYPE_HOME)) {
                houseTypeLayout.setVisibility(View.VISIBLE);
                houseType.setText(BusinessCovertUtil.convertHouseTypeToShow(requirement.getHouse_type()));
            }
            houseAreaLayout.setVisibility(View.VISIBLE);
            houseArea.setText(requirement.getHouse_area() + getString(R.string.str_sq_unit));
            if (!TextUtils.isEmpty(requirement.getWork_type())) {
                decorateTypeLayout.setVisibility(View.VISIBLE);
                decorateType.setText(BusinessCovertUtil.getWorkType(requirement.getWork_type()));
            }
            totalDate.setText(plan.getDuration() + "天");
            price.setText(plan.getTotal_price() + "元");
            designText.setText(plan.getDescription());
            initViewPager(viewPager, indicatorGroup_lib, plan.getImages());
        }
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(itemPosition == null ? getResources().getString(R.string.designerPlan) :
                itemPosition);
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
        PreviewAdapter adapter = new PreviewAdapter(PreviewDesignerPlanActivity.this, imgList, new
                ViewPagerClickListener() {
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

    @OnClick({R.id.head_back_layout, R.id.head_right_title, R.id.btnDetail})
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

    private void startToActivity(Plan plan) {
        Bundle priceBundle = new Bundle();
        priceBundle.putSerializable(Global.PLAN_DETAIL, plan);
        priceBundle.putSerializable(Global.REQUIREMENT_INFO,requirement);
        startActivity(DetailPriceActivity.class, priceBundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_designer_plan;
    }
}
