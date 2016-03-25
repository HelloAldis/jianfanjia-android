package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.beautifulpic.PreviewDecorationActivity;
import com.jianfanjia.cn.adapter.SearchDecorationImgAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
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

/**
 * @author fengliang
 * @ClassName: SearchDecorationImgFragment
 * @Description: 装修美图收藏
 * @date 2015-8-26 下午1:07:52
 */
public class SearchDecorationImgFragment extends BaseFragment {

    private static final String TAG = SearchDecorationImgFragment.class.getName();
    @Bind(R.id.recycleview)
    protected RecyclerView recyclerView;
    @Bind(R.id.empty_include)
    protected RelativeLayout emptyLayout;
    @Bind(R.id.empty_text)
    protected TextView emptyText;
    @Bind(R.id.empty_img)
    protected ImageView emptyImage;
    @Bind(R.id.error_include)
    protected RelativeLayout errorLayout;
    private SearchDecorationImgAdapter decorationAdapter = null;
    private String search = null;
    private int total = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    public void initView() {
        search = getArguments().getString(Global.SEARCH_TEXT);
        LogTool.d(TAG, "search=" + search);
        emptyText.setText(getString(R.string.search_no_beautyimg));
        emptyImage.setImageResource(R.mipmap.icon_img);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        decorationAdapter = new SearchDecorationImgAdapter(getContext(), recyclerView, new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                LogTool.d(TAG, "position=" + position);
                BeautyImgInfo beautyImgInfo = decorationAdapter.getData().get(position);
                LogTool.d(TAG, "beautyImgInfo:" + beautyImgInfo);
                Bundle decorationBundle = new Bundle();
                decorationBundle.putString(Global.DECORATION_ID, beautyImgInfo.get_id());
                decorationBundle.putInt(Global.POSITION, position);
                decorationBundle.putSerializable(Global.IMG_LIST, decorationAdapter.getData());
                decorationBundle.putInt(Global.TOTAL_COUNT, total);
                decorationBundle.putInt(Global.VIEW_TYPE, Constant.SEARCH_BEAUTY_FRAGMENT);
                decorationBundle.putString(Global.SEARCH_TEXT, search);
                startActivity(PreviewDecorationActivity.class, decorationBundle);
            }
        });
        decorationAdapter.setLoadMoreListener(new BaseRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                getDecorationImgInfo(decorationAdapter.getData().size(), search, listener);
            }
        });
        decorationAdapter.setErrorView(errorLayout);
        decorationAdapter.setEmptyView(emptyLayout);
        recyclerView.setAdapter(decorationAdapter);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        lp.leftMargin = MyApplication.dip2px(getContext().getApplicationContext(), 5);
        lp.rightMargin = lp.leftMargin;
        recyclerView.setLayoutParams(lp);

        SpacesItemDecoration decoration = new SpacesItemDecoration(MyApplication.dip2px(getContext()
                .getApplicationContext(), 5));
        recyclerView.addItemDecoration(decoration);
        getDecorationImgInfo(decorationAdapter.getData().size(), search, listener);
    }

    private void getDecorationImgInfo(int from, String searchText, ApiUiUpdateListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        param.put("from", from);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDecorationImg(new SearchDecorationImgRequest(getContext(), param), listener, this);
    }

    @OnClick(R.id.error_include)
    protected void errorClick() {
        getDecorationImgInfo(decorationAdapter.getData().size(), search, listener);
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
                if (total > 0) {
                    LogTool.d(this.getClass().getName(), "total size =" + total);
                    LogTool.d(this.getClass().getName(), "searchDesignerAdapter.getData().size() =" +
                            decorationAdapter.getData().size());
                    decorationAdapter.addData(decorationItemInfo.getBeautiful_images());
                    if (total > decorationAdapter.getData().size()) {
                        decorationAdapter.setState(BaseRecycleAdapter.STATE_LOAD_MORE);
                    } else {
                        decorationAdapter.setState(BaseRecycleAdapter.STATE_NO_MORE);
                    }
                    decorationAdapter.hideErrorAndEmptyView();
                } else {
                    decorationAdapter.setEmptyViewShow();
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            decorationAdapter.setErrorViewShow();
            decorationAdapter.setState(BaseRecycleAdapter.STATE_NETWORK_ERROR);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_common;
    }

}
