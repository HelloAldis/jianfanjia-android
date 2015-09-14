package com.jianfanjia.cn.fragment;

import java.util.Observable;
import java.util.Observer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.UserByDesignerInfo;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.layout.CircleImageView;
import com.jianfanjia.cn.view.MainHeadView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @ClassName: DesignerFragment
 * @Description:���ʦ
 * @author fengliang
 * @date 2015-8-26 ����3:41:31
 * 
 */
public class DesignerFragment extends BaseFragment{
	protected static final String TAG = "DesignerFragment";
	private ImageView bgView;// ���ʦ����
	private CircleImageView headView;//
	private TextView nameView;// ����
	private ImageView sexView;// �Ա�
	private ImageView authView;// �Ƿ�Ϊ��֤���ʦ
	private TextView productSumView;// ��Ʒ��
	private TextView appointmentSum;// ԤԼ��
	private TextView cityView;// �������
	private TextView goodAtView;// �ó�
	private TextView budgetView;// ��Ʒ�
	private UserByDesignerInfo designerInfo;

	private MainHeadView mainHeadView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView(View view) {
		initMainHead(view);
		bgView = (ImageView) view.findViewById(R.id.designer_bg);
		headView = (CircleImageView) view
				.findViewById(R.id.my_designer_head_icon);
		nameView = (TextView) view.findViewById(R.id.my_designer_name);
		sexView = (ImageView) view.findViewById(R.id.my_designer_sex_icon);
		authView = (ImageView) view.findViewById(R.id.my_designer_verify);
		productSumView = (TextView) view
				.findViewById(R.id.my_designer_product_sum);
		appointmentSum = (TextView) view
				.findViewById(R.id.my_designer_appointment_sum);
		cityView = (TextView) view.findViewById(R.id.my_designer_city);
		goodAtView = (TextView) view.findViewById(R.id.my_designer_style);
		budgetView = (TextView) view.findViewById(R.id.my_designer_budget);

		String designerId = DataManager.getInstance().getDefaultDesignerId();
		designerInfo = DataManager.getInstance().getDesignerInfo(designerId);
		if (designerInfo != null) {
			setData();
		}else{
			if(designerId != null){
				dataManager.getDesignerInfoById(designerId, handler);
			}else{
				//loadempty
			}
		}
	}
	
	@Override
	public void onLoadSuccess() {
		// TODO Auto-generated method stub
		super.onLoadSuccess();
		designerInfo = DataManager.getInstance().getDesignerInfo(
				DataManager.getInstance().getDefaultDesignerId());
		setData();
	}

	private void initMainHead(View view) {
		mainHeadView = (MainHeadView) view
				.findViewById(R.id.my_designer_head_layout);
		mainHeadView.setHeadImage(mUserImageId);
		mainHeadView.setBackListener(this);
		mainHeadView.setRightTitleVisable(View.GONE);
		mainHeadView.setMianTitle(getResources()
				.getString(R.string.my_designer));
		mainHeadView.setBackgroundTransparent();
		mainHeadView.setMainTextColor(getResources().getColor(
				R.color.font_white));
		mainHeadView.setDividerVisable(View.GONE);
	}

	private void setData() {
		if (designerInfo != null) {
			nameView.setText(designerInfo.getUsername() == null ? getString(R.string.designer)
					: designerInfo.getUsername());
			sexView.setImageResource(designerInfo.getSex().equals(
					Constant.SEX_MAN) ? R.drawable.icon_designer_user_man
					: R.drawable.icon_designer_user_woman);
			authView.setVisibility(designerInfo.getAuth_type().equals(
					Constant.DESIGNER_FINISH_AUTH_TYPE) ? View.VISIBLE
					: View.GONE);
			productSumView.setText(designerInfo.getProduct_count() + "");
			appointmentSum.setText(designerInfo.getOrder_count() + "");
			cityView.setText(designerInfo.getCity());
			budgetView.setText(getResources().getStringArray(
					R.array.design_fee_range)[Integer.parseInt(designerInfo
					.getDesign_fee_range())]);
			// �����ó����
			String[] dec_styles = getResources().getStringArray(
					R.array.dec_style);
			StringBuffer decBuffer = new StringBuffer();
			for (String item : designerInfo.getDec_styles()) {
				decBuffer.append(dec_styles[Integer.parseInt(item)]);
				decBuffer.append("��");
			}
			String dec_style = decBuffer.toString();
			goodAtView
					.setText(dec_style.subSequence(0, dec_style.length() - 2));

			if (designerInfo.getBig_imageid() != null) {
				Log.i(TAG, Url.GET_IMAGE + designerInfo.getBig_imageid());
				ImageLoader.getInstance().displayImage(
						Url.GET_IMAGE + designerInfo.getBig_imageid(), bgView);
			} else {
				bgView.setImageResource(R.drawable.bg_login_720);
			}
			if (designerInfo.getImageid() != null) {
				Log.i(TAG, Url.GET_IMAGE + designerInfo.getImageid());
				ImageLoader.getInstance().displayImage(
						Url.GET_IMAGE + designerInfo.getImageid(), headView);
			} else {
				headView.setImageResource(R.drawable.icon_sidebar_default_designer);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon_head:
			((MainActivity) getActivity()).getSlidingPaneLayout().openPane();
			break;
		default:
			break;
		}
	}

	@Override
	public void setListener() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_my_designer;
	}

}
