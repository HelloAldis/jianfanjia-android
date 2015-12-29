package com.jianfanjia.cn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.beautifulpic.PreviewDecorationActivity;
import com.jianfanjia.cn.adapter.DecorationImgAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.DecorationItemInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author fengliang
 * @ClassName: DecorationImgFragment
 * @Description: 装修美图收藏
 * @date 2015-8-26 下午1:07:52
 */
public class DecorationImgFragment extends BaseFragment implements ApiUiUpdateListener, RecyclerViewOnItemClickListener {
    private static final String TAG = DecorationImgFragment.class.getName();
    private RecyclerView decoration_img_listview = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private List<BeautyImgInfo> beautyImgList = new ArrayList<BeautyImgInfo>();
    private DecorationImgAdapter decorationImgAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        decoration_img_listview = (RecyclerView) view.findViewById(R.id.decoration_img_listview);
        decoration_img_listview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        decoration_img_listview.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        decoration_img_listview.addItemDecoration(decoration);
        getDecorationImgList();
    }

    @Override
    public void setListener() {
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getDecorationImgList();
                break;
            default:
                break;
        }
    }

    private void getDecorationImgList() {
        JianFanJiaClient.getBeautyImgListByUser(getActivity(), 0, 100, this, this);
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
                decorationImgAdapter = new DecorationImgAdapter(getActivity(), beautyImgList, this);
                decoration_img_listview.setAdapter(decorationImgAdapter);
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
        makeTextLong(error_msg);
        decoration_img_listview.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnItemClick(View view, int position) {
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

    public void onEventMainThread(MessageEvent event) {
        switch (event.getEventType()) {
            case Constant.UPDATE_BEAUTY_FRAGMENT:
                getDecorationImgList();
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
