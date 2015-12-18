package com.jianfanjia.cn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.activity.beautifulpic.PreviewDecorationActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DecorationAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.DecorationItemInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.GetItemCallback;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.DecorationPopWindow;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.SpacesItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DecorationFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = DecorationFragment.class.getName();
    private static final int SECTION = 1;
    private static final int HOUSETYPE = 2;
    private static final int DECSTYLE = 3;
    private static final int NOT = 4;
    private MainHeadView mainHeadView = null;
    private LinearLayout topLayout = null;
    private RelativeLayout sectionLayout = null;
    private RelativeLayout houseTypeLayout = null;
    private RelativeLayout decStyleLayout = null;
    private PullToRefreshRecycleView decoration_listview = null;
    private StaggeredGridLayoutManager mLayoutManager = null;
    private DecorationAdapter decorationAdapter = null;
    private DecorationPopWindow window = null;
    private List<BeautyImgInfo> beautyImgList = new ArrayList<BeautyImgInfo>();
    private String section = null;
    private String houseStyle = null;
    private String decStyle = null;

    @Override
    public void initView(View view) {
        initMainHeadView(view);
        topLayout = (LinearLayout) view.findViewById(R.id.topLayout);
        sectionLayout = (RelativeLayout) view.findViewById(R.id.sectionLayout);
        houseTypeLayout = (RelativeLayout) view.findViewById(R.id.houseTypeLayout);
        decStyleLayout = (RelativeLayout) view.findViewById(R.id.decStyleLayout);
        decoration_listview = (PullToRefreshRecycleView) view.findViewById(R.id.decoration_listview);
        decoration_listview.setMode(PullToRefreshBase.Mode.BOTH);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        decoration_listview.setLayoutManager(mLayoutManager);
        decoration_listview.setItemAnimator(new DefaultItemAnimator());
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        decoration_listview.addItemDecoration(decoration);
        searchDecorationImg(section, houseStyle, decStyle, 0, 8, getDecorationImgListener);
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
        sectionLayout.setOnClickListener(this);
        houseTypeLayout.setOnClickListener(this);
        decStyleLayout.setOnClickListener(this);
        decoration_listview.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sectionLayout:
                setSelectState(SECTION);
                break;
            case R.id.houseTypeLayout:
                setSelectState(HOUSETYPE);
                break;
            case R.id.decStyleLayout:
                setSelectState(DECSTYLE);
                break;
            default:
                break;
        }
    }

    private void setSelectState(int type) {
        switch (type) {
            case SECTION:
                showWindow(R.array.section_item, SECTION);
                sectionLayout.setSelected(true);
                houseTypeLayout.setSelected(false);
                decStyleLayout.setSelected(false);
                break;
            case HOUSETYPE:
                showWindow(R.array.housetype_item, HOUSETYPE);
                sectionLayout.setSelected(false);
                houseTypeLayout.setSelected(true);
                decStyleLayout.setSelected(false);
                break;
            case DECSTYLE:
                showWindow(R.array.decstyle_item, DECSTYLE);
                sectionLayout.setSelected(false);
                houseTypeLayout.setSelected(false);
                decStyleLayout.setSelected(true);
                break;
            case NOT:
                sectionLayout.setSelected(false);
                houseTypeLayout.setSelected(false);
                decStyleLayout.setSelected(false);
            default:
                break;
        }
    }

    private void searchDecorationImg(String section, String houseStyle, String decStyle, int from, int limit, ApiUiUpdateListener listener) {
        LogTool.d(TAG, "section:" + section + " houseStyle:" + houseStyle + " decStyle:" + decStyle);
        JianFanJiaClient.searchDecorationImg(getContext(), section, houseStyle, decStyle, "", -1, from, limit, listener, this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        decoration_listview.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        decoration_listview.onRefreshComplete();
    }

    private ApiUiUpdateListener getDecorationImgListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            DecorationItemInfo decorationItemInfo = JsonParser.jsonToBean(data.toString(), DecorationItemInfo.class);
            LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
            if (null != decorationItemInfo) {
                beautyImgList.addAll(decorationItemInfo.getBeautiful_images());
                LogTool.d(TAG, "beautyImgList:" + beautyImgList);
                decorationAdapter = new DecorationAdapter(getActivity(), beautyImgList, new OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        BeautyImgInfo beautyImgInfo = beautyImgList.get(position);
                        LogTool.d(TAG, "beautyImgInfo:" + beautyImgInfo);
                        Intent decorationIntent = new Intent(getActivity(), PreviewDecorationActivity.class);
                        Bundle decorationBundle = new Bundle();
                        decorationBundle.putString(Global.DECORATION_ID, beautyImgInfo.get_id());
                        decorationIntent.putExtras(decorationBundle);
                        startActivity(decorationIntent);
                    }
                });
                LogTool.d(TAG, "decorationAdapter:" + decorationAdapter);
                decoration_listview.setAdapter(decorationAdapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    private ApiUiUpdateListener getDecorationBySearchListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            DecorationItemInfo decorationItemInfo = JsonParser.jsonToBean(data.toString(), DecorationItemInfo.class);
            LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
            if (null != decorationItemInfo) {
                beautyImgList.clear();
                beautyImgList.addAll(decorationItemInfo.getBeautiful_images());
                LogTool.d(TAG, "beautyImgList:" + beautyImgList);
                decorationAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {

        }

        @Override
        public void loadFailture(String error_msg) {

        }
    };

    private ApiUiUpdateListener pullUpListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {

        }

        @Override
        public void loadFailture(String error_msg) {

        }
    };

    private void showWindow(int resId, int type) {
        switch (type) {
            case SECTION:
                window = new DecorationPopWindow(getActivity(), resId, getSectionCallback, Global.SECTION_POSITION);
                break;
            case HOUSETYPE:
                window = new DecorationPopWindow(getActivity(), resId, getHouseStyleCallback, Global.HOUSE_TYPE_POSITION);
                break;
            case DECSTYLE:
                window = new DecorationPopWindow(getActivity(), resId, getDecStyleCallback, Global.DEC_STYLE_POSITION);
                break;
            default:
                break;
        }
        window.show(topLayout);
    }

    private GetItemCallback getSectionCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            Global.SECTION_POSITION = position;
            section = title;
            searchDecorationImg(section, houseStyle, decStyle, 0, 8, getDecorationBySearchListener);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }
    };
    private GetItemCallback getHouseStyleCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            Global.HOUSE_TYPE_POSITION = position;
            houseStyle = title;
            searchDecorationImg(section, houseStyle, decStyle, 0, 8, getDecorationBySearchListener);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }
    };
    private GetItemCallback getDecStyleCallback = new GetItemCallback() {
        @Override
        public void onItemCallback(int position, String title) {
            Global.DEC_STYLE_POSITION = position;
            decStyle = title;
            searchDecorationImg(section, houseStyle, decStyle, 0, 8, getDecorationBySearchListener);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }

        @Override
        public void onDismissCallback() {
            setSelectState(NOT);
            if (null != window) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decoration;
    }

}
