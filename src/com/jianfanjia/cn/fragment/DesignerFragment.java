package com.jianfanjia.cn.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;

/**
 * 
 * @ClassName: DesignerFragment
 * @Description:���ʦ
 * @author fengliang
 * @date 2015-8-26 ����3:41:31
 * 
 */
public class DesignerFragment extends BaseFragment {

	private TextView NameView;// ����
	private ImageView SexView;// �Ա�
	private TextView productSumView;// ��Ʒ��
	private TextView appointmentSum;// ԤԼ��
	private TextView cityView;// �������
	private TextView goodAtView;// �ó�
	private TextView budgetView;// ��Ʒ�

	@Override
	public void initView(View view) {
		NameView = (TextView)view.findViewById(R.id.my_designer_name);
		SexView = (ImageView)view.findViewById(R.id.my_designer_sex_icon);
		productSumView = (TextView)view.findViewById(R.id.my_designer_product_sum);
		appointmentSum = (TextView)view.findViewById(R.id.my_designer_appointment_sum);
		cityView = (TextView)view.findViewById(R.id.my_designer_city);
		goodAtView = (TextView)view.findViewById(R.id.my_designer_style);
		budgetView = (TextView)view.findViewById(R.id.my_designer_budget);
		
		
	}

	@Override
	public void setListener() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_my_designer;
	}

}
