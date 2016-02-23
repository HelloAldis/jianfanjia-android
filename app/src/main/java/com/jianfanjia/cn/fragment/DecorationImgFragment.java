package com.jianfanjia.cn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.beautifulpic.PreviewDecorationActivity;
import com.jianfanjia.cn.adapter.DecorationAdapter;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.DecorationItemInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.SpacesItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author fengliang
 * @ClassName: DecorationImgFragment
 * @Description: 装修美图收藏
 * @date 2015-8-26 下午1:07:52
 */
public class DecorationImgFragment extends CollectFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = DecorationImgFragment.class.getName();
    private PullToRefreshRecycleView decoration_img_listview = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private List<BeautyImgInfo> beautyImgList = new ArrayList<>();
    private DecorationAdapter decorationImgAdapter = null;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private int total = 0;
    private int currentPos = -1;
    private int FROM = 0;

    public static DecorationImgFragment newInstance() {
        DecorationImgFragment imgFragment = new DecorationImgFragment();
        return imgFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decoration_img, container, false);
        init(view);
        isPrepared = true;
        load();
        return view;
    }

    public void init(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        decoration_img_listview = (PullToRefreshRecycleView) view.findViewById(R.id.decoration_img_listview);
        decoration_img_listview.setMode(PullToRefreshBase.Mode.BOTH);
        decoration_img_listview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        decoration_img_listview.setHasFixedSize(true);
        decoration_img_listview.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(6);
        decoration_img_listview.addItemDecoration(decoration);
    }

    @Override
    protected void load() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getDecorationImgList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @Override
    public void setListener() {
        decoration_img_listview.setOnRefreshListener(this);
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getDecorationImgList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
                break;
            default:
                break;
        }
    }

    private void getDecorationImgList(int from, int limit, ApiUiUpdateListener listener) {
        JianFanJiaClient.getBeautyImgListByUser(getActivity(), from, limit, listener, this);
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


    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            mHasLoadedOnce = true;
            decoration_img_listview.onRefreshComplete();
            DecorationItemInfo decorationItemInfo = JsonParser.jsonToBean(data.toString(), DecorationItemInfo.class);
            LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
            if (null != decorationItemInfo) {
                total = decorationItemInfo.getTotal();
                beautyImgList.clear();
                beautyImgList.addAll(decorationItemInfo.getBeautiful_images());
                if (null != beautyImgList && beautyImgList.size() > 0) {
                    if (null == decorationImgAdapter) {
                        decorationImgAdapter = new DecorationAdapter(getActivity(), beautyImgList, new OnItemClickListener() {
                            @Override
                            public void OnItemClick(int position) {
                                LogTool.d(TAG, "position:" + position);
                                currentPos = position;
                                BeautyImgInfo beautyImgInfo = beautyImgList.get(position);
                                LogTool.d(TAG, "beautyImgInfo:" + beautyImgInfo);
                                Intent decorationIntent = new Intent(getActivity(), PreviewDecorationActivity.class);
                                Bundle decorationBundle = new Bundle();
                                decorationBundle.putString(Global.DECORATION_ID, beautyImgInfo.get_id());
                                decorationBundle.putInt(Global.POSITION, position);
                                decorationBundle.putSerializable(Global.IMG_LIST, (ArrayList<BeautyImgInfo>) beautyImgList);
                                decorationBundle.putInt(Global.TOTAL_COUNT, total);
                                decorationIntent.putExtras(decorationBundle);
                                startActivity(decorationIntent);
                            }
                        });
                        decoration_img_listview.setAdapter(decorationImgAdapter);
                    } else {
                        decorationImgAdapter.notifyDataSetChanged();
                    }
                    decoration_img_listview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
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
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            decoration_img_listview.onRefreshComplete();
            decoration_img_listview.setVisibility(View.GONE);
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
            decoration_img_listview.onRefreshComplete();
            DecorationItemInfo decorationItemInfo = JsonParser.jsonToBean(data.toString(), DecorationItemInfo.class);
            LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
            if (null != decorationItemInfo) {
                List<BeautyImgInfo> beautyList = decorationItemInfo.getBeautiful_images();
                if (null != beautyList && beautyList.size() > 0) {
                    decorationImgAdapter.add(FROM, beautyList);
                    FROM += Constant.HOME_PAGE_LIMIT;
                } else {
                    makeTextShort(getResources().getString(R.string.no_more_data));
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            decoration_img_listview.onRefreshComplete();
        }
    };

    public void onEventMainThread(MessageEvent event) {
        switch (event.getEventType()) {
            case Constant.UPDATE_BEAUTY_FRAGMENT:
                decorationImgAdapter.remove(currentPos);
                total = beautyImgList.size();
                if (total == 0) {
                    decoration_img_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
                break;
            case Constant.UPDATE_BEAUTY_IMG_FRAGMENT:
                LogTool.d(TAG, "88888888888888888888888");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decoration_img;
    }

}
