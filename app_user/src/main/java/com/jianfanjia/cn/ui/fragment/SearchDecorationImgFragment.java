package com.jianfanjia.cn.ui.fragment;

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

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.ui.adapter.SearchDecorationImgAdapter;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.BeautifulImage;
import com.jianfanjia.api.model.BeautifulImageList;
import com.jianfanjia.api.request.guest.SearchDecorationImgRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.ui.activity.beautifulpic.PreviewDecorationActivity;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.interf.OnItemClickListener;
import com.jianfanjia.cn.view.recycleview.itemdecoration.SpacesItemDecoration;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * @author fengliang
 * @ClassName: SearchDecorationImgFragment
 * @Description: 装修美图收藏
 * @date 2015-8-26 下午1:07:52
 */
public class SearchDecorationImgFragment extends BaseFragment {
    private static final String TAG = SearchDecorationImgFragment.class.getName();

    @Bind(R.id.recycleview)
    RecyclerView recyclerView;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.empty_text)
    TextView emptyText;

    @Bind(R.id.empty_img)
    ImageView emptyImage;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private SearchDecorationImgAdapter decorationAdapter = null;
    private String search = null;
    private int total = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search = getArguments().getString(IntentConstant.SEARCH_TEXT);
        LogTool.d("search=" + search);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        emptyText.setText(getString(R.string.search_no_beautyimg));
        emptyImage.setImageResource(R.mipmap.icon_img);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        decorationAdapter = new SearchDecorationImgAdapter(getContext(), recyclerView, new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                LogTool.d("position=" + position);
                BeautifulImage beautyImgInfo = decorationAdapter.getData().get(position);
                LogTool.d("beautyImgInfo:" + beautyImgInfo);
                Bundle decorationBundle = new Bundle();
                decorationBundle.putString(IntentConstant.DECORATION_BEAUTY_IAMGE_ID, beautyImgInfo.get_id());
                decorationBundle.putInt(IntentConstant.POSITION, position);
                decorationBundle.putSerializable(IntentConstant.IMG_LIST, decorationAdapter.getData());
                decorationBundle.putInt(IntentConstant.TOTAL_COUNT, total);
                decorationBundle.putInt(IntentConstant.VIEW_TYPE, IntentConstant.SEARCH_BEAUTY_FRAGMENT);
                decorationBundle.putString(IntentConstant.SEARCH_TEXT, search);
                startActivity(PreviewDecorationActivity.class, decorationBundle);
            }
        });
        decorationAdapter.setLoadMoreListener(new BaseLoadMoreRecycleAdapter.LoadMoreListener() {
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
        lp.leftMargin = TDevice.dip2px(getContext().getApplicationContext(), 5);
        lp.rightMargin = lp.leftMargin;
        recyclerView.setLayoutParams(lp);

        SpacesItemDecoration decoration = new SpacesItemDecoration(TDevice.dip2px(getContext()
                .getApplicationContext(), 5));
        recyclerView.addItemDecoration(decoration);
        getDecorationImgInfo(decorationAdapter.getData().size(), search, listener);
    }

    private void getDecorationImgInfo(int from, String searchText, ApiCallback<ApiResponse<BeautifulImageList>>
            listener) {
        SearchDecorationImgRequest request = new SearchDecorationImgRequest();
        request.setSearch_word(searchText);
        request.setFrom(from);
        request.setLimit(Constant.HOME_PAGE_LIMIT);
        Api.searchDecorationImg(request, listener,this);
    }

    @OnClick(R.id.error_include)
    protected void errorClick() {
        getDecorationImgInfo(decorationAdapter.getData().size(), search, listener);
    }

    private ApiCallback<ApiResponse<BeautifulImageList>> listener = new ApiCallback<ApiResponse<BeautifulImageList>>() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<BeautifulImageList> apiResponse) {
            BeautifulImageList BeautifulImageList = apiResponse.getData();

            LogTool.d("BeautifulImageList:" + BeautifulImageList);
            if (null != BeautifulImageList) {
                total = BeautifulImageList.getTotal();
                if (total > 0) {
                    LogTool.d("total size =" + total);
                    LogTool.d("searchDesignerAdapter.getData().size() =" +
                            decorationAdapter.getData().size());
                    decorationAdapter.addData(BeautifulImageList.getBeautiful_images());
                    if (total > decorationAdapter.getData().size()) {
                        decorationAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_LOAD_MORE);
                    } else {
                        decorationAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NO_MORE);
                    }
                    decorationAdapter.hideErrorAndEmptyView();
                } else {
                    decorationAdapter.setEmptyViewShow();
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
            decorationAdapter.setErrorViewShow();
            decorationAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
        }

    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_common;
    }

}
