package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.AppointDesignerActivity;
import com.jianfanjia.cn.activity.EditRequirementActivity_;
import com.jianfanjia.cn.activity.MyDesignerActivity_;
import com.jianfanjia.cn.activity.R;
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
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.DividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

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
    public static final int ITEM_EDIT = 0x00;
    public static final int ITEM_GOTOPRO = 0x01;
    public static final int ITEM_HEAD = 0x02;
    public static final int ITEM_ADD = 0x03;

    public static final int ITEM_GOTOMYDESI = 0x04;//去我的设计师
    public static final int ITEM_GOTOODERDESI = 0x05;//去预约设计师

    protected RequirementNewAdapter requirementAdapter;
    private List<RequirementInfo> requirementInfos;

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
    protected LinearLayout req_listview_wrap;

    @ViewById
    protected RecyclerView req_listView;

    @ViewById(R.id.req_pull_refresh)
    protected SwipeRefreshLayout refreshLayout;

    protected Intent gotoOrderDesigner;
    protected Intent gotoMyDesigner;
    protected Intent gotoEditRequirement;

    private int lastVisibleItem;

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
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        // 创建一个线性布局管理器
        req_listView.setLayoutManager(mLayoutManager);
        req_listView.setItemAnimator(new FadeInUpAnimator(new DecelerateInterpolator(0.5F)));
        req_listView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        requirementAdapter = new RequirementNewAdapter(getActivity(), new ClickCallBack() {
            @Override
            public void click(int position, int itemType) {
                switch (itemType) {
                    case ITEM_EDIT:
                        gotoEditRequirement.putExtra(Global.REQUIREMENT_INFO, requirementInfos.get(position));
                        startActivityForResult(gotoEditRequirement, REQUESTCODE_PUBLISH_REQUIREMENT);
                        break;
                    case ITEM_GOTOPRO:
                        break;
                    case ITEM_GOTOMYDESI:
                        gotoMyDesigner.putExtra(Global.REQUIREMENT_ID, requirementInfos.get(position).get_id());
                        startActivity(gotoMyDesigner);
                        break;
                    case ITEM_GOTOODERDESI:
                        gotoOrderDesigner.putExtra(Global.REQUIREMENT_ID, requirementInfos.get(position).get_id());
                        startActivity(gotoOrderDesigner);
                        break;
                }
            }
        });
        req_listView.setAdapter(requirementAdapter);
        req_listView.getItemAnimator().setAddDuration(300);
    }

    @Click({R.id.req_publish_wrap, R.id.head_right_title})
    protected void publish_requirement() {
        Intent intent = new Intent(getActivity(), EditRequirementActivity_.class);
        startActivityForResult(intent, REQUESTCODE_PUBLISH_REQUIREMENT);
//        requirementAdapter.remove(3);
    }

    @AfterViews
    protected void initMainHeadView() {
        mainHeadView
                .setMianTitle(getResources().getString(R.string.xuqiu));
        mainHeadView.setRightTitle(getResources().getString(R.string.str_create));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.GONE);
        initListView();
        initIntent();
        initdata();
        initPullRefresh();
    }

    private void initPullRefresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
    }

    protected void initIntent() {
        gotoEditRequirement = new Intent(getActivity(), EditRequirementActivity_.class);
        gotoOrderDesigner = new Intent(getActivity(), AppointDesignerActivity.class);
        gotoMyDesigner = new Intent(getActivity(), MyDesignerActivity_.class);
    }

    protected void initdata() {
        if (NetTool.isNetworkAvailable(getActivity())) {
            JianFanJiaClient.get_Requirement_List(getActivity(), new ApiUiUpdateListener() {
                @Override
                public void preLoad() {
                }

                @Override
                public void loadSuccess(Object data) {
                    refreshLayout.setRefreshing(false);
                    if (data != null) {
                        requirementInfos = JsonParser.jsonToList(data.toString(), new TypeToken<List<RequirementInfo>>() {
                        }.getType());
                        requirementAdapter.addItem(requirementInfos);
                        if (requirementInfos.size() > 0) {
                            setListVisiable();
                            if (requirementInfos.size() >= Constant.ROST_REQUIREMTNE_TOTAL) {
                                mainHeadView.setRigthTitleEnable(false);
                            }
                        } else {
                            setPublishVisiable();
                        }
                    }
                }

                @Override
                public void loadFailture(String error_msg) {
                    setPublishVisiable();
                    refreshLayout.setRefreshing(false);
                }
            }, this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUESTCODE_PUBLISH_REQUIREMENT:
                initdata();
                break;
            default:
                break;
        }
    }
}
