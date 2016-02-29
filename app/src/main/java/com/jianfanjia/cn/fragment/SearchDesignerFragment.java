package com.jianfanjia.cn.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.SearchDesignerAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchDesignerRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengliang
 * @ClassName: CollectProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class SearchDesignerFragment extends BaseFragment {
    private static final String TAG = SearchDesignerFragment.class.getName();
    private RecyclerView recyclerView = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private SearchDesignerAdapter searchDesignerAdapter = null;
    private int FROM = 0;
    private String search = null;

    @Override
    public void initView(View view) {
        search = getArguments().getString(Global.SEARCH_TEXT);
        LogTool.d(TAG, "search=" + search);
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_designer));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_designer);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        searchDesignerAdapter = new SearchDesignerAdapter(getContext(), recyclerView, new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

            }

            @Override
            public void OnViewClick(int position) {
                String designerId = searchDesignerAdapter.getData().get(position).get_id();
                LogTool.d(TAG, "designerId:" + designerId);
                Intent designerIntent = new Intent(getActivity(), DesignerInfoActivity.class);
                Bundle designerBundle = new Bundle();
                designerBundle.putString(Global.DESIGNER_ID, designerId);
                designerIntent.putExtras(designerBundle);
                startActivity(designerIntent);
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
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        searchDesignerInfo(searchDesignerAdapter.getData().size(), search, listener);
    }

    private void searchDesignerInfo(int from, String searchText, ApiUiUpdateListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        param.put("from", from);
        param.put("limit", 10);
        JianFanJiaClient.searchDesigner(new SearchDesignerRequest(getContext(), param), listener, this);
    }

    @Override
    public void setListener() {
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                searchDesignerInfo(searchDesignerAdapter.getData().size(), search, listener);
                break;
            default:
                break;
        }
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
                    LogTool.d(this.getClass().getName(), "searchDesignerAdapter.getData().size() =" + searchDesignerAdapter.getData().size());
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
            searchDesignerAdapter.notifyDataSetChanged();
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_designer;
    }
}
