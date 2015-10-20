package com.jianfanjia.cn.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.EditRequirementActivity_;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.RequirementNewAdapter;
import com.jianfanjia.cn.base.BaseAnnotationFragment;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.EditReqPopWindow;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.DividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;

/**
 * Description:需求
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
@EFragment(R.layout.fragment_requirement)
public class XuQiuFragment extends BaseAnnotationFragment implements ClickCallBack {
    private static final String TAG = XuQiuFragment.class.getName();

    public static final int ITEM_EDIT = 0x00;
    public static final int ITEM_GOTOPRO = 0x01;
    public static final int ITEM_HEAD = 0x02;
    public static final int ITEM_ADD = 0x03;

    private EditReqPopWindow editReqPopWindow;
    @Bean
    protected RequirementNewAdapter requirementAdapter;
    private List<RequirementInfo> requirementInfos;

    @ViewById(R.id.frag_req_rootview)
    protected LinearLayout rootView;

    @ViewById(R.id.req_head)
    protected MainHeadView mainHeadView = null;

    @ViewById(R.id.req_tip)
    protected TextView req_tip;

    @ViewById(R.id.req_publish)
    protected Button req_publish;

    @ViewById
    protected RelativeLayout req_publish_wrap;

    @ViewById
    protected LinearLayout req_listview_wrap;

    @ViewById
    protected RecyclerView req_listView;

    protected void setVisiable() {
        LogTool.d(getClass().getName(), "setVisiable()");
        req_listview_wrap.setVisibility(View.VISIBLE);
        req_publish_wrap.setVisibility(View.GONE);
    }

    protected void initListView() {
        requirementInfos = new ArrayList<RequirementInfo>();
        for (int i = 0; i < 10; i++) {
            RequirementInfo requirementInfo = new RequirementInfo();
            requirementInfo.setCell("关南小区" + i);
            requirementInfos.add(requirementInfo);
        }
        // 创建一个线性布局管理器
        req_listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        req_listView.setItemAnimator(new FlipInLeftYAnimator(new DecelerateInterpolator(0.5F)));
        req_listView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        req_listView.setAdapter(requirementAdapter);
        req_listView.getItemAnimator().setAddDuration(1000);
        requirementAdapter.addItem(requirementInfos);
    }

    public void initPopView() {
        editReqPopWindow = new EditReqPopWindow(getActivity(), this);
    }

    @Click({R.id.req_publish, R.id.head_right_title})
    protected void publish_requirement() {
        makeTextLong("发布需求");
        startActivity(EditRequirementActivity_.class);
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
        initPopView();
        setVisiable();
        initListView();
    }

    @Override
    public void click(int position, int itemType) {
        switch (itemType) {
            case ITEM_EDIT:
                editReqPopWindow.show(rootView);
                break;
        }
    }
}
