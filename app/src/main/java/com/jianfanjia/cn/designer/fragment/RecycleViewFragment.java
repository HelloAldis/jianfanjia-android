package com.jianfanjia.cn.designer.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.designer.Event.UpdateEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.SettingContractActivity_;
import com.jianfanjia.cn.designer.activity.SettingMeasureDateActivity_;
import com.jianfanjia.cn.designer.activity.requirement.DesignerPlanListActivity;
import com.jianfanjia.cn.designer.activity.requirement.PingJiaInfoActivity;
import com.jianfanjia.cn.designer.activity.requirement.PreviewBusinessRequirementActivity_;
import com.jianfanjia.cn.designer.activity.requirement.PreviewRequirementActivity_;
import com.jianfanjia.cn.designer.adapter.MyHandledRequirementAdapter;
import com.jianfanjia.cn.designer.base.BaseAnnotationFragment;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.bean.RequirementList;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.http.OkHttpClientManager;
import com.jianfanjia.cn.designer.http.request.NotifyOwnerMeasureHouseRequest;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.designer.view.library.PullToRefreshScrollView;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.designer.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-19 14:00
 */
public class RecycleViewFragment extends BaseAnnotationFragment {

    private static final String TAG = "RecycleViewFragment";

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

    protected PullToRefreshRecycleView pullrefresh;
    protected PullToRefreshScrollView emptyPullRefresh;

    protected RelativeLayout emptyLayout;

    protected RelativeLayout errorLayout;

    private RelativeLayout rootLayout;

    private MyHandledRequirementAdapter myHandledRequirementAdapter;

    private boolean isVisiable;

    private View view = null;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    private List<RequirementInfo> requirementInfos;

    private List<RequirementInfo> currentRequirementInfo;

    private RequirementList requirementList;

    private Context _context;


    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static RecycleViewFragment newInstance(int num) {
        RecycleViewFragment f = new RecycleViewFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;

        LogTool.d(this.getClass().getName(), "num =" + mNum);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.d(TAG, "onCreateView");
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_recycleview, container, false);
            initRecycleView();
            isPrepared = true;
            lazyLoad();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {

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

    private void lazyLoad() {
        if (!isPrepared || !isVisiable) {
            return;
        }
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    protected void initRecycleView() {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        errorLayout.findViewById(R.id.img_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        rootLayout = (RelativeLayout) view.findViewById(R.id.root_layout);
        emptyPullRefresh = (PullToRefreshScrollView) view.findViewById(R.id.emptyPullRefreshScrollView);
        emptyPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }
        });
        pullrefresh = (PullToRefreshRecycleView) view.findViewById(R.id.pull_refresh_recycle_view);
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
                RequirementInfo requirementInfo = currentRequirementInfo.get(position);
                if (requirementInfo.getDec_style() == null) {//此处是对老的可能没有家装类型的数据进行初始化，防止异常
                    requirementInfo.setDec_style(Global.DEC_TYPE_HOME);
                }
                switch (itemType) {
                    case REFUSE_TYPE:
                        showRefuseDialog(requirementInfo.get_id());
                        break;
                    case RESPONDE_TYPE:
                        Intent settingHouseTimeIntent = new Intent(_context, SettingMeasureDateActivity_.class);
                        Bundle settingHouseTimeBundle = new Bundle();
                        settingHouseTimeBundle.putString(Global.REQUIREMENT_ID, requirementInfo.get_id());
                        settingHouseTimeBundle.putString(Global.PHONE, requirementInfo.getUser().getPhone());
                        settingHouseTimeIntent.putExtras(settingHouseTimeBundle);
                        startActivity(settingHouseTimeIntent);
                        getActivity().overridePendingTransition(R.anim.slide_and_fade_in_from_bottom, R.anim.fade_out);
                        responseRequirement(requirementInfo.get_id(), 0);
                        break;
                    case PRIVIEW_REQUIREMENT_TYPE:
                        Intent gotoPriviewRequirement = null;
                        if (requirementInfo.getDec_type().equals(Global.DEC_TYPE_BUSINESS)) {
                            gotoPriviewRequirement = new Intent(getActivity(), PreviewBusinessRequirementActivity_
                                    .class);
                        } else {
                            gotoPriviewRequirement = new Intent(getActivity(), PreviewRequirementActivity_.class);
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
                        Intent settingStartAt = new Intent(_context, SettingContractActivity_.class);
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

    private void notifyOwnerConfirmHouse(RequirementInfo requirementInfo) {
        Map<String, Object> param = new HashMap<>();
        param.put(Global.PLAN_ID, requirementInfo.getPlan().get_id());
        param.put(Global.USER_ID, requirementInfo.getUserid());
        JianFanJiaClient.notifyOwnerConfirmHouse(new NotifyOwnerMeasureHouseRequest(getContext(), param), new
                ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                makeTextShort(getString(R.string.notify_success));
            }

            @Override
            public void loadFailture(String error_msg) {
                //一天只能提醒一次
                if (!error_msg.equals(OkHttpClientManager.NOT_NET_ERROR) || error_msg.equals(OkHttpClientManager
                        .SERVER_ERROR)) {
                    makeTextShort(getString(R.string.notify_not_more_once_everyday));
                } else {
                    makeTextShort(error_msg);
                }
            }
        }, this);
    }

    private void refuseRequirement(String requirementid, String msg) {
        JianFanJiaClient.refuseRequirement(getActivity(), new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }


            public void loadSuccess(Object data) {
                if (refuseDialog != null) {
                    refuseDialog.dismiss();
                }
                initData();
            }

            @Override
            public void loadFailture(String error_msg) {

            }
        }, requirementid, msg, this);
    }

    public void onEventMainThread(UpdateEvent event) {
//        LogTool.d(TAG, "event:" + event.getEventType());
        initData();
    }

    String refuseMsg;
    CommonDialog refuseDialog;

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

    private void responseRequirement(String requirementid, long houseCheckTime) {
        JianFanJiaClient.responseRequirement(getActivity(), new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                initData();
            }

            @Override
            public void loadFailture(String error_msg) {

            }
        }, requirementid, houseCheckTime, this);
    }

    private void initData() {
        JianFanJiaClient.getAllRequirementList(getActivity(), new ApiUiUpdateListener() {
            @Override
            public void preLoad() {
                if (!mHasLoadedOnce) {
                    showWaitDialog();
                }
            }

            @Override
            public void loadSuccess(Object data) {
                hideWaitDialog();
                pullrefresh.onRefreshComplete();
                emptyPullRefresh.onRefreshComplete();
                LogTool.d(this.getClass().getName(), data.toString());
                mHasLoadedOnce = true;
                requirementInfos = JsonParser.jsonToList(data.toString(), new TypeToken<List<RequirementInfo>>() {
                }.getType());
                requirementList = new RequirementList(requirementInfos);
                errorLayout.setVisibility(View.GONE);
                disposeData(requirementList);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
                hideWaitDialog();
                pullrefresh.onRefreshComplete();
                emptyPullRefresh.onRefreshComplete();
                if (!mHasLoadedOnce) {
                    errorLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                }
            }
        }, this);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context.getApplicationContext();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
