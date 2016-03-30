package com.jianfanjia.cn.designer.activity.requirement;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.PriceDetailAdapter;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.PlanPriceDetail;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;

/**
 * Description:方案详细报价
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DetailPriceActivity extends BaseActivity{
    private static final String TAG = DetailPriceActivity.class.getName();

    @Bind(R.id.my_price_head_layout)
    protected MainHeadView mainHeadView;
    @Bind(R.id.detail_price_listview)
    protected RecyclerView detail_price_listview;
    private PriceDetailAdapter adapter = null;
    private Plan plan = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent(){
        Intent intent = this.getIntent();
        Bundle priceBundle = intent.getExtras();
        plan = (Plan) priceBundle.getSerializable(Global.PLAN_DETAIL);
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
            PlanPriceDetail detail = new PlanPriceDetail();
            detail.setItem(getResources().getString(R.string.project_text));
            detail.setPrice(getResources().getString(R.string.project_price_text));
            detail.setDescription(getResources().getString(R.string.des_text));
            List<PlanPriceDetail> details = plan.getPrice_detail();
            if (null != details && details.size() > 0) {
                details.add(0, detail);
            }
            PriceDetailAdapter adapter = new PriceDetailAdapter(DetailPriceActivity.this, details, plan);
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
