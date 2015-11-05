package com.jianfanjia.cn.activity;

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
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.OwnerUpdateInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.PhotoUtils;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CityWheelDialog;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import java.io.File;

/**
 * 
 * @ClassName: UserByOwnerInfoActivity
 * @Description:用户个人信息(业主)
 * @author fengliang
 * @date 2015-8-18 下午12:11:49
 * 
 */
public class UserByOwnerInfoActivity extends BaseActivity implements
		OnClickListener,PopWindowCallBack{
	private static final String TAG = UserByOwnerInfoActivity.class.getName();
	private RelativeLayout headLayout = null;
	private RelativeLayout ownerInfoLayout = null;
	private ScrollView scrollView = null;
	private View errorView = null;
	private TextView nameText = null;
	private TextView sexText = null;
	private TextView phoneText = null;
	private TextView addressText = null;
	private TextView homeText = null;
	private ImageView headImageView = null;
	private Button btn_confirm = null;
	private RelativeLayout addressLayout;
	private RelativeLayout userNameRelativeLayout = null;
	private RelativeLayout homeRelativeLayout = null;
	private RelativeLayout sexRelativeLayout = null;
	private RelativeLayout phoneLayout = null;
	private String sex = null;

	private MainHeadView mainHeadView;

	private OwnerInfo ownerInfo = null;

	private OwnerUpdateInfo ownerUpdateInfo = null;

	private CityWheelDialog cityWheelDialog = null;

	private static String[] provices = { "湖北", "湖南", "安徽" };
	private static String[] cities = { "武汉", "长沙", "合肥" };
	private static String[] areas = { "武昌", "汉口", "长沙县", "常州", "青山", "江夏", "汉阳" };

	private String provice = null;
	private String city = null;
	private String area = null;

	private File mTmpFile = null;
	private String imageId = null;

	@Override
	public void initView() {
		initMainHead();
		ownerInfoLayout = (RelativeLayout) this
				.findViewById(R.id.ownerinfoLayout);
		scrollView = (ScrollView) this.findViewById(R.id.ownerinfo_scrollview);
		errorView = this.findViewById(R.id.error_view);
		headLayout = (RelativeLayout) this.findViewById(R.id.head_layout);
		nameText = (TextView) this.findViewById(R.id.nameText);
		sexText = (TextView) this.findViewById(R.id.sexText);
		phoneText = (TextView) this.findViewById(R.id.phoneText);
		addressText = (TextView) this.findViewById(R.id.addressText);
		homeText = (TextView) this.findViewById(R.id.homeText);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		addressLayout = (RelativeLayout) this.findViewById(R.id.address_layout);
		headImageView = (ImageView) this.findViewById(R.id.head_icon);
		userNameRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.name_layout);
		homeRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.home_layout);
		sexRelativeLayout = (RelativeLayout) this.findViewById(R.id.sex_layout);
		phoneLayout = (RelativeLayout) this.findViewById(R.id.phone_layout);
		phoneLayout.setEnabled(false);
		setConfimEnable(false);
		ownerInfo = dataManager.getOwnerInfo();
		if (ownerInfo == null) {
			get_Owner_Info();
		} else {
			setData();
			setOwnerUpdateInfo();
		}
