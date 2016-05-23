package com.jianfanjia.cn.designer.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductImageInfo;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.UploadProductActivity;
import com.jianfanjia.cn.designer.ui.adapter.UploadProductAdapter;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.designer.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-23 10:05
 */
public class UploadProduct2Fragment extends BaseFragment {

    @Bind(R.id.upload_product_head_layout)
    MainHeadView mMainHeadView;

    private UploadProductActivity mUploadProductActivity;

    @Bind(R.id.upload_product_recyclerview)
    RecyclerView mRecyclerView;

    UploadProductAdapter mUploadProductAdapter;

    public static UploadProduct2Fragment getInstance(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Global.PRODUCT_INFO, product);
        UploadProduct2Fragment uploadProduct2Fragment = new UploadProduct2Fragment();
        uploadProduct2Fragment.setArguments(bundle);
        return uploadProduct2Fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUploadProductActivity = (UploadProductActivity) getActivity();
        initView();
    }

    @OnClick({R.id.head_back_layout, R.id.head_right_title})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                if (mUploadProductActivity != null) {
                    mUploadProductActivity.lastFragment();
                }
                break;
            case R.id.head_right_title:
                if (mUploadProductActivity != null) {
//                    mUploadProductActivity.nextFragment();
                }
                break;
        }
    }

    private void initView(){
        initMainView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mUploadProductAdapter = new UploadProductAdapter(getContext());
        mRecyclerView.setAdapter(mUploadProductAdapter);

        mUploadProductAdapter.addEffectItem(new ProductImageInfo());
        mUploadProductAdapter.addPlanItem(new ProductImageInfo());
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.upload_product));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_upload_product2;
    }
}
