package com.jianfanjia.cn.designer.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseAnnotationFragment;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.bean.RequirementList;
import com.jianfanjia.cn.designer.cache.DataManagerNew;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.designer.view.dialog.CommonDialog;
import com.jianfanjia.cn.designer.view.dialog.DialogHelper;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;

import java.util.List;

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

    private final int FIRST_FRAGMENT = 0;
    private final int SECOND_FRAGMENT = 1;
    private final int THIRD_FRAGMENT = 2;

    private int mNum;

    protected PullToRefreshRecycleView pullrefresh;

    protected RelativeLayout emptyLayout;

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
            emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
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
        if (!isPrepared || !isVisiable || mHasLoadedOnce) {
            return;
        }
        initData();
    }

    protected void initRecycleView() {
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
                RequirementInfo requirementInfo = null;
                switch (mNum) {
                    case FIRST_FRAGMENT:
                        requirementInfo = requirementList.getUnHandleRequirementInfoList().get(position);
                        break;
                    case SECOND_FRAGMENT:
                        requirementInfo = requirementList.getCommunicationRequirementInfoList().get(position);
                        break;
                    case THIRD_FRAGMENT:
                        requirementInfo = requirementList.getOverRequirementInfoLists().get(position);
                        break;
                }
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
                            gotoPriviewRequirement = new Intent(getActivity(), PreviewBusinessRequirementActivity_.class);
                        } else {
                            gotoPriviewRequirement = new Intent(getActivity(), PreviewRequirementActivity_.class);
                        }
                        gotoPriviewRequirement.putExtra(Global.REQUIREMENT_INFO, requirementInfo);
                        getActivity().startActivity(gotoPriviewRequirement);
                        break;
                    case PHONE_TYPE:
                        UiHelper.IntentToPhone(_context, requirementInfo.getUser().getPhone());
                        break;
                    case RREVIEW_COMMENT_TYPE:
                        Intent viewCommentIntent = new Intent(_context, PingJiaInfoActivity.class);
                        Bundle viewBundle = new Bundle();
                        viewBundle.putString(Global.IMAGE_ID, DataManagerNew.getInstance().getUserImagePath());
                        viewBundle.putString(Global.DESIGNER_NAME, DataManagerNew.getInstance().getUserName());
                        viewBundle.putSerializable(Global.EVALUATION, requirementInfo.getEvaluation());
                        viewCommentIntent.putExtras(viewBundle);
                        startActivity(viewCommentIntent);
                        break;
                    case PREVIEW_CONTRACT_TYPE:
                    case SETTING_STARTAT_TYPE:
                        Intent settingStartAt = new Intent(_context, SettingContractActivity_.class);
                        Bundle settingStartAtBundle = new Bundle();
                        settingStartAtBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
                        settingStartAtBundle.putSerializable(Global.PLAN, requirementInfo.getPlan());
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
                }
            }
        });
        pullrefresh.setAdapter(myHandledRequirementAdapter);
        Paint paint = new Paint();
        paint.setStrokeWidth(MyApplication.dip2px(getActivity(), 8));
        paint.setColor(getResources().getColor(R.color.transparent));
        paint.setAntiAlias(true);
        pullrefresh.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        LogTool.d(this.getClass().getName(), "initRecycle item count =" + myHandledRequirementAdapter.getItemCount());
    }

    private void refuseRequirement(String requirementid, String msg) {
        JianFanJiaClient.refuseRequirement(getActivity(), new ApiUiUpdateListener() {
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
        }, requirementid, msg, this);
    }

    public void onEventMainThread(UpdateEvent event) {
//        LogTool.d(TAG, "event:" + event.getEventType());
        initData();
    }

    String refuseMsg;

    private void showRefuseDialog(final String requirementid) {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        dialog.setTitle(getString(R.string.refuse_reason));
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
        dialog.setContent(contentView);
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (refuseMsg != null) {
                            refuseRequirement(requirementid, refuseMsg);
                        } else {

                        }
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
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
                LogTool.d(this.getClass().getName(), data.toString());
                mHasLoadedOnce = true;
                requirementInfos = JsonParser.jsonToList(data.toString(), new TypeToken<List<RequirementInfo>>() {
                }.getType());
                requirementList = new RequirementList(requirementInfos);
                disposeData(requirementList);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
                hideWaitDialog();
                pullrefresh.onRefreshComplete();
            }
        }, this);
    }


    private void disposeData(RequirementList requirementList) {
        switch (mNum) {
            case FIRST_FRAGMENT:
                myHandledRequirementAdapter.addItem(requirementList.getUnHandleRequirementInfoList());
                if (requirementList.getUnHandleRequirementInfoList().size() == 0) {
                    emptyLayout.setVisibility(View.VISIBLE);
                    ((TextView) emptyLayout.findViewById(R.id.tipContent)).setText(getString(R.string.tip_no_unhandle));
                } else {
                    emptyLayout.setVisibility(View.GONE);
                }
                break;
            case SECOND_FRAGMENT:
                myHandledRequirementAdapter.addItem(requirementList.getCommunicationRequirementInfoList());
                if (requirementList.getCommunicationRequirementInfoList().size() == 0) {
                    emptyLayout.setVisibility(View.VISIBLE);
                    ((TextView) emptyLayout.findViewById(R.id.tipContent)).setText(getString(R.string.tip_handled));
                } else {
                    emptyLayout.setVisibility(View.GONE);
                }
                break;
            case THIRD_FRAGMENT:
                myHandledRequirementAdapter.addItem(requirementList.getOverRequirementInfoLists());
                if (requirementList.getOverRequirementInfoLists().size() == 0) {
                    emptyLayout.setVisibility(View.VISIBLE);
                    ((TextView) emptyLayout.findViewById(R.id.tipContent)).setText(getString(R.string.tip_already_handle));
                } else {
                    emptyLayout.setVisibility(View.GONE);
                }
                break;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context.getApplicationContext();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogTool.d(TAG, "onActivityCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
