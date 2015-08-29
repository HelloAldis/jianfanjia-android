package com.jianfanjia.cn.activity;

import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;

public class OwnerDetailActivity extends BaseActivity {
	private TextView backView;// ������ͼ
	private ImageView ownerHeadView;// �û�ͷ����ͼ
	private ImageView ownerSexView;// �û��Ա���ͼ
	private TextView proStageView;// ���������׶���ͼ
	private TextView ownerNameView;// �û�����ͼ
	private TextView cityView;// ���ڳ���
	private TextView villageNameView;// С������
	private TextView houseStyleView;// ����
	private TextView decorateAreaView;// װ�����
	private TextView loveStyleView;// ϲ���ķ��
	private TextView decorateStyleView;// װ������
	private TextView decorateBudgetView;// װ��Ԥ��
	private TextView startDateView;// ��������
	private TextView totalDateView;// �ܹ���
	private TextView confirmView;// ȷ�ϰ�ť

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
