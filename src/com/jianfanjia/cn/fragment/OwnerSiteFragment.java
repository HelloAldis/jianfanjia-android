package com.jianfanjia.cn.fragment;

import java.util.Calendar;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.cache.CacheManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: OwnerSiteFragment
 * @Description: �ҵĹ���(ҵ��)
 * @author fengliang
 * @date 2015-8-26 ����3:49:05
 * 
 */
public class OwnerSiteFragment extends BaseFragment {
	private static final String TAG = "OwnerSiteFragment";
	private ProcessInfo processInfo;// ������Ϣ��
	private RequirementInfo requirementInfo;// ʵ����Ϣ��
	private DatePickerDialog datePickerDialog;// ʱ��ѡ��Ի���
	private Calendar calendar = Calendar.getInstance();

	private ImageView headBackView;// ����ͷ��
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
	private ImageView startDateGoto;
	private ImageView totalDateGoto;

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
		startDateGoto = (ImageView) view
				.findViewById(R.id.startdate_select_goto);
		totalDateGoto = (ImageView) view
				.findViewById(R.id.totaldate_select_goto);

		startDateLayout = (RelativeLayout) view
				.findViewById(R.id.my_startdate_layout);
		totalDateLayout = (RelativeLayout) view
				.findViewById(R.id.my_totaldate_layout);

		processInfo = (ProcessInfo) CacheManager.readObject(getActivity(),
				Constant.PROCESSINFO_CACHE);
		if (processInfo == null) {
			getRequirement();
		} else {
			initData();
		}
	}

	private void getSiteInfo() {
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
								processInfo = JsonParser.jsonToBean(response
										.get(Constant.DATA).toString(),
										ProcessInfo.class);
								CacheManager
										.saveObject(getActivity(), processInfo,
												Constant.PROCESSINFO_CACHE);
								// �����깤�ر����ҵ����ʦid
								shared.setValue(Constant.FINAL_DESIGNER_ID,
										processInfo.getFinal_designerid());
								handlerSuccess();
								makeTextLong("���óɹ�");
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

	// ��ʼ������
	private void initData() {
		if (processInfo != null) {
			String city = processInfo.getProvince() + processInfo.getCity()
					+ processInfo.getDistrict();
			cityView.setText(city);
			villageNameView.setText(processInfo.getCell());
			houseStyleView.setText(getResources().getStringArray(
					R.array.house_type)[Integer.parseInt(processInfo
					.getHouse_type())]);
			decorateAreaView.setText(processInfo.getHouse_area());
			loveStyleView.setText(getResources().getStringArray(
					R.array.dec_style)[Integer.parseInt(processInfo
					.getDec_style())]);
			decorateStyleView.setText(getResources().getStringArray(
					R.array.work_type)[Integer.parseInt(processInfo
					.getWork_type())]);
			decorateBudgetView.setText(processInfo.getTotal_price());
			startDateView.setText(DateFormatTool.covertLongToString(
					processInfo.getStart_at(),"yyyy-MM-dd"));
			totalDateView.setText(processInfo.getDuration());
			startDateGoto.setVisibility(View.GONE);
			totalDateGoto.setVisibility(View.GONE);
			confirmView.setEnabled(false);
			startDateLayout.setEnabled(false);
			totalDateLayout.setEnabled(false);
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

	// ��ȡ����ɹ�
	private void handlerSuccess() {
		if (processInfo == null) {
			// ��ȡ���󣬼��ش�����ͼ
		} else {

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
			DateWheelDialog dateWheelDialog = new DateWheelDialog(getActivity(),Calendar.getInstance());
			dateWheelDialog.setTitle("ѡ��ʱ��");
			dateWheelDialog.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							makeTextLong(StringUtils.getDateString(((DateWheelDialog)dialog).getChooseCalendar().getTime()));
							startDateView.setText(StringUtils.getDateString(((DateWheelDialog)dialog).getChooseCalendar().getTime()));
							if(requirementInfo != null){
								requirementInfo.setStart_at(((DateWheelDialog)dialog).getChooseCalendar().getTimeInMillis());
							}
							dialog.dismiss();
						}
					});
			dateWheelDialog.setNegativeButton(R.string.no, null);
			dateWheelDialog.show();
			break;
		case R.id.my_site_confirm:
			Log.i(TAG, "confirm");
			getSiteInfo();
			break;
		case R.id.ower_site_head:
			((MainActivity)getActivity()).getSlidingPaneLayout().openPane();
			break;
			
		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_owner_site;
	}
}
