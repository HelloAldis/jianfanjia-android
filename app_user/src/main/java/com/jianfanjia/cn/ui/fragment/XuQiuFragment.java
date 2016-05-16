package com.jianfanjia.cn.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.request.user.GetRequirementListRequest;
import com.jianfanjia.cn.ui.Event.ScrollEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.ui.activity.my.BindingPhoneActivity;
import com.jianfanjia.cn.ui.activity.requirement.AppointDesignerActivity;
import com.jianfanjia.cn.ui.activity.requirement.AppointHighPointDesignerActivity;
import com.jianfanjia.cn.ui.activity.requirement.ContractActivity;
import com.jianfanjia.cn.ui.activity.requirement.MyDesignerActivity;
import com.jianfanjia.cn.ui.activity.requirement.MyProcessDetailActivity;
import com.jianfanjia.cn.ui.activity.requirement.PreviewBusinessRequirementActivity;
import com.jianfanjia.cn.ui.activity.requirement.PreviewHomeRequirementActivity;
import com.jianfanjia.cn.ui.activity.requirement.PublishRequirementActivity;
import com.jianfanjia.cn.ui.activity.requirement.UpdateRequirementActivity;
import com.jianfanjia.cn.ui.adapter.RequirementNewAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.interf.ClickCallBack;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;

