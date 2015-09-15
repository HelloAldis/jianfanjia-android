package com.jianfanjia.cn.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.DesignerUpdateInfo;
import com.jianfanjia.cn.bean.Message;
import com.jianfanjia.cn.bean.MyDesignerInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.PhotoUtils;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: UserByDesignerInfoActivity
 * @Description:用户个人信息(设计师)
 * @author fengliang
 * @date 2015-9-5 下午2:01:42
 * 
 */
public class UserByDesignerInfoActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = UserByDesignerInfoActivity.class
			.getName();
	private RelativeLayout designerInfoLayout = null;
	private TextView ownerinfo_back = null;
	private RelativeLayout headLayout = null;
	private TextView nameText = null;
	private TextView sexText = null;
	private TextView phoneText = null;
	private TextView addressText = null;
	private TextView homeText = null;
	private Button btn_confirm = null;
	private ImageView headImageView = null;
	private RelativeLayout userNameRelativeLayout = null;
	private RelativeLayout homeRelativeLayout = null;
	private RelativeLayout sexLayout = null;
	private DesignerInfo designerInfo = null;
	private DesignerUpdateInfo designerUpdateInfo = null;

	@Override
	public void initView() {
		designerInfoLayout = (RelativeLayout) findViewById(R.id.designerInfoLayout);
		ownerinfo_back = (TextView) this.findViewById(R.id.ownerinfo_back);
		headLayout = (RelativeLayout) this.findViewById(R.id.head_layout);
		nameText = (TextView) this.findViewById(R.id.nameText);
		sexText = (TextView) this.findViewById(R.id.sexText);
		phoneText = (TextView) this.findViewById(R.id.phoneText);
		addressText = (TextView) this.findViewById(R.id.addressText);
		homeText = (TextView) this.findViewById(R.id.homeText);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		headImageView = (ImageView) this.findViewById(R.id.head_icon);
		userNameRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.name_layout);
		homeRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.home_layout);
		sexLayout = (RelativeLayout) this.findViewById(R.id.sex_layout);
		
		designerInfo = dataManager.getDesignerInfo();
		if(designerInfo == null){
			get_Designer_Info();
		}else{
			setData();
		}
	}

	private void setData() {
		imageLoader.displayImage(designerInfo.getImageid() == null? Constant.DEFALUT_OWNER_PIC : (Url.GET_IMAGE + designerInfo.getImageid()),headImageView);
		if (!TextUtils.isEmpty(designerInfo.getUsername())) {
			nameText.setText(designerInfo.getUsername());
		} else {
			nameText.setText("设计师");
		}
		String sexInfo = designerInfo.getSex();
		if (!TextUtils.isEmpty(sexInfo)) {
			if (sexInfo.equals(Constant.SEX_MAN)) {
				sexText.setText("男");
			} else {
				sexText.setText("女");
			}
		} else {
			sexText.setText(getString(R.string.not_edit));
		}
		if (!TextUtils.isEmpty(designerInfo.getPhone())) {
			phoneText.setText(designerInfo.getPhone());
		} else {
			phoneText.setText(getString(R.string.not_edit));
		}
		if (!TextUtils.isEmpty(designerInfo.getDistrict())) {
			addressText.setText(designerInfo.getDistrict());
		} else {
			addressText.setText(getString(R.string.not_edit));
		}
		if (!TextUtils.isEmpty(designerInfo.getAddress())) {
			homeText.setText(designerInfo.getAddress());
		} else {
			homeText.setText(getString(R.string.not_edit));
		}
	}

	@Override
	public void setListener() {
		ownerinfo_back.setOnClickListener(this);
		headLayout.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		userNameRelativeLayout.setOnClickListener(this);
		homeRelativeLayout.setOnClickListener(this);
		sexLayout.setOnClickListener(this);
	}
	
	//修改设计师个人资料
	private void put_Designer_Info(){
		JianFanJiaApiClient.put_DesignerInfo(this, designerInfo,
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
					if (response.has(Constant.SUCCESS_MSG)) {
						makeTextLong("修改成功");
					} else if (response.has(Constant.ERROR_MSG)) {
						makeTextLong(response.get(Constant.ERROR_MSG)
								.toString());
					}
				} catch (JSONException e) {
					e.printStackTrace();
					makeTextLong(getString(R.string.load_failure));
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LogTool.d(TAG,
						"Throwable throwable:" + throwable.toString());
				makeTextLong(getString(R.string.tip_no_internet));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LogTool.d(TAG, "throwable:" + throwable);
				makeTextLong(getString(R.string.tip_no_internet));
			};
		});
	}

	private void get_Designer_Info() {
		JianFanJiaApiClient.get_Designer_Info(UserByDesignerInfoActivity.this,
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
								designerInfo = JsonParser
										.jsonToBean(response.get(Constant.DATA)
												.toString(),
												DesignerInfo.class);
								if (null != designerInfo) {
									dataManager.setDesignerInfo(designerInfo);
									setData();
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
							makeTextLong(getString(R.string.load_failure));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_no_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_no_internet));
					};
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ownerinfo_back:
			finish();
			break;
		case R.id.head_layout:
			showPopWindow(designerInfoLayout);
			break;
		case R.id.btn_confirm:
			break;
		case R.id.name_layout:
			Intent name = new Intent(UserByDesignerInfoActivity.this,
					EditInfoActivity.class);
			name.putExtra(Constant.EDIT_TYPE,
					Constant.REQUESTCODE_EDIT_USERNAME);
			startActivityForResult(name, Constant.REQUESTCODE_EDIT_USERNAME);
			break;
		case R.id.address_layout:
			Intent address = new Intent(UserByDesignerInfoActivity.this,
					EditInfoActivity.class);
			address.putExtra(Constant.EDIT_TYPE,
					Constant.REQUESTCODE_EDIT_ADDRESS);
			startActivityForResult(address, Constant.REQUESTCODE_EDIT_ADDRESS);
			break;
		case R.id.sex_layout:
			showSexChooseDialog();
			break;
		default:
			break;
		}
	}

	private void showSexChooseDialog() {
		CommonDialog commonDialog = DialogHelper.getPinterestDialogCancelable(this);
		View contentView = inflater.inflate(R.layout.sex_choose, null);
		commonDialog.setContent(contentView);
		commonDialog.setTitle("选择性别");
		commonDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		commonDialog.setNegativeButton(R.string.no, null);
		commonDialog.show();
	}

	@Override
	public void takecamera() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
	}

	@Override
	public void takePhoto() {
		Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
		albumIntent.setType("image/*");
		startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.REQUESTCODE_EDIT_USERNAME:
			
			break;
		case Constant.REQUESTCODE_EDIT_ADDRESS:
			break;
		case Constant.REQUESTCODE_CAMERA:// 拍照
			LogTool.d(TAG, "data:" + data);
			if (data != null) {
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");
				LogTool.d(TAG, "bitmap:" + bitmap);
				Uri mImageUri = null;
				if (null != data.getData()) {
					mImageUri = data.getData();
				} else {
					mImageUri = Uri.parse(MediaStore.Images.Media.insertImage(
							getContentResolver(), bitmap, null, null));
				}
				LogTool.d(TAG, "mImageUri:" + mImageUri);
				startPhotoZoom(mImageUri);
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

					}
				}
			}
			break;
		default:
			break;
		}
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
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constant.REQUESTCODE_CROP);
	}

	@Override
	public void onReceiveMsg(Message message) {
		LogTool.d(TAG, "message=" + message);
		showNotify(message);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_designer_info;
	}

}
