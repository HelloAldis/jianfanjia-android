package com.jianfanjia.cn.activity;

import java.util.Calendar;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
import com.loopj.android.http.JsonHttpResponseHandler;

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
	private ImageView totalDateGoto;

	private RelativeLayout startDateLayout;
	private RelativeLayout totalDateLayout;

	private MainHeadView mainHeadView;

	@Override
	public void initView() {
		initMainHeadView();
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
		totalDateGoto = (ImageView) findViewById(R.id.totaldate_select_goto);
		startDateLayout = (RelativeLayout) findViewById(R.id.my_startdate_layout);
		totalDateLayout = (RelativeLayout) findViewById(R.id.my_totaldate_layout);

		processInfo = dataManager.getDefaultProcessInfo();
		LogTool.d(TAG, "processInfo:" + processInfo);
		if (processInfo == null) {
			getRequirement();
		} else {
			initData();
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
		startDateGoto.setVisibility(View.GONE);
		totalDateGoto.setVisibility(View.GONE);
		confirmView.setEnabled(false);
		startDateLayout.setEnabled(false);
		totalDateLayout.setEnabled(false);
	}

	@Override
	public void setListener() {
		confirmView.setOnClickListener(this);
		startDateLayout.setOnClickListener(this);
		totalDateLayout.setOnClickListener(this);
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
								requirementInfo.setDuration("60");
							}
							dialog.dismiss();
						}
					});
			dateWheelDialog.setNegativeButton(R.string.no, null);
			dateWheelDialog.show();
			break;
		case R.id.my_site_confirm:
			postProcessInfo();
			break;
		case R.id.head_back_layout:
			finish();
			break;
		default:
			break;
		}
	}

	private void getRequirement() {
		JianFanJiaApiClient.get_Requirement(getApplication(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							if (response.has(Constant.DATA)) {
								requirementInfo = JsonParser.jsonToBean(
										response.get(Constant.DATA).toString(),
										RequirementInfo.class);
								setData();
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							makeTextLong(getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_login_error_for_network));
					};
				});
	}

	// 配置工地信息
	private void postProcessInfo() {
		makeTextLong(JsonParser.beanToJson(requirementInfo));
		JianFanJiaApiClient.post_Owner_Process(getApplication(),
				requirementInfo, new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						makeTextLong("getRequirement");
						try {
							if (response.has(Constant.DATA)) {
								makeTextLong("配置成功");
								handlerSuccess();
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							makeTextLong(getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_login_error_for_network));
					};
				});

	}

	// 获取需求成功
	private void handlerSuccess() {
		confirmView.setEnabled(false);
		Intent intent = new Intent();
		intent.putExtra("Key", "1");
		setResult(Constant.REQUESTCODE_CONFIG_SITE, intent);
		finish();
	}

	private void setData() {
		if (requirementInfo != null) {
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
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_owner_site;
	}

}
