package com.jianfanjia.cn.designer.fragment;

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

import com.jianfanjia.cn.designer.Event.MessageEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.my.NoticeDetailActivity;
import com.jianfanjia.cn.designer.adapter.NoticeAdapter;
import com.jianfanjia.cn.designer.bean.NoticeInfo;
import com.jianfanjia.cn.designer.bean.NoticeListInfo;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.http.request.SearchUserMsgRequest;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.RecyclerItemCallBack;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;

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
    private NoticeAdapter noticeAdapter = null;
    private List<NoticeInfo> noticeList = new ArrayList<>();
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private boolean isFirst = true;
    private int FROM = 0;
    private String[] typeArray = null;

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
        EventBus.getDefault().register(this);
        typeArray = getArguments().getStringArray("TypeArray");
        LogTool.d(TAG, "typeArray=" + typeArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.empty_view_no_notice_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_notice);
        all_notice_listview = (PullToRefreshRecycleView) view.findViewById(R.id.all_notice_listview);
        all_notice_listview.setMode(PullToRefreshBase.Mode.BOTH);
        all_notice_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        all_notice_listview.setHasFixedSize(true);
        all_notice_listview.setItemAnimator(new DefaultItemAnimator());
        all_notice_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getContext()));
    }

    @Override
    protected void load() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getNoticeList(typeArray, pullDownListener);
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
                getNoticeList(typeArray, pullDownListener);
                break;
            default:
                break;
        }
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
            if (isFirst) {
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
                    isFirst = false;
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

    public void onEventMainThread(MessageEvent event) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}