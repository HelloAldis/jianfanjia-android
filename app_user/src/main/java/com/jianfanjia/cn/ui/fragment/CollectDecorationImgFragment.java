package com.jianfanjia.cn.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.BeautifulImage;
import com.jianfanjia.api.model.BeautifulImageList;
import com.jianfanjia.api.request.common.GetBeautyImgListRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.ui.Event.CollectBeautyImageEvent;
import com.jianfanjia.cn.ui.activity.beautifulpic.PreviewDecorationActivity;
import com.jianfanjia.cn.ui.adapter.DecorationAdapter;
import com.jianfanjia.cn.view.recycleview.itemdecoration.SpacesItemDecoration;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import de.greenrobot.event.EventBus;

/**
 * @author fengliang
 * @ClassName: CollectDecorationImgFragment
 * @Description: 装修美图收藏
 * @date 2015-8-26 下午1:07:52
 */

public class CollectDecorationImgFragment extends BaseFragment implements PullToRefreshBase
        .OnRefreshListener2<RecyclerView> {
    private static final String TAG = CollectDecorationImgFragment.class.getName();

    @Bind(R.id.decoration_img_listview)
    PullToRefreshRecycleView mPullToRefreshRecycleView;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private List<BeautifulImage> beautyImgList = new ArrayList<>();
    private DecorationAdapter decorationImgAdapter = null;
    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private int total = 0;
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

    public static CollectDecorationImgFragment newInstance() {
        CollectDecorationImgFragment imgFragment = new CollectDecorationImgFragment();
        return imgFragment;
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
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.empty_view_no_img_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_img);
        mPullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshRecycleView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager
                .VERTICAL));
        mPullToRefreshRecycleView.setHasFixedSize(true);
        mPullToRefreshRecycleView.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(TDevice.dip2px(getContext()
                .getApplicationContext(), 5));
        mPullToRefreshRecycleView.addItemDecoration(decoration);
        mPullToRefreshRecycleView.setOnRefreshListener(this);
    }

    private void onVisible() {
        load();
    }

    private void onInvisible() {

    }

    private void load() {
        if (!isPrepared || !isVisible) {
            return;
        }
        getDecorationImgList(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @OnClick(R.id.error_include)
    public void onClick() {
        getDecorationImgList(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    private void getDecorationImgList(int from, int limit, ApiCallback<ApiResponse<BeautifulImageList>> listener) {
        GetBeautyImgListRequest request = new GetBeautyImgListRequest();
        request.setFrom(from);
        request.setLimit(limit);
        Api.getBeautyImgListByUser(request, listener, this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDecorationImgList(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDecorationImgList(beautyImgList.size(), Constant.HOME_PAGE_LIMIT, pullUpListener);
    }


    private ApiCallback<ApiResponse<BeautifulImageList>> pullDownListener = new
            ApiCallback<ApiResponse<BeautifulImageList>>() {


                @Override
                public void onPreLoad() {
                    if (!mHasLoadedOnce) {
                        Hud.show(getUiContext());
                    }
                }

                @Override
                public void onHttpDone() {
                    Hud.dismiss();
                    if (mPullToRefreshRecycleView != null) {
                        mPullToRefreshRecycleView.onRefreshComplete();
                    }
                }

                @Override
                public void onSuccess(ApiResponse<BeautifulImageList> apiResponse) {
                    mHasLoadedOnce = true;
                    BeautifulImageList decorationItemInfo = apiResponse.getData();
                    LogTool.d("decorationItemInfo:" + decorationItemInfo);
                    if (null != decorationItemInfo) {
                        total = decorationItemInfo.getTotal();
                        beautyImgList.clear();
                        beautyImgList.addAll(decorationItemInfo.getBeautiful_images());
                        if (null != beautyImgList && beautyImgList.size() > 0) {
                            if (null == decorationImgAdapter) {
                                decorationImgAdapter = new DecorationAdapter(getActivity(), beautyImgList, new
                                        BaseRecyclerViewAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(int position) {
                                                LogTool.d("position:" + position);
                                                currentPos = position;
                                                LogTool.d("currentPos====" + currentPos);
                                                BeautifulImage beautyImgInfo = beautyImgList.get(currentPos);
                                                LogTool.d("beautyImgInfo:" + beautyImgInfo);
                                                Bundle decorationBundle = new Bundle();
                                                decorationBundle.putString(IntentConstant.DECORATION_BEAUTY_IAMGE_ID,
                                                        beautyImgInfo.get_id
                                                                ());
                                                decorationBundle.putInt(IntentConstant.POSITION, position);
                                                decorationBundle.putSerializable(IntentConstant.IMG_LIST,
                                                        (ArrayList<BeautifulImage>)
                                                                beautyImgList);
                                                decorationBundle.putInt(IntentConstant.TOTAL_COUNT, total);
                                                decorationBundle.putInt(IntentConstant.VIEW_TYPE, IntentConstant
                                                        .COLLECT_BEAUTY_FRAGMENT);
                                                startActivity(PreviewDecorationActivity.class, decorationBundle);
                                            }
                                        });
                                mPullToRefreshRecycleView.setAdapter(decorationImgAdapter);
                            } else {
                                decorationImgAdapter.notifyDataSetChanged();
                            }
                            mPullToRefreshRecycleView.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                        } else {
                            mPullToRefreshRecycleView.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                        errorLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailed(ApiResponse<BeautifulImageList> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.getMsg(code));
                    mPullToRefreshRecycleView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                }

            };

    private ApiCallback<ApiResponse<BeautifulImageList>> pullUpListener = new
            ApiCallback<ApiResponse<BeautifulImageList>>() {


                @Override
                public void onPreLoad() {

                }

                @Override
                public void onHttpDone() {
                    if (mPullToRefreshRecycleView != null) {
                        mPullToRefreshRecycleView.onRefreshComplete();
                    }
                }

                @Override
                public void onSuccess(ApiResponse<BeautifulImageList> apiResponse) {
                    BeautifulImageList decorationItemInfo = apiResponse.getData();
                    LogTool.d("decorationItemInfo:" + decorationItemInfo);
                    if (null != decorationItemInfo) {
                        List<BeautifulImage> beautyList = decorationItemInfo.getBeautiful_images();
                        if (null != beautyList && beautyList.size() > 0) {
                            decorationImgAdapter.add(beautyImgList.size(), beautyList);
                        } else {
                            makeTextShort(getResources().getString(R.string.no_more_data));
                        }
                    }
                }

                @Override
                public void onFailed(ApiResponse<BeautifulImageList> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.getMsg(code));
                }

            };

    public void onEventMainThread(CollectBeautyImageEvent collectBeautyImageEvent) {
        notifyChangeItemState(collectBeautyImageEvent.getImageid(), collectBeautyImageEvent.isCollect());
    }

    private void notifyChangeItemState(String imageid, boolean isCollect) {
        if (isCollect) {
            if (beautyImgList.size() > Constant.HOME_PAGE_LIMIT) {
                getDecorationImgList(0, (beautyImgList.size() / Constant.HOME_PAGE_LIMIT + 1) * Constant
                        .HOME_PAGE_LIMIT, pullDownListener);
            } else {
                getDecorationImgList(0, Constant.HOME_PAGE_LIMIT, pullDownListener);
            }
        } else {
            int removePos = -1;
            BeautifulImage beautyImgInfo = null;
            for (int i = 0; i < beautyImgList.size(); i++) {
                beautyImgInfo = beautyImgList.get(i);
                if (beautyImgInfo.get_id().equals(imageid)) {
                    removePos = i;
                }
            }
            LogTool.d(removePos + "");
            if (removePos != -1) {
                decorationImgAdapter.remove(removePos);
                total = beautyImgList.size();
                if (total == 0) {
                    mPullToRefreshRecycleView.setVisibility(View.GONE);
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
        return R.layout.fragment_collect_decoration_img;
    }
}
