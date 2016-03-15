package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.jianfanjia.cn.activity.requirement.PublishRequirementActivity_;
import com.jianfanjia.cn.activity.requirement.UpdateRequirementActivity_;
import com.jianfanjia.cn.adapter.RequirementNewAdapter;
import com.jianfanjia.cn.base.BaseAnnotationFragment;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
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
    public static final int ITEM_PRIVIEW = 0x06;
    public static final int ITEM_EDIT = 0x00;
    public static final int ITEM_GOTOPRO = 0x01;

    public static final int ITEM_GOTOMYDESI = 0x04;//去我的设计师
    public static final int ITEM_GOTOODERDESI = 0x05;//去预约设计师

    protected RequirementNewAdapter requirementAdapter;
    private List<RequirementInfo> requirementInfos = new ArrayList<>();
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
                if (requirementInfo.getDec_style() == null) {//此处是对老的可能没有家装类型的数据进行初始化，防止异常
                    requirementInfo.setDec_style(Global.DEC_TYPE_HOME);
                }
                switch (itemType) {
                    case ITEM_PRIVIEW:
                        Bundle gotoPriviewRequirementBundle = new Bundle();
                        gotoPriviewRequirementBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
                        if (requirementInfo.getDec_type().equals(Global.DEC_TYPE_BUSINESS)) {
                            startActivity(PreviewBusinessRequirementActivity_.class, gotoPriviewRequirementBundle);
                        } else {
                            startActivity(PreviewRequirementActivity_.class, gotoPriviewRequirementBundle);
                        }
                        break;
                    case ITEM_EDIT:
                        Bundle requirementInfoBundle = new Bundle();
                        requirementInfoBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
                        startActivityForResult(UpdateRequirementActivity_.class, requirementInfoBundle, REQUESTCODE_EDIT_REQUIREMENT);
                        break;
                    case ITEM_GOTOPRO:
                        Bundle gotoMyProcessBundle = new Bundle();
                        gotoMyProcessBundle.putSerializable(Global.PROCESS_INFO, requirementInfo.getProcess());
                        startActivity(MyProcessDetailActivity_.class,gotoMyProcessBundle);
                        break;
                    case ITEM_GOTOMYDESI:
                        Bundle gotoMyDesignerBundle = new Bundle();
                        gotoMyDesignerBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfos.get(position));
                        startActivity(MyDesignerActivity_.class,gotoMyDesignerBundle);
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
        pullrefresh.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity().getApplicationContext()));
    }

    protected void gotoOrderDesigner() {
        Bundle gotoOrderDesignerBundle = new Bundle();
        if (requirementInfo.getOrder_designers() != null && requirementInfo.getOrder_designers().size() > 0) {
            gotoOrderDesignerBundle.putInt(Global.REQUIREMENT_DESIGNER_NUM, requirementInfo.getOrder_designers().size());
        } else {
            gotoOrderDesignerBundle.putInt(Global.REQUIREMENT_DESIGNER_NUM, 0);
        }
        gotoOrderDesignerBundle.putString(Global.REQUIREMENT_ID, requirementInfo.get_id());
        startActivity(AppointDesignerActivity.class,gotoOrderDesignerBundle);
    }

    @Click({R.id.req_publish_layout, R.id.head_right_title})
    protected void publish_requirement() {
        startActivityForResult(PublishRequirementActivity_.class, REQUESTCODE_PUBLISH_REQUIREMENT);
    }

    @Click(R.id.error_include)
    protected void errorRefresh() {
        initData();
    }

    @AfterViews
    protected void initAnnotationView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.requirement_list));
        mainHeadView.setRightTitle(getResources().getString(R.string.str_create));
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.GONE);
        setListVisiable();
        initPullRefresh();
        initListView();
        initData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateBroadcastReceiver = new UpdateBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Global.ACTION_UPDATE);    //只有持有相同的action的接受者才能接收此广播
        context.registerReceiver(updateBroadcastReceiver, filter);
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
