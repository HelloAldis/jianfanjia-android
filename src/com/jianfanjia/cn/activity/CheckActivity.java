package com.jianfanjia.cn.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.adapter.MyGridViewAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.UploadImageListener;
import com.jianfanjia.cn.interf.UploadListener;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.PhotoUtils;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
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
		UploadListener, UploadImageListener {
	private static final String TAG = CheckActivity.class.getName();
	private RelativeLayout checkLayout = null;
	private TextView backView = null;// 返回视图
	private TextView check_pic_title = null;
	private TextView check_pic_edit = null;
	private GridView gridView = null;
	private TextView btn_confirm = null;
	private MyGridViewAdapter adapter = null;
	private List<GridItem> checkGridList = new ArrayList<GridItem>();
	private int currentList;// 当前的工序
	private String processInfoId = null;// 工地id
	private String sectionInfoName = null;// 工序名称
	private int processInfoStatus = -1;// 工序状态
	private String key = null;
	private File mTmpFile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			currentList = bundle.getInt(Constant.CURRENT_LIST, 0);
			processInfoId = bundle.getString(Constant.SITE_ID);
			sectionInfoName = bundle.getString(Constant.PROCESS_NAME);
			processInfoStatus = bundle.getInt(Constant.PROCESS_STATUS, 0);
		}
		LogTool.d(TAG, "currentList:" + currentList + " processInfoId:"
				+ processInfoId + " sectionInfoName:" + sectionInfoName
				+ " processInfoStatus:" + processInfoStatus);
		initData();
	}

	@Override
	public void initView() {
		checkLayout = (RelativeLayout) findViewById(R.id.checkLayout);
		backView = (TextView) findViewById(R.id.check_pic_back);
		check_pic_title = (TextView) findViewById(R.id.check_pic_title);
		check_pic_edit = (TextView) findViewById(R.id.check_pic_edit);
		gridView = (GridView) findViewById(R.id.mygridview);
		btn_confirm = (TextView) findViewById(R.id.btn_confirm);
		if (!TextUtils.isEmpty(userIdentity)) {
			if (userIdentity.equals(Constant.IDENTITY_OWNER)) {
				check_pic_edit.setVisibility(View.GONE);
				btn_confirm.setText(this.getResources().getString(
						R.string.confirm_done));
			} else if (userIdentity.equals(Constant.IDENTITY_DESIGNER)) {
				btn_confirm.setText(this.getResources().getString(
						R.string.confirm_upload));
			}
		}
		gridView.setFocusable(false);
	}

	private void initData() {
		check_pic_title.setText(MyApplication.getInstance().getStringById(
				sectionInfoName)
				+ "阶段验收");
		switch (processInfoStatus) {
		case Constant.NOT_START:
			break;
		case Constant.WORKING:
			break;
		case Constant.FINISH:
			btn_confirm.setEnabled(false);
			break;
		case Constant.OWNER_APPLY_DELAY:
			break;
		case Constant.DESIGNER_APPLY_DELAY:
			break;
		default:
			break;
		}
		List<GridItem> picList = getCheckedImageById(sectionInfoName);
		adapter = new MyGridViewAdapter(CheckActivity.this, picList, this);
		gridView.setAdapter(adapter);
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
		check_pic_edit.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.check_pic_back:
			finish();
			break;
		case R.id.check_pic_edit:
			break;
		case R.id.btn_confirm:
			if (!TextUtils.isEmpty(userIdentity)) {
				if (userIdentity.equals(Constant.IDENTITY_OWNER)) {
					onClickCheckDone();
				} else if (userIdentity.equals(Constant.IDENTITY_DESIGNER)) {
					onClickCheckConfirm();
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onUpload(int position) {
		LogTool.d(TAG, "position:" + position);
		key = position + "";
		LogTool.d(TAG, "key:" + key);
		showPopWindow(checkLayout);
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
	public void takecamera() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mTmpFile = FileUtil.createTmpFile(CheckActivity.this);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
		startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
	}

	@Override
	public void takePhoto() {
		Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
		albumIntent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.REQUESTCODE_CAMERA:// 拍照
			if (mTmpFile != null) {
				Uri uri = Uri.fromFile(mTmpFile);
				LogTool.d(TAG, "uri:" + uri);
				if (null != uri) {
					startPhotoZoom(uri);
				}
			}
			break;
		case Constant.REQUESTCODE_LOCATION:// 本地选取
			if (data != null) {
				Uri uri = data.getData();
				LogTool.d(TAG, "uri:" + uri);
				if (null != uri) {
					startPhotoZoom(uri);
				}
			}
			break;
		case Constant.REQUESTCODE_CROP:
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					// 得到返回来的数据，是bitmap类型的数据
					Bitmap bitmap = extras.getParcelable("data");
					LogTool.d(TAG, "avatar - bitmap = " + bitmap);
					String imgPath = PhotoUtils.savaPicture(bitmap);
					LogTool.d(TAG, "imgPath=============" + imgPath);
					if (!TextUtils.isEmpty(imgPath)) {
						uploadManager.uploadCheckImage(imgPath, processInfoId,
								sectionInfoName, key, this);
					}
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onSuccess(String msg) {
		LogTool.d(TAG, "msg===========" + msg);
		if ("success".equals(msg)) {
			LogTool.d(TAG, "--------------------------------------------------");
		}

	}

	@Override
	public void onFailure() {
		// TODO Auto-generated method stub

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constant.REQUESTCODE_CROP);
	}

	private void onClickCheckDone() {
		CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(CheckActivity.this);
		dialog.setTitle("确认完工");
		dialog.setMessage("确定完工吗？");
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						confirmCheckDoneByOwner(processInfoId, sectionInfoName);
					}
				});
		dialog.setNegativeButton(R.string.no, null);
		dialog.show();
	}

	private void onClickCheckConfirm() {
		CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(CheckActivity.this);
		dialog.setTitle("确认验收");
		dialog.setMessage("确定验收吗？");
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						confirmCanCheckByDesigner(processInfoId,
								sectionInfoName);
					}
				});
		dialog.setNegativeButton(R.string.no, null);
		dialog.show();
	}

	// 设计师确认可以开始验收
	private void confirmCanCheckByDesigner(String siteid, String processid) {
		JianFanJiaApiClient.confirm_canCheckBydesigner(CheckActivity.this,
				siteid, processid, new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								JSONObject obj = new JSONObject(response
										.toString());
								String msg = obj
										.getString(Constant.SUCCESS_MSG);
								makeTextLong(msg);
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

	// 业主确认对比验收完成
	private void confirmCheckDoneByOwner(String siteid, String processid) {
		JianFanJiaApiClient.confirm_CheckDoneByOwner(CheckActivity.this,
				siteid, processid, new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								JSONObject obj = new JSONObject(response
										.toString());
								String msg = obj
										.getString(Constant.SUCCESS_MSG);
								makeTextLong(msg);
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

	/**
	 * 根据工序名获取验收图片
	 * 
	 * @param sectionName
	 * @return
	 */
	private List<GridItem> getCheckedImageById(String sectionName) {
		try {
			List<GridItem> gridList = new ArrayList<GridItem>();
			int arrId = getResources().getIdentifier(sectionName, "array",
					MyApplication.getInstance().getPackageName());
			TypedArray ta = getResources().obtainTypedArray(arrId);
			for (int i = 0; i < ta.length(); i++) {
				LogTool.d(TAG, "res id:" + ta.getResourceId(i, 0));
				GridItem item = new GridItem();
				item.setImgId("drawable://" + ta.getResourceId(i, 0));
				gridList.add(item);
			}
			return gridList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onReceiveMsg(NotifyMessage message) {
		LogTool.d(TAG, "message=" + message);
		// sendNotifycation(message);
		showNotify(message);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_check_pic;
	}

}
