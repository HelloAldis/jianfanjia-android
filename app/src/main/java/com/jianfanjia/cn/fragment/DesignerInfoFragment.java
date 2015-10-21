package com.jianfanjia.cn.fragment;

import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;

/**
 * @author fengliang
 * @ClassName: DesignerInfoFragment
 * @Description: 设计师资料
 * @date 2015-8-26 下午1:07:52
 */
public class DesignerInfoFragment extends BaseFragment implements ApiUiUpdateListener {
    private static final String TAG = DesignerInfoFragment.class.getName();

    @Override
    public void initView(View view) {
        getDesignerProduct("55ebfc02d6e8f37706e4f1b7", 0, 5);
    }

    @Override
    public void setListener() {

    }

    private void getDesignerProduct(String designerid, int from, int limit) {
        JianFanJiaClient.getDesignerProduct(getContext(), designerid, from, limit, this, this);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data);
    }

    @Override
    public void loadFailture(String error_msg) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_designer_info;
    }

}
