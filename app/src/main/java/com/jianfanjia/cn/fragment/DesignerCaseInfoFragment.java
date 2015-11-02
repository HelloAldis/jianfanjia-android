package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerCaseAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerCaseInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName:DesignerCaseInfoFragment
 * @Description:设计师案例列表
 * @date 2015-8-26 上午11:14:00
 */
public class DesignerCaseInfoFragment extends BaseFragment implements ApiUiUpdateListener, OnItemClickListener {
    private static final String TAG = DesignerCaseInfoFragment.class.getName();
    private ListView designer_case_listview = null;
    private DesignerCaseAdapter adapter = null;
    private List<DesignerCaseInfo> designerCaseList = new ArrayList<DesignerCaseInfo>();
    private String productid = null;

    @Override
    public void initView(View view) {
        Bundle bundle = getArguments();
        productid = bundle.getString(Global.PRODUCT_ID);
        LogTool.d(TAG, "productid=" + productid);
        designer_case_listview = (ListView) view.findViewById(R.id.designer_case_listview);
        initDesignerCasesList();
    }

    @Override
    public void setListener() {
        designer_case_listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void initDesignerCasesList() {
        getProductHomePageInfo(productid);
    }

    private void getProductHomePageInfo(String productid) {
        JianFanJiaClient.getProductHomePage(getActivity(), productid, this, this);
    }

    @Override
    public void preLoad() {
        showWaitDialog(R.string.loding);
    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data======" + data);
        hideWaitDialog();
        parseResponse(data.toString());
    }

    private void parseResponse(String response) {
        DesignerCaseInfo designerCaseInfo = JsonParser.jsonToBean(response, DesignerCaseInfo.class);
        LogTool.d(TAG, "designerCaseInfo" + designerCaseInfo);
        if (null != designerCaseInfo) {
            adapter = new DesignerCaseAdapter(getActivity(), designerCaseInfo.getImages());
            designer_case_listview.setAdapter(adapter);
            UiHelper.setListViewHeightBasedOnChildren(designer_case_listview);//此处是必须要做的计算Listview的高度
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        hideWaitDialog();
        makeTextLong(error_msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_designer_case_info;
    }
}
