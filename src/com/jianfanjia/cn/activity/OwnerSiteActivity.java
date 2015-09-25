package com.jianfanjia.cn.activity;

import java.util.Calendar;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.GetRequirementRequest;
import com.jianfanjia.cn.http.request.PostRequirementRequest;
import com.jianfanjia.cn.http.request.ProcessListRequest;
import com.jianfanjia.cn.http.request.TotalDurationRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;

/**
 * 
 * @ClassName: OwnerSiteActivity
 * @Description: 业主工地
 * @author fengliang
 * @date 2015-9-16 上午9:44:53
 * 
 */
public class OwnerSiteActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = OwnerSiteActivity.class.getName();
	private ProcessInfo processInfo;// 工地信息类
	private RequirementInfo requirementInfo;// 实体信息类
	private String processId;

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
	private ImageView startDateGoto;
	private ScrollView scrollView;// 工地数据view
	private View errorView;// 错误view

	private RelativeLayout startDateLayout;
	private RelativeLayout totalDateLayout;

	private MainHeadView mainHeadView;

	@Override
	public void initView() {
		initMainHeadView();
		scrollView = (ScrollView) findViewById(R.id.scrollview);
		errorView = findViewById(R.id.error_view);
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
		startDateGoto = (ImageView) findViewById(R.id.startdate_select_goto);
		startDateLayout = (RelativeLayout) findViewById(R.id.my_startdate_layout);
		totalDateLayout = (RelativeLayout) findViewById(R.id.my_totaldate_layout);

		processInfo = dataManager.getDefaultProcessInfo();
		processId = dataManager.getDefaultProcessId();
		requirementInfo = dataManager.getRequirementInfo();
		LogTool.d(TAG, "processInfo:" + processInfo);
		if (processInfo == null) {
			if(processId != null){
				processInfo = dataManager.getProcessInfoById(processId);
			}
			if(processInfo == null){
				if (requirementInfo == null) {
					LogTool.d(TAG, "getRequirement()");
					getRequirement();
				} else {
					getTotalDuration();
				}
			}else{
				initData();
			}
		} else {
			initData();
		}
	}

	private void getTotalDuration() {
		String planId = requirementInfo.getFinal_planid();
		if (dataManager.getTotalDuration() != null) {
			requirementInfo.setDuration(dataManager.getTotalDuration());
			setData();
		} else {
			if (!TextUtils.isEmpty(planId)) {
				loadTotalDuration(planId);
			} else {
				setErrorView();
			}
		}
	}

	private void initMainHeadView() {
		mainHeadView = (MainHeadView) findViewById(R.id.ower_site_head_layout);
		mainHeadView.setBackListener(this);
		mainHeadView.setMianTitle(getResources().getString(R.string.my_site));
		mainHeadView.setLayoutBackground(R.color.head_layout_bg);
		mainHeadView.setDividerVisable(View.VISIBLE);
	}

	// 初始化数据
	private void initData() {
		setViewChange();

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
		startDateGoto.setVisibility(View.GONE);
		totalDateView.setText(processInfo.getDuration());
		confirmView.setEnabled(false);
		startDateLayout.setEnabled(false);
		totalDateLayout.setEnabled(false);
	}

	// 显示有数据的layout
	private void setViewChange() {
		scrollView.setVisibility(View.VISIBLE);
		errorView.setVisibility(View.GONE);
	}

	// 显示错误试图
	@Override
	public void setErrorView() {
		((TextView) errorView.findViewById(R.id.tv_error)).setText("暂无工地数据");
	}

	@Override
	public void setListener() {
		confirmView.setOnClickListener(this);
		startDateLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_startdate_layout:
			DateWheelDialog dateWheelDialog = new DateWheelDialog(
					OwnerSiteActivity.this, Calendar.getInstance());
			dateWheelDialog.setTitle("选择时间");
			dateWheelDialog.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							makeTextLong(StringUtils
									.getDateString(((DateWheelDialog) dialog)
											.getChooseCalendar().getTime()));
							startDateView.setText(StringUtils
									.getDateString(((DateWheelDialog) dialog)
											.getChooseCalendar().getTime()));
							if (requirementInfo != null) {
								requirementInfo
										.setStart_at(((DateWheelDialog) dialog)
												.getChooseCalendar()
												.getTimeInMillis());
							}
							dialog.dismiss();
						}
					});
			dateWheelDialog.setNegativeButton(R.string.no, null);
			dateWheelDialog.show();
			break;
		case R.id.my_site_confirm:
			if (requirementInfo != null && requirementInfo.getStart_at() != -1) {// 没有需求数据或者没有设置开工日期，就无法提交
				postProcessInfo();
			} else {
				makeTextLong("请配置开工日期");
			}
			break;
		case R.id.head_back_layout:
			finish();
			break;
		default:
			break;
		}
	}

	private void getRequirement() {
		if(!NetTool.isNetworkAvailable(this)){
			makeTextLong(getString(R.string.tip_internet_not));
			return;
		}
		LoadClientHelper.get_Requirement(this, new GetRequirementRequest(this),
				new LoadDataListener() {

					@Override
					public void preLoad() {
						showWaitDialog();
					}

					@Override
					public void loadSuccess() {
						hideWaitDialog();
						requirementInfo = dataManager.getRequirementInfo();
						if (requirementInfo != null) {
							getTotalDuration();
						} else {
							setErrorView();
						}
					}

					@Override
					public void loadFailture() {
						makeTextLong(getString(R.string.tip_error_internet));
						hideWaitDialog();
						setErrorView();
					}
				});

	}

	protected void loadTotalDuration(String planId) {
		if(!NetTool.isNetworkAvailable(this)){
			makeTextLong(getString(R.string.tip_internet_not));
			return;
		}
		LoadClientHelper.getPlanTotalDuration(OwnerSiteActivity.this,
				new TotalDurationRequest(OwnerSiteActivity.this, planId),
				new LoadDataListener() {

					@Override
					public void preLoad() {
						// TODO Auto-generated method stub
					}

					@Override
					public void loadSuccess() {
						if (dataManager.getTotalDuration() != null) {
							requirementInfo.setDuration(dataManager
									.getTotalDuration());
							setData();
						} else {
							setErrorView();
						}
					}

					@Override
					public void loadFailture() {
						makeTextLong(getString(R.string.tip_error_internet));
						setErrorView();
					}
				});
	}

	@Override
	public void loadSuccess() {
		processInfo = dataManager.getDefaultProcessInfo();
		if (processInfo != null) {
			LoadClientHelper.requestProcessList(this, new ProcessListRequest(this), new LoadDataListener() {
				
				@Override
				public void preLoad() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void loadSuccess() {
					// TODO Auto-generated method stub
					hideWaitDialog();
					processInfo = dataManager.getDefaultProcessInfo();
					makeTextLong("配置成功");
					initData();
					Intent intent = new Intent();
					intent.putExtra("Key", "1");
					setResult(Constant.REQUESTCODE_CONFIG_SITE, intent);
					finish();
				}
				
				@Override
				public void loadFailture() {
					// TODO Auto-generated method stub
					hideWaitDialog();
					setErrorView();
				}
			});
		} else {
			// loadempty
			setErrorView();
		}
		
	}

	// 配置工地信息
	private void postProcessInfo() {
		LoadClientHelper.post_Requirement(this, new PostRequirementRequest(
				this, requirementInfo), this);
	}

	private void setData() {
		if (requirementInfo != null) {
			setViewChange();

			String city = requirementInfo.getProvince()
					+ requirementInfo.getCity() + requirementInfo.getDistrict();
			cityView.setText(city);
			villageNameView.setText(requirementInfo.getCell());
			houseStyleView.setText(getResources().getStringArray(
					R.array.house_type)[Integer.parseInt(requirementInfo
					.getHouse_type())]);
			decorateAreaView.setText(requirementInfo.getHouse_area());
			loveStyleView.setText(getResources().getStringArray(
					R.array.dec_style)[Integer.parseInt(requirementInfo
					.getDec_style())]);
			decorateStyleView.setText(getResources().getStringArray(
					R.array.work_type)[Integer.parseInt(requirementInfo
					.getWork_type())]);
			decorateBudgetView.setText(requirementInfo.getTotal_price());
			totalDateView.setText(dataManager.getTotalDuration());
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
		return R.layout.activity_owner_site;
	}

}
