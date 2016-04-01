package com.jianfanjia.cn.activity.requirement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.request.user.ChooseDesignerPlanRequest;
import com.jianfanjia.cn.Event.ChoosedPlanEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.common.ShowPicActivity;
import com.jianfanjia.cn.adapter.PreviewAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.common.tool.LogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Description:预览设计师方案
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PreviewDesignerPlanActivity extends BaseSwipeBackActivity {

    public static final String PLAN_INTENT_FLAG = "plan_intent_flag";//标识从哪个屏传过来的

    public static final int NOTICE_INTENT = 0;//通知进入的；
    public static final int PLAN_LIST_INTENT = 1;//方案列表进入的
    public static final int COMMENT_INTENT = 2;//评论进入的

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
    @Bind(R.id.btn_choose)
    protected Button btn_choose;

    protected Plan planDetailInfo = null;
    private String designerid = null;
    private String planid = null;
    private String requirementid;
    private Requirement requirementInfo = null;
    private String itemPosition;
    private int flagIntent = -1;

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
        planDetailInfo = (Plan) planBundle.getSerializable(Global.PLAN_DETAIL);
        planid = planDetailInfo.get_id();
        itemPosition = planDetailInfo.getName();
        requirementInfo = (Requirement) planBundle.getSerializable(Global.REQUIREMENT_INFO);
        requirementid = requirementInfo.get_id();
        flagIntent = planBundle.getInt(PLAN_INTENT_FLAG);
        LogTool.d(TAG, "planid=" + planid + " itemPosition=" + itemPosition);
    }

    private void initData() {
        if (null != planDetailInfo) {
            totalDateLayout.setVisibility(View.VISIBLE);
            priceLayout.setVisibility(View.VISIBLE);
            designTextLayout.setVisibility(View.VISIBLE);
            mainHeadView.setRigthTitleEnable(true);
            designerid = planDetailInfo.getDesignerid();
            LogTool.d(TAG, "requirementid:" + requirementid + " designerid:" + designerid + " requirementInfo:" +
                    requirementInfo);
            if (!TextUtils.isEmpty(requirementInfo.getBasic_address())) {
                cellName.setVisibility(View.VISIBLE);
                cellName.setText(requirementInfo.getBasic_address());
            }
            if (!TextUtils.isEmpty(requirementInfo.getHouse_type())) {
                houseTypeLayout.setVisibility(View.VISIBLE);
                houseType.setText(BusinessCovertUtil.convertHouseTypeToShow(requirementInfo.getHouse_type()));
            }
            if (!TextUtils.isEmpty(requirementInfo.getHouse_area())) {
                houseAreaLayout.setVisibility(View.VISIBLE);
                houseArea.setText(requirementInfo.getHouse_area() + getString(R.string.str_sq_unit));
            }
            if (!TextUtils.isEmpty(requirementInfo.getWork_type())) {
                decorateTypeLayout.setVisibility(View.VISIBLE);
                decorateType.setText(BusinessCovertUtil.getWorkType(requirementInfo.getWork_type()));
            }
            totalDate.setText(planDetailInfo.getDuration() + "天");
            price.setText(planDetailInfo.getTotal_price() + "元");
            designText.setText(planDetailInfo.getDescription());
            initChooseButton();
            initViewPager(viewPager, indicatorGroup_lib, planDetailInfo.getImages());
        }
    }

    private void initChooseButton() {
        String planStatus = planDetailInfo.getStatus();
        switch (planStatus) {
            case Global.PLAN_STATUS3:
                btn_choose.setEnabled(true);
                btn_choose.setText(getString(R.string.str_choose_plan));
                break;
            case Global.PLAN_STATUS4:
                btn_choose.setEnabled(false);
                btn_choose.setText(getString(R.string.str_not_choose_plan));
                break;
            case Global.PLAN_STATUS5:
                btn_choose.setEnabled(false);
                btn_choose.setText(getString(R.string.str_has_choosed_plan));
                break;
            default:
                btn_choose.setEnabled(false);
                btn_choose.setText(getString(R.string.str_not_choose_plan));
                break;
        }
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(itemPosition);
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
                        Bundle showPicBundle = new Bundle();
                        showPicBundle.putInt(Constant.CURRENT_POSITION, pos);
                        showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                                (ArrayList<String>) imgList);
                        startActivity(ShowPicActivity.class, showPicBundle);
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

    @OnClick({R.id.head_back_layout, R.id.head_right_title, R.id.btnDetail, R.id.btn_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.head_right_title:
                startToActivity(planDetailInfo);
                break;
            case R.id.btnDetail:
                startToActivity(planDetailInfo);
                break;
            case R.id.btn_choose:
                choosePlanDialog();
                break;
            default:
                break;
        }
    }

    private void choosePlanDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(PreviewDesignerPlanActivity.this);
        dialog.setTitle(getResources().getString(R.string.hint_choose_plan_text));
        dialog.setMessage(getResources().getString(R.string.hint_choose_plan_str));
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        chooseDesignerPlan(requirementid, designerid, planid);
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    private void startToActivity(Plan detailInfo) {
        Bundle priceBundle = new Bundle();
        priceBundle.putSerializable(Global.PLAN_DETAIL, detailInfo);
        startActivity(DetailPriceActivity.class, priceBundle);
    }

    //选定方案
    private void chooseDesignerPlan(String requirementid, String designerid, String planid) {
        LogTool.d(TAG, "requirementid=" + requirementid + " designerid=" + designerid + " planid=" + planid);
        ChooseDesignerPlanRequest chooseDesignerPlanRequest = new ChooseDesignerPlanRequest();
        chooseDesignerPlanRequest.setRequirementid(requirementid);
        chooseDesignerPlanRequest.setDesignerid(designerid);
        chooseDesignerPlanRequest.setPlanid(planid);
        Api.chooseDesignerPlan(chooseDesignerPlanRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                afterChooseSuccess();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void afterChooseSuccess() {
        btn_choose.setEnabled(false);
        btn_choose.setText(getString(R.string.str_has_choosed_plan));
        switch (flagIntent) {
            case PLAN_LIST_INTENT:
                startActivity(MyDesignerActivity.class);
                appManager.finishActivity(PreviewDesignerPlanActivity.this);
                break;
            case COMMENT_INTENT:
            case NOTICE_INTENT:
                EventBus.getDefault().post(new ChoosedPlanEvent());
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_designer_plan;
    }
}
