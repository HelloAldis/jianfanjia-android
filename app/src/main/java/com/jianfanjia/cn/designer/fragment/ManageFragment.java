package com.jianfanjia.cn.designer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.designer.Event.MessageEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.SettingContractActivity_;
import com.jianfanjia.cn.designer.activity.requirement.MyProcessDetailActivity_;
import com.jianfanjia.cn.designer.activity.requirement.PreviewBusinessRequirementActivity_;
import com.jianfanjia.cn.designer.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.designer.activity.requirement.PreviewRequirementActivity_;
import com.jianfanjia.cn.designer.adapter.MySiteAdapter;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.bean.Process;
import com.jianfanjia.cn.designer.bean.SiteProcessItem;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.designer.view.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Description:工地管理
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ManageFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = ManageFragment.class.getName();
    public static final int ITEM_PRIVIEW = 10;
    public static final int ITEM_CONTRACT = 20;
    public static final int ITEM_PLAN = 30;
    public static final int ITEM_GOTOO_SITE = 40;
    private MainHeadView mainHeadView = null;
    private PullToRefreshRecycleView manage_pullfefresh = null;
    private String[] proTitle = null;
    private List<Process> processList = new ArrayList<Process>();
    private List<SiteProcessItem> siteProcessList = new ArrayList<SiteProcessItem>();
    protected PullToRefreshScrollView emptyPullRefresh;

    protected RelativeLayout emptyLayout;

    protected RelativeLayout errorLayout;

    private LinearLayout rootLayout;
    private TextView process_tip_text = null;

    private String processId = null;
    private int itemPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView(View view) {
        initMainHeadView(view);
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        rootLayout = (LinearLayout) view.findViewById(R.id.frag_req_rootview);
        emptyPullRefresh = (PullToRefreshScrollView) view.findViewById(R.id.emptyPullRefreshScrollView);
        emptyPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getProcessList();
            }
        });
        process_tip_text = (TextView) view.findViewById(R.id.process_tip_text);
        proTitle = getActivity().getApplication().getResources().getStringArray(
                R.array.site_procedure);
        setProcessList();
        manage_pullfefresh = (PullToRefreshRecycleView) view.findViewById(R.id.manage_pullfefresh);
        manage_pullfefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        manage_pullfefresh.setLayoutManager(new LinearLayoutManager(getActivity()));
        manage_pullfefresh.setItemAnimator(new DefaultItemAnimator());
        manage_pullfefresh.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getContext()));
        getProcessList();
    }

    private void initMainHeadView(View view) {
        mainHeadView = (MainHeadView) view.findViewById(R.id.manage_head);
        mainHeadView.setMianTitle("我的工地");
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.GONE);
    }

    @Override
    public void setListener() {
        manage_pullfefresh.setOnRefreshListener(this);
        errorLayout.setOnClickListener(this);
        process_tip_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getProcessList();
                break;
            case R.id.process_tip_text:
                Intent gotoMyProcess = new Intent(getActivity(), MyProcessDetailActivity_.class);
                gotoMyProcess.putExtra(Global.PROCESS_ID, processId);
                startActivity(gotoMyProcess);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(MessageEvent event) {
        LogTool.d(TAG, "event:" + event.getEventType());
        switch (event.getEventType()) {
            case Constant.UPDATE_MANAAGE_FRAGMENT:

                break;
            default:
                break;
        }
    }

    private void getProcessList() {
        JianFanJiaClient.get_Process_List(getActivity(), new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data=" + data);
                processList = JsonParser.jsonToList(data.toString(), new TypeToken<List<Process>>() {
                }.getType());
                LogTool.d(TAG, "processList:" + processList);
                if (null != processList && processList.size() > 0) {
                    MySiteAdapter adapter = new MySiteAdapter(getActivity(), processList, siteProcessList, new ClickCallBack() {
                        @Override
                        public void click(int position, int itemType) {
                            Process process = processList.get(position);
                            processId = process.get_id();
                            LogTool.d(TAG, "processId:" + processId + "  itemPosition:" + itemPosition);
                            switch (itemType) {
                                case ITEM_PRIVIEW:
                                    Intent gotoPriviewRequirement = null;
                                    if (process.getRequirement().getDec_type().equals(Global.DEC_TYPE_BUSINESS)) {
                                        gotoPriviewRequirement = new Intent(getActivity(), PreviewBusinessRequirementActivity_.class);
                                    } else {
                                        gotoPriviewRequirement = new Intent(getActivity(), PreviewRequirementActivity_.class);
                                    }
                                    gotoPriviewRequirement.putExtra(Global.REQUIREMENT_INFO, process.getRequirement());
                                    startActivity(gotoPriviewRequirement);
                                    break;
                                case ITEM_CONTRACT:
                                    Intent viewContractIntent = new Intent(getActivity(), SettingContractActivity_.class);
                                    Bundle contractBundle = new Bundle();
                                    contractBundle.putSerializable(Global.REQUIREMENT_INFO, process.getRequirement());
                                    contractBundle.putSerializable(Global.PLAN_DETAIL, process.getPlan());
                                    viewContractIntent.putExtras(contractBundle);
                                    startActivity(viewContractIntent);
                                    break;
                                case ITEM_PLAN:
                                    Intent viewPlanIntent = new Intent(getActivity(), PreviewDesignerPlanActivity.class);
                                    Bundle planBundle = new Bundle();
                                    planBundle.putSerializable(Global.PLAN_DETAIL, process.getPlan());
                                    planBundle.putSerializable(Global.REQUIREMENT_INFO, process.getRequirement());
                                    viewPlanIntent.putExtras(planBundle);
                                    startActivity(viewPlanIntent);
                                    break;
                                case ITEM_GOTOO_SITE:
                                    Intent gotoMyProcess = new Intent(getActivity(), MyProcessDetailActivity_.class);
                                    gotoMyProcess.putExtra(Global.PROCESS_ID, processId);
                                    startActivity(gotoMyProcess);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    manage_pullfefresh.setAdapter(adapter);
                    manage_pullfefresh.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                } else {
                    manage_pullfefresh.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyLayout.findViewById(R.id.empty_contentLayout).setLayoutParams(//动态设置内容高度，防止滚动
                            new RelativeLayout.LayoutParams(rootLayout.getWidth(), rootLayout.getHeight() - mainHeadView.getHeight()));
                    errorLayout.setVisibility(View.GONE);
                }
                manage_pullfefresh.onRefreshComplete();
                emptyPullRefresh.onRefreshComplete();
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
                manage_pullfefresh.onRefreshComplete();
                emptyPullRefresh.onRefreshComplete();
                manage_pullfefresh.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        }, this);
    }

    private void setProcessList() {
        for (int i = 0; i < proTitle.length; i++) {
            SiteProcessItem item = new SiteProcessItem();
            item.setRes(getResources()
                    .getIdentifier("icon_home_bg" + (i + 1), "drawable",
                            getActivity().getApplication().getPackageName()));
            item.setTitle(proTitle[i]);
            siteProcessList.add(item);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getProcessList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_manage2;
    }

}
