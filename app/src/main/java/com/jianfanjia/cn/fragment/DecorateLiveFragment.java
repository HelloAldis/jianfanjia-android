package com.jianfanjia.cn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.jianfanjia.cn.activity.home.WebViewActivity;
import com.jianfanjia.cn.adapter.DecorateLiveAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.DecorateLiveList;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.GetDecorateLiveRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description: com.jianfanjia.cn.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-11 14:10
 */
public class DecorateLiveFragment extends BaseFragment {
    private static final String TAG = DecorateLiveFragment.class.getName();

    private View view;

    @Bind(R.id.pullrefresh_recycleview)
    PullToRefreshRecycleView recyclerView;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private int mNum;

    private boolean isPrepared;
    private boolean mHasLoadedOnce;


    private DecorateLiveAdapter decorateLiveAdapter;

    private Context _context;

    public static DecorateLiveFragment newInstance(int num) {
        DecorateLiveFragment f = new DecorateLiveFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 0;
        LogTool.d(TAG, "num =" + mNum);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.d(TAG, "onCreateView");
        view = super.onCreateView(inflater, container, savedInstanceState);

        initRecycleView();
        isPrepared = true;
        lazyLoad();

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initRecycleView() {
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_process));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_designer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(_context));
        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                searchShare(Constant.FROM_START, mNum, pullDownUpdateListener);
            }
        });
        decorateLiveAdapter = new DecorateLiveAdapter(_context, recyclerView.getRefreshableView(), new
                OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        //跳到装修直播详情页面
                        String pid = decorateLiveAdapter.getData().get(position).get_id();
                        Bundle bundle = new Bundle();
                        bundle.putString(Global.WEB_VIEW_URL, Url_New.getInstance().DECORATE_LIVE_URL + pid);
                        startActivity(WebViewActivity.class, bundle);
                    }
                });
        decorateLiveAdapter.setLoadMoreListener(new BaseRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                searchShare(decorateLiveAdapter.getData().size(), mNum, loadmoreUpdateListener);
            }
        });
        decorateLiveAdapter.setErrorView(errorLayout);
        decorateLiveAdapter.setEmptyView(emptyLayout);
        recyclerView.setAdapter(decorateLiveAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context.getApplicationContext();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyLoad();
        } else {

        }
    }

    private void lazyLoad() {
        if (!isPrepared || !getUserVisibleHint() || mHasLoadedOnce) {
            return;
        }
        searchShare(Constant.FROM_START, mNum, pullDownUpdateListener);
    }

    @OnClick(R.id.error_include)
    public void onClick() {
        searchShare(Constant.FROM_START, mNum, pullDownUpdateListener);
    }

    private ApiUiUpdateListener pullDownUpdateListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            if (!mHasLoadedOnce) {
                showWaitDialog();
            }
        }

        @Override
        public void loadSuccess(Object data) {
            recyclerView.onRefreshComplete();
            hideWaitDialog();
            DecorateLiveList decorateLiveList = JsonParser.jsonToBean(data.toString(), DecorateLiveList.class);
            if (decorateLiveList != null) {
                int total = decorateLiveList.getTotal();
                if (total > 0) {
                    decorateLiveAdapter.clear();
                    decorateLiveAdapter.addData(decorateLiveList.getShares());
                    LogTool.d(TAG, "total size =" + total);
                    LogTool.d(TAG, "myCommentInfoAdapter.getData().size() =" +
                            decorateLiveAdapter.getData().size());
                    if (total > decorateLiveAdapter.getData().size()) {
                        decorateLiveAdapter.setState(BaseRecycleAdapter.STATE_LOAD_MORE);
                    } else {
                        decorateLiveAdapter.setState(BaseRecycleAdapter.STATE_NO_MORE);
                    }
                    decorateLiveAdapter.hideErrorAndEmptyView();
                } else {
                    decorateLiveAdapter.setEmptyViewShow();
                }
                mHasLoadedOnce = true;
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            hideWaitDialog();
            recyclerView.onRefreshComplete();
            decorateLiveAdapter.setErrorViewShow();
            decorateLiveAdapter.setState(BaseRecycleAdapter.STATE_NETWORK_ERROR);
        }
    };

    private ApiUiUpdateListener loadmoreUpdateListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            DecorateLiveList decorateLiveList = JsonParser.jsonToBean(data.toString(), DecorateLiveList.class);
            if (decorateLiveList != null) {
                int total = decorateLiveList.getTotal();
                if (total > 0) {
                    decorateLiveAdapter.clear();
                    decorateLiveAdapter.addData(decorateLiveList.getShares());
                    LogTool.d(TAG, "total size =" + total);
                    LogTool.d(TAG, "myCommentInfoAdapter.getData().size() =" +
                            decorateLiveAdapter.getData().size());
                    if (total > decorateLiveAdapter.getData().size()) {
                        decorateLiveAdapter.setState(BaseRecycleAdapter.STATE_LOAD_MORE);
                    } else {
                        decorateLiveAdapter.setState(BaseRecycleAdapter.STATE_NO_MORE);
                    }
                    decorateLiveAdapter.hideErrorAndEmptyView();
                } else {
                    decorateLiveAdapter.setEmptyViewShow();
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            decorateLiveAdapter.setErrorViewShow();
            decorateLiveAdapter.setState(BaseRecycleAdapter.STATE_NETWORK_ERROR);
        }
    };

    private void searchShare(int from, int queryStatus, ApiUiUpdateListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put(Constant.FROM, from);
        param.put(Constant.LIMIT, Constant.HOME_PAGE_LIMIT);
        Map<String, Object> query = new HashMap<>();
        query.put("progress", queryStatus + "");
        param.put(Constant.QUERY, query);
        JianFanJiaClient.searchShare(new GetDecorateLiveRequest(_context, param), listener, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decorate_live;
    }
}
