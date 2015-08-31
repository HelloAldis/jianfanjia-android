package com.jianfanjia.cn.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.bean.SiteInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: OwnerSiteFragment
 * @Description: 我的工地(业主)
 * @author fengliang
 * @date 2015-8-26 下午3:49:05
 * 
 */
public class OwnerSiteFragment extends BaseFragment {
	private static final String TAG = "OwnerSiteFragment";
	private SiteInfo siteInfo;// 工地信息类
	private RequirementInfo requirementInfo;// 实体信息类
	private DatePickerDialog datePickerDialog;// 时间选择对话框
	private Calendar calendar = Calendar.getInstance();

	private ImageView headBackView;// 返回头像
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

	private RelativeLayout startDateLayout;
	private RelativeLayout totalDateLayout;

	@Override
	public void initView(View view) {
		headBackView = (ImageView) view.findViewById(R.id.ower_site_head);
		cityView = (TextView) view.findViewById(R.id.my_site_city);
		villageNameView = (TextView) view
				.findViewById(R.id.my_site_villagename);
		houseStyleView = (TextView) view.findViewById(R.id.my_site_housestyle);
		decorateAreaView = (TextView) view
				.findViewById(R.id.my_site_derationArea);
		loveStyleView = (TextView) view.findViewById(R.id.my_site_lovestyle);
		decorateStyleView = (TextView) view
				.findViewById(R.id.my_site_decorationstyle);
		decorateBudgetView = (TextView) view
				.findViewById(R.id.my_site_decorationbudget);
		startDateView = (TextView) view.findViewById(R.id.my_site_startdate);
		totalDateView = (TextView) view.findViewById(R.id.my_site_totaldate);
		confirmView = (TextView) view.findViewById(R.id.my_site_confirm);

		startDateLayout = (RelativeLayout) view
				.findViewById(R.id.my_startdate_layout);
		totalDateLayout = (RelativeLayout) view
				.findViewById(R.id.my_totaldate_layout);
		siteInfo = MyApplication.getInstance().getSiteInfo();
		if (siteInfo == null) {
			getRequirement();
		} else {
			initData();
		}
	}

	private void getSiteInfo() {
		makeTextLong(JsonParser.beanToJson(requirementInfo));
		JianFanJiaApiClient.post_Requiremeng(getApplication(), requirementInfo,
				new JsonHttpResponseHandler() {
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
								requirementInfo = JsonParser.jsonToBean(
										response.get(Constant.DATA).toString(),
										RequirementInfo.class);
								handlerSuccess();
								makeTextLong(response.toString());
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

	// 初始化数据
	private void initData() {
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
		if (requirementInfo == null) {
			// 获取错误，加载错误视图
		} else {
			initData();
		}
	}

	@Override
	public void setListener() {
		confirmView.setOnClickListener(this);
		headBackView.setOnClickListener(this);
		startDateLayout.setOnClickListener(this);
		totalDateLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		int viewId = v.getId();
		switch (viewId) {
		case R.id.my_startdate_layout:
			datePickerDialog = new DatePickerDialog(getActivity(),
					dateSetListener, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			datePickerDialog.show();
			break;
		default:
			break;
		}
	}

	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			Calendar calendar1 = Calendar.getInstance();
			calendar1.set(arg1, arg2, arg3);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String date = simpleDateFormat.format(calendar1.getTime());
			startDateView.setText(date);
			requirementInfo.setTotal_price("60");
			requirementInfo.setStart_at(calendar1.getTimeInMillis());
			getSiteInfo();
		}
	};

	@Override
	public int getLayoutId() {
		return R.layout.fragment_owner_site;
	}
}
