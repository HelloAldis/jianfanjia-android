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
import com.jianfanjia.cn.adapter.SearchDecorationImgAdapter;
import com.jianfanjia.cn.adapter.base.BaseLoadingAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.DecorationItemInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchDecorationImgRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.SpacesItemDecoration;

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
public class SearchDecorationImgFragment extends BaseFragment implements ApiUiUpdateListener, RecyclerViewOnItemClickListener {
    private static final String TAG = SearchDecorationImgFragment.class.getName();

    public static final int PAGE_COUNT = 10;
    private RecyclerView decoration_img_listview = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private List<BeautyImgInfo> beautyImgList;
    private SearchDecorationImgAdapter decorationImgAdapter = null;
    private int currentPos = 0;
    private String search = null;

    @Override
    public void initView(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        ((TextView)emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_beautyimg));
        ((ImageView)emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_img);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        decoration_img_listview = (RecyclerView) view.findViewById(R.id.recycleview);
        decoration_img_listview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        decoration_img_listview.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        decoration_img_listview.addItemDecoration(decoration);

        search = getArguments().getString(Global.SEARCH_TEXT);
        getDecorationImgInfo(currentPos, PAGE_COUNT,search , this);
    }

    private void getDecorationImgInfo(int from, int limit, String searchText, ApiUiUpdateListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        param.put("from", from);
        param.put("limit", limit);
        JianFanJiaClient.searchDecorationImg(new SearchDecorationImgRequest(getContext(), param), listener, this);
    }

    @Override
    public void setListener() {
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getDecorationImgInfo(currentPos, PAGE_COUNT, search, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data.toString());
        DecorationItemInfo decorationItemInfo = JsonParser.jsonToBean(data.toString(), DecorationItemInfo.class);
        LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
        if (null != decorationItemInfo) {
            beautyImgList = decorationItemInfo.getBeautiful_images();
            if (null != beautyImgList && beautyImgList.size() > 0) {
                currentPos += beautyImgList.size();
                if (decorationImgAdapter == null) {
                    decorationImgAdapter = new SearchDecorationImgAdapter(getActivity(), decoration_img_listview, beautyImgList, PAGE_COUNT, new OnItemClickListener() {
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
//                        decorationBundle.putString(Global.HOUSE_SECTION, section);
//                        decorationBundle.putString(Global.HOUSE_STYLE, houseStyle);
//                        decorationBundle.putString(Global.DEC_STYLE, decStyle);
//                        decorationBundle.putInt(Global.TOTAL_COUNT, total);
                            decorationIntent.putExtras(decorationBundle);
                            startActivity(decorationIntent);
                        }
                    });
                    decorationImgAdapter.setOnLoadingListener(new BaseLoadingAdapter.OnLoadingListener() {
                        @Override
                        public void loading() {
                            getDecorationImgInfo(currentPos, PAGE_COUNT, search, SearchDecorationImgFragment.this);
                        }
                    });
                    decoration_img_listview.setAdapter(decorationImgAdapter);
                } else {
                    decorationImgAdapter.addAll(beautyImgList);
                }
                decoration_img_listview.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            } else {
                decoration_img_listview.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
            errorLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextShort(error_msg);
        decoration_img_listview.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnItemClick(View view, int position) {
        LogTool.d(TAG, "position:" + position);
        currentPos = position;
        BeautyImgInfo beautyImgInfo = beautyImgList.get(position);
        LogTool.d(TAG, "beautyImgInfo:" + beautyImgInfo);
        String decorationid = beautyImgInfo.get_id();
        Intent decorationIntent = new Intent(getActivity(), PreviewDecorationActivity.class);
        Bundle decorationBundle = new Bundle();
        decorationBundle.putString(Global.DECORATION_ID, decorationid);
        decorationIntent.putExtras(decorationBundle);
        startActivity(decorationIntent);
    }

    @Override
    public void OnViewClick(int position) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_decoration_img;
    }

}