/**
 * Description:需求
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class XuQiuFragment extends BaseFragment {
    private static final String TAG = XuQiuFragment.class.getName();
    public static final int REQUESTCODE_PUBLISH_REQUIREMENT = 1;
    public static final int REQUESTCODE_EDIT_REQUIREMENT = 2;
    public static final int ITEM_PRIVIEW = 0x06;
    public static final int ITEM_EDIT = 0x00;
    public static final int ITEM_GOTOPRO = 0x01;

    public static final int ITEM_GOTOMYDESI = 0x04;//去我的设计师
    public static final int ITEM_GOTOODERDESI = 0x05;//去预约设计师
    public static final int ITEM_GOTOCONTRACT = 0x07;//查看合同

    protected RequirementNewAdapter requirementAdapter;
    private List<Requirement> requirementInfos = new ArrayList<>();
    private boolean isFirst = true;//第一次加载成功之前都只显示等待对话框

    @Bind(R.id.frag_req_rootview)
    protected LinearLayout rootView;

    @Bind(R.id.req_head)
    protected MainHeadView mainHeadView = null;

    @Bind(R.id.req_tip)
    protected TextView req_tip;

    @Bind(R.id.req_publish)
    protected TextView req_publish;

    @Bind(R.id.req_publish_wrap)
    protected LinearLayout req_publish_wrap;

    @Bind(R.id.req_listview_wrap)
    protected FrameLayout req_listview_wrap;

    @Bind(R.id.req_pullfefresh)
    protected PullToRefreshRecycleView pullrefresh;

    @Bind(R.id.error_include)
    RelativeLayout error_Layout;

    private Requirement requirementInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    protected void setListVisiable() {
        LogTool.d(getClass().getName(), "setVisiable()");
        req_listview_wrap.setVisibility(View.VISIBLE);
        req_publish_wrap.setVisibility(View.GONE);
    }

    protected void setPublishVisiable() {
        LogTool.d(getClass().getName(), "setPublishVisiable()");
        req_listview_wrap.setVisibility(View.GONE);
        req_publish_wrap.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    protected void initListView() {
        requirementAdapter = new RequirementNewAdapter(getActivity(), new ClickCallBack() {
            @Override
            public void click(int position, int itemType) {
                requirementInfo = requirementInfos.get(position);
                if (requirementInfo.getDec_style() == null) {//此处是对老的可能没有家装类型的数据进行初始化，防止异常
                    requirementInfo.setDec_style(Global.DEC_TYPE_HOME);
                }
                switch (itemType) {
                    case ITEM_PRIVIEW:
                        Bundle gotoPriviewRequirementBundle = new Bundle();
                        gotoPriviewRequirementBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirementInfo);
                        if (requirementInfo.getDec_type().equals(Global.DEC_TYPE_BUSINESS)) {
                            startActivity(PreviewBusinessRequirementActivity.class, gotoPriviewRequirementBundle);
                        } else {
                            startActivity(PreviewHomeRequirementActivity.class, gotoPriviewRequirementBundle);
                        }
                        break;
                    case ITEM_EDIT:
                        Bundle requirementInfoBundle = new Bundle();
                        requirementInfoBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirementInfo);
                        startActivityForResult(UpdateRequirementActivity.class, requirementInfoBundle,
                                REQUESTCODE_EDIT_REQUIREMENT);
                        break;
                    case ITEM_GOTOPRO:
                        Bundle gotoMyProcessBundle = new Bundle();
                        gotoMyProcessBundle.putSerializable(IntentConstant.PROCESS_INFO, requirementInfo.getProcess());
                        startActivity(MyProcessDetailActivity.class, gotoMyProcessBundle);
                        break;
                    case ITEM_GOTOMYDESI:
                        Bundle gotoMyDesignerBundle = new Bundle();
                        gotoMyDesignerBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirementInfos.get
                                (position));
                        startActivity(MyDesignerActivity.class, gotoMyDesignerBundle);
                        break;
                    case ITEM_GOTOODERDESI:
                        gotoOrderDesigner();
                        break;
                    case ITEM_GOTOCONTRACT:
                        Bundle gotoContractBundle = new Bundle();
                        gotoContractBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirementInfos.get
                                (position));
                        startActivity(ContractActivity.class, gotoContractBundle);
                        break;
                    default:
                        break;
                }
            }
        });
        pullrefresh.setAdapter(requirementAdapter);
        pullrefresh.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity().getApplicationContext()));
    }

    protected void gotoOrderDesigner() {
        Bundle gotoOrderDesignerBundle = new Bundle();
        if (requirementInfo.getPackage_type().equals(RequirementBusiness.PACKGET_HIGH_POINT)) {//匠心定制的需求预约高端设计师
            gotoOrderDesignerBundle.putString(IntentConstant.REQUIREMENT_ID, requirementInfo.get_id());
            startActivity(AppointHighPointDesignerActivity.class, gotoOrderDesignerBundle);
        } else {
            if (requirementInfo.getOrder_designers() != null && requirementInfo.getOrder_designers().size() > 0) {
                gotoOrderDesignerBundle.putInt(IntentConstant.REQUIREMENT_DESIGNER_NUM, requirementInfo
                        .getOrder_designers().size());
            } else {
                gotoOrderDesignerBundle.putInt(IntentConstant.REQUIREMENT_DESIGNER_NUM, 0);
            }
            gotoOrderDesignerBundle.putString(IntentConstant.REQUIREMENT_ID, requirementInfo.get_id());
            startActivity(AppointDesignerActivity.class, gotoOrderDesignerBundle);
        }
    }

    @OnClick({R.id.req_publish_layout, R.id.head_right_title})
    protected void publish_requirement() {
        if (dataManager.getAccount() != null) {
            startActivity(PublishRequirementActivity.class);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(IntentConstant.BINDING_PHONE_INTENT, IntentConstant.BINDING_PHONE_REQUIREMENT);
            startActivity(BindingPhoneActivity.class, bundle);
            getActivity().overridePendingTransition(R.anim.slide_and_fade_in_from_bottom, R.anim.fade_out);
        }
    }

    @OnClick(R.id.error_include)
    protected void errorRefresh() {
        initData();
    }

    protected void initView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.create_my_requirement));
        mainHeadView.setRightTitle(getResources().getString(R.string.str_create));
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.GONE);
        setListVisiable();
        initPullRefresh();
        initListView();
//        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //每次show都刷新一下数据
            initData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initPullRefresh() {
        pullrefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullrefresh.setLayoutManager(new LinearLayoutManager(getActivity()));
        pullrefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                initData();
            }
        });
    }

    private void initData() {
        GetRequirementListRequest request = new GetRequirementListRequest();
        Api.getRequirementList(request, new ApiCallback<ApiResponse<List<Requirement>>>() {
            @Override
            public void onPreLoad() {
                if (isFirst) {
                    showWaitDialog();
                }
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
                pullrefresh.onRefreshComplete();
            }

            @Override
            public void onSuccess(ApiResponse<List<Requirement>> apiResponse) {
                requirementInfos = apiResponse.getData();
                if (null != requirementInfos && requirementInfos.size() > 0) {
                    requirementAdapter.addItem(requirementInfos);
                    setListVisiable();
                    isFirst = false;
                } else {
                    setPublishVisiable();
                }
                error_Layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(ApiResponse<List<Requirement>> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                setListVisiable();
                if (isFirst) {
                    error_Layout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onEventMainThread(ScrollEvent event) {
        pullrefresh.scrollToPosition(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_requirement;
    }
}
