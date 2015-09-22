package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.Process;
import com.jianfanjia.cn.bean.User;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.DesignerOwnerInfoRequest;
import com.jianfanjia.cn.http.request.OwnerInfoRequest;
import com.jianfanjia.cn.http.request.ProcessInfoRequest;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName:OwnerInfoActivity
 * @Description:业主信息
 * @author fengliang
 * @date 2015-9-5 上午11:26:30
 * 
 */
public class OwnerInfoActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = OwnerInfoActivity.class.getName();
	private TextView backView = null;// 返回视图
	private ImageView ownerHeadView = null;// 用户头像视图
	private ImageView ownerSexView = null;// 用户性别视图
	private TextView proStageView = null;// 工地所处阶段视图
	private TextView ownerNameView = null;// 用户名视图
	private TextView cityView = null;// 所在城市
	private TextView villageNameView = null;// 小区名字
	private TextView houseStyleView = null;// 户型
	private TextView decorateAreaView = null;// 装修面积
	private TextView loveStyleView = null;// 喜欢的风格
	private TextView decorateStyleView = null;// 装修类型
	private TextView decorateBudgetView = null;// 装修预算
	private TextView startDateView = null;// 开工日期
	private TextView totalDateView = null;// 总工期
	private User ownerInfo = null;
	private Process process = null;
	private ProcessInfo processInfo = null;
	private String processId = null;

	@Override
	public void initView() {
		Intent intent = this.getIntent();
		process = (Process) intent.getSerializableExtra(MyOwnerActivity.PROCESS);
		if(process != null){
			ownerInfo = process.getUser();
			processId = process.get_id();
			if(processId != null){
				processInfo = dataManager.getProcessInfoById(process.get_id());
			}
		}
		ownerHeadView = (ImageView) findViewById(R.id.owner_detail_head_icon);
		ownerSexView = (ImageView) findViewById(R.id.owner_detail_sex_icon);
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
		if (null != ownerInfo) {
			// get_one_owner_info(ownerId);
			imageLoader.displayImage(
					ownerInfo.getImageid() != null ? Url.GET_IMAGE
							+ ownerInfo.getImageid()
							: Constant.DEFALUT_OWNER_PIC, ownerHeadView,
					options);
			if (!TextUtils.isEmpty(ownerInfo.getUsername())) {
				ownerNameView.setText(ownerInfo.getUsername());
			} else {
				ownerNameView.setText(getString(R.string.ower));
			}
		}
		
		LogTool.d(TAG, "processInfo:" + processInfo);
		if (null != processInfo) {
			setData();
		} else {
			LoadClientHelper.requestProcessInfoById(this,new DesignerOwnerInfoRequest(
					this, processId), this);
		}

	}

	private void setData() {
		String city = processInfo.getProvince() + processInfo.getCity()
				+ processInfo.getDistrict();
		cityView.setText(city);
		villageNameView.setText(processInfo.getCell());
		houseStyleView.setText(getResources()
				.getStringArray(R.array.house_type)[Integer
				.parseInt(processInfo.getHouse_type())]);
		decorateAreaView.setText(processInfo.getHouse_area());
		loveStyleView
				.setText(getResources().getStringArray(R.array.dec_style)[Integer
						.parseInt(processInfo.getDec_style())]);
		decorateStyleView
				.setText(getResources().getStringArray(R.array.work_type)[Integer
						.parseInt(processInfo.getWork_type())]);
		decorateBudgetView.setText(processInfo.getTotal_price());
		startDateView.setText(DateFormatTool.covertLongToString(
				processInfo.getStart_at(), "yyyy-MM-dd"));
		totalDateView.setText(processInfo.getDuration());
	}

	@Override
	public void loadSuccess() {
		// TODO Auto-generated method stub
		super.loadSuccess();
		processInfo = dataManager.getProcessInfoById(processId);
		LogTool.d(TAG, "processInfo:" + processInfo);
		if (null != processInfo) {
			setData();
		}
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.owner_detail_back:
			finish();
			break;
		default:
			break;
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
		return R.layout.activity_my_owner_detail;
	}

}
