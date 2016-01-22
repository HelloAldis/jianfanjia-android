package com.jianfanjia.cn.designer.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
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
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

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
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private TextView process_tip_text = null;

    private String processId = null;

    @Override
    public void initView(View view) {
        initMainHeadView(view);
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        process_tip_text = (TextView) view.findViewById(R.id.process_tip_text);
        proTitle = getActivity().getApplication().getResources().getStringArray(
                R.array.site_procedure);
        setProcessList();
        manage_pullfefresh = (PullToRefreshRecycleView) view.findViewById(R.id.manage_pullfefresh);
        manage_pullfefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        manage_pullfefresh.setLayoutManager(new LinearLayoutManager(getActivity()));
        manage_pullfefresh.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        manage_pullfefresh.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
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
                            LogTool.d(TAG, "processId:" + processId);
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
                                    contractBundle.putSerializable(Global.PLAN, process.getPlan());
                                    viewContractIntent.putExtras(contractBundle);
                                    startActivity(viewContractIntent);
                                    break;
                                case ITEM_PLAN:
                                    Intent viewPlanIntent = new Intent(getActivity(), PreviewDesignerPlanActivity.class);
                                    Bundle planBundle = new Bundle();
                                    planBundle.putSerializable(Global.PLAN, process.getPlan());
                                    planBundle.putSerializable(Global.REQUIRE, process.getRequirement());
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
                    errorLayout.setVisibility(View.GONE);
                }
                manage_pullfefresh.onRefreshComplete();
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextShort(error_msg);
                manage_pullfefresh.onRefreshComplete();
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
    public int getLayoutId() {
        return R.layout.fragment_manage2;
    }

}
