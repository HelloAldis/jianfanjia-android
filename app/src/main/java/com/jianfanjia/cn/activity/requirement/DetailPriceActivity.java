package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.PriceDetailAdapter;
import com.jianfanjia.cn.bean.PlandetailInfo;
import com.jianfanjia.cn.bean.PriceDetail;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Description:方案详细报价
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DetailPriceActivity extends SwipeBackActivity implements OnClickListener {
    private static final String TAG = DetailPriceActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private RecyclerView detail_price_listview = null;
    private PriceDetailAdapter adapter = null;
    private PlandetailInfo detailInfo = null;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle priceBundle = intent.getExtras();
        detailInfo = (PlandetailInfo) priceBundle.getSerializable(Global.PLAN_DETAIL);
        LogTool.d(TAG, "detailInfo =" + detailInfo);
        initMainHeadView();
        detail_price_listview = (RecyclerView) findViewById(R.id.detail_price_listview);
        detail_price_listview.setLayoutManager(new LinearLayoutManager(DetailPriceActivity.this));
        detail_price_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.light_white_color));
        paint.setAntiAlias(true);
        detail_price_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).showLastDivider().build());
        if (null != detailInfo) {
            PriceDetail detail = new PriceDetail();
            detail.setItem(getResources().getString(R.string.project_text));
            detail.setPrice(getResources().getString(R.string.project_price_text));
            detail.setDescription(getResources().getString(R.string.des_text));
            List<PriceDetail> details = detailInfo.getPrice_detail();
            if (null != details && details.size() > 0) {
                details.add(0, detail);
            }
            PriceDetailAdapter adapter = new PriceDetailAdapter(DetailPriceActivity.this, details, detailInfo);
            detail_price_listview.setAdapter(adapter);
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
                appManager.finishActivity(this);
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
