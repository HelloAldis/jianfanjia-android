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
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ScrollableHelper;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: DesignerWorksFragment
 * @Description: 设计师作品
 * @date 2015-8-26 下午1:07:52
 */

public class DesignerWorksFragment extends BaseFragment implements OnItemClickListener, ApiUiUpdateListener, ScrollableHelper.ScrollableContainer {
    private static final String TAG = DesignerWorksFragment.class.getName();
    private RecyclerView designer_works_listview = null;
    private LinearLayoutManager mLayoutManager = null;
    private DesignerWorksAdapter adapter = null;
    private List<Product> productList = new ArrayList<Product>();
    private String designerid = null;

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
        designer_works_listview = (RecyclerView) view.findViewById(R.id.designer_works_listview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        designer_works_listview.setLayoutManager(mLayoutManager);
        designer_works_listview.setItemAnimator(new DefaultItemAnimator());
        designer_works_listview.setHasFixedSize(true);
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        designer_works_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        designer_works_listview.setFocusable(false);
        getDesignerProduct(designerid, 0, 10);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void OnItemClick(int position) {
        Product product = productList.get(position);
        String productid = product.get_id();
        LogTool.d(TAG, "productid:" + productid);
        Bundle productBundle = new Bundle();
        productBundle.putString(Global.PRODUCT_ID, productid);
        startActivity(DesignerCaseInfoActivity.class, productBundle);
    }

    private void getDesignerProduct(String designerid, int from, int limit) {
        JianFanJiaClient.getDesignerProduct(getActivity(), "", "", "", "", designerid, from, limit, this, this);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data);
        DesignerWorksInfo worksInfo = JsonParser.jsonToBean(data.toString(), DesignerWorksInfo.class);
        LogTool.d(TAG, "worksInfo :" + worksInfo);
        if (null != worksInfo) {
            productList = worksInfo.getProducts();
            adapter = new DesignerWorksAdapter(getActivity(), productList, this);
            designer_works_listview.setAdapter(adapter);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
    }

    @Override
    public View getScrollableView() {
        return designer_works_listview;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_designer_works;
    }

}
