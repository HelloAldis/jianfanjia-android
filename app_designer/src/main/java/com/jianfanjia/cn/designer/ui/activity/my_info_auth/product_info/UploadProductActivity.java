package com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.fragment.UploadProduct1Fragment;
import com.jianfanjia.cn.designer.ui.fragment.UploadProduct2Fragment;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-23 14:28
 */
public class UploadProductActivity extends BaseSwipeBackActivity {

    private static final String UPLOAD_FRAGMENT1 = "upload_fragment1";
    private static final String UPLOAD_FRAGMENT2 = "upload_fragment2";

    private UploadProduct1Fragment mUploadProduct1Fragment;
    private UploadProduct2Fragment mUploadProduct2Fragment;
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

        } else {
            this.getDataFromIntent();
            initFragment();
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mProduct = (Product) bundle.getSerializable(Global.PRODUCT_INFO);
        }
    }

    private void initFragment() {
        if (mProduct == null) {
            mProduct = new Product();
        }
        mUploadProduct1Fragment = UploadProduct1Fragment.getInstance(mProduct);
        getSupportFragmentManager().beginTransaction().add(R.id.container_layout, mUploadProduct1Fragment,
                UPLOAD_FRAGMENT1).addToBackStack(null).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void nextFragment() {
        mUploadProduct2Fragment = UploadProduct2Fragment.getInstance(mProduct);
        getSupportFragmentManager().beginTransaction().add(R.id.container_layout, mUploadProduct2Fragment,
                UPLOAD_FRAGMENT2).addToBackStack(null).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void lastFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 返回上一个fragment
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                lastFragment();
            } else {
                appManager.finishActivity(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_product;
    }
}
