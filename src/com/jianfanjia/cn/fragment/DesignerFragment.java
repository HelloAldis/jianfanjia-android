package com.jianfanjia.cn.fragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.cache.CacheManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @ClassName: DesignerFragment
 * @Description:设计师
 * @author fengliang
 * @date 2015-8-26 下午3:41:31
 * 
 */
public class DesignerFragment extends BaseFragment {
	protected static final String TAG = "DesignerFragment";
	private ImageView bgView;// 设计师背景
	private ImageView headView;// 设计师头像
	private ImageView ownerHeadView;// 业主的头像
	private TextView nameView;// 姓名
	private ImageView sexView;// 性别
	private ImageView authView;// 是否为认证设计师
	private TextView productSumView;// 作品数
	private TextView appointmentSum;// 预约数
	private TextView cityView;// 服务城市
	private TextView goodAtView;// 擅长
	private TextView budgetView;// 设计费
	private DesignerInfo designerInfo;// 设计师信息

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView(View view) {
		ownerHeadView = (ImageView) view.findViewById(R.id.owner_head);
		bgView = (ImageView) view.findViewById(R.id.designer_bg);
		headView = (ImageView) view.findViewById(R.id.my_designer_head_icon);
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

		designerInfo = (DesignerInfo) CacheManager.getObjectByFile(
				getActivity(), Constant.DESIGNERINFO_CACHE);
		String designerid = shared.getValue(Constant.FINAL_DESIGNER_ID, null);
		if (designerInfo == null) {
			if (designerid != null) {
				getDesignerInfo(designerid);
			} else {
				// loadNoDataLayout
			}
		} else {
			setData();
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
			productSumView.setText(designerInfo.getProduct_count());
			appointmentSum.setText(designerInfo.getOrder_count());
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
					.setText(dec_style.subSequence(0, dec_style.length() - 2));

			if (designerInfo.getBig_imageid() != null) {
				Log.i(TAG, Url.GET_IMAGE + designerInfo.getBig_imageid());
				ImageLoader.getInstance().displayImage(
						Url.GET_IMAGE + designerInfo.getBig_imageid(), bgView);
			} else {
				bgView.setImageResource(R.drawable.bg_login_720);
			}
			if (designerInfo.getImageId() != null) {
				Log.i(TAG, Url.GET_IMAGE + designerInfo.getImageId());
				ImageLoader.getInstance().displayImage(
						Url.GET_IMAGE + designerInfo.getImageId(), headView);
			} else {
				headView.setImageResource(R.drawable.icon_sidebar_default_designer);
			}

		}

	}

	private void getDesignerInfo(String designerid) {
		Log.i(TAG, "getDesignerInfo");
		JianFanJiaApiClient.getDesignerInfoById(getApplication(), designerid,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "response:" + response.toString());
						try {
							if (response.has(Constant.DATA)) {
								designerInfo = JsonParser.jsonToBean(response
										.get(Constant.DATA).toString(),
										DesignerInfo.class);
								// 数据请求成功保存在缓存中
								CacheManager.saveObject(getActivity(),
										designerInfo,
										Constant.DESIGNERINFO_CACHE);
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

	private void handlerSuccess() {
		setData();
	}

	@Override
	public void setListener() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_my_designer;
	}

}
