package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerInfo;
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
	private DesignerInfo designerInfo;
	private String designerId;
	private RelativeLayout contentLayout;
	private View errorView;
	private Button callDesignerView;

	private MainHeadView mainHeadView;

	@Override
	public void initView() {
		initMainHead();
		contentLayout = (RelativeLayout) findViewById(R.id.my_designer_content_layout);
		errorView = findViewById(R.id.error_view);
		bgView = (ImageView) findViewById(R.id.designer_bg);
		headView = (CircleImageView) findViewById(R.id.my_designer_head_icon);
		nameView = (TextView) findViewById(R.id.my_designer_name);
		sexView = (ImageView) findViewById(R.id.my_designer_sex_icon);
		authView = (ImageView) findViewById(R.id.my_designer_verify);
		productSumView = (TextView) findViewById(R.id.my_designer_product_sum);
		appointmentSum = (TextView) findViewById(R.id.my_designer_appointment_sum);
		callDesignerView = (Button) findViewById(R.id.btn_call_designer);
		designerId = dataManager.getDefaultDesignerId();
		if (designerId != null) {
			designerInfo = dataManager.getDesignerInfoById(designerId);
			if (designerInfo != null) {
				setData();
			} else {
				LoadClientHelper.getOwnerDesignerInfoById(this,
						new DesignerInfoRequest(this, designerId), this);
			}
		} else {
			setErrorView();
		}
	}

	private void initMainHead() {
		mainHeadView = (MainHeadView) findViewById(R.id.my_designer_head_layout);
		mainHeadView.setBackListener(this);
		mainHeadView.setMianTitle(getResources()
				.getString(R.string.my_designer));
	}

	@Override
	public void loadSuccess() {
		super.loadSuccess();
		designerInfo = dataManager.getDesignerInfoById(designerId);
		if (designerInfo != null) {
			setData();
		} else {
			// loadempty
			setErrorView();
		}
	}

	@Override
	public void setErrorView() {
		((TextView) errorView.findViewById(R.id.tv_error)).setText("暂无设计师数据");
	}

	@Override
	public void setListener() {
		callDesignerView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_back_layout:
			finish();
			break;
		case R.id.btn_call_designer:
			String phone = designerInfo.getPhone();
			if (!TextUtils.isEmpty(phone)) {
				LogTool.d(phone, phone);
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ phone));
				startActivity(intent);
			}
		default:
			break;
		}
	}

	private void setData() {
		if (designerInfo != null) {
			setViewChange();
			nameView.setText(TextUtils.isEmpty(designerInfo.getUsername()) ? getString(R.string.designer)
					: designerInfo.getUsername());
			if (!TextUtils.isEmpty(designerInfo.getSex())) {
				sexView.setImageResource(designerInfo.getSex().equals(
						Constant.SEX_MAN) ? R.drawable.icon_designer_user_man
						: R.drawable.icon_designer_user_woman);
			} else {
				sexView.setVisibility(View.GONE);
			}
			authView.setVisibility(designerInfo.getAuth_type().equals(
					Constant.DESIGNER_FINISH_AUTH_TYPE) ? View.VISIBLE
					: View.GONE);
			productSumView.setText(designerInfo.getProduct_count() + "");
			appointmentSum.setText(designerInfo.getOrder_count() + "");
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

	// 有数据就改变展示的样式
	private void setViewChange() {
		mainHeadView.setBackgroundTransparent();
		mainHeadView.setMainTextColor(getResources().getColor(
				R.color.font_white));
		mainHeadView.setDividerVisable(View.GONE);
		contentLayout.setVisibility(View.VISIBLE);
		bgView.setVisibility(View.VISIBLE);
		errorView.setVisibility(View.GONE);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_my_designer;
	}

}
