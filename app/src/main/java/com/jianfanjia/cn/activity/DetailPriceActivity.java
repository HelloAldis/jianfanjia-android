package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.PriceDetailAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.PlandetailInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:方案详细报价
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DetailPriceActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = DetailPriceActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private ListView priceListView = null;
    private TextView totalPrice = null;
    private PriceDetailAdapter adapter = null;
    private PlandetailInfo planDetailInfo = null;

    @Override
    public void initView() {
        initMainHeadView();
        priceListView = (ListView) findViewById(R.id.price_listview);
        totalPrice = (TextView) findViewById(R.id.priceText);
        Intent intent = this.getIntent();
        Bundle priceBundle = intent.getExtras();
        planDetailInfo = (PlandetailInfo) priceBundle.getSerializable(Global.PLAN_DETAIL);
        LogTool.d(TAG, "planDetailInfo =" + planDetailInfo);
        if (null != planDetailInfo) {
            totalPrice.setText("项目总造价:" + planDetailInfo.getTotal_price() + "元");
            adapter = new PriceDetailAdapter(DetailPriceActivity.this, planDetailInfo.getPrice_detail());
            priceListView.setAdapter(adapter);
        }
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_price_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.str_view_price));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_price;
    }

}
