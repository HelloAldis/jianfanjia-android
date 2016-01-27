package com.jianfanjia.cn.designer.activity.requirement;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.PriceDetailAdapter;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.PlanInfo;
import com.jianfanjia.cn.designer.bean.PriceDetail;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Description:方案详细报价
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DetailPriceActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = DetailPriceActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private RecyclerView detail_price_listview = null;
    private PriceDetailAdapter adapter = null;
    private PlanInfo planInfo = null;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle priceBundle = intent.getExtras();
        planInfo = (PlanInfo) priceBundle.getSerializable(Global.PLAN_DETAIL);
        LogTool.d(TAG, "planInfo =" + planInfo);
        initMainHeadView();
        detail_price_listview = (RecyclerView) findViewById(R.id.detail_price_listview);
        detail_price_listview.setLayoutManager(new LinearLayoutManager(DetailPriceActivity.this));
        detail_price_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.light_white_color));
        paint.setAntiAlias(true);
        detail_price_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).showLastDivider().build());
        if (null != planInfo) {
            PriceDetail detail = new PriceDetail();
            detail.setItem("项目");
            detail.setPrice("项目总价(元)");
            detail.setDescription("备注");
            List<PriceDetail> details = planInfo.getPrice_detail();
            details.add(0, detail);
            PriceDetailAdapter adapter = new PriceDetailAdapter(DetailPriceActivity.this, details, planInfo);
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
