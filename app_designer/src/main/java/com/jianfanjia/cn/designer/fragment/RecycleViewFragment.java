package com.jianfanjia.cn.designer.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.request.designer.GetRequirementListRequest;
import com.jianfanjia.api.request.designer.NotifyOwnerMeasureHouseRequest;
import com.jianfanjia.api.request.designer.RefuseRequirementRequest;
import com.jianfanjia.cn.designer.Event.UpdateEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.SettingContractActivity;
import com.jianfanjia.cn.designer.activity.SettingMeasureDateActivity;
import com.jianfanjia.cn.designer.activity.requirement.DesignerPlanListActivity;
import com.jianfanjia.cn.designer.activity.requirement.PingJiaInfoActivity;
import com.jianfanjia.cn.designer.activity.requirement.PreviewBusinessRequirementActivity;
import com.jianfanjia.cn.designer.activity.requirement.PreviewRequirementActivity;
import com.jianfanjia.cn.designer.adapter.MyHandledRequirementAdapter;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.bean.RequirementList;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.designer.view.library.PullToRefreshScrollView;
import com.jianfanjia.common.tool.LogTool;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.designer.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-19 14:00
 */
public class RecycleViewFragment extends BaseFragment {
    private static final String TAG = RecycleViewFragment.class.getName();

    public static final int REFUSE_TYPE = 0x04;
    public static final int RESPONDE_TYPE = 0x05;
    public static final int PRIVIEW_REQUIREMENT_TYPE = 0x06;
    public static final int PHONE_TYPE = 0x07;
    public static final int RREVIEW_COMMENT_TYPE = 0x08;
    public static final int PREVIEW_PLAN_TYPE = 0x09;
    public static final int PREVIEW_CONTRACT_TYPE = 0x10;
    public static final int SETTING_STARTAT_TYPE = 0x11;
    public static final int NOTIFY_MEASURE_HOUSE_TYPE = 0x12;

    private final int FIRST_FRAGMENT = 0;
    private final int SECOND_FRAGMENT = 1;
    private final int THIRD_FRAGMENT = 2;

    private int mNum;

    @Bind(R.id.pull_refresh_recycle_view)
    PullToRefreshRecycleView pullrefresh;

    @Bind(R.id.emptyPullRefreshScrollView)
    PullToRefreshScrollView emptyPullRefresh;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    @Bind(R.id.root_layout)
    RelativeLayout rootLayout;

    private MyHandledRequirementAdapter myHandledRequirementAdapter;

    private boolean isVisiable;

    private View view = null;

    private boolean isPrepared;

    private boolean mHasLoadedOnce;

    private List<Requirement> requirementInfos;

    private List<Requirement> currentRequirementInfo;

    private RequirementList requirementList;

    private Context _context;

    private String refuseMsg;
    private CommonDialog refuseDialog;

