package com.jianfanjia.cn.designer.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.request.designer.GetRequirementListRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.bean.RequirementList;
import com.jianfanjia.cn.designer.bean.SelectItem;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description:我的业主
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MyOwnerFragment extends BaseFragment {

    @Bind(R.id.viewpager)
    protected ViewPager viewPager;

    @Bind(R.id.tablayout)
    protected TabLayout tabLayout;

    @Bind(R.id.my_owner_head_layout)
    protected MainHeadView mainHeadView;

    private RequirementList mRequirementList;
    private MyFragmentPagerAdapter mMyOwnerFragmentPagerAdapter;
    private List<SelectItem> mSelectItemList = new ArrayList<>();
    private List<Requirement> requirementInfos;
    private RequirementList requirementList;
    private boolean mHasLoadOnce;

    private String[] tabs = new String[]{"待响应", "进行中", "已放弃"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        initHeadView();
        initFragmentList();
        initViewPagerAndTab();
    }

    private void initHeadView() {
        mainHeadView.setBackLayoutVisable(View.GONE);
        mainHeadView.setMianTitle(getString(R.string.my_ower));
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setDividerVisable(View.GONE);
    }

    private void initFragmentList() {
        mSelectItemList.clear();
        for (int i = 0; i < tabs.length; i++) {
            SelectItem selectItem = new SelectItem(RecycleViewFragment.newInstance(i), tabs[i]);
            mSelectItemList.add(selectItem);
        }
    }

    private void initViewPagerAndTab() {
        mMyOwnerFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mSelectItemList);
        viewPager.setAdapter(mMyOwnerFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
               loadData();
            }
        });
    }

    public void loadData(){
        GetRequirementListRequest request = new GetRequirementListRequest();
        Api.getAllRequirementList(request, new ApiCallback<ApiResponse<List<Requirement>>>() {
            @Override
            public void onPreLoad() {
                if(!mHasLoadOnce){
                    showWaitDialog();
                }
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
                notifyAllFragmentHttpDone();
            }

            @Override
            public void onSuccess(ApiResponse<List<Requirement>> apiResponse) {
                mHasLoadOnce = true;
                requirementInfos = apiResponse.getData();
                requirementList = new RequirementList(requirementInfos);
                setRequirementList(requirementList);
            }

            @Override
            public void onFailed(ApiResponse<List<Requirement>> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                notifyAllFragmentNetError();
            }
        });
    }

    public void setRequirementList(RequirementList requirementList) {
        mRequirementList = requirementList;
        notifyAllFragmentRefresh();
    }

    private void notifyAllFragmentRefresh() {
        for (SelectItem selectItem : mSelectItemList) {
            RecycleViewFragment recycleViewFragment = (RecycleViewFragment) selectItem.getFragment();
            recycleViewFragment.disposeData(mRequirementList);
        }
    }

    private void notifyAllFragmentNetError(){
        for (SelectItem selectItem : mSelectItemList) {
            RecycleViewFragment recycleViewFragment = (RecycleViewFragment) selectItem.getFragment();
            recycleViewFragment.onNetError();
        }
    }

    private void notifyAllFragmentHttpDone(){
        for (SelectItem selectItem : mSelectItemList) {
            RecycleViewFragment recycleViewFragment = (RecycleViewFragment) selectItem.getFragment();
            recycleViewFragment.httpDone();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_owner;
    }
}
