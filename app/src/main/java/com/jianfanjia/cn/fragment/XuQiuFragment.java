package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.jianfanjia.cn.interf.ActivityToFragmentCallBack;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.interf.SwitchTabCallBack;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

/**
 * Description:需求
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
@EFragment(R.layout.fragment_requirement)
public class XuQiuFragment extends BaseAnnotationFragment implements ActivityToFragmentCallBack {
    private static final String TAG = XuQiuFragment.class.getName();
    private SwitchTabCallBack switchTabCallBack = null;
    public static final int REQUESTCODE_PUBLISH_REQUIREMENT = 1;
    public static final int REQUESTCODE_EDIT_REQUIREMENT = 2;
    public static final int ITEM_EDIT = 0x00;
    public static final int ITEM_GOTOPRO = 0x01;
    public static final int ITEM_HEAD = 0x02;
    public static final int ITEM_ADD = 0x03;

    public static final int ITEM_GOTOMYDESI = 0x04;//去我的设计师
    public static final int ITEM_GOTOODERDESI = 0x05;//去预约设计师

    protected RequirementNewAdapter requirementAdapter;
    private List<RequirementInfo> requirementInfos = new ArrayList<RequirementInfo>();

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

    @ViewById
    protected RecyclerView req_listView;

    @ViewById(R.id.req_pull_refresh)
    protected SwipeRefreshLayout refreshLayout;

    @ViewById(R.id.error_include)
    RelativeLayout error_Layout;

    protected Intent gotoOrderDesigner;
    protected Intent gotoMyDesigner;
    protected Intent gotoEditRequirement;

    private int lastVisibleItem;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            switchTabCallBack = (SwitchTabCallBack) context;
        } catch (ClassCastException e) {
            LogTool.d(TAG, "e:" + e);
        }
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
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        // 创建一个线性布局管理器
        req_listView.setLayoutManager(mLayoutManager);
        req_listView.setItemAnimator(new FadeInUpAnimator(new DecelerateInterpolator(0.5F)));
//        req_listView.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL_LIST));
        requirementAdapter = new RequirementNewAdapter(getActivity(), new ClickCallBack() {
            @Override
            public void click(int position, int itemType) {
                switch (itemType) {
                    case ITEM_EDIT:
                        gotoEditRequirement.putExtra(Global.REQUIREMENT_INFO, requirementInfos.get(position));
                        startActivityForResult(gotoEditRequirement, REQUESTCODE_PUBLISH_REQUIREMENT);
                        break;
                    case ITEM_GOTOPRO:
                        //工地id
                        switchTabCallBack.switchTab(Constant.MANAGE, "siteid");
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

        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        req_listView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .paint(paint)
                .showLastDivider()
                .build());
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
                .setMianTitle(getResources().getString(R.string.requirement_list));
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
                    error_Layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextLong(error_msg);
                setListVisiable();
                if (requirementInfos == null || requirementInfos.size() == 0) {
                    error_Layout.setVisibility(View.VISIBLE);
                }
                refreshLayout.setRefreshing(false);
            }
        }, this);
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

    @Override
    public void onTransmit(String params) {

    }
}
