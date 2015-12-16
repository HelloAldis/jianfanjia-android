package com.jianfanjia.cn.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.EditBussinessRequirementFragment_;
import com.jianfanjia.cn.fragment.EditHomeRequirementFragment_;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:发布需求
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PublishRequirementActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = PublishRequirementActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;

    private EditHomeRequirementFragment_ editHomeRequirementFragment_;
    private EditBussinessRequirementFragment_ editBussinessRequirementFragment_;

    protected String status;//当前页面的状态，家装还是商装

    @Override
    public void initView() {

        initMainHeadView();
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                status = tab.getPosition() + "";
                switch (status){
                    case Global.DEC_TYPE_HOME:
                        setMainRightEnable(editHomeRequirementFragment_.isFinish());
                        break;
                    case Global.DEC_TYPE_BUSINESS:
                        setMainRightEnable(editBussinessRequirementFragment_.isFinish());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        status = Global.DEC_TYPE_HOME;
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.act_edit_req_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitle(getString(R.string.finish));
        mainHeadView.setRigthTitleEnable(false);
        mainHeadView.setRightTextListener(this);
    }

    public void setMainRightEnable(boolean isenable){
        mainHeadView.setRigthTitleEnable(isenable);
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
        bundle.putSerializable(Global.REQUIREMENT_INFO,requirementInfo);
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
