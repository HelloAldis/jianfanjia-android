package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.my.NoticeDetailActivity;
import com.jianfanjia.cn.adapter.NoticeAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NoticeInfo;
import com.jianfanjia.cn.bean.NoticeListInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchUserMsgRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerItemCallBack;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author fengliang
 * @ClassName: NoticeFragment
 * @Description: 通知 全部
 * @date 2015-8-26 下午1:07:52
 */

public class NoticeFragment extends BaseFragment implements PullToRefreshBase
        .OnRefreshListener2<RecyclerView> {
    private static final String TAG = NoticeFragment.class.getName();
    private View view = null;

    @Bind(R.id.all_notice_listview)
    PullToRefreshRecycleView all_notice_listview;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private NoticeAdapter noticeAdapter = null;
    private List<NoticeInfo> noticeList = new ArrayList<>();
    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = true;
    private int FROM = 0;
    private String[] typeArray = null;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    public static NoticeFragment newInstance(String[] typeArray) {
        Bundle args = new Bundle();
        NoticeFragment noticeFragment = new NoticeFragment();
        args.putStringArray("TypeArray", typeArray);
        noticeFragment.setArguments(args);
        return noticeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeArray = getArguments().getStringArray("TypeArray");
        LogTool.d(TAG, "typeArray=" + typeArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.d(TAG, "=====onCreateView");
        if (null == view) {
            view = super.onCreateView(inflater, container, savedInstanceState);
            initView();
            isPrepared = true;
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (null != parent) {
            parent.removeView(view);
        }
        return view;
    }

    private void initView() {
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.empty_view_no_notice_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_notice);
        all_notice_listview.setMode(PullToRefreshBase.Mode.BOTH);
        all_notice_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        all_notice_listview.setHasFixedSize(true);
        all_notice_listview.setItemAnimator(new DefaultItemAnimator());
        all_notice_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity()
                .getApplicationContext()));
        all_notice_listview.setOnRefreshListener(this);
    }

    private void onVisible() {
        loadData();
    }

    private void onInvisible() {

    }

    private void loadData() {
        if (!isPrepared || !isVisible) {
            return;
        }
        FROM = 0;
        getNoticeList(typeArray, pullDownListener);
    }

    @OnClick(R.id.error_include)
    public void onClick() {
        getNoticeList(typeArray, pullDownListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void getNoticeList(String[] typeStr, ApiUiUpdateListener listener) {
        Map<String, Object> params = new HashMap<>();
        params.put("$in", typeStr);
        Map<String, Object> conditionParam = new HashMap<>();
        conditionParam.put("message_type", params);
        Map<String, Object> param = new HashMap<>();
        param.put("query", conditionParam);
        param.put("from", FROM);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchUserMsg(new SearchUserMsgRequest(getContext(), param), listener, this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        mHasLoadedOnce = false;
        FROM = 0;
        getNoticeList(typeArray, pullDownListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getNoticeList(typeArray, pullUpListener);
    }

    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            if (mHasLoadedOnce) {
                showWaitDialog();
            }
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            hideWaitDialog();
            mHasLoadedOnce = true;
            all_notice_listview.onRefreshComplete();
            NoticeListInfo noticeListInfo = JsonParser.jsonToBean(data.toString(), NoticeListInfo.class);
            LogTool.d(TAG, "noticeListInfo:" + noticeListInfo);
            if (null != noticeListInfo) {
                noticeList.clear();
                noticeList.addAll(noticeListInfo.getList());
                if (null != noticeList && noticeList.size() > 0) {
                    noticeAdapter = new NoticeAdapter(getActivity(), noticeList, new RecyclerItemCallBack() {
                        @Override
                        public void onClick(int position, Object obj) {
                            NoticeInfo noticeInfo = (NoticeInfo) obj;
                            LogTool.d(TAG, "position=" + position + " noticeInfo:" + noticeInfo.getContent());
                            Bundle detailBundle = new Bundle();
                            detailBundle.putString(Global.MSG_ID, noticeInfo.get_id());
                            startActivity(NoticeDetailActivity.class, detailBundle);
                        }
                    });
                    all_notice_listview.setAdapter(noticeAdapter);
                    all_notice_listview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                } else {
                    all_notice_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                }
                FROM = noticeList.size();
                LogTool.d(TAG, "FROM:" + FROM);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            hideWaitDialog();
            all_notice_listview.onRefreshComplete();
            all_notice_listview.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    };

    private ApiUiUpdateListener pullUpListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            all_notice_listview.onRefreshComplete();
            NoticeListInfo noticeListInfo = JsonParser.jsonToBean(data.toString(), NoticeListInfo.class);
            LogTool.d(TAG, "noticeListInfo:" + noticeListInfo);
            if (null != noticeListInfo) {
                List<NoticeInfo> noticeLists = noticeListInfo.getList();
                if (null != noticeLists && noticeLists.size() > 0) {
                    noticeAdapter.add(FROM, noticeLists);
                    FROM += Constant.HOME_PAGE_LIMIT;
                } else {
                    makeTextShort(getResources().getString(R.string.no_more_data));
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            all_notice_listview.onRefreshComplete();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_all_notice;
    }
}
