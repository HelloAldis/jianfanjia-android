package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.adapter.DesignerWorksAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.api.request.guest.SearchDesignerProductRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ScrollableHelper;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author fengliang
 * @ClassName: DesignerProductFragment
 * @Description: 设计师作品
 * @date 2015-8-26 下午1:07:52
 */

public class DesignerProductFragment extends BaseFragment implements PullToRefreshBase
        .OnRefreshListener2<RecyclerView>, ScrollableHelper.ScrollableContainer {
    private static final String TAG = DesignerProductFragment.class.getName();

    @Bind(R.id.designer_works_listview)
    PullToRefreshRecycleView designer_works_listview;

    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private DesignerWorksAdapter adapter = null;
    private List<Product> productList = new ArrayList<Product>();
    private String designerid = null;
    private int FROM = 0;

    private boolean isUp = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    public static DesignerProductFragment newInstance(String info) {
        Bundle args = new Bundle();
        DesignerProductFragment productFragment = new DesignerProductFragment();
        args.putString(Global.DESIGNER_ID, info);
        productFragment.setArguments(args);
        return productFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        designerid = bundle.getString(Global.DESIGNER_ID);
        LogTool.d(TAG, "designerid:" + designerid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.d(TAG, "onCreateView()");
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        isPrepared = true;
        load();
        return view;
    }

    private void initView() {
        designer_works_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        designer_works_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        designer_works_listview.setItemAnimator(new DefaultItemAnimator());
        designer_works_listview.setHasFixedSize(true);
        designer_works_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity()
                .getApplicationContext()));
        designer_works_listview.setFocusable(false);
        designer_works_listview.setOnRefreshListener(this);
    }

    private void onVisible() {
        load();
    }

    private void onInvisible() {

    }

    private void load() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getDesignerProduct(designerid, pullUpListener);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDesignerProduct(designerid, pullUpListener);
    }

    private void getDesignerProduct(String designerid, ApiUiUpdateListener listener) {
        Map<String, Object> conditionParam = new HashMap<>();
        conditionParam.put("designerid", designerid);
        Map<String, Object> param = new HashMap<>();
        param.put("query", conditionParam);
        param.put("from", FROM);
        param.put("limit", Constant.HOME_PAGE_LIMIT);
        JianFanJiaClient.searchDesignerProduct(new SearchDesignerProductRequest(getActivity(), param), listener, this);
    }

    private ApiUiUpdateListener pullUpListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            mHasLoadedOnce = true;
            designer_works_listview.onRefreshComplete();
            DesignerWorksInfo worksInfo = JsonParser.jsonToBean(data.toString(), DesignerWorksInfo.class);
            LogTool.d(TAG, "worksInfo :" + worksInfo);
            if (null != worksInfo) {
                if (null == adapter) {
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
                } else {
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
