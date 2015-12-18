package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
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
    private View headView = null;
    private TextView project_total_price = null;
    private TextView project_price_before_discount = null;
    private TextView project_price_after_discount = null;
    private TextView total_design_fee = null;
    private PriceDetailAdapter adapter = null;
    private PlandetailInfo planDetailInfo = null;

    @Override
    public void initView() {
        initMainHeadView();
        headView = inflater.inflate(R.layout.list_item_price_header_item, null);
        priceListView = (ListView) findViewById(R.id.price_listview);
        priceListView.setFocusable(false);
        project_total_price = (TextView) findViewById(R.id.project_total_price);
        project_price_before_discount = (TextView) findViewById(R.id.project_price_before_discount);
        project_price_after_discount = (TextView) findViewById(R.id.project_price_after_discount);
        total_design_fee = (TextView) findViewById(R.id.total_design_fee);
        Intent intent = this.getIntent();
        Bundle priceBundle = intent.getExtras();
        planDetailInfo = (PlandetailInfo) priceBundle.getSerializable(Global.PLAN_DETAIL);
        LogTool.d(TAG, "planDetailInfo =" + planDetailInfo);
        if (null != planDetailInfo) {
            priceListView.addHeaderView(headView);
            project_total_price.setText("工程总造价:" + planDetailInfo.getProject_price_before_discount() + "元");
            project_price_after_discount.setText("工程折后价:" + planDetailInfo.getProject_price_after_discount() + "元");
            total_design_fee.setText("设计费:" + planDetailInfo.getTotal_design_fee() + "元");
            project_price_before_discount.setText("折后总价:" + planDetailInfo.getTotal_price() + "元");
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
