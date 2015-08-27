package com.jianfanjia.cn.fragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
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

		getRequirement();

	}

	public void getRequirement() {
		JianFanJiaApiClient.get_Requirement(getApplication(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						makeTextLong(response.toString());
						try {
							if (response.has(Constant.DATA)) {
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

	/*
	 * public void send_site() {
	 * JianFanJiaApiClient.get_Requirement(getApplication(), new
	 * JsonHttpResponseHandler() {
	 * 
	 * @Override public void onStart() { LogTool.d(TAG, "onStart()"); }
	 * 
	 * @Override public void onSuccess(int statusCode, Header[] headers,
	 * JSONObject response) { makeTextLong(response.toString()); try { if
	 * (response.has(Constant.DATA)) { makeTextLong(response.toString()); } else
	 * if (response.has(Constant.ERROR_MSG)) {
	 * makeTextLong(response.get(Constant.ERROR_MSG) .toString()); } } catch
	 * (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace();
	 * makeTextLong(getString(R.string.tip_login_error_for_network)); } }
	 * 
	 * @Override public void onFailure(int statusCode, Header[] headers,
	 * Throwable throwable, JSONObject errorResponse) { LogTool.d(TAG,
	 * "Throwable throwable:" + throwable.toString());
	 * makeTextLong(getString(R.string.tip_login_error_for_network)); }
	 * 
	 * @Override public void onFailure(int statusCode, Header[] headers, String
	 * responseString, Throwable throwable) { LogTool.d(TAG, "throwable:" +
	 * throwable);
	 * makeTextLong(getString(R.string.tip_login_error_for_network)); }; }); }
	 */
	@Override
	public void setListener() {
		confirmView.setOnClickListener(this);
		headBackView.setOnClickListener(this);
		startDateLayout.setOnClickListener(this);
		totalDateLayout.setOnClickListener(this);
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_ower_site;
	}
}
