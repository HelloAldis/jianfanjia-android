package com.jianfanjia.cn.designer.activity.requirement;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jianfanjia.cn.designer.Event.MessageEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.designer.base.BaseAnnotationActivity;
import com.jianfanjia.cn.designer.bean.OwnerInfo;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.bean.SelectItem;
import com.jianfanjia.cn.designer.cache.BusinessManager;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.fragment.EditBussinessRequirementFragment_;
import com.jianfanjia.cn.designer.fragment.EditHomeRequirementFragment_;
import com.jianfanjia.cn.designer.fragment.XuQiuFragment;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.NotifyActivityStatusChange;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Description:发布需求
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
@EActivity(R.layout.activity_edit_requirement)
public class PublishRequirementActivity extends BaseAnnotationActivity implements NotifyActivityStatusChange {
    private static final String TAG = PublishRequirementActivity.class.getName();
    @ViewById(R.id.act_edit_req_head_layout)
    protected MainHeadView mainHeadView = null;
    @ViewById(R.id.tablayout)
    protected TabLayout tabLayout = null;
    @ViewById(R.id.viewpager)
    protected ViewPager viewPager = null;

    private EditHomeRequirementFragment_ editHomeRequirementFragment_;
    private EditBussinessRequirementFragment_ editBussinessRequirementFragment_;

    private MyFragmentPagerAdapter adapter;

    protected String status = Global.DEC_TYPE_HOME;//当前页面的状态，家装还是商装

    private OwnerInfo ownerInfo;
    private RequirementInfo requirementInfoInit = new RequirementInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void initAnnotationView() {
        initMainHeadView();
        initData();
    }

    private void initMainHeadView() {
        mainHeadView.setRightTitle(getString(R.string.finish));
        mainHeadView.setRigthTitleEnable(false);
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
        LogTool.d(this.getClass().getName(),"setupViewPager");
        List<SelectItem> listViews = new ArrayList<>();
        editBussinessRequirementFragment_ = new EditBussinessRequirementFragment_();
        editBussinessRequirementFragment_.setArguments(getBundleByType(Global.DEC_TYPE_BUSINESS));
        editHomeRequirementFragment_ = new EditHomeRequirementFragment_();
        editHomeRequirementFragment_.setArguments(getBundleByType(Global.DEC_TYPE_HOME));
        SelectItem designerItem = new SelectItem(editHomeRequirementFragment_, getString(R.string.home_dec));
        SelectItem productItem = new SelectItem(editBussinessRequirementFragment_, getString(R.string.business_dec));
        listViews.add(designerItem);
        listViews.add(productItem);
        adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
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
        EventBus.getDefault().post(new MessageEvent(Constant.UPDATE_HOME_FRAGMENT));
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


    protected Bundle getBundleByType(String type) {
        Bundle bundle = new Bundle();
        RequirementInfo requirementInfo = new RequirementInfo();
        requirementInfo.setDec_type(type);
        String family_des = requirementInfoInit.getFamily_description();
        if (family_des != null) {
            requirementInfo.setFamily_description(family_des);
        }
        String lovestyle = requirementInfoInit.getDec_style();
        if (lovestyle != null) {
            requirementInfo.setDec_style(lovestyle);
        }
        bundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
        bundle.putInt(Global.REQUIREMENG_ACTION_TYPE, XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT);
        return bundle;
    }

    @Override
    public void setListener() {

    }

    protected void back() {
        boolean isChange = false;
        requirementInfoInit.setDec_type(Global.DEC_TYPE_BUSINESS);
        if (BusinessManager.isRequirementChange(
                editBussinessRequirementFragment_.getRequirementInfo(), requirementInfoInit)) {
            isChange = true;
        }
        requirementInfoInit.setDec_type(Global.DEC_TYPE_HOME);
        if (BusinessManager.isRequirementChange(
                editHomeRequirementFragment_.getRequirementInfo(), requirementInfoInit)) {
            isChange = true;
        }
        if (!isChange) {
            LogTool.d(this.getClass().getName(), "没有改变");
            appManager.finishActivity(this);
        } else {
            LogTool.d(this.getClass().getName(), "有改变");
            showTipDialog();
        }
    }

    @Click({
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

}