package com.jianfanjia.cn.activity.requirement;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.EditBussinessRequirementFragment_;
import com.jianfanjia.cn.fragment.EditHomeRequirementFragment_;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.NotifyActivityStatusChange;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:发布需求
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PublishRequirementActivity extends BaseActivity implements OnClickListener, NotifyActivityStatusChange {
    private static final String TAG = PublishRequirementActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;

    private EditHomeRequirementFragment_ editHomeRequirementFragment_;
    private EditBussinessRequirementFragment_ editBussinessRequirementFragment_;

    protected String status;//当前页面的状态，家装还是商装

    private OwnerInfo ownerInfo;
    private RequirementInfo requirementInfoInit = new RequirementInfo();

    @Override
    public void initView() {
        initMainHeadView();
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        initData();
    }

    protected void initData() {
        JianFanJiaClient.get_Owner_Info(this, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                ownerInfo = JsonParser.jsonToBean(data.toString(), OwnerInfo.class);
                if (ownerInfo != null) {
                    String family_des = ownerInfo.getFamily_description();
                    if (family_des != null) {
                        requirementInfoInit.setFamily_description(family_des);
                    }
                    List<String> lovestyle = ownerInfo.getDec_styles();
                    if (lovestyle != null && lovestyle.size() > 0) {
                        requirementInfoInit.setDec_style(lovestyle.get(0));
                    }
                }
                initViewPager();
            }

            @Override
            public void loadFailture(String error_msg) {
                initViewPager();
            }
        }, this);
    }

    private void initViewPager(){
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                status = tab.getPosition() + "";
                resetRightTitleStatus();
            }
        });
        status = Global.DEC_TYPE_HOME;
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.act_edit_req_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTitle(getString(R.string.confirm));
        mainHeadView.setRigthTitleEnable(false);
        mainHeadView.setRightTextListener(this);
    }

    private void resetRightTitleStatus() {
        switch (status) {
            case Global.DEC_TYPE_HOME:
                mainHeadView.setRigthTitleEnable(editHomeRequirementFragment_.isFinish());
                break;
            case Global.DEC_TYPE_BUSINESS:
                mainHeadView.setRigthTitleEnable(editBussinessRequirementFragment_.isFinish());
                break;
        }
    }

    @Override
    public void notifyStatusChange() {
        //重置完成按钮的状态
        resetRightTitleStatus();
    }

    protected void confirm() {
        RequirementInfo requirementInfo = getConfirmRequirement();
        JianFanJiaClient.add_Requirement(this, requirementInfo, this, this);
    }

    protected RequirementInfo getConfirmRequirement() {
        RequirementInfo requirementInfo = null;
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

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        setResult(Activity.RESULT_OK);
        appManager.finishActivity(this);
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
                setResult(RESULT_CANCELED);
                appManager.finishActivity(PublishRequirementActivity.this);
            }
        });
        commonDialog.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        List<SelectItem> listViews = new ArrayList<>();
        editBussinessRequirementFragment_ = new EditBussinessRequirementFragment_();
        editBussinessRequirementFragment_.setArguments(getBundleByType(Global.DEC_TYPE_BUSINESS));
        editHomeRequirementFragment_ = new EditHomeRequirementFragment_();
        editHomeRequirementFragment_.setArguments(getBundleByType(Global.DEC_TYPE_HOME));
        SelectItem designerItem = new SelectItem(editHomeRequirementFragment_,
                getString(R.string.home_dec));
        SelectItem productItem = new SelectItem(editBussinessRequirementFragment_,
                getString(R.string.business_dec));
        listViews.add(designerItem);
        listViews.add(productItem);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
    }

    protected Bundle getBundleByType(String type) {
        Bundle bundle = new Bundle();
        RequirementInfo requirementInfo = new RequirementInfo();
        requirementInfo.setDec_type(type);
        String family_des = requirementInfoInit.getFamily_description();
        if (family_des != null) {
            requirementInfo.setFamily_description(family_des);
        }
        String lovestyle = requirementInfoInit.getDec_style();
        if(lovestyle != null){
            requirementInfo.setDec_style(lovestyle);
        }
        bundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
        bundle.putInt(Global.REQUIREMENG_ACTION_TYPE, XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT);
        return bundle;
    }

    @Override
    public void setListener() {

    }

    protected void back(){
        boolean isChange = false;
        requirementInfoInit.setDec_type(Global.DEC_TYPE_BUSINESS);
        if(BusinessManager.isRequirementChange(
                editBussinessRequirementFragment_.getRequirementInfo(),requirementInfoInit)){
            isChange = true;
        }
        requirementInfoInit.setDec_type(Global.DEC_TYPE_HOME);
        if(BusinessManager.isRequirementChange(
                editHomeRequirementFragment_.getRequirementInfo(),requirementInfoInit)){
            isChange = true;
        }
        if(!isChange){
            LogTool.d(this.getClass().getName(), "没有改变");
            appManager.finishActivity(this);
        }else{
            LogTool.d(this.getClass().getName(), "有改变");
            showTipDialog();
        }
    }

    @Override
    public void onClick(View v) {
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
