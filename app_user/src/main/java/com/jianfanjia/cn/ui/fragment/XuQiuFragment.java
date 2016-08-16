package com.jianfanjia.cn.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.request.guest.PostUserRequirementRequest;
import com.jianfanjia.api.request.user.GetRequirementListRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.Event.ScrollEvent;
import com.jianfanjia.cn.ui.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.ui.activity.requirement.MyProcessDetailActivity;
import com.jianfanjia.cn.ui.activity.requirement.PreviewBusinessRequirementActivity;
import com.jianfanjia.cn.ui.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.ui.activity.requirement.PreviewHomeRequirementActivity;
import com.jianfanjia.cn.ui.activity.requirement.UpdateRequirementActivity;
import com.jianfanjia.cn.ui.adapter.RequirementNewAdapter;
import com.jianfanjia.cn.ui.interf.ClickCallBack;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.DesignerAppointSuccessDialog;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
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

    public static final int ITEM_GOTODESIGNER = 0x05;//
    // 去预约设计师
    public static final int ITEM_GOTOPLAN = 0x07;//查看合同

    protected RequirementNewAdapter requirementAdapter;
    private List<Requirement> requirementInfos = new ArrayList<>();
    private boolean isFirst = true;//第一次加载成功之前都只显示等待对话框

    @Bind(R.id.req_head)
    protected MainHeadView mainHeadView = null;

    @Bind(R.id.req_publish_wrap)
    protected ScrollView req_publish_wrap;

    @Bind(R.id.req_listview_wrap)
    protected FrameLayout req_listview_wrap;

    @Bind(R.id.req_pullfefresh)
    protected PullToRefreshRecycleView mPullToRefreshRecycleView;

    @Bind(R.id.error_include)
    RelativeLayout error_Layout;

    @Bind(R.id.edit_requirement_bg)
    ImageView ivEditRequirementBg;

    @Bind(R.id.et_name)
    EditText etUserName;

    @Bind(R.id.et_phone)
    EditText etPhone;

    private Requirement requirementInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    protected void setListVisiable() {
        LogTool.d("setVisiable()");
        req_listview_wrap.setVisibility(View.VISIBLE);
        req_publish_wrap.setVisibility(View.GONE);
    }

    protected void setPublishVisiable() {
        LogTool.d("setPublishVisiable()");
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
                    case ITEM_GOTODESIGNER:
                        gotoOrderDesigner();
                        break;
                    case ITEM_GOTOPLAN:
                        gotoPreviewPlanActivity(RequirementBusiness.getLastUpdateDesignerPlan(requirementInfo));
                        break;
                    default:
                        break;
                }
            }
        });
        mPullToRefreshRecycleView.setAdapter(requirementAdapter);
        mPullToRefreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity().getApplicationContext()));
    }

    private void gotoPreviewPlanActivity(Plan planInfo) {
        Bundle planBundle = new Bundle();
        planBundle.putSerializable(IntentConstant.PLAN_DETAIL, planInfo);
        planBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirementInfo);
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }



    protected void gotoOrderDesigner() {
        Bundle gotoOrderDesignerBundle = new Bundle();
        gotoOrderDesignerBundle.putString(IntentConstant.DESIGNER_ID,requirementInfo.getFinal_designerid());
        startActivity(DesignerInfoActivity.class,gotoOrderDesignerBundle);

    }

    @OnClick(R.id.error_include)
    protected void errorRefresh() {
        initData();
    }

    @OnClick({R.id.head_right_title,R.id.btn_apply})
    protected void onClick(View view){
        switch (view.getId()){
            case R.id.head_right_title:
                UiHelper.callPhoneIntent(getUiContext(),getString(R.string.app_phone));
                break;
            case R.id.btn_apply:
                String username = etUserName.getEditableText().toString().trim();
                String phone = etPhone.getEditableText().toString().trim();
                if(checkLoginInput(phone,username)){
                    addAppointRequirement(username,phone);
                }
                break;
        }
    }

    private boolean checkLoginInput(String phone, String username) {
        if (TextUtils.isEmpty(username)) {
            makeTextShort(getResources().getString(
                    R.string.input_username));
            etUserName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            makeTextShort(getResources().getString(
                    R.string.hint_username));
            etPhone.requestFocus();
            return false;
        }
        if (!phone.matches(Global.PHONE_MATCH)) {
            makeTextShort(getString(R.string.tip_input_corrent_phone));
            etPhone.requestFocus();
            return false;
        }
        return true;
    }

    private void addAppointRequirement(String userName,String phone) {
        PostUserRequirementRequest postUserRequirement = new PostUserRequirementRequest();
        postUserRequirement.setPhone(phone);
        postUserRequirement.setName(userName);
        postUserRequirement.setDistrict(RequirementBusiness.REQUIREMENT_DISTRICT_ADD);

        Api.postUserRequirement(postUserRequirement, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                etPhone.setText("");
                etUserName.setText("");
                showAppointSuccessDialog();
            }



            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.getMsg(code));
            }
        },this);
    }

    private void showAppointSuccessDialog() {
        DesignerAppointSuccessDialog designerAppointSuccessDialog = new DesignerAppointSuccessDialog(getUiContext());
        designerAppointSuccessDialog.show();
    }

    protected void initView() {
        initEditBackGround();
        initMainView();
        setListVisiable();
        initPullRefresh();
        initListView();
    }

    private void initEditBackGround() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)ivEditRequirementBg.getLayoutParams();

        lp.width = (int)TDevice.getScreenWidth();
        lp.height = (int)( ((float)lp.width / 1242) * 850);

        LogTool.d("width =" + lp.width + ",height =" + lp.height);
        ivEditRequirementBg.setLayoutParams(lp);
    }

    private void initMainView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.create_my_requirement));
        mainHeadView.setRightTitle(getResources().getString(R.string.phone_kefu));
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.GONE);
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
        mPullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPullToRefreshRecycleView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
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
                    Hud.show(getUiContext());
                }
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
                if(mPullToRefreshRecycleView != null){
                    mPullToRefreshRecycleView.onRefreshComplete();
                }
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
                makeTextShort(HttpCode.getMsg(code));
                setListVisiable();
                if (isFirst) {
                    error_Layout.setVisibility(View.VISIBLE);
                }
            }
        },this);
    }

    public void onEventMainThread(ScrollEvent event) {
        mPullToRefreshRecycleView.scrollToPosition(0);
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
