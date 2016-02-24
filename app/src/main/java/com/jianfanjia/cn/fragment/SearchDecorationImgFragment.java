package com.jianfanjia.cn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.beautifulpic.PreviewDecorationActivity;
import com.jianfanjia.cn.adapter.DecorationAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.DecorationItemInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchDecorationImgRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.SpacesItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengliang
 * @ClassName: CollectDecorationImgFragment
 * @Description: 装修美图收藏
 * @date 2015-8-26 下午1:07:52
 */
public class SearchDecorationImgFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = SearchDecorationImgFragment.class.getName();
    private PullToRefreshRecycleView decoration_img_listview = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private List<BeautyImgInfo> beautyImgList = new ArrayList<>();
    private DecorationAdapter decorationAdapter = null;
    private int FROM = 0;
    private String search = null;
    private int total = 0;

    @Override
    public void initView(View view) {
        search = getArguments().getString(Global.SEARCH_TEXT);
        LogTool.d(TAG, "search=" + search);
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_beautyimg));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_img);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        decoration_img_listview = (PullToRefreshRecycleView) view.findViewById(R.id.beauty_listview);
        decoration_img_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        decoration_img_listview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        decoration_img_listview.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        decoration_img_listview.addItemDecoration(decoration);
        getDecorationImgInfo(FROM, search, listener);
    }

    private void getDecorationImgInfo(int from, String searchText, ApiUiUpdateListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        param.put("from", from);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDecorationImg(new SearchDecorationImgRequest(getContext(), param), listener, this);
    }

    @Override
    public void setListener() {
        errorLayout.setOnClickListener(this);
        decoration_img_listview.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getDecorationImgInfo(FROM, search, listener);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDecorationImgInfo(FROM, search, pullUpListener);
    }

    private ApiUiUpdateListener listener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data.toString());
            DecorationItemInfo decorationItemInfo = JsonParser.jsonToBean(data.toString(), DecorationItemInfo.class);
            LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
            if (null != decorationItemInfo) {
                total = decorationItemInfo.getTotal();
                beautyImgList.addAll(decorationItemInfo.getBeautiful_images());
                if (null != beautyImgList && beautyImgList.size() > 0) {
                    decorationAdapter = new DecorationAdapter(getActivity(), beautyImgList, new OnItemClickListener() {
                        @Override
                        public void OnItemClick(int position) {
                            LogTool.d(TAG, "position=" + position);
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
                    decoration_img_listview.setAdapter(decorationAdapter);
                    FROM = beautyImgList.size();
                    LogTool.d(TAG, "FROM:" + FROM);
                    decoration_img_listview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                } else {
                    decoration_img_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
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
            decoration_img_listview.onRefreshComplete();
            DecorationItemInfo decorationItemInfo = JsonParser.jsonToBean(data.toString(), DecorationItemInfo.class);
            LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
            if (null != decorationItemInfo) {
                List<BeautyImgInfo> beautyImgs = decorationItemInfo.getBeautiful_images();
                if (null != beautyImgs && beautyImgs.size() > 0) {
                    decorationAdapter.add(FROM, beautyImgs);
                    FROM += Constant.HOME_PAGE_LIMIT;
                    LogTool.d(TAG, "FROM=" + FROM);
                } else {
                    makeTextShort(getResources().getString(R.string.no_more_data));
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            decoration_img_listview.onRefreshComplete();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_decoration_img;
    }

}
