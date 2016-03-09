package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchUserMsgRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @author fengliang
 * @ClassName: NoticeFragment
 * @Description: 通知 全部
 * @date 2015-8-26 下午1:07:52
 */
public class NoticeFragment extends CommonFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = NoticeFragment.class.getName();
    private View view = null;
    private PullToRefreshRecycleView all_notice_listview = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private List<String> msgTypeList = new ArrayList<>();
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private int FROM = 0;
    private int mType = -1;

    public static NoticeFragment newInstance(int type) {
        Bundle args = new Bundle();
        NoticeFragment noticeFragment = new NoticeFragment();
        args.putInt("Type", type);
        noticeFragment.setArguments(args);
        return noticeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mType = getArguments().getInt("Type");
        LogTool.d(TAG, "mType=" + mType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.d(TAG, "=========onCreateView()");
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_all_notice, container, false);
            initView();
            isPrepared = true;
            load();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (null != parent) {
            parent.removeView(view);
        }
        return view;
    }

    public void initView() {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        all_notice_listview = (PullToRefreshRecycleView) view.findViewById(R.id.all_notice_listview);
        all_notice_listview.setMode(PullToRefreshBase.Mode.BOTH);
        all_notice_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        all_notice_listview.setHasFixedSize(true);
        all_notice_listview.setItemAnimator(new DefaultItemAnimator());
        all_notice_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity().getApplicationContext()));
    }

    @Override
    protected void load() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getNoticeList(msgTypeList, pullDownListener);
    }

    @Override
    public void setListener() {
        all_notice_listview.setOnRefreshListener(this);
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:

                break;
            default:
                break;
        }
    }

    private void getNoticeList(List<String> msgType, ApiUiUpdateListener listener) {
        Map<String, Object> params = new HashMap<>();
        params.put("$in", msgType);
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
        all_notice_listview.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        all_notice_listview.onRefreshComplete();
    }

    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());

        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
        }
    };

    private ApiUiUpdateListener pullUpListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());

        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    public void onEventMainThread(MessageEvent event) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
