package com.jianfanjia.cn.fragment;

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
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.BeautifulImage;
import com.jianfanjia.api.model.BeautifulImageList;
import com.jianfanjia.api.request.common.GetBeautyImgListRequest;
import com.jianfanjia.cn.Event.CollectBeautyImageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.beautifulpic.PreviewDecorationActivity;
import com.jianfanjia.cn.adapter.DecorationAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.view.baseview.SpacesItemDecoration;
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
    PullToRefreshRecycleView decoration_img_listview;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private List<BeautifulImage> beautyImgList = new ArrayList<>();
    private DecorationAdapter decorationImgAdapter = null;
    private boolean isFirst = true;
    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private int total = 0;
    private int currentPos = -1;
    private int FROM = 0;

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
        decoration_img_listview.setMode(PullToRefreshBase.Mode.BOTH);
        decoration_img_listview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager
                .VERTICAL));
        decoration_img_listview.setHasFixedSize(true);
        decoration_img_listview.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(TDevice.dip2px(getContext()
                .getApplicationContext(), 5));
        decoration_img_listview.addItemDecoration(decoration);
        decoration_img_listview.setOnRefreshListener(this);
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
        getDecorationImgList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @OnClick(R.id.error_include)
    public void onClick() {
        getDecorationImgList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    private void getDecorationImgList(int from, int limit, ApiCallback<ApiResponse<BeautifulImageList>> listener) {
        GetBeautyImgListRequest request = new GetBeautyImgListRequest();
        request.setFrom(from);
        request.setLimit(limit);
        Api.getBeautyImgListByUser(request, listener);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        getDecorationImgList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDecorationImgList(FROM, Constant.HOME_PAGE_LIMIT, pullUpListener);
    }


    private ApiCallback<ApiResponse<BeautifulImageList>> pullDownListener = new
            ApiCallback<ApiResponse<BeautifulImageList>>() {


                @Override
                public void onPreLoad() {
                    if (isFirst) {
                        showWaitDialog();
                    }
                }

                @Override
                public void onHttpDone() {
                    decoration_img_listview.onRefreshComplete();
                    hideWaitDialog();
                }

                @Override
                public void onSuccess(ApiResponse<BeautifulImageList> apiResponse) {
                    mHasLoadedOnce = true;
                    BeautifulImageList decorationItemInfo = apiResponse.getData();
                    LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
                    if (null != decorationItemInfo) {
                        total = decorationItemInfo.getTotal();
                        beautyImgList.clear();
                        beautyImgList.addAll(decorationItemInfo.getBeautiful_images());
                        if (null != beautyImgList && beautyImgList.size() > 0) {
                            if (null == decorationImgAdapter) {
                                decorationImgAdapter = new DecorationAdapter(getActivity(), beautyImgList, new
                                        OnItemClickListener() {
                                            @Override
                                            public void OnItemClick(int position) {
                                                LogTool.d(TAG, "position:" + position);
                                                currentPos = position;
                                                LogTool.d(TAG, "currentPos====" + currentPos);
                                                BeautifulImage beautyImgInfo = beautyImgList.get(currentPos);
                                                LogTool.d(TAG, "beautyImgInfo:" + beautyImgInfo);
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
                                decoration_img_listview.setAdapter(decorationImgAdapter);
                            } else {
                                decorationImgAdapter.notifyDataSetChanged();
                            }
                            decoration_img_listview.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                            isFirst = false;
                        } else {
                            decoration_img_listview.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                        FROM = beautyImgList.size();
                        LogTool.d(TAG, "FROM:" + FROM);
                        errorLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailed(ApiResponse<BeautifulImageList> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                    decoration_img_listview.setVisibility(View.GONE);
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
                    decoration_img_listview.onRefreshComplete();
                }

                @Override
                public void onSuccess(ApiResponse<BeautifulImageList> apiResponse) {
                    BeautifulImageList decorationItemInfo = apiResponse.getData();
                    LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
                    if (null != decorationItemInfo) {
                        List<BeautifulImage> beautyList = decorationItemInfo.getBeautiful_images();
                        if (null != beautyList && beautyList.size() > 0) {
                            decorationImgAdapter.add(FROM, beautyList);
                            FROM += Constant.HOME_PAGE_LIMIT;
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
                    makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                }

            };

    public void onEventMainThread(CollectBeautyImageEvent collectBeautyImageEvent) {
        notifyChangeItemState(collectBeautyImageEvent.getImageid(), collectBeautyImageEvent.isCollect());
    }

    private void notifyChangeItemState(String imageid, boolean isCollect) {
        if (isCollect) return; //如果是收藏，则此处直接返回
        int removePos = -1;
        BeautifulImage beautyImgInfo = null;
        for (int i = 0; i < beautyImgList.size(); i++) {
            beautyImgInfo = beautyImgList.get(i);
            if (beautyImgInfo.get_id().equals(imageid)) {
                removePos = i;
            }
        }
        LogTool.d("notifyChangeItemState",removePos + "");
        if (removePos != -1) {
            decorationImgAdapter.remove(removePos);
            total = beautyImgList.size();
            if (total == 0) {
                decoration_img_listview.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
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
