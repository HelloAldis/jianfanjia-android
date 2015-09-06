package com.jianfanjia.cn.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName:OwnerInfoActivity
 * @Description:ҵ����Ϣ
 * @author fengliang
 * @date 2015-9-5 ����11:26:30
 * 
 */
public class OwnerInfoActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = LoginActivity.class.getName();
	private TextView backView;// ������ͼ
	private ImageView ownerHeadView;// �û�ͷ����ͼ
	private ImageView ownerSexView;// �û��Ա���ͼ
	private TextView proStageView;// ���������׶���ͼ
	private TextView ownerNameView;// �û�����ͼ
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
		case R.id.btn_login:
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
	public int getLayoutId() {
		return R.layout.activity_my_owner_detail;
	}

}
