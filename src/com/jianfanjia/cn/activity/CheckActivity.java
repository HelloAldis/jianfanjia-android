package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.jianfanjia.cn.R;
import com.jianfanjia.cn.adapter.MyGridViewAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.UploadListener;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: CheckActivity
 * @Description: 验收
 * @author fengliang
 * @date 2015-8-28 下午2:25:36
 * 
 */
public class CheckActivity extends BaseActivity implements OnClickListener,
		UploadListener {
	private static final String TAG = CheckActivity.class.getName();
	private TextView backView = null;// 返回视图
	private GridView gridView = null;
	private Button btn_confirm_check = null;
	private static final int ICON[] = { R.drawable.pix_default,
			R.drawable.pix_default, R.drawable.pix_default,
			R.drawable.pix_default, R.drawable.pix_default,
			R.drawable.pix_default };
	private List<GridItem> gridList = new ArrayList<GridItem>();
	private int currentList;// 当前的工序

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			currentList = bundle.getInt(Constant.CURRENT_LIST, 0);
		}
		LogTool.d(TAG, "currentList:" + currentList);
	}

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.check_pic_back);
		gridView = (GridView) findViewById(R.id.mygridview);
		btn_confirm_check = (Button) findViewById(R.id.btn_confirm_check);
		gridView.setFocusable(false);
		initData();
	}

	private void initData() {
		for (int i = 0; i < ICON.length; i++) {
			GridItem item = new GridItem();
			item.setImgId(ICON[i]);
			gridList.add(item);
		}
		MyGridViewAdapter adapter = new MyGridViewAdapter(CheckActivity.this,
				gridList, this);
		gridView.setAdapter(adapter);
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
		btn_confirm_check.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.check_pic_back:
			finish();
			break;
		case R.id.btn_confirm_check:
			upload(Constant.COMMON_PATH + "myqr.jpg");// 测试
			break;
		default:
			break;
		}
	}

	@Override
	public void onUpload(int position) {
		LogTool.d(TAG, "position:" + position);

	}

	private void upload(String imgPath) {
		JianFanJiaApiClient.uploadImage(CheckActivity.this, imgPath,
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
								JSONObject obj = new JSONObject(response
										.toString());
								String imageid = obj.getString("data");
								LogTool.d(TAG, "imageid:" + imageid);
								submitImageToProgress(imageid);
							} else if (response.has(Constant.ERROR_MSG)) {

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
					};
				});
	}

	private void submitImageToProgress(String imageid) {
		JianFanJiaApiClient.submitImageToProcess(CheckActivity.this,
				"55e3b1c0765e1e1f20c4fc15", "kai_gong", "xcjd", imageid,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
					};
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_check_pic;
	}

}
