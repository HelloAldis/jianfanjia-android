package com.jianfanjia.cn.fragment;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DecorationAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.DecorationItemInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.List;

/**
 * Description:装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DecorationFragment extends BaseFragment implements ApiUiUpdateListener {
    private static final String TAG = DecorationFragment.class.getName();
    private MainHeadView mainHeadView = null;
    private PullToRefreshRecycleView decoration_listview = null;
    private StaggeredGridLayoutManager mLayoutManager = null;
    private DecorationAdapter decorationAdapter = null;

    @Override
    public void initView(View view) {
        initMainHeadView(view);
        decoration_listview = (PullToRefreshRecycleView) view.findViewById(R.id.decoration_listview);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        decoration_listview.setLayoutManager(mLayoutManager);
        searchDecorationImg();
    }

    private void initMainHeadView(View view) {
        mainHeadView = (MainHeadView) view.findViewById(R.id.dec_head);
        mainHeadView.setMianTitle(getResources().getString(R.string.decoration_img));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.GONE);
    }

    @Override
    public void setListener() {

    }

    private void searchDecorationImg() {
        JianFanJiaClient.searchDecorationImg(getContext(), "不限", "不限", "不限", "", -1, 0, 5, this, this);
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
            List<BeautyImgInfo> beautyImgList = decorationItemInfo.getBeautiful_images();
            LogTool.d(TAG, "beautyImgList:" + beautyImgList);
            decorationAdapter = new DecorationAdapter(getActivity(), beautyImgList, new OnItemClickListener() {
                @Override
                public void OnItemClick(int position) {
                    LogTool.d(TAG, "position:" + position);
                }
            });
            decoration_listview.setAdapter(decorationAdapter);
        }
    }

    @Override
    public void loadFailture(String error_msg) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decoration;
    }
}
