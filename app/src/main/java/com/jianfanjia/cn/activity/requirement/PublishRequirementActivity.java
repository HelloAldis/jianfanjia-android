package com.jianfanjia.cn.activity.requirement;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.request.user.PublishRequirementRequest;
import com.jianfanjia.api.request.user.UserByOwnerInfoRequest;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.EditBussinessRequirementFragment;
import com.jianfanjia.cn.fragment.EditHomeRequirementFragment;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.interf.NotifyActivityStatusChange;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

/**
 * Description:发布需求
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PublishRequirementActivity extends SwipeBackActivity implements NotifyActivityStatusChange {

    private static final String TAG = PublishRequirementActivity.class.getName();
    @Bind(R.id.act_edit_req_head_layout)
    protected MainHeadView mainHeadView = null;
    @Bind(R.id.tablayout)
    protected TabLayout tabLayout = null;
    @Bind(R.id.viewpager)
    protected ViewPager viewPager = null;

    private EditHomeRequirementFragment editHomeRequirementFragment_;
    private EditBussinessRequirementFragment editBussinessRequirementFragment_;

    private MyFragmentPagerAdapter adapter;

    private User ownerInfo;

    protected String status = Global.DEC_TYPE_HOME;//当前页面的状态，家装还是商装

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    public void initView() {
        initMainHeadView();
    }

    private void initMainHeadView() {
        mainHeadView.setRightTitle(getString(R.string.finish));
        mainHeadView.setRigthTitleEnable(false);
    }

    protected void initData() {
        Api.getUserInfo(new UserByOwnerInfoRequest(), new ApiCallback<ApiResponse<User>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<User> apiResponse) {
                ownerInfo = apiResponse.getData();
                initViewPager();
            }

            @Override
            public void onFailed(ApiResponse<User> apiResponse) {
                initViewPager();
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void initViewPager() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                status = tab.getPosition() + "";
                resetRightTitleStatus();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        LogTool.d(this.getClass().getName(), "setupViewPager");
        List<SelectItem> listViews = new ArrayList<>();
        editBussinessRequirementFragment_ = new EditBussinessRequirementFragment();
        editBussinessRequirementFragment_.setArguments(getBundleByType(Global.DEC_TYPE_BUSINESS));
        editHomeRequirementFragment_ = new EditHomeRequirementFragment();
        editHomeRequirementFragment_.setArguments(getBundleByType(Global.DEC_TYPE_HOME));
        SelectItem designerItem = new SelectItem(editHomeRequirementFragment_, getString(R.string.home_dec));
        SelectItem productItem = new SelectItem(editBussinessRequirementFragment_, getString(R.string.business_dec));
        listViews.add(designerItem);
        listViews.add(productItem);
        adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    private void resetRightTitleStatus() {
        switch (status) {
            case Global.DEC_TYPE_HOME:
                if (editHomeRequirementFragment_ != null) {
                    LogTool.d(this.getClass().getName(), "editHomeRequirementFragment_ is not null");
                    mainHeadView.setRigthTitleEnable(editHomeRequirementFragment_.isFinish());
                } else {
                    LogTool.d(this.getClass().getName(), "editHomeRequirementFragment_ is null");
                }
                break;
            case Global.DEC_TYPE_BUSINESS:
                if (editBussinessRequirementFragment_ != null) {
                    mainHeadView.setRigthTitleEnable(editBussinessRequirementFragment_.isFinish());
                }
                break;
        }
    }

    @Override
    public void notifyStatusChange() {
        //重置完成按钮的状态
        resetRightTitleStatus();
    }

    protected void confirm() {
//        JianFanJiaClient.add_Requirement(this, requirementInfo, this, this);
//
        Requirement requirementInfo = getConfirmRequirement();
        PublishRequirementRequest publishRequirementRequest = new PublishRequirementRequest();
        publishRequirementRequest.setRequirement(requirementInfo);

        Api.publishRequirement(publishRequirementRequest, new ApiCallback<ApiResponse<String>>() {
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
                startActivity(MainActivity.class);
                appManager.finishActivity(PublishRequirementActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    protected Requirement getConfirmRequirement() {
        Requirement requirementInfo = null;
        switch (status) {
            case Global.DEC_TYPE_HOME:
                requirementInfo = editHomeRequirementFragment_.getRequirementInfo();
                break;
            case Global.DEC_TYPE_BUSINESS:
                requirementInfo = editBussinessRequirementFragment_.getRequirementInfo();
                break;
        }
        return requirementInfo;
    }

    //显示放弃提交提醒
    protected void showTipDialog() {
        CommonDialog commonDialog = DialogHelper.getPinterestDialogCancelable(this);
        commonDialog.setTitle(R.string.tip_confirm);
        commonDialog.setMessage(getString(R.string.abandon_confirm_req));
        commonDialog.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        commonDialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                setResult(RESULT_CANCELED);
                appManager.finishActivity(PublishRequirementActivity.this);
            }
        });
        commonDialog.show();
    }

    protected Bundle getBundleByType(String type) {
        Bundle bundle = new Bundle();
        Requirement requirementInfo = getSourceRequirement(type);
        bundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
        LogTool.d(TAG, "requirmentInfo =" + JsonParser.beanToJson(requirementInfo));
        bundle.putInt(Global.REQUIREMENG_ACTION_TYPE, XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT);
        return bundle;
    }

    protected void back() {
        if (isHomeTypeChange() || isBusinessTypeChange()) {
            LogTool.d(this.getClass().getName(), "有改变");
            showTipDialog();
        } else {
            LogTool.d(this.getClass().getName(), "没有改变");
            appManager.finishActivity(this);
        }
    }

    private Requirement getSourceRequirement(String type) {
        Requirement requirement = new Requirement();
        switch (type) {
            case Global.DEC_TYPE_HOME:
                RequirementBusiness.initHomeRequirement(requirement);
                break;
            case Global.DEC_TYPE_BUSINESS:
                RequirementBusiness.initHomeRequirement(requirement);
                break;
        }
        if (ownerInfo != null) {
            String family_des = ownerInfo.getFamily_description();
            if (family_des != null) {
                requirement.setFamily_description(family_des);
            }
            if (ownerInfo.getDec_styles().size() > 0) {
                String lovestyle = ownerInfo.getDec_styles().get(0);
                if (lovestyle != null) {
                    requirement.setDec_style(lovestyle);
                }
            }
        }
        return requirement;
    }

    private boolean isHomeTypeChange() {
        Requirement requirementInit = getSourceRequirement(Global.DEC_TYPE_HOME);
        if (RequirementBusiness.isRequirementChange(
                editHomeRequirementFragment_.getRequirementInfo(), requirementInit)) {
            return true;
        }
        return false;
    }

    private boolean isBusinessTypeChange() {
        Requirement requirementInit = getSourceRequirement(Global.DEC_TYPE_BUSINESS);
        if (RequirementBusiness.isRequirementChange(
                editBussinessRequirementFragment_.getRequirementInfo(), requirementInit)) {
            return true;
        }
        return false;
    }

    @OnClick({
            R.id.head_back_layout, R.id.head_right_title
    })
    public void click(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                back();
                break;
            case R.id.head_right_title:
                confirm();
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_requirement;
    }
}
