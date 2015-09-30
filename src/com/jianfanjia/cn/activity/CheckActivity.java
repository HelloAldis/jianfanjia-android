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
import com.jianfanjia.cn.bean.CheckInfo.Imageid;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.AddPicToCheckRequest;
import com.jianfanjia.cn.http.request.DeletePicRequest;
import com.jianfanjia.cn.http.request.ProcessInfoRequest;
import com.jianfanjia.cn.http.request.UploadPicRequestNew;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.interf.UploadListener;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.ImageUtils;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.UiHelper;
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
		UploadListener, ItemClickCallBack {
	private static final String TAG = CheckActivity.class.getName();
	public static final int EDIT_STATUS = 0;
	public static final int FINISH_STATUS = 1;
	public static final String SHOW_LIST = "show_list";
	public static final String POSITION = "position";
	
	private RelativeLayout checkLayout = null;
	private TextView backView = null;// 返回视图
	private TextView check_pic_title = null;
	private TextView check_pic_edit = null;
	private GridView gridView = null;
	private TextView btn_confirm = null;
	private MyGridViewAdapter adapter = null;
	private List<GridItem> checkGridList = new ArrayList<GridItem>();
	private List<String> showSamplePic = new ArrayList<String>();
	private List<String> showProcessPic = new ArrayList<String>();
	private List<Imageid> imageids = null;
	private String processInfoId = null;// 工地id
	private String sectionInfoName = null;// 工序名称
	private int sectionInfoStatus = -1;// 工序状态
	private String key = null;
	private File mTmpFile = null;
	private ProcessInfo processInfo;
	private int currentState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			sectionInfoName = bundle.getString(Constant.PROCESS_NAME);
			sectionInfoStatus = bundle.getInt(Constant.PROCESS_STATUS, 0);
			LogTool.d(TAG, "processInfoId:" + processInfoId
					+ " sectionInfoName:" + sectionInfoName
					+ " processInfoStatus:" + sectionInfoStatus);
		}
		if (NetTool.isNetworkAvailable(this)) {
			loadCurrentProcess();
		} else {
			initProcessInfo();
		}
	}

	private void initProcessInfo() {
		processInfo = dataManager.getDefaultProcessInfo();
		if (processInfo != null) {
			processInfoId = processInfo.get_id();
			initData();
		}
	}

	private void loadCurrentProcess() {
		LoadClientHelper.requestProcessInfoById(this, new ProcessInfoRequest(
				this, dataManager.getDefaultProcessId()),
				new LoadDataListener() {

					@Override
					public void preLoad() {
						showWaitDialog();
					}

					@Override
					public void loadSuccess() {
						hideWaitDialog();
						initProcessInfo();
					}

					@Override
					public void loadFailture() {
						hideWaitDialog();
						initProcessInfo();
					}
				});
	}

	private void initShowList() {
		showProcessPic.clear();
		showSamplePic.clear();
		for (int i = 0; i < checkGridList.size(); i = i + 2) {
			showSamplePic.add(checkGridList.get(i).getImgId());
		}
		for (int i = 1; i < checkGridList.size(); i = i + 2) {
			if (checkGridList.get(i).getImgId() != null) {
				showProcessPic.add(checkGridList.get(i).getImgId());
			}
		}
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
				check_pic_edit.setVisibility(View.VISIBLE);
				check_pic_edit.setText("编辑");
				currentState = EDIT_STATUS;
			}
		}
		gridView.setFocusable(false);
	}

	private void initData() {
		check_pic_title.setText(MyApplication.getInstance().getStringById(
				sectionInfoName)
				+ "阶段验收");
		switch (sectionInfoStatus) {
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
		adapter = new MyGridViewAdapter(CheckActivity.this, checkGridList,
				this, this);
		gridView.setAdapter(adapter);
		initList();
	}

	private void initList() {
		checkGridList.clear();
		checkGridList = getCheckedImageById(sectionInfoName);
		processInfo = dataManager.getDefaultProcessInfo();
		if (processInfo == null) {
			processInfo = dataManager.getProcessInfoById(processInfoId);
		}
		if (processInfo != null) {
			dataManager.setCurrentProcessInfo(processInfo);
			LogTool.d(TAG, "processInfo != null");
			imageids = processInfo.getImageidsByName(sectionInfoName);
			int imagecount = 0;
			for (int i = 0; imageids != null && i < imageids.size(); i++) {
				String key = imageids.get(i).getKey();
				if (imageids.get(i).getImageid() != null) {
					LogTool.d(TAG, imageids.get(i).getImageid());
					checkGridList.get(Integer.parseInt(key) * 2 + 1).setImgId(
							imageids.get(i).getImageid());
					imagecount++;
				}
			}
			setConfimStatus(imagecount);
			adapter.setList(checkGridList);
		}
		initShowList();
	}

	private void setConfimStatus(int count) {
		if (!TextUtils.isEmpty(userIdentity)) {
			if (userIdentity.equals(Constant.IDENTITY_OWNER)) {
				btn_confirm.setText(this.getResources().getString(
						R.string.confirm_done));
				if (count < BusinessManager
						.getCheckPicCountBySection(sectionInfoName)) {
					btn_confirm.setEnabled(false);
				} else {
					btn_confirm.setEnabled(true);
					btn_confirm.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onClickCheckDone();
						}
					});
				}
				if (sectionInfoStatus == Constant.FINISH) {
					btn_confirm.setEnabled(false);
				}
			} else if (userIdentity.equals(Constant.IDENTITY_DESIGNER)) {
				btn_confirm.setText(this.getResources().getString(
						R.string.confirm_upload));
				if (count < BusinessManager
						.getCheckPicCountBySection(sectionInfoName)) {
					btn_confirm.setText(this.getResources().getString(
							R.string.confirm_upload));
					btn_confirm.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});
				} else {
					btn_confirm.setText(this.getResources().getString(
							R.string.confirm_tip));
					btn_confirm.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onClickCheckConfirm();
						}
					});
				}
				if (sectionInfoStatus == Constant.FINISH) {
					btn_confirm.setEnabled(false);
				}
			}
		}

		if (sectionInfoStatus == Constant.FINISH) {
			btn_confirm.setEnabled(false);
		}
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
		check_pic_edit.setOnClickListener(this);
	}

	public void changeEditStatus() {
		if (currentState == FINISH_STATUS) {
			check_pic_edit.setText("编辑");
			currentState = EDIT_STATUS;
			adapter.setCanDelete(false);
			btn_confirm.setEnabled(true);
			adapter.notifyDataSetInvalidated();
		} else {
			btn_confirm.setEnabled(false);
			check_pic_edit.setText("完成");
			currentState = FINISH_STATUS;
			adapter.setCanDelete(true);
			adapter.notifyDataSetInvalidated();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.check_pic_back:
			finish();
			break;
		case R.id.check_pic_edit:
			changeEditStatus();
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
	public void delete(int position) {
		LogTool.d(TAG, "position:" + position);
		key = position + "";
		LogTool.d(TAG, "key:" + key);
		LoadClientHelper.delete_Image(this, new DeletePicRequest(this,
				processInfoId, sectionInfoName, key), this);
	}

	@Override
	public void loadSuccess() {
		super.loadSuccess();
		initList();
		changeEditStatus();
	}

	@Override
	public void takecamera() {
		/*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mTmpFile = FileUtil.createTmpFile(CheckActivity.this);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
		startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);*/
		
		mTmpFile = UiHelper.getTempPath();
		if(mTmpFile != null){
			Intent cameraIntent = UiHelper.createShotIntent(mTmpFile);
			startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
		}else{
			makeTextLong("没有sd卡，无法打开相机");
		}
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
			mTmpFile = new File(dataManager.getPicPath());
			if (mTmpFile != null) {
				Uri uri = Uri.fromFile(mTmpFile);
				LogTool.d(TAG, "uri:" + uri);
				uploadImage(mTmpFile.getPath());
			}
			break;
		case Constant.REQUESTCODE_LOCATION:// 本地选取
			if (data != null) {
				Uri uri = data.getData();
				LogTool.d(TAG, "uri:" + uri);
				if (null != uri) {
					uploadImage(ImageUtils.getImagePath(uri, this));
				}
			}
			break;
		default:
			break;
		}
	}

	private void uploadImage(String imagePath) {
		Bitmap imageBitmap = ImageUtil.getImage(imagePath);
		if (null != imageBitmap) {
			LoadClientHelper.upload_Image(this, new UploadPicRequestNew(this,
					imageBitmap), new LoadDataListener() {

				@Override
				public void preLoad() {
					showWaitDialog();
				}

				@Override
				public void loadSuccess() {
					AddPicToCheckRequest addPicToCheckRequest = new AddPicToCheckRequest(
							CheckActivity.this, processInfoId, sectionInfoName,
							key, dataManager.getCurrentUploadImageId());
					LoadClientHelper.submitCheckedImg(CheckActivity.this,
							addPicToCheckRequest, new LoadDataListener() {

								@Override
								public void preLoad() {
									// TODO Auto-generated
									// method stub

								}

								@Override
								public void loadSuccess() {
									hideWaitDialog();
									initList();
									adapter.notifyDataSetChanged();
								}

								@Override
								public void loadFailture() {
									hideWaitDialog();
									makeTextLong(getString(R.string.tip_error_internet));
								}
							});
				}

				@Override
				public void loadFailture() {
					hideWaitDialog();
					makeTextLong(getString(R.string.tip_error_internet));
				}
			});
		}
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
								btn_confirm.setEnabled(false);
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
								btn_confirm.setEnabled(false);
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
	public int getLayoutId() {
		return R.layout.activity_check_pic;
	}

	private void startShowActivity(int arg2) {
		Bundle bundle = new Bundle();
		if (arg2 % 2 == 0) {
			bundle.putStringArrayList(Constant.IMAGE_LIST,
					(ArrayList<String>) showSamplePic);
			bundle.putInt(Constant.CURRENT_POSITION, arg2 / 2);
		} else {
			String currentImageId = checkGridList.get(arg2).getImgId();
			for (int i = 0; i < showProcessPic.size(); i++) {
				if (currentImageId.equals(showProcessPic.get(i))) {
					arg2 = i;
				}
			}
			bundle.putStringArrayList(Constant.IMAGE_LIST,
					(ArrayList<String>) showProcessPic);
			bundle.putInt(Constant.CURRENT_POSITION, arg2);

		}
		startActivity(ShowPicActivity.class, bundle);
	}

	@Override
	public void click(int position, int itemType) {
		LogTool.d(TAG, "position = " + position);
		startShowActivity(position);
	}

	@Override
	public void click(int position, int itemType, List<String> imageUrlList) {
		// TODO Auto-generated method stub

	}

}