//		commonWheelDialog = new CommonWheelDialog(this);
		cityWheelDialog = new CityWheelDialog(this,false,null);
	}

	private void initMainHead() {
		mainHeadView = (MainHeadView) findViewById(R.id.ownerinfo_head_layout);
		mainHeadView.setBackListener(this);
		mainHeadView.setMianTitle(getResources().getString(R.string.userinfo));
	}

	private void setConfimEnable(boolean enabled) {
		btn_confirm.setEnabled(enabled);
	}

	public void setViewChange() {
		errorView.setVisibility(View.GONE);
		scrollView.setVisibility(View.VISIBLE);
	}

	@Override
	public void setErrorView() {
		((TextView) errorView.findViewById(R.id.tv_error)).setText("暂无个人信息数据");
	}

	private void setData() {
		setViewChange();
		imageLoader
				.displayImage(
						TextUtils.isEmpty(ownerInfo.getImageid()) ? Constant.DEFALUT_OWNER_PIC
								: (Url_New.GET_IMAGE + ownerInfo.getImageid()),
						headImageView, options);
		nameText.setText(TextUtils.isEmpty(ownerInfo.getUsername()) ? getString(R.string.ower)
				: ownerInfo.getUsername());
		String sexInfo = ownerInfo.getSex();
		if (!TextUtils.isEmpty(sexInfo)) {
			if (sexInfo.equals(Constant.SEX_MAN)) {
				sexText.setText("男");
			} else if (sexInfo.equals(Constant.SEX_WOMEN)) {
				sexText.setText("女");
			}
		} else {
			sexText.setText(getString(R.string.not_edit));
		}
		phoneText
				.setText(TextUtils.isEmpty(ownerInfo.getPhone()) ? getString(R.string.not_edit)
						: ownerInfo.getPhone());
		addressText
				.setText(TextUtils.isEmpty(ownerInfo.getDistrict()) ? getString(R.string.not_edit)
						: ownerInfo.getDistrict());
		homeText.setText(TextUtils.isEmpty(ownerInfo.getAddress()) ? getString(R.string.not_edit)
				: ownerInfo.getAddress());
	}

	@Override
	public void setListener() {
		headLayout.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		 addressLayout.setOnClickListener(this);
		homeRelativeLayout.setOnClickListener(this);
		userNameRelativeLayout.setOnClickListener(this);
		sexRelativeLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_back_layout:
			finish();
			break;
		case R.id.head_layout:
			showPopWindow(ownerInfoLayout);
			break;
		case R.id.btn_confirm:
			if (ownerUpdateInfo != null) {
				put_Owner_Info();
			}
			break;
		case R.id.address_layout:
			 showWheelDialog();
			break;
		case R.id.name_layout:
			Intent name = new Intent(UserByOwnerInfoActivity.this,
					EditInfoActivity.class);
			name.putExtra(Constant.EDIT_TYPE,
					Constant.REQUESTCODE_EDIT_USERNAME);
			startActivityForResult(name, Constant.REQUESTCODE_EDIT_USERNAME);
			break;
		case R.id.home_layout:
			Intent address = new Intent(UserByOwnerInfoActivity.this,
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
		if (ownerUpdateInfo != null) {
			if (!TextUtils.isEmpty(ownerUpdateInfo.getSex())) {
				radioGroup.check(ownerUpdateInfo.getSex().equals(
						Constant.SEX_MAN) ? R.id.sex_radio0 : R.id.sex_radio1);
				sex = ownerUpdateInfo.getSex().equals(Constant.SEX_MAN) ? Constant.SEX_MAN
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
						if (ownerUpdateInfo != null) {
							if (ownerUpdateInfo.getSex() == null
									|| !ownerUpdateInfo.getSex().equals(sex)) {
								ownerUpdateInfo.setSex(sex);
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

	private void showWheelDialog() {
		/*commonWheelDialog.setWheelAdapter1(new ArrayWheelAdapter<String>(
				provices));
		commonWheelDialog
				.setWheelAdapter2(new ArrayWheelAdapter<String>(cities));
		commonWheelDialog
				.setWheelAdapter3(new ArrayWheelAdapter<String>(areas));
		commonWheelDialog.setWheelChangeListen(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (wheel == commonWheelDialog.getWheelView1()) {
					commonWheelDialog.getWheelView1().setCurrentItem(newValue);
					provice = provices[commonWheelDialog.getWheelView1()
							.getCurrentItem()];
				} else if (wheel == commonWheelDialog.getWheelView2()) {
					commonWheelDialog.getWheelView2().setCurrentItem(newValue);
					city = cities[commonWheelDialog.getWheelView2()
							.getCurrentItem()];
				} else if (wheel == commonWheelDialog.getWheelView3()) {
					commonWheelDialog.getWheelView3().setCurrentItem(newValue);
					area = areas[commonWheelDialog.getWheelView3()
							.getCurrentItem()];
				}
			}
		});*/
		cityWheelDialog.setTitle("选择地区");
		cityWheelDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						addressText.setText(provice + city + area);
						dialog.dismiss();
					}
				});
		cityWheelDialog.setNegativeButton(R.string.no, null);
		cityWheelDialog.show();
	}

	// 修改设计师个人资料
	private void put_Owner_Info() {
		JianFanJiaClient.put_OwnerInfo(this, ownerUpdateInfo,
				new ApiUiUpdateListener() {

					@Override
					public void preLoad() {
						showWaitDialog();
					}

					@Override
					public void loadSuccess(Object data) {
						hideWaitDialog();
						makeTextShort("修改成功");
						setConfimEnable(false);
						if (!TextUtils.isEmpty(ownerUpdateInfo.getUsername())
								|| ownerUpdateInfo.getUsername() != dataManager
								.getUserName()) {
							dataManager.setUserName(ownerUpdateInfo
									.getUsername());
						}
						updateOwnerInfo();
						dataManager.setOwnerInfo(ownerInfo);
					}

					@Override
					public void loadFailture(String error_msg) {
						hideWaitDialog();
						makeTextLong(getString(R.string.tip_error_internet));
					}
				}, this);
	}

	protected void updateOwnerInfo() {
		ownerInfo.setImageid(ownerUpdateInfo.getImageid());
		ownerInfo.setAddress(ownerUpdateInfo.getAddress());
		ownerInfo.setCity(ownerUpdateInfo.getCity());
		ownerInfo.setDistrict(ownerUpdateInfo.getDistrict());
		ownerInfo.setSex(ownerUpdateInfo.getSex());
		ownerInfo.setUsername(ownerUpdateInfo.getUsername());
	}

	@Override
	public void loadSuccess(Object data) {
		super.loadSuccess(data);
		ownerInfo = dataManager.getOwnerInfo();
		if (null != ownerInfo) {
			setOwnerUpdateInfo();
			setData();
		} else {
			setErrorView();
		}
	}

	private void setOwnerUpdateInfo() {
		ownerUpdateInfo = new OwnerUpdateInfo();
		ownerUpdateInfo.setImageid(ownerInfo.getImageid());
		ownerUpdateInfo.setAddress(ownerInfo.getAddress());
		ownerUpdateInfo.setCity(ownerInfo.getCity());
		ownerUpdateInfo.setDistrict(ownerInfo.getDistrict());
		ownerUpdateInfo.setSex(ownerInfo.getSex());
		ownerUpdateInfo.setUsername(ownerInfo.getUsername());
	}

	private void get_Owner_Info() {
		JianFanJiaClient.get_Owner_Info(this, this, this);
	}

	@Override
	public void firstItemClick() {
		/*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mTmpFile = FileUtil.createTmpFile(UserByOwnerInfoActivity.this);
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
	public void secondItemClick() {
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
				if (ownerUpdateInfo != null) {
					if (TextUtils.isEmpty(ownerUpdateInfo.getUsername())
							|| !name.equals(ownerUpdateInfo.getUsername())) {
						ownerUpdateInfo.setUsername(name);
						setConfimEnable(true);
					}
				}
			}
			break;
		case Constant.REQUESTCODE_EDIT_ADDRESS:
			if (data != null) {
				String address = data.getStringExtra(Constant.EDIT_CONTENT);
				homeText.setText(address);
				if (ownerUpdateInfo != null) {
					if (TextUtils.isEmpty(ownerUpdateInfo.getAddress())
							|| !ownerUpdateInfo.getAddress().equals(address)) {
						setConfimEnable(true);
						ownerUpdateInfo.setAddress(address);
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
					LogTool.d(TAG, "imgPath==" + imgPath);
					if (!TextUtils.isEmpty(imgPath)) {
//						uploadManager.uploadPortrait(imgPath, this);
						JianFanJiaClient.uploadImage(this,bitmap,new ApiUiUpdateListener(){
							@Override
							public void preLoad() {

							}

							@Override
							public void loadSuccess(Object data) {
								if(data != null) {
									String imageid = data.toString();
									LogTool.d(TAG, "imageid:" + imageid);
									if (!TextUtils.isEmpty(imageid)) {
										getImageId(imageid);
									}
								}
							}

							@Override
							public void loadFailture(String msg) {

							}
						},this);
					}
				}
			}
			break;
		default:
			break;
		}
	}

	public void getImageId(String imageid) {
		LogTool.d(TAG, "imageid=" + imageid);
		imageId = imageid;
		dataManager.setUserImagePath(imageId);
		imageLoader.displayImage(
				TextUtils.isEmpty(imageId) ? Constant.DEFALUT_OWNER_PIC
						: (Url_New.GET_IMAGE + imageId), headImageView, options);
		if (ownerUpdateInfo != null) {
			ownerUpdateInfo.setImageid(imageId);
		}
		if (mTmpFile != null && mTmpFile.exists()) {
			mTmpFile.delete();
		}
		setConfimEnable(true);
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
		return R.layout.activity_owner_info;
	}

}
