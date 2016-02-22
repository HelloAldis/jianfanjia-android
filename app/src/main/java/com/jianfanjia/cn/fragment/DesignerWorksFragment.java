package com.jianfanjia.cn.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.adapter.DesignerWorksAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerWorksInfo;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchDesignerProductRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ScrollableHelper;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengliang
 * @ClassName: DesignerWorksFragment
 * @Description: 设计师作品
 * @date 2015-8-26 下午1:07:52
 */

public class DesignerWorksFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView>, ScrollableHelper.ScrollableContainer {
    private static final String TAG = DesignerWorksFragment.class.getName();
    private PullToRefreshRecycleView designer_works_listview = null;
    private DesignerWorksAdapter adapter = null;
    private List<Product> productList = new ArrayList<Product>();
    private String designerid = null;
    private int FROM = 0;

    public static DesignerWorksFragment newInstance(String info) {
        Bundle args = new Bundle();
        DesignerWorksFragment workFragment = new DesignerWorksFragment();
        args.putString(Global.DESIGNER_ID, info);
        workFragment.setArguments(args);
        return workFragment;
    }

    @Override
    public void initView(View view) {
        Bundle bundle = getArguments();
        designerid = bundle.getString(Global.DESIGNER_ID);
        LogTool.d(TAG, "designerid:" + designerid);
        designer_works_listview = (PullToRefreshRecycleView) view.findViewById(R.id.designer_works_listview);
        designer_works_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        designer_works_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        designer_works_listview.setItemAnimator(new DefaultItemAnimator());
        designer_works_listview.setHasFixedSize(true);
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        designer_works_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        designer_works_listview.setFocusable(false);
        getDesignerProduct(designerid, FROM, listener);
    }

    @Override
    public void setListener() {
        designer_works_listview.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDesignerProduct(designerid, FROM, pullUpListener);
    }

    private void getDesignerProduct(String designerid, int from, ApiUiUpdateListener listener) {
        Map<String, Object> conditionParam = new HashMap<>();
        conditionParam.put("designerid", designerid);
        Map<String, Object> param = new HashMap<>();
        param.put("query", conditionParam);
        param.put("from", from);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDesignerProduct(new SearchDesignerProductRequest(getActivity(), param), listener, this);
    }

    private ApiUiUpdateListener listener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            designer_works_listview.onRefreshComplete();
            DesignerWorksInfo worksInfo = JsonParser.jsonToBean(data.toString(), DesignerWorksInfo.class);
            LogTool.d(TAG, "worksInfo :" + worksInfo);
            if (null != worksInfo) {
                productList.addAll(worksInfo.getProducts());
                adapter = new DesignerWorksAdapter(getActivity(), productList, new OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        Product product = productList.get(position);
                        String productid = product.get_id();
                        LogTool.d(TAG, "productid:" + productid);
                        Bundle productBundle = new Bundle();
                        productBundle.putString(Global.PRODUCT_ID, productid);
                        startActivity(DesignerCaseInfoActivity.class, productBundle);
                    }
                });
                designer_works_listview.setAdapter(adapter);
                FROM = productList.size();
                LogTool.d(TAG, "FROM:" + FROM);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextShort(error_msg);
            designer_works_listview.onRefreshComplete();
        }
    };

    private ApiUiUpdateListener pullUpListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            designer_works_listview.onRefreshComplete();
            DesignerWorksInfo worksInfo = JsonParser.jsonToBean(data.toString(), DesignerWorksInfo.class);
            LogTool.d(TAG, "worksInfo :" + worksInfo);
            if (null != worksInfo) {
                List<Product> products = worksInfo.getProducts();
                if (null != products && products.size() > 0) {
                    adapter.add(FROM, products);
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
            designer_works_listview.onRefreshComplete();
        }
    };

    @Override
    public View getScrollableView() {
        return designer_works_listview.getRefreshableView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_designer_works;
    }

}
