package com.jianfanjia.cn.activity;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.MyDesignerInfo;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.DesignerInfoRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.layout.CircleImageView;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @ClassName: MyDesignerActivity
 * @Description: 我的设计师
 * @author fengliang
 * @date 2015-9-11 上午9:56:20
 * 
 */
public class MyDesignerActivity extends BaseActivity implements
		OnClickListener, LoadDataListener {
	private static final String TAG = MyDesignerActivity.class.getName();
	private ImageView bgView;// 设计师背景
	private CircleImageView headView;//
	private TextView nameView;// 姓名
	private ImageView sexView;// 性别
	private ImageView authView;// 是否为认证设计师
	private TextView productSumView;// 作品数
	private TextView appointmentSum;// 预约数
	private TextView cityView;// 服务城市
	private TextView goodAtView;// 擅长
	private TextView budgetView;// 设计费
	private MyDesignerInfo designerInfo;
	private String designerId;

	private MainHeadView mainHeadView;

	@Override
	public void initView() {
		initMainHead();
		bgView = (ImageView) findViewById(R.id.designer_bg);
		headView = (CircleImageView) findViewById(R.id.my_designer_head_icon);
		nameView = (TextView) findViewById(R.id.my_designer_name);
		sexView = (ImageView) findViewById(R.id.my_designer_sex_icon);
		authView = (ImageView) findViewById(R.id.my_designer_verify);
		productSumView = (TextView) findViewById(R.id.my_designer_product_sum);
		appointmentSum = (TextView) findViewById(R.id.my_designer_appointment_sum);
		cityView = (TextView) findViewById(R.id.my_designer_city);
		goodAtView = (TextView) findViewById(R.id.my_designer_style);
		budgetView = (TextView) findViewById(R.id.my_designer_budget);

		designerId = dataManager.getDefaultDesignerId();
		if (designerId != null) {
			designerInfo = dataManager.getMyDesignerInfoById(designerId);
			if (designerInfo != null) {
				setData();
			} else {
				LoadClientHelper.getDesignerInfoById(this,
						new DesignerInfoRequest(this, designerId), this);
			}
		}
	}

	private void initMainHead() {
		mainHeadView = (MainHeadView) findViewById(R.id.my_designer_head_layout);
		mainHeadView.setBackListener(this);
		mainHeadView.setMianTitle(getResources()
				.getString(R.string.my_designer));
		mainHeadView.setBackgroundTransparent();
		mainHeadView.setMainTextColor(getResources().getColor(
				R.color.font_white));
		mainHeadView.setDividerVisable(View.GONE);
	}

	@Override
	public void loadSuccess() {
		super.loadSuccess();
		designerInfo = dataManager.getMyDesignerInfoById(designerId);
		if (designerInfo != null) {
			setData();
		} else {
			// loadempty
		}
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

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
			// 解析擅长风格
			String[] dec_styles = getResources().getStringArray(
					R.array.dec_style);
			StringBuffer decBuffer = new StringBuffer();
			for (String item : designerInfo.getDec_styles()) {
				decBuffer.append(dec_styles[Integer.parseInt(item)]);
				decBuffer.append("，");
			}
			String dec_style = decBuffer.toString();
			goodAtView
					.setText(dec_style.subSequence(0, dec_style.length() - 1));

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
						Url.GET_IMAGE + designerInfo.getImageid(), headView,
						options);
			} else {
				headView.setImageResource(R.drawable.icon_sidebar_default_designer);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTool.d(TAG, "---onResume()");
		listenerManeger.addPushMsgReceiveListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTool.d(TAG, "---onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogTool.d(TAG, "---onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTool.d(TAG, "---onDestroy()");
		listenerManeger.removePushMsgReceiveListener(this);
	}

	@Override
	public void onReceiveMsg(NotifyMessage message) {
		LogTool.d(TAG, "message=" + message);
		// sendNotifycation(message);
		showNotify(message);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_my_designer;
	}

}
