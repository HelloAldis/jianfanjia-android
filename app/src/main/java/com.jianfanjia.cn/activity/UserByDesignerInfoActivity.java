package com.jianfanjia.cn.activity;

import java.io.File;
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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.DesignerUpdateInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.UserByDesignerInfoRequest;
import com.jianfanjia.cn.http.request.UserByDesignerInfoUpdateRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.interf.UploadPortraitListener;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.PhotoUtils;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

/**
 * 
 * @ClassName: UserByDesignerInfoActivity
 * @Description:用户个人信息(设计师)
 * @author fengliang
 * @date 2015-9-5 下午2:01:42
 * 
 */
public class UserByDesignerInfoActivity extends BaseActivity implements
		OnClickListener, UploadPortraitListener {
	private static final String TAG = UserByDesignerInfoActivity.class
			.getName();
	private RelativeLayout designerInfoLayout = null;
	private ScrollView scrollView = null;
	private View errorView = null;
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
	private RelativeLayout addressRelativeLayout = null;
	private RelativeLayout sexLayout = null;
	private DesignerInfo designerInfo = null;
	private DesignerUpdateInfo designerUpdateInfo = null;
	private String sex = null;
	private File mTmpFile = null;
	private String imageId = null;

	private MainHeadView mainHeadView;

	@Override
	public void initView() {
		initMainHead();
		designerInfoLayout = (RelativeLayout) this
				.findViewById(R.id.designerInfoLayout);
		scrollView = (ScrollView) this
				.findViewById(R.id.designerinfo_scrollview);
		errorView = this.findViewById(R.id.error_view);
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
		addressRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.address_layout);
		sexLayout = (RelativeLayout) this.findViewById(R.id.sex_layout);
		setConfimEnable(false);
		designerInfo = dataManager.getDesignerInfo();
		if (designerInfo == null) {
			get_Designer_Info();
		} else {
			setData();
			setDesignerUpdateInfo();
		}
	}

	private void initMainHead() {
		mainHeadView = (MainHeadView) findViewById(R.id.designerinfo_head_layout);
		mainHeadView.setBackListener(this);
		mainHeadView.setMianTitle(getResources().getString(R.string.userinfo));
	}

	public void setViewChange() {
		errorView.setVisibility(View.GONE);
		scrollView.setVisibility(View.VISIBLE);
	}

	@Override
	public void setErrorView() {
		((TextView) errorView.findViewById(R.id.tv_error)).setText("暂无个人信息数据");
	}

	private void setConfimEnable(boolean enabled) {
		btn_confirm.setEnabled(enabled);
	}

	private void setData() {
		setViewChange();
		imageLoader
				.displayImage(
						TextUtils.isEmpty(designerInfo.getImageid()) ? Constant.DEFALUT_OWNER_PIC
								: (Url.GET_IMAGE + designerInfo.getImageid()),
						headImageView, options);
		if (!TextUtils.isEmpty(designerInfo.getUsername())) {
			nameText.setText(designerInfo.getUsername());
		} else {
			nameText.setText("设计师");
		}
		String sexInfo = designerInfo.getSex();
		if (!TextUtils.isEmpty(sexInfo)) {
			if (sexInfo.equals(Constant.SEX_MAN)) {
				sexText.setText("男");
			} else if (sexInfo.equals(Constant.SEX_WOMEN)) {
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
		String address = designerInfo.getProvince() + designerInfo.getCity()
				+ designerInfo.getDistrict();
		if (!TextUtils.isEmpty(address)) {
			addressText.setText(address);
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
		headLayout.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		userNameRelativeLayout.setOnClickListener(this);
		homeRelativeLayout.setOnClickListener(this);
		sexLayout.setOnClickListener(this);
		// addressRelativeLayout.setOnClickListener(this);
	}

	// 修改设计师个人资料
	private void put_Designer_Info() {
		LoadClientHelper.postDesignerUpdateInfo(this,
				new UserByDesignerInfoUpdateRequest(this, designerUpdateInfo),
				new LoadDataListener() {

					@Override
					public void preLoad() {
						// TODO Auto-generated method stub
						showWaitDialog();
						LogTool.d(TAG,
								JsonParser.beanToJson(designerUpdateInfo));
					}

					@Override
					public void loadSuccess() {
						hideWaitDialog();
						makeTextLong("修改成功");
						setConfimEnable(false);
						if (!TextUtils.isEmpty(designerUpdateInfo.getUsername())
								|| designerUpdateInfo.getUsername() != dataManager
										.getUserName()) {
							dataManager.setUserName(designerUpdateInfo
									.getUsername());
						}
						updateUpdateInfo();
						dataManager.setDesignerInfo(designerInfo);
					}

					@Override
					public void loadFailture() {
						hideWaitDialog();
						makeTextLong(getString(R.string.tip_error_internet));
					}
				});
	}

	private void updateUpdateInfo() {
		designerInfo.setImageid(designerUpdateInfo.getImageid());
		designerInfo.setAddress(designerUpdateInfo.getAddress());
		designerInfo.setCity(designerUpdateInfo.getCity());
		designerInfo.setDistrict(designerUpdateInfo.getDistrict());
		designerInfo.setSex(designerUpdateInfo.getSex());
		designerInfo.setUsername(designerUpdateInfo.getUsername());
	}

	@Override
	public void loadSuccess() {
		super.loadSuccess();
		designerInfo = dataManager.getDesignerInfo();
		if (null != designerInfo) {
			setData();
			setDesignerUpdateInfo();
		} else {
			setErrorView();
		}
	}

	private void setDesignerUpdateInfo() {
		designerUpdateInfo = new DesignerUpdateInfo();
		designerUpdateInfo.setImageid(designerInfo.getImageid());
		designerUpdateInfo.setAddress(designerInfo.getAddress());
		designerUpdateInfo.setCity(designerInfo.getCity());
		designerUpdateInfo.setDistrict(designerInfo.getDistrict());
		designerUpdateInfo.setSex(designerInfo.getSex());
		designerUpdateInfo.setUsername(designerInfo.getUsername());
	}

	private void get_Designer_Info() {
		LoadClientHelper.getUserInfoByDesigner(this,
				new UserByDesignerInfoRequest(this), this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_back_layout:
			finish();
			break;
		case R.id.head_layout:
			showPopWindow(designerInfoLayout);
			break;
		case R.id.btn_confirm:
			if (designerUpdateInfo != null) {
				put_Designer_Info();
			}
			break;
		case R.id.name_layout:
			Intent name = new Intent(UserByDesignerInfoActivity.this,
					EditInfoActivity.class);
			name.putExtra(Constant.EDIT_TYPE,
					Constant.REQUESTCODE_EDIT_USERNAME);
			startActivityForResult(name, Constant.REQUESTCODE_EDIT_USERNAME);
			break;
		case R.id.home_layout:
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

	// 显示选择性别对话框
	private void showSexChooseDialog() {
		CommonDialog commonDialog = DialogHelper
				.getPinterestDialogCancelable(this);
		View contentView = inflater.inflate(R.layout.sex_choose, null);
		RadioGroup radioGroup = (RadioGroup) contentView
				.findViewById(R.id.sex_radioGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (group.getCheckedRadioButtonId() == R.id.sex_radio0) {
					sex = Constant.SEX_MAN;
				} else {
					sex = Constant.SEX_WOMEN;
				}
			}
		});
		if (designerUpdateInfo != null) {
			if (!TextUtils.isEmpty(designerUpdateInfo.getSex())) {
				radioGroup.check(designerUpdateInfo.getSex().equals(
						Constant.SEX_MAN) ? R.id.sex_radio0 : R.id.sex_radio1);
				sex = designerUpdateInfo.getSex().equals(Constant.SEX_MAN) ? Constant.SEX_MAN
						: Constant.SEX_WOMEN;
			} else {
				radioGroup.check(R.id.sex_radio0);
				sex = Constant.SEX_MAN;
			}
		}
		commonDialog.setContent(contentView);
		commonDialog.setTitle("选择性别");
		commonDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (designerUpdateInfo != null) {
							if (TextUtils.isEmpty(designerUpdateInfo.getSex())
									|| !designerUpdateInfo.getSex().equals(sex)) {
								designerUpdateInfo.setSex(sex);
								setConfimEnable(true);
								sexText.setText(sex.equals(Constant.SEX_MAN) ? "男"
										: "女");
							}
						}
						dialog.dismiss();
					}
				});
		commonDialog.setNegativeButton(R.string.no, null);
		commonDialog.show();
	}

	@Override
	public void takecamera() {
		/*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mTmpFile = FileUtil.createTmpFile(UserByDesignerInfoActivity.this);
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
		Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
		albumIntent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.REQUESTCODE_EDIT_USERNAME:
			if (data != null) {
				String name = data.getStringExtra(Constant.EDIT_CONTENT);
				nameText.setText(name);
				if (designerUpdateInfo != null) {
					if (TextUtils.isEmpty(designerUpdateInfo.getUsername())
							|| !name.equals(designerUpdateInfo.getUsername())) {
						designerUpdateInfo.setUsername(name);
						setConfimEnable(true);
					}
				}
			}
			break;
		case Constant.REQUESTCODE_EDIT_ADDRESS:
			if (data != null) {
				String address = data.getStringExtra(Constant.EDIT_CONTENT);
				homeText.setText(address);
				if (designerUpdateInfo != null) {
					if (TextUtils.isEmpty(designerUpdateInfo.getAddress())
							|| !designerUpdateInfo.getAddress().equals(address)) {
						setConfimEnable(true);
						designerUpdateInfo.setAddress(address);
					}
				}
			}
			break;
		case Constant.REQUESTCODE_CAMERA:// 拍照
			mTmpFile = new File(dataManager.getPicPath());
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
						uploadManager.uploadPortrait(imgPath, this);
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
		intent.putExtra("scale", true);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constant.REQUESTCODE_CROP);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_designer_info;
	}

	@Override
	public void getImageId(String imageid) {
		LogTool.d(TAG, "imageid=" + imageid);
		imageId = imageid;
		dataManager.setUserImagePath(imageId);
		imageLoader.displayImage(
				TextUtils.isEmpty(imageId) ? Constant.DEFALUT_OWNER_PIC
						: (Url.GET_IMAGE + imageId), headImageView, options);
		if (designerUpdateInfo != null) {
			designerUpdateInfo.setImageid(imageId);
		}
		if (mTmpFile != null && mTmpFile.exists()) {
			mTmpFile.delete();
		}
		setConfimEnable(true);
	}

}