    public static RecycleViewFragment newInstance(int num) {
        RecycleViewFragment f = new RecycleViewFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogTool.d(this.getClass().getName(), "onAttach");
        _context = context.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        LogTool.d(this.getClass().getName(), "num =" + mNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.d(TAG, "onCreateView()");
        if (view == null) {
            view = super.onCreateView(inflater, container, savedInstanceState);
            initView();
            isPrepared = true;
            lazyLoad();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisiable = true;
            onVisible();
        } else {
            isVisiable = false;
            onInvisible();
        }
    }

    private void onVisible() {
        lazyLoad();
    }

    private void onInvisible() {

    }

    private void lazyLoad() {
        if (!isPrepared || !isVisiable) {
            return;
        }
        initData();
    }

    private void initView() {
        emptyPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }
        });
        pullrefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullrefresh.setLayoutManager(new LinearLayoutManager(getActivity()));
        pullrefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                initData();
            }
        });
        myHandledRequirementAdapter = new MyHandledRequirementAdapter(getActivity(), new ClickCallBack() {
            @Override
            public void click(int position, int itemType) {
                Requirement requirementInfo = currentRequirementInfo.get(position);
                if (requirementInfo.getDec_style() == null) {//此处是对老的可能没有家装类型的数据进行初始化，防止异常
                    requirementInfo.setDec_style(Global.DEC_TYPE_HOME);
                }
                switch (itemType) {
                    case REFUSE_TYPE:
                        showRefuseDialog(requirementInfo.get_id());
                        break;
                    case RESPONDE_TYPE:
                        Intent settingHouseTimeIntent = new Intent(_context, SettingMeasureDateActivity.class);
                        Bundle settingHouseTimeBundle = new Bundle();
                        settingHouseTimeBundle.putString(Global.REQUIREMENT_ID, requirementInfo.get_id());
                        settingHouseTimeBundle.putString(Global.PHONE, requirementInfo.getUser().getPhone());
                        settingHouseTimeIntent.putExtras(settingHouseTimeBundle);
                        startActivity(settingHouseTimeIntent);
                        getActivity().overridePendingTransition(R.anim.slide_and_fade_in_from_bottom, R.anim.fade_out);
                        break;
                    case PRIVIEW_REQUIREMENT_TYPE:
                        Intent gotoPriviewRequirement = null;
                        if (requirementInfo.getDec_type().equals(Global.DEC_TYPE_BUSINESS)) {
                            gotoPriviewRequirement = new Intent(getActivity(), PreviewBusinessRequirementActivity
                                    .class);
                        } else {
                            gotoPriviewRequirement = new Intent(getActivity(), PreviewRequirementActivity.class);
                        }
                        gotoPriviewRequirement.putExtra(Global.REQUIREMENT_INFO, requirementInfo);
                        getActivity().startActivity(gotoPriviewRequirement);
                        break;
                    case PHONE_TYPE:
                        UiHelper.callPhoneIntent(_context, requirementInfo.getUser().getPhone());
                        break;
                    case RREVIEW_COMMENT_TYPE:
                        Intent viewCommentIntent = new Intent(_context, PingJiaInfoActivity.class);
                        Bundle viewBundle = new Bundle();
                        viewBundle.putSerializable(Global.EVALUATION, requirementInfo.getEvaluation());
                        viewBundle.putSerializable(Global.DESIGNER_INFO, requirementInfo.getDesigner());
                        viewCommentIntent.putExtras(viewBundle);
                        startActivity(viewCommentIntent);
                        break;
                    case PREVIEW_CONTRACT_TYPE:
                    case SETTING_STARTAT_TYPE:
                        Intent settingStartAt = new Intent(_context, SettingContractActivity.class);
                        Bundle settingStartAtBundle = new Bundle();
                        settingStartAtBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
                        settingStartAtBundle.putSerializable(Global.PLAN_DETAIL, requirementInfo.getPlan());
                        settingStartAt.putExtras(settingStartAtBundle);
                        startActivity(settingStartAt);
                        getActivity().overridePendingTransition(R.anim.slide_and_fade_in_from_bottom, R.anim.fade_out);
                        break;
                    case PREVIEW_PLAN_TYPE:
                        Intent viewPlanIntent = new Intent(_context, DesignerPlanListActivity.class);
                        Bundle planBundle = new Bundle();
                        planBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
                        viewPlanIntent.putExtras(planBundle);
                        startActivity(viewPlanIntent);
                        break;
                    case NOTIFY_MEASURE_HOUSE_TYPE:
                        notifyOwnerConfirmHouse(requirementInfo);
                        break;
                }
            }
        });
        pullrefresh.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getContext()));
        pullrefresh.setAdapter(myHandledRequirementAdapter);
        LogTool.d(this.getClass().getName(), "initRecycle item count =" + myHandledRequirementAdapter.getItemCount());
    }

    private void initData() {
        GetRequirementListRequest request = new GetRequirementListRequest();
        Api.getAllRequirementList(request, new ApiCallback<ApiResponse<List<Requirement>>>() {
            @Override
            public void onPreLoad() {
                if (!mHasLoadedOnce) {
                    showWaitDialog();
                }
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<List<Requirement>> apiResponse) {
                pullrefresh.onRefreshComplete();
                emptyPullRefresh.onRefreshComplete();
                mHasLoadedOnce = true;
                requirementInfos = apiResponse.getData();
                requirementList = new RequirementList(requirementInfos);
                errorLayout.setVisibility(View.GONE);
                disposeData(requirementList);
            }

            @Override
            public void onFailed(ApiResponse<List<Requirement>> apiResponse) {
                pullrefresh.onRefreshComplete();
                emptyPullRefresh.onRefreshComplete();
                if (!mHasLoadedOnce) {
                    errorLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void disposeData(RequirementList requirementList) {
        switch (mNum) {
            case FIRST_FRAGMENT:
                currentRequirementInfo = requirementList.getUnHandleRequirementInfoList();
                ((TextView) emptyLayout.findViewById(R.id.tipContent)).setText(getString(R.string.tip_no_unhandle));
                break;
            case SECOND_FRAGMENT:
                currentRequirementInfo = requirementList.getCommunicationRequirementInfoList();
                ((TextView) emptyLayout.findViewById(R.id.tipContent)).setText(getString(R.string.tip_handled));
                break;
            case THIRD_FRAGMENT:
                currentRequirementInfo = requirementList.getOverRequirementInfoLists();
                ((TextView) emptyLayout.findViewById(R.id.tipContent)).setText(getString(R.string.tip_already_handle));
                break;
        }
        myHandledRequirementAdapter.addItem(currentRequirementInfo);
        if (currentRequirementInfo.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            emptyLayout.findViewById(R.id.empty_contentLayout).setLayoutParams(
                    new RelativeLayout.LayoutParams(rootLayout.getWidth(), rootLayout.getHeight()));
        } else {
            emptyLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.error_include)
    public void onClick() {
        initData();
    }

    private void showRefuseDialog(final String requirementid) {
        refuseDialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        refuseDialog.setTitle(getString(R.string.refuse_reason));
        View contentView = LayoutInflater.from(_context).inflate(R.layout.dialog_refuse_requirement, null);
        RadioGroup radioGroup = (RadioGroup) contentView
                .findViewById(R.id.refuse_radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getCheckedRadioButtonId() == R.id.refuse_radio0) {
                    refuseMsg = getString(R.string.refuse_msg0);
                } else if (group.getCheckedRadioButtonId() == R.id.refuse_radio1) {
                    refuseMsg = getString(R.string.refuse_msg1);
                } else if (group.getCheckedRadioButtonId() == R.id.refuse_radio2) {
                    refuseMsg = getString(R.string.refuse_msg2);
                } else {
                    refuseMsg = getString(R.string.refuse_msg3);
                }
            }
        });
        refuseDialog.setContent(contentView);
        refuseDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (refuseMsg != null) {
                            refuseRequirement(requirementid, refuseMsg);
                            dialog.dismiss();
                        } else {
                            makeTextShort(getString(R.string.tip_choose_refuse_reason));
                        }
                    }
                });
        refuseDialog.setNegativeButton(R.string.no, null);
        refuseDialog.show();
    }

    private void notifyOwnerConfirmHouse(Requirement requirementInfo) {
        NotifyOwnerMeasureHouseRequest request = new NotifyOwnerMeasureHouseRequest();
        request.setPlanid(requirementInfo.getPlan().get_id());
        request.setUserid(requirementInfo.getUserid());
        Api.notifyOwnerConfirmHouse(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                makeTextShort(getString(R.string.notify_success));
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(getString(R.string.notify_not_more_once_everyday));
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void refuseRequirement(String requirementid, String msg) {
        RefuseRequirementRequest request = new RefuseRequirementRequest();
        request.setRequirementid(requirementid);
        request.setReject_respond_msg(msg);
        Api.refuseRequirement(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                if (refuseDialog != null) {
                    refuseDialog.dismiss();
                }
                initData();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    public void onEventMainThread(UpdateEvent event) {
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycleview;
    }
}
