package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.requirement.AppointDesignerActivity;
import com.jianfanjia.cn.activity.requirement.MyDesignerActivity_;
import com.jianfanjia.cn.activity.requirement.MyProcessDetailActivity_;
import com.jianfanjia.cn.activity.requirement.PreviewBusinessRequirementActivity_;
import com.jianfanjia.cn.activity.requirement.PreviewRequirementActivity_;
import com.jianfanjia.cn.activity.requirement.PublishRequirementActivity;
import com.jianfanjia.cn.activity.requirement.UpdateRequirementActivity_;
import com.jianfanjia.cn.adapter.RequirementNewAdapter;
import com.jianfanjia.cn.base.BaseAnnotationFragment;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:需求
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
@EFragment(R.layout.fragment_requirement)
public class XuQiuFragment extends BaseAnnotationFragment {
    private static final String TAG = XuQiuFragment.class.getName();
    public static final int REQUESTCODE_PUBLISH_REQUIREMENT = 1;
    public static final int REQUESTCODE_EDIT_REQUIREMENT = 2;
    public static final int REQUESTCODE_FRESH_REQUIREMENT = 3;
    public static final int ITEM_PRIVIEW = 0x06;
    public static final int ITEM_EDIT = 0x00;
    public static final int ITEM_GOTOPRO = 0x01;

    public static final int ITEM_GOTOMYDESI = 0x04;//去我的设计师
    public static final int ITEM_GOTOODERDESI = 0x05;//去预约设计师

    protected RequirementNewAdapter requirementAdapter;
    private List<RequirementInfo> requirementInfos = new ArrayList<RequirementInfo>();
    private boolean isFirst = true;//第一次加载成功之前都只显示等待对话框

    @ViewById(R.id.frag_req_rootview)
    protected LinearLayout rootView;

    @ViewById(R.id.req_head)
    protected MainHeadView mainHeadView = null;

    @ViewById(R.id.req_tip)
    protected TextView req_tip;

    @ViewById(R.id.req_publish)
    protected TextView req_publish;

    @ViewById
    protected LinearLayout req_publish_wrap;

    @ViewById
    protected FrameLayout req_listview_wrap;

    @ViewById(R.id.req_pullfefresh)
    protected PullToRefreshRecycleView pullrefresh;

    @ViewById(R.id.error_include)
    RelativeLayout error_Layout;

    protected Intent gotoOrderDesigner;
    protected Intent gotoMyDesigner;
    protected Intent gotoMyProcess;

