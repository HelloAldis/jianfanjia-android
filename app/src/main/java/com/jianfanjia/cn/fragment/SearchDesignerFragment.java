package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.guest.SearchDesignerRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.SearchDesignerAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author fengliang
 * @ClassName: SearchDesignerFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class SearchDesignerFragment extends BaseFragment {
    private static final String TAG = SearchDesignerFragment.class.getName();

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

    private SearchDesignerAdapter searchDesignerAdapter = null;
    private int FROM = 0;
    private String search = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search = getArguments().getString(Global.SEARCH_TEXT);
        LogTool.d(TAG, "search=" + search);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        emptyText.setText(getString(R.string.search_no_designer));
        emptyImage.setImageResource(R.mipmap.icon_designer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        searchDesignerAdapter = new SearchDesignerAdapter(getContext(), recyclerView, new
                RecyclerViewOnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {

                    }

                    @Override
                    public void OnViewClick(int position) {
                        String designerId = searchDesignerAdapter.getData().get(position).get_id();
                        LogTool.d(TAG, "designerId:" + designerId);
                        Bundle designerBundle = new Bundle();
                        designerBundle.putString(Global.DESIGNER_ID, designerId);
                        startActivity(DesignerInfoActivity.class, designerBundle);
                    }
                });
        searchDesignerAdapter.setLoadMoreListener(new BaseRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                searchDesignerInfo(searchDesignerAdapter.getData().size(), search, listener);
            }
        });
        searchDesignerAdapter.setEmptyView(emptyLayout);
        searchDesignerAdapter.setErrorView(errorLayout);
        recyclerView.setAdapter(searchDesignerAdapter);
        recyclerView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity().getApplicationContext()));
        searchDesignerInfo(searchDesignerAdapter.getData().size(), search, listener);
    }

    private void searchDesignerInfo(int from, String searchText, ApiCallback<ApiResponse<MyFavoriteDesigner>> listener) {
        SearchDesignerRequest request = new SearchDesignerRequest();
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        request.setQuery(param);
        request.setFrom(from);
        request.setLimit(Constant.HOME_PAGE_LIMIT);
        Api.searchDesigner(request, listener);
    }

    @OnClick(R.id.error_include)
    protected void errorClick() {
        searchDesignerInfo(searchDesignerAdapter.getData().size(), search, listener);
    }

    private ApiCallback<ApiResponse<MyFavoriteDesigner>> listener = new ApiCallback<ApiResponse<MyFavoriteDesigner>>() {

        @Override
        public void onPreLoad() {

        }

        @Override
        public void onHttpDone() {

        }

        @Override
        public void onSuccess(ApiResponse<MyFavoriteDesigner> apiResponse) {
            MyFavoriteDesigner designer = apiResponse.getData();
            if (designer != null) {
                int total = designer.getTotal();
                if (total > 0) {
                    LogTool.d(TAG, "total size =" + total);
                    LogTool.d(TAG, "searchDesignerAdapter.getData().size() =" +
                            searchDesignerAdapter.getData().size());
                    searchDesignerAdapter.addData(designer.getDesigners());
                    if (total > searchDesignerAdapter.getData().size()) {
                        searchDesignerAdapter.setState(BaseRecycleAdapter.STATE_LOAD_MORE);
                    } else {
                        searchDesignerAdapter.setState(BaseRecycleAdapter.STATE_NO_MORE);
                    }
                    searchDesignerAdapter.hideErrorAndEmptyView();
                } else {
                    searchDesignerAdapter.setEmptyViewShow();
                }

            }
        }

        @Override
        public void onFailed(ApiResponse<MyFavoriteDesigner> apiResponse) {
            searchDesignerAdapter.setErrorViewShow();
            searchDesignerAdapter.setState(BaseRecycleAdapter.STATE_NETWORK_ERROR);
        }

        @Override
        public void onNetworkError(int code) {

        }

    };


    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_common;
    }
}
