package com.jianfanjia.cn.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.EditBussinessRequirementFragment_;
import com.jianfanjia.cn.fragment.EditHomeRequirementFragment_;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.NotifyActivityStatusChange;
import com.jianfanjia.cn.tools.JsonParser;
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
public class PublishRequirementActivity extends BaseActivity implements OnClickListener,NotifyActivityStatusChange {
    private static final String TAG = PublishRequirementActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;

    private EditHomeRequirementFragment_ editHomeRequirementFragment_;
    private EditBussinessRequirementFragment_ editBussinessRequirementFragment_;

    protected String status;//当前页面的状态，家装还是商装

    private OwnerInfo ownerInfo;
    private RequirementInfo requirementInfoInit;

    @Override
    public void initView() {
        initMainHeadView();
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                status = tab.getPosition() + "";
                resetRightTitleStatus();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        status = Global.DEC_TYPE_HOME;
//        initData();
    }

    protected void initData(){
        JianFanJiaClient.get_Owner_Info(this, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                ownerInfo = JsonParser.jsonToBean(data.toString(), OwnerInfo.class);
                setupViewPager(viewPager);
            }

            @Override
            public void loadFailture(String error_msg) {

            }
        },this);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.act_edit_req_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitle(getString(R.string.finish));
        mainHeadView.setRigthTitleEnable(false);
        mainHeadView.setRightTextListener(this);
    }

    private void resetRightTitleStatus(){
        switch (status){
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
        RequirementInfo requirementInfo = null;
        switch (status){
            case Global.DEC_TYPE_HOME:
                requirementInfo = editHomeRequirementFragment_.getRequirementInfo();
                break;
            case Global.DEC_TYPE_BUSINESS:
                requirementInfo = editBussinessRequirementFragment_.getRequirementInfo();
                break;
        }
        JianFanJiaClient.add_Requirement(this, requirementInfo, this, this);
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        setResult(Activity.RESULT_OK);
        finish();
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
                finish();
            }
        });
        commonDialog.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        List<SelectItem> listViews = new ArrayList<SelectItem>();
        editBussinessRequirementFragment_ = new EditBussinessRequirementFragment_();
        editBussinessRequirementFragment_.setArguments(getBundleByType(Global.DEC_TYPE_BUSINESS));
        editHomeRequirementFragment_ = new EditHomeRequirementFragment_();
        editHomeRequirementFragment_.setArguments(getBundleByType(Global.DEC_TYPE_HOME));
        SelectItem designerItem = new SelectItem(editHomeRequirementFragment_,
                "家装");
        SelectItem productItem = new SelectItem(editBussinessRequirementFragment_,
                "商装");
        listViews.add(designerItem);
        listViews.add(productItem);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
    }

    protected Bundle getBundleByType(String type){
        Bundle bundle = new Bundle();
        RequirementInfo requirementInfo = new RequirementInfo();
        requirementInfo.setDec_type(type);
        bundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
        bundle.putInt(Global.REQUIREMENG_ACTION_TYPE, XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT);
        return bundle;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                finish();
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
