package com.jianfanjia.cn.activity.ui.fragment;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.base.BaseFragment;
import com.jianfanjia.cn.activity.tools.UiHelper;
import com.jianfanjia.cn.activity.ui.Event.CollectDesignerEvent;
import com.jianfanjia.cn.activity.ui.adapter.FavoriteDesignerAdapter;
import com.jianfanjia.cn.activity.ui.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.DesignerList;
import com.jianfanjia.api.request.user.FavoriteDesignerListRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.ui.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.activity.api.Api;
import com.jianfanjia.cn.activity.config.Constant;
import com.jianfanjia.cn.activity.constant.IntentConstant;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;


/**
 * @author fengliang
 * @ClassName: CollectDesignerFragment
 * @Description: 我的意向设计师
 * @date 2015-8-26 下午1:07:52
 */
public class CollectDesignerFragment extends BaseFragment implements PullToRefreshBase
        .OnRefreshListener2<RecyclerView> {
    private static final String TAG = CollectDesignerFragment.class.getName();

    @Bind(R.id.my_favorite_designer_listview)
    PullToRefreshRecycleView my_favorite_designer_listview;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private FavoriteDesignerAdapter designAdapter = null;
    private DesignerList myFavoriteDesigner = null;
    private List<Designer> designers = new ArrayList<>();
    private boolean isFirst = true;
    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private int FROM = 0;
    private int currentPos = -1;

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

    public static CollectDesignerFragment newInstance() {
        CollectDesignerFragment designerFragment = new CollectDesignerFragment();
        return designerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        isPrepared = true;
        load();
        return view;
    }

    private void initView() {
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.emtpy_view_no_designer_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_designer);
        my_favorite_designer_listview.setMode(PullToRefreshBase.Mode.BOTH);
        my_favorite_designer_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        my_favorite_designer_listview.setHasFixedSize(true);
        my_favorite_designer_listview.setItemAnimator(new DefaultItemAnimator());
        my_favorite_designer_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity()
                .getApplicationContext()));
        my_favorite_designer_listview.setOnRefreshListener(this);
    }

    @OnClick(R.id.error_include)
    public void onClick() {
        getMyFavoriteDesignerList(FROM, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
    }

    private void onVisible() {
        load();
    }

    private void onInvisible() {

    }

    private void load() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getMyFavoriteDesignerList(FROM, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        getMyFavoriteDesignerList(FROM, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getMyFavoriteDesignerList(FROM, Constant.HOME_PAGE_LIMIT, getUpMyFavoriteDesignerListener);
    }

    private void getMyFavoriteDesignerList(int from, int limit, ApiCallback<ApiResponse<DesignerList>> listener) {
        FavoriteDesignerListRequest request = new FavoriteDesignerListRequest();
        request.setFrom(from);
        request.setLimit(limit);
        Api.get_MyFavoriteDesignerList(request, listener);
    }

    private ApiCallback<ApiResponse<DesignerList>> getDownMyFavoriteDesignerListener = new
            ApiCallback<ApiResponse<DesignerList>>() {
                @Override
                public void onPreLoad() {
                    if (isFirst) {
                        showWaitDialog();
                    }
                }

                @Override
                public void onHttpDone() {
                    hideWaitDialog();
                    my_favorite_designer_listview.onRefreshComplete();
                }

                @Override
                public void onSuccess(ApiResponse<DesignerList> apiResponse) {

                    mHasLoadedOnce = true;
                    myFavoriteDesigner = apiResponse.getData();
                    LogTool.d(TAG, "myFavoriteDesigner=" + myFavoriteDesigner);
                    if (myFavoriteDesigner != null) {
                        designers.clear();
                        designers.addAll(myFavoriteDesigner.getDesigners());
                        if (null != designers && designers.size() > 0) {
                            designAdapter = new FavoriteDesignerAdapter(getActivity(), designers, new
                                    RecyclerViewOnItemClickListener() {
                                        @Override
                                        public void OnItemClick(View view, int position) {
                                            LogTool.d(TAG, "position=" + position);
                                            currentPos = position;
                                            LogTool.d(TAG, "currentPos========" + currentPos);
                                            String designerId = designers.get(currentPos).get_id();
                                            LogTool.d(TAG, "designerId:" + designerId);
                                            Bundle designerBundle = new Bundle();
                                            designerBundle.putString(IntentConstant.DESIGNER_ID, designerId);
                                            startActivity(DesignerInfoActivity.class, designerBundle);
                                        }

                                        @Override
                                        public void OnViewClick(int position) {

                                        }
                                    });
                            my_favorite_designer_listview.setAdapter(designAdapter);
                            my_favorite_designer_listview.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.GONE);
                            isFirst = false;
                        } else {
                            my_favorite_designer_listview.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            errorLayout.setVisibility(View.GONE);
                        }
                        FROM = designers.size();
                        LogTool.d(TAG, "FROM:" + FROM);
                    }
                }

                @Override
                public void onFailed(ApiResponse<DesignerList> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                    my_favorite_designer_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                }
            };

    private ApiCallback<ApiResponse<DesignerList>> getUpMyFavoriteDesignerListener = new
            ApiCallback<ApiResponse<DesignerList>>() {

                @Override
                public void onPreLoad() {

                }

                @Override
                public void onHttpDone() {
                    my_favorite_designer_listview.onRefreshComplete();
                }

                @Override
                public void onSuccess(ApiResponse<DesignerList> apiResponse) {
                    myFavoriteDesigner = apiResponse.getData();
                    LogTool.d(TAG, "myFavoriteDesigner=" + myFavoriteDesigner);
                    if (myFavoriteDesigner != null) {
                        List<Designer> designerList = myFavoriteDesigner.getDesigners();
                        if (null != designerList && designerList.size() > 0) {
                            designAdapter.add(FROM, designerList);
                            FROM += Constant.HOME_PAGE_LIMIT;
                        } else {
                            makeTextShort(getResources().getString(R.string.no_more_data));
                        }
                    }
                }

                @Override
                public void onFailed(ApiResponse<DesignerList> apiResponse) {

                }

                @Override
                public void onNetworkError(int code) {

                }

            };

    public void onEventMainThread(CollectDesignerEvent collectDesignerEvent) {
        boolean isCollect = collectDesignerEvent.isCollect();
        if (isCollect) {
            if (designers.size() > Constant.HOME_PAGE_LIMIT) {
                getMyFavoriteDesignerList(0, (designers.size() / Constant.HOME_PAGE_LIMIT + 1) * Constant
                        .HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
            } else {
                getMyFavoriteDesignerList(0, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
            }
        } else {
            int removeSize = -1;
            Designer designer = null;
            for (int i = 0; i < designers.size(); i++) {
                designer = designers.get(i);
                if (designer.get_id().equals(collectDesignerEvent.getDesignerId())) {
                    removeSize = i;
                }
            }
            if (removeSize != -1) {
                designAdapter.remove(removeSize);
                if (designers.size() == 0) {
                    my_favorite_designer_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collect_designer;
    }
}