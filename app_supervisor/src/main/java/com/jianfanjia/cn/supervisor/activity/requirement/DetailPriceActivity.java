package com.jianfanjia.cn.supervisor.activity.requirement;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.adapter.PriceDetailAdapter;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.supervisor.config.Global;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.common.tool.LogTool;

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
    private Plan plan = null;
    private Requirement requirement = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle priceBundle = intent.getExtras();
        plan = (Plan) priceBundle.getSerializable(Global.PLAN_DETAIL);
        requirement = (Requirement) priceBundle.getSerializable(Global.REQUIREMENT_INFO);
        LogTool.d(TAG, "plan =" + plan);
    }

    public void initView() {

        initMainHeadView();
        detail_price_listview.setLayoutManager(new LinearLayoutManager(DetailPriceActivity.this));
        detail_price_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.light_white_color));
        paint.setAntiAlias(true);
        detail_price_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint)
                .showLastDivider().build());
        if (null != plan) {
            PriceDetailAdapter adapter = new PriceDetailAdapter(DetailPriceActivity.this, plan.getPrice_detail(),
                    plan, requirement.getPackage_type());
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
