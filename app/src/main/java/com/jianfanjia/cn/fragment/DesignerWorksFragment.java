package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jianfanjia.cn.activity.DesignerCaseInfoActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerWorksAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerWorksInfo;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: DesignerWorksFragment
 * @Description: 设计师作品
 * @date 2015-8-26 下午1:07:52
 */
public class DesignerWorksFragment extends BaseFragment implements OnItemClickListener, ApiUiUpdateListener {
    private static final String TAG = DesignerWorksFragment.class.getName();
    private ListView designer_works_listview = null;
    private DesignerWorksAdapter adapter = null;
    private List<Product> productList = new ArrayList<Product>();

    private String designerid = null;

    @Override
    public void initView(View view) {
        Bundle bundle = getArguments();
        designerid = bundle.getString(Global.DESIGNER_ID);
        LogTool.d(TAG, "designerid:" + designerid);
        designer_works_listview = (ListView) view.findViewById(R.id.designer_works_listview);
        designer_works_listview.setFocusable(false);

        getDesignerProduct(designerid, 0, 5);
    }


    @Override
    public void setListener() {
        designer_works_listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Product product = productList.get(position);
        String productid = product.get_id();
        LogTool.d(TAG, "productid:" + productid);
        Bundle productBundle = new Bundle();
        productBundle.putString("productId", productid);
        startActivity(DesignerCaseInfoActivity.class, productBundle);
    }

    private void getDesignerProduct(String designerid, int from, int limit) {
        JianFanJiaClient.getDesignerProduct(getActivity(), designerid, from, limit, this, this);
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
            adapter = new DesignerWorksAdapter(getActivity(), productList);
            designer_works_listview.setAdapter(adapter);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_designer_works;
    }

}
