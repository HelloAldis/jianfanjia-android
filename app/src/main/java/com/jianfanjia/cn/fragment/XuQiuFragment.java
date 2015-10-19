package com.jianfanjia.cn.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.EditRequirementActivity_;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.RequirementAdapter;
import com.jianfanjia.cn.base.BaseAnnotationFragment;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.EditReqPopWindow;
import com.jianfanjia.cn.view.MainHeadView;

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
public class XuQiuFragment extends BaseAnnotationFragment implements ClickCallBack {
    private static final String TAG = XuQiuFragment.class.getName();

    public static final int ITEM_EDIT = 0x00;
    public static final int ITEM_GOTOPRO = 0x01;
    public static final int ITEM_HEAD = 0x02;
    public static final int ITEM_ADD = 0x03;

    private EditReqPopWindow editReqPopWindow;
    private RequirementAdapter requirementAdapter;
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
    protected ListView req_listView;

    protected void setVisiable() {
        LogTool.d(getClass().getName(),"setVisiable()");
        req_listview_wrap.setVisibility(View.VISIBLE);
        req_publish_wrap.setVisibility(View.GONE);
    }

    protected void initListView() {
        requirementInfos = new ArrayList<RequirementInfo>();
        for (int i = 0; i < 3; i++) {
            RequirementInfo requirementInfo = new RequirementInfo();
            requirementInfos.add(requirementInfo);
        }
        requirementAdapter = new RequirementAdapter(getActivity(), requirementInfos, this);
        req_listView.setAdapter(requirementAdapter);
    }

    public void initPopView() {
        editReqPopWindow = new EditReqPopWindow(getActivity(), this);
    }

    @Click({R.id.req_publish, R.id.head_right_title})
    protected void publish_requirement() {
        makeTextLong("发布需求");
        startActivity(EditRequirementActivity_.class);
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
