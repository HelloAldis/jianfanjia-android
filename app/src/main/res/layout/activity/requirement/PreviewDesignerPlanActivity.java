package layout.activity.requirement;

import android.content.DialogInterface;
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

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.common.ShowPicActivity;
import com.jianfanjia.cn.adapter.PreviewAdapter;
import com.jianfanjia.cn.bean.PlandetailInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:预览设计师方案
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PreviewDesignerPlanActivity extends SwipeBackActivity implements OnClickListener {
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
    private Button btn_choose = null;
    private PlandetailInfo planDetailInfo = null;
    private String designerid = null;
    private String planid = null;
    private String requirementid = null;
    private RequirementInfo requirementInfo = null;
    private String itemPosition;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle planBundle = intent.getExtras();
        planid = planBundle.getString(Global.PLAN_ID);
        itemPosition = planBundle.getString(Global.POSITION);
        requirementInfo = (RequirementInfo) planBundle.getSerializable(Global.REQUIREMENT_INFO);
        requirementid = requirementInfo.get_id();
        LogTool.d(TAG, "planid=" + planid + " itemPosition=" + itemPosition);
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
        btn_choose = (Button) findViewById(R.id.btn_choose);

        String requirementStatus = requirementInfo.getStatus();
        if (requirementStatus.equals(Global.REQUIREMENT_STATUS4) || requirementStatus.equals(Global.REQUIREMENT_STATUS5)
                || requirementStatus.equals(Global.REQUIREMENT_STATUS7) || requirementStatus.equals(Global.REQUIREMENT_STATUS8)) {
            btn_choose.setEnabled(false);
        } else {
            btn_choose.setEnabled(true);
        }
        getPlanInfo(planid);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_prieview_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
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
        PreviewAdapter adapter = new PreviewAdapter(PreviewDesignerPlanActivity.this, imgList, new ViewPagerClickListener() {
            @Override
            public void onClickItem(int pos) {
                LogTool.d(TAG, "pos:" + pos);
                Bundle showPicBundle = new Bundle();
                showPicBundle.putInt(Constant.CURRENT_POSITION, pos);
                showPicBundle.putStringArrayList(Constant.IMAGE_LIST,
                        (ArrayList<String>) imgList);
                startActivity(ShowPicActivity.class,showPicBundle);
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
        btn_choose.setOnClickListener(this);
    }

    @Override
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

    private void startToActivity(PlandetailInfo detailInfo) {
        Bundle priceBundle = new Bundle();
        priceBundle.putSerializable(Global.PLAN_DETAIL, detailInfo);
        startActivity(DetailPriceActivity.class, priceBundle);
    }

    //获取某个方案信息
    private void getPlanInfo(String planid) {
        JianFanJiaClient.getPlanInfo(PreviewDesignerPlanActivity.this, planid, getPlanInfoListener, this);
    }

    //选的方案
    private void chooseDesignerPlan(String requirementid, String designerid, String planid) {
        LogTool.d(TAG, "requirementid=" + requirementid + " designerid=" + designerid + " planid=" + planid);
        JianFanJiaClient.chooseDesignerPlan(PreviewDesignerPlanActivity.this, requirementid, designerid, planid, chooseDesignerPlanListener, this);
    }

    private ApiUiUpdateListener getPlanInfoListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.loading);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            hideWaitDialog();
            planDetailInfo = JsonParser.jsonToBean(data.toString(), PlandetailInfo.class);
            LogTool.d(TAG, "planDetailInfo:" + planDetailInfo);
            if (null != planDetailInfo) {
                totalDateLayout.setVisibility(View.VISIBLE);
                priceLayout.setVisibility(View.VISIBLE);
                designTextLayout.setVisibility(View.VISIBLE);
                mainHeadView.setRigthTitleEnable(true);
                RequirementInfo requirementInfo = planDetailInfo.getRequirement();
                designerid = planDetailInfo.getDesignerid();
                LogTool.d(TAG, "requirementid:" + requirementid + " designerid:" + designerid + " requirementInfo:" + requirementInfo);
                if (!TextUtils.isEmpty(requirementInfo.getCell())) {
                    cellName.setVisibility(View.VISIBLE);
                    cellName.setText(requirementInfo.getCell());
                }
                if (!TextUtils.isEmpty(requirementInfo.getHouse_type())) {
                    houseTypeLayout.setVisibility(View.VISIBLE);
                    houseType.setText(BusinessManager.convertHouseTypeToShow(requirementInfo.getHouse_type()));
                }
                if (!TextUtils.isEmpty(requirementInfo.getHouse_area())) {
                    houseAreaLayout.setVisibility(View.VISIBLE);
                    houseArea.setText(requirementInfo.getHouse_area() + getString(R.string.str_sq_unit));
                }
                if (!TextUtils.isEmpty(requirementInfo.getWork_type())) {
                    decorateTypeLayout.setVisibility(View.VISIBLE);
                    decorateType.setText(BusinessManager.getWorkType(requirementInfo.getWork_type()));
                }
                totalDate.setText(planDetailInfo.getDuration() + "天");
                price.setText(planDetailInfo.getTotal_price() + "元");
                designText.setText(planDetailInfo.getDescription());
                initViewPager(viewPager, indicatorGroup_lib, planDetailInfo.getImages());
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };


    private ApiUiUpdateListener chooseDesignerPlanListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.loading);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            hideWaitDialog();
            btn_choose.setEnabled(false);
            //发送数据刷新广播
            UiHelper.intentTo(PreviewDesignerPlanActivity.this, MyDesignerActivity_.class, null);
            appManager.finishActivity(PreviewDesignerPlanActivity.this);
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_designer_plan;
    }
}
