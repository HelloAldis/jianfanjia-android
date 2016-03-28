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

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.SearchDesignerAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
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

    private void searchDesignerInfo(int from, String searchText, ApiUiUpdateListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        param.put("from", from);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDesigner(new SearchDesignerRequest(getContext(), param), listener, this);
    }

    @OnClick(R.id.error_include)
    protected void errorClick() {
        searchDesignerInfo(searchDesignerAdapter.getData().size(), search, listener);
    }

    private ApiUiUpdateListener listener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data.toString());
            MyFavoriteDesigner designer = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
            if (designer != null) {
                int total = designer.getTotal();
                if (total > 0) {
                    LogTool.d(this.getClass().getName(), "total size =" + total);
                    LogTool.d(this.getClass().getName(), "searchDesignerAdapter.getData().size() =" +
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
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            searchDesignerAdapter.setErrorViewShow();
            searchDesignerAdapter.setState(BaseRecycleAdapter.STATE_NETWORK_ERROR);
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_common;
    }
}
