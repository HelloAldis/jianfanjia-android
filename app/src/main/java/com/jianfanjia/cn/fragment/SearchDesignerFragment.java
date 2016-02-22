package com.jianfanjia.cn.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.SearchDesignerAdapter;
import com.jianfanjia.cn.adapter.base.BaseLoadingAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerInfo;
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
import java.util.List;
import java.util.Map;

/**
 * @author fengliang
 * @ClassName: ProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class SearchDesignerFragment extends BaseFragment implements ApiUiUpdateListener {

    private static final String TAG = SearchDesignerFragment.class.getName();

    public static final int PAGE_COUNT = 10;
    private RecyclerView recycleView = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private SearchDesignerAdapter searchDesignerAdapter = null;
    private int currentPos = 0;
    private String search = null;

    @Override
    public void initView(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        recycleView = (RecyclerView) view.findViewById(R.id.recycleview);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        recycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());

        search = getArguments().getString(Global.SEARCH_TEXT);
        searchDesignerInfo(currentPos, search);
    }

    private void searchDesignerInfo(int from, String searchText) {
        Map<String, Object> param = new HashMap<>();
        param.put("search_word", searchText);
        param.put("limit", PAGE_COUNT);
        param.put("from", from);
        JianFanJiaClient.searchDesigner(new SearchDesignerRequest(getContext(), param), this, this);
    }

    @Override
    public void setListener() {
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                searchDesignerInfo(currentPos, search);
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
        LogTool.d(TAG, "data=" + data.toString());
        MyFavoriteDesigner myFavoriteDesigner = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
        if (myFavoriteDesigner != null) {
            final List<DesignerInfo> designerInfoList = myFavoriteDesigner.getDesigners();
            if (designerInfoList != null && designerInfoList.size() > 0) {
                currentPos += designerInfoList.size();
                if (searchDesignerAdapter == null) {
                    searchDesignerAdapter = new SearchDesignerAdapter(getContext(), recycleView, designerInfoList, PAGE_COUNT ,new RecyclerViewOnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {

                        }

                        @Override
                        public void OnViewClick(int position) {
                            String designerId = designerInfoList.get(position).get_id();
                            LogTool.d(TAG, "designerId:" + designerId);
                            Intent designerIntent = new Intent(getContext(), DesignerInfoActivity.class);
                            Bundle designerBundle = new Bundle();
                            designerBundle.putString(Global.DESIGNER_ID, designerId);
                            designerIntent.putExtras(designerBundle);
                            startActivity(designerIntent);
                        }
                    });
                    searchDesignerAdapter.setOnLoadingListener(new BaseLoadingAdapter.OnLoadingListener() {
                        @Override
                        public void loading() {
                            searchDesignerInfo(currentPos, search);
                        }
                    });
                    recycleView.setAdapter(searchDesignerAdapter);
                } else {
                    searchDesignerAdapter.addAll(designerInfoList);
                }
                recycleView.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            } else {
                recycleView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
        }
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextShort(error_msg);
        recycleView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_designer;
    }
}
