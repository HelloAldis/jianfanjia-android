package com.jianfanjia.cn.fragment;

import android.content.Intent;
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

import com.jianfanjia.cn.Event.MessageEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerCaseInfoActivity;
import com.jianfanjia.cn.adapter.CollectProductAdapter;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.bean.ProductInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author fengliang
 * @ClassName: CollectProductFragment
 * @Description: 作品
 * @date 2015-8-26 下午1:07:52
 */
public class CollectProductFragment extends CommonFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = CollectProductFragment.class.getName();
    private PullToRefreshRecycleView prodtct_listview = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private CollectProductAdapter productAdapter = null;
    private List<Product> products = new ArrayList<>();
    private boolean isFirst = true;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private int currentPos = -1;
    private int FROM = 0;

    public static CollectProductFragment newInstance() {
        CollectProductFragment productFragment = new CollectProductFragment();
        return productFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_product, container, false);
        init(view);
        isPrepared = true;
        load();
        return view;
    }

    public void init(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.empty_view_no_product_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_product);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        prodtct_listview = (PullToRefreshRecycleView) view.findViewById(R.id.prodtct_listview);
        prodtct_listview.setMode(PullToRefreshBase.Mode.BOTH);
        prodtct_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        prodtct_listview.setHasFixedSize(true);
        prodtct_listview.setItemAnimator(new DefaultItemAnimator());
        prodtct_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity().getApplicationContext()));
    }

    @Override
    protected void load() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getProductList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    private void getProductList(int from, int limit, ApiUiUpdateListener listener) {
        JianFanJiaClient.getCollectListByUser(getActivity(), from, limit, listener, this);
    }

    @Override
    public void setListener() {
        prodtct_listview.setOnRefreshListener(this);
        errorLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getProductList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        FROM = 0;
        getProductList(FROM, Constant.HOME_PAGE_LIMIT, pullDownListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getProductList(FROM, Constant.HOME_PAGE_LIMIT, pullUpListener);
    }


    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            if (isFirst) {
                showWaitDialog();
            }
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data=" + data.toString());
            hideWaitDialog();
            mHasLoadedOnce = true;
            prodtct_listview.onRefreshComplete();
            ProductInfo productInfo = JsonParser.jsonToBean(data.toString(), ProductInfo.class);
            LogTool.d(TAG, "productInfo=" + productInfo);
            if (productInfo != null) {
                products.clear();
                products.addAll(productInfo.getProducts());
                if (null != products && products.size() > 0) {
                    productAdapter = new CollectProductAdapter(getActivity(), products, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {
                            LogTool.d(TAG, "position:" + position);
                            currentPos = position;
                            LogTool.d(TAG, "currentPos-------" + currentPos);
                            String productid = products.get(currentPos).get_id();
                            LogTool.d(TAG, "productid:" + productid);
                            Intent productIntent = new Intent(getActivity(), DesignerCaseInfoActivity.class);
                            Bundle productBundle = new Bundle();
                            productBundle.putString(Global.PRODUCT_ID, productid);
                            productIntent.putExtras(productBundle);
                            startActivity(productIntent);
                        }

                        @Override
                        public void OnViewClick(int position) {

                        }
                    });
                    prodtct_listview.setAdapter(productAdapter);
                    prodtct_listview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    isFirst = false;
                } else {
                    prodtct_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                }
                FROM = products.size();
                LogTool.d(TAG, "FROM:" + FROM);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
            prodtct_listview.onRefreshComplete();
            prodtct_listview.setVisibility(View.GONE);
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
            LogTool.d(TAG, "data=" + data.toString());
            prodtct_listview.onRefreshComplete();
            ProductInfo productInfo = JsonParser.jsonToBean(data.toString(), ProductInfo.class);
            LogTool.d(TAG, "productInfo=" + productInfo);
            if (productInfo != null) {
                List<Product> productList = productInfo.getProducts();
                if (null != productList && productList.size() > 0) {
                    productAdapter.add(FROM, productList);
                    FROM += Constant.HOME_PAGE_LIMIT;
                } else {
                    makeTextShort(getResources().getString(R.string.no_more_data));
                }
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            prodtct_listview.onRefreshComplete();
        }
    };

    public void onEventMainThread(MessageEvent event) {
        switch (event.getEventType()) {
            case Constant.UPDATE_PRODUCT_FRAGMENT:
                productAdapter.remove(currentPos);
                if (products.size() == 0) {
                    prodtct_listview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
