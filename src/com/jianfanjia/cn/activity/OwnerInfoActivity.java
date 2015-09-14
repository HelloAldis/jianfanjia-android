package com.jianfanjia.cn.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Message;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

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
	private TextView confirmView = null;// 确认按钮
	private String ownerId = null;

	@Override
	public void initView() {
		Intent intent = this.getIntent();
		ownerId = intent.getStringExtra("ownerId");
		LogTool.d(TAG, "ownerId:" + ownerId);
		ownerHeadView = (ImageView) findViewById(R.id.owner_detail_head_icon);
		ownerHeadView = (ImageView) findViewById(R.id.owner_detail_sex_icon);
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
		confirmView = (TextView) findViewById(R.id.my_site_confirm);
		if (null != ownerId) {
			get_one_owner_info(ownerId);
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

	private void get_one_owner_info(String ownerId) {
		JianFanJiaApiClient.getOwnerInfoById(OwnerInfoActivity.this, ownerId,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.DATA)) {
								OwnerInfo myOwnerInfo = JsonParser.jsonToBean(
										response.get(Constant.DATA).toString(),
										OwnerInfo.class);
								Log.i(TAG, "myOwnerInfo＝" + myOwnerInfo);
								if (null != myOwnerInfo) {

								}
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

	@Override
	public void onReceiveMsg(Message message) {
		LogTool.d(TAG, "message=" + message);
		showNotify(message);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_my_owner_detail;
	}

}
