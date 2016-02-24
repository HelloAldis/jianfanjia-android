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
import com.jianfanjia.cn.adapter.DesignerListAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchDesignerRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengliang
 * @ClassName: CollectProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class SearchDesignerFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = SearchDesignerFragment.class.getName();
    private PullToRefreshRecycleView recycleView = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private DesignerListAdapter designerListAdapter = null;
    private List<DesignerInfo> designerList = new ArrayList<>();
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
        recycleView = (PullToRefreshRecycleView) view.findViewById(R.id.designer_listview);
        recycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setHasFixedSize(true);
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        recycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        searchDesignerInfo(FROM, search, listener);
    }

    private void searchDesignerInfo(int from, String searchText, ApiUiUpdateListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        param.put("from", from);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDesigner(new SearchDesignerRequest(getContext(), param), listener, this);
    }

    @Override
    public void setListener() {
        recycleView.setOnRefreshListener(this);
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                searchDesignerInfo(FROM, search, listener);
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
        searchDesignerInfo(FROM, search, pullUpListener);
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
                designerList.addAll(designer.getDesigners());
                if (null != designerList && designerList.size() > 0) {
                    designerListAdapter = new DesignerListAdapter(getActivity(), designerList, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {

                        }

                        @Override
                        public void OnViewClick(int position) {
                            String designerId = designerList.get(position).get_id();
                            LogTool.d(TAG, "designerId:" + designerId);
                            Intent designerIntent = new Intent(getActivity(), DesignerInfoActivity.class);
                            Bundle designerBundle = new Bundle();
                            designerBundle.putString(Global.DESIGNER_ID, designerId);
                            designerIntent.putExtras(designerBundle);
                            startActivity(designerIntent);
                        }
                    });
                    recycleView.setAdapter(designerListAdapter);
                    FROM = designerList.size();
                    LogTool.d(TAG, "FROM:" + FROM);
                    recycleView.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                } else {
                    recycleView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            recycleView.setVisibility(View.GONE);
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
            recycleView.onRefreshComplete();
            MyFavoriteDesigner designer = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
            if (designer != null) {
                List<DesignerInfo> designers = designer.getDesigners();
                if (null != designers && designers.size() > 0) {
                    designerListAdapter.add(FROM, designers);
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
            recycleView.onRefreshComplete();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_designer;
    }
}