    // Header View
    private UpdateBroadcastReceiver updateBroadcastReceiver;
    private RequirementInfo requirementInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    protected void initListView() {
        requirementAdapter = new RequirementNewAdapter(getActivity(), new ClickCallBack() {
            @Override
            public void click(int position, int itemType) {
                requirementInfo = requirementInfos.get(position);
                switch (itemType) {
                    case ITEM_PRIVIEW:
                        Intent gotoPriviewRequirement = null;
                        if (requirementInfo.getDec_type().equals(Global.DEC_TYPE_BUSINESS)) {
                            gotoPriviewRequirement = new Intent(getActivity(), PreviewBusinessRequirementActivity_.class);
                        } else {
                            gotoPriviewRequirement = new Intent(getActivity(), PreviewRequirementActivity_.class);
                        }
                        gotoPriviewRequirement.putExtra(Global.REQUIREMENT_INFO, requirementInfo);
                        getActivity().startActivityForResult(gotoPriviewRequirement, REQUESTCODE_FRESH_REQUIREMENT);
                        break;
                    case ITEM_EDIT:
                        Intent intent = new Intent(getActivity(), UpdateRequirementActivity_.class);
                        intent.putExtra(Global.REQUIREMENT_INFO, requirementInfo);
                        getActivity().startActivityForResult(intent, REQUESTCODE_EDIT_REQUIREMENT);
                        break;
                    case ITEM_GOTOPRO:
                        gotoMyProcess.putExtra(Global.PROCESS_INFO, requirementInfo.getProcess());
                        startActivity(gotoMyProcess);
                        break;
                    case ITEM_GOTOMYDESI:
                        gotoMyDesigner.putExtra(Global.REQUIREMENT_ID, requirementInfos.get(position).get_id());
                        startActivity(gotoMyDesigner);
                        break;
                    case ITEM_GOTOODERDESI:
                        gotoOrderDesigner();
                        break;
                    default:
                        break;
                }
            }
        });
        pullrefresh.setAdapter(requirementAdapter);
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        pullrefresh.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .paint(paint)
                .showLastDivider()
                .build());
    }

    protected void gotoOrderDesigner() {
        if (requirementInfo.getOrder_designers() != null && requirementInfo.getOrder_designers().size() > 0) {
            gotoOrderDesigner.putExtra(Global.REQUIREMENT_DESIGNER_NUM, requirementInfo.getOrder_designers().size());
        } else {
            gotoOrderDesigner.putExtra(Global.REQUIREMENT_DESIGNER_NUM, 0);
        }
        gotoOrderDesigner.putExtra(Global.REQUIREMENT_ID, requirementInfo.get_id());
        startActivity(gotoOrderDesigner);
    }

    @Click({R.id.req_publish_layout, R.id.head_right_title})
    protected void publish_requirement() {
        Intent intent = new Intent(getActivity(), PublishRequirementActivity.class);
        startActivityForResult(intent, REQUESTCODE_PUBLISH_REQUIREMENT);
    }

    @Click(R.id.error_include)
    protected void errorRefresh() {
        initData();
    }

    @AfterViews
    protected void initView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.requirement_list));
        mainHeadView.setRightTitle(getResources().getString(R.string.str_create));
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.GONE);
        initPullRefresh();
        initListView();
        initIntent();
        initData();
    }


    @Override
    public void onAttach(Context context) {
        updateBroadcastReceiver = new UpdateBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Global.ACTION_UPDATE);    //只有持有相同的action的接受者才能接收此广播
        context.registerReceiver(updateBroadcastReceiver, filter);
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    protected void initIntent() {
        gotoOrderDesigner = new Intent(getActivity(), AppointDesignerActivity.class);
        gotoMyDesigner = new Intent(getActivity(), MyDesignerActivity_.class);
        gotoMyProcess = new Intent(getActivity(), MyProcessDetailActivity_.class);
    }

    protected void initData() {
        JianFanJiaClient.get_Requirement_List(getActivity(), new ApiUiUpdateListener() {
            @Override
            public void preLoad() {
                if (isFirst) {
                    showWaitDialog();
                }
            }

            @Override
            public void loadSuccess(Object data) {
                hideWaitDialog();
                pullrefresh.onRefreshComplete();
                if (data != null) {
                    requirementInfos = JsonParser.jsonToList(data.toString(), new TypeToken<List<RequirementInfo>>() {
                    }.getType());
                    if (null != requirementInfos && requirementInfos.size() > 0) {
                        requirementAdapter.addItem(requirementInfos);
                        setListVisiable();
                        if (requirementInfos.size() >= Constant.ROST_REQUIREMTNE_TOTAL) {
                            mainHeadView.setRigthTitleEnable(false);
                        }
                        isFirst = false;
                    } else {
                        setPublishVisiable();
                    }
                    error_Layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void loadFailture(String error_msg) {
                hideWaitDialog();
                makeTextShort(error_msg);
                setListVisiable();
                if (isFirst) {
                    error_Layout.setVisibility(View.VISIBLE);
                }
                pullrefresh.onRefreshComplete();
            }
        }, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogTool.d(TAG, "onActivityResult = " + requestCode + " resultCode=" + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUESTCODE_PUBLISH_REQUIREMENT:
            case REQUESTCODE_EDIT_REQUIREMENT:
                initData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(updateBroadcastReceiver);
    }

    //刷新数据的广播
    class UpdateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }
}
