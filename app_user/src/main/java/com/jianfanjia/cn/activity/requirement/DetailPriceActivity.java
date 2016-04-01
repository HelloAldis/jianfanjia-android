package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.PlanPriceDetail;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.PriceDetailAdapter;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.HorizontalDividerDecoration;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description:方案详细报价
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DetailPriceActivity extends BaseSwipeBackActivity {
    private static final String TAG = DetailPriceActivity.class.getName();

    @Bind(R.id.my_price_head_layout)
    protected MainHeadView mainHeadView;
    @Bind(R.id.detail_price_listview)
    protected RecyclerView detail_price_listview;

    private PriceDetailAdapter adapter = null;
    private Plan detailInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent(){
        Intent intent = this.getIntent();
        Bundle priceBundle = intent.getExtras();
        detailInfo = (Plan) priceBundle.getSerializable(Global.PLAN_DETAIL);
        LogTool.d(TAG, "detailInfo =" + detailInfo);
    }

    public void initView() {
        initMainHeadView();
        initRecycleView();
    }

    private void initRecycleView(){
        detail_price_listview.setLayoutManager(new LinearLayoutManager(DetailPriceActivity.this));
        detail_price_listview.setItemAnimator(new DefaultItemAnimator());
        detail_price_listview.addItemDecoration(new HorizontalDividerDecoration(TDevice.dip2px(this, 1)));
        if (null != detailInfo) {
            PlanPriceDetail detail = new PlanPriceDetail();
            detail.setItem(getResources().getString(R.string.project_text));
            detail.setPrice(getResources().getString(R.string.project_price_text));
            detail.setDescription(getResources().getString(R.string.des_text));
            List<PlanPriceDetail> details = detailInfo.getPrice_detail();
            if (null != details && details.size() > 0) {
                details.add(0, detail);
            }
            PriceDetailAdapter adapter = new PriceDetailAdapter(DetailPriceActivity.this, details, detailInfo);
            detail_price_listview.setAdapter(adapter);
        }
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.str_view_price));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout})
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
