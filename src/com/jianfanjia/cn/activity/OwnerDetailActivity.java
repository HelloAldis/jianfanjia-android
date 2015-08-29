package com.jianfanjia.cn.activity;

import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;

public class OwnerDetailActivity extends BaseActivity {
	private TextView backView;// 返回视图
	private ImageView ownerHeadView;// 用户头像视图
	private ImageView ownerSexView;// 用户性别视图
	private TextView proStageView;// 工地所处阶段视图
	private TextView ownerNameView;// 用户名视图
	private TextView cityView;// 所在城市
	private TextView villageNameView;// 小区名字
	private TextView houseStyleView;// 户型
	private TextView decorateAreaView;// 装修面积
	private TextView loveStyleView;// 喜欢的风格
	private TextView decorateStyleView;// 装修类型
	private TextView decorateBudgetView;// 装修预算
	private TextView startDateView;// 开工日期
	private TextView totalDateView;// 总工期
	private TextView confirmView;// 确认按钮

	@Override
	public void initView() {
		ownerHeadView = (ImageView) findViewById(R.id.owner_detail_head_icon);
		ownerHeadView = (ImageView) findViewById(R.id.owner_detail_sex_icon);
		proStageView = (TextView) findViewById(R.id.pro_stage);
		ownerNameView = (TextView) findViewById(R.id.owner_detail_name);
		backView = (TextView) findViewById(R.id.owner_detail_back);
		cityView = (TextView) findViewById(R.id.my_site_city);
		villageNameView = (TextView) findViewById(R.id.my_site_villagename);
		houseStyleView = (TextView) findViewById(R.id.my_site_housestyle);
		decorateAreaView = (TextView) findViewById(R.id.my_site_derationArea);
		loveStyleView = (TextView) findViewById(R.id.my_site_lovestyle);
		decorateStyleView = (TextView) findViewById(R.id.my_site_decorationstyle);
		decorateBudgetView = (TextView) findViewById(R.id.my_site_decorationbudget);
		startDateView = (TextView) findViewById(R.id.my_site_startdate);
		totalDateView = (TextView) findViewById(R.id.my_site_totaldate);
		confirmView = (TextView) findViewById(R.id.my_site_confirm);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_my_owner_detail;
	}

}
