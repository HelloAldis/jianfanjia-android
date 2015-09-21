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
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.OwnerUpdateInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.UserByOwnerInfoRequest;
import com.jianfanjia.cn.http.request.UserByOwnerInfoUpdateRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.interf.UploadImageListener;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.PhotoUtils;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.CommonWheelDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.cn.view.wheel.ArrayWheelAdapter;
import com.jianfanjia.cn.view.wheel.OnWheelChangedListener;
import com.jianfanjia.cn.view.wheel.WheelView;

/**
 * 
 * @ClassName: UserByOwnerInfoActivity
 * @Description:用户个人信息(业主)
 * @author fengliang
 * @date 2015-8-18 下午12:11:49
 * 
 */
public class UserByOwnerInfoActivity extends BaseActivity implements
		OnClickListener, UploadImageListener {
	private static final String TAG = UserByOwnerInfoActivity.class.getName();
	private RelativeLayout infoLayout = null;
	private RelativeLayout headLayout = null;
	private TextView ownerinfo_back = null;
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
	private String sex;

	private OwnerInfo ownerInfo;

	private OwnerUpdateInfo ownerUpdateInfo;

	private CommonWheelDialog commonWheelDialog;

	private static String[] provices = { "湖北", "湖南", "安徽" };
	private static String[] cities = { "武汉", "长沙", "合肥" };
	private static String[] areas = { "武昌", "汉口", "长沙县", "常州", "青山", "江夏", "汉阳" };

	private String provice;
	private String city;
	private String area;

	private File mTmpFile = null;

	@Override
	public void initView() {
		infoLayout = (RelativeLayout) findViewById(R.id.infoLayout);
		ownerinfo_back = (TextView) this.findViewById(R.id.ownerinfo_back);
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
		setConfimEnable(false);
		ownerInfo = dataManager.getOwnerInfo();
		if (ownerInfo == null) {
			get_Owner_Info();
		} else {
			setData();
			setOwnerUpdateInfo();
		}

		commonWheelDialog = new CommonWheelDialog(this);
	}

	private void setConfimEnable(boolean enabled) {
		btn_confirm.setEnabled(enabled);
	}

	private void setData() {
		imageLoader.displayImage(
				ownerInfo.getImageid() == null ? Constant.DEFALUT_OWNER_PIC
						: (Url.GET_IMAGE + ownerInfo.getImageid()),
				headImageView);
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
		ownerinfo_back.setOnClickListener(this);
		headLayout.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		// addressLayout.setOnClickListener(this);
		homeRelativeLayout.setOnClickListener(this);
		userNameRelativeLayout.setOnClickListener(this);
		sexRelativeLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ownerinfo_back:
			finish();
			break;
		case R.id.head_layout:
			showPopWindow(infoLayout);
			break;
		case R.id.btn_confirm:
			if (ownerUpdateInfo != null) {
				put_Owner_Info();
			}
			break;
		case R.id.address_layout:
			// showWheelDialog();
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
		commonWheelDialog.setWheelAdapter1(new ArrayWheelAdapter<String>(
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
		});
		commonWheelDialog.setTitle("选择地区");
		commonWheelDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						addressText.setText(provice + city + area);
						dialog.dismiss();
					}
				});
		commonWheelDialog.setNegativeButton(R.string.no, null);
		commonWheelDialog.show();
	}

	// 修改设计师个人资料
	private void put_Owner_Info() {
		LoadClientHelper.postOwnerUpdateInfo(this,
				new UserByOwnerInfoUpdateRequest(this, ownerUpdateInfo),
				new LoadDataListener() {

					@Override
					public void preLoad() {
						// TODO Auto-generated method stub
						showWaitDialog();
					}

					@Override
					public void loadSuccess() {
						// TODO Auto-generated method stub
						hideWaitDialog();
						makeTextLong("修改成功");
						setConfimEnable(false);
						if (!TextUtils.isEmpty(ownerUpdateInfo.getUsername())
								|| ownerUpdateInfo.getUsername() != dataManager
										.getUserName()) {
							dataManager.setUserName(ownerUpdateInfo
									.getUsername());
							sendBroadcast(new Intent(
									Constant.INTENT_ACTION_USERINFO_CHANGE));
						}
						updateOwnerInfo();
						dataManager.setOwnerInfo(ownerInfo);
					}

					@Override
					public void loadFailture() {
						// TODO Auto-generated method stub
						hideWaitDialog();
						makeTextLong(getString(R.string.tip_no_internet));
					}
				});
	}

	protected void updateOwnerInfo() {
		ownerInfo.setAddress(ownerUpdateInfo.getAddress());
		ownerInfo.setCity(ownerUpdateInfo.getCity());
		ownerInfo.setDistrict(ownerUpdateInfo.getDistrict());
		ownerInfo.setSex(ownerUpdateInfo.getSex());
		ownerInfo.setUsername(ownerUpdateInfo.getUsername());
	}

	@Override
	public void loadSuccess() {
		super.loadSuccess();
		ownerInfo = dataManager.getOwnerInfo();
		if (null != ownerInfo) {
			// dataManager.setOwnerInfo(ownerInfo);
			setOwnerUpdateInfo();
			setData();
		}
	}

	private void setOwnerUpdateInfo() {
		ownerUpdateInfo = new OwnerUpdateInfo();
		ownerUpdateInfo.setAddress(ownerInfo.getAddress());
		ownerUpdateInfo.setCity(ownerInfo.getCity());
		ownerUpdateInfo.setDistrict(ownerInfo.getDistrict());
		ownerUpdateInfo.setSex(ownerInfo.getSex());
		ownerUpdateInfo.setUsername(ownerInfo.getUsername());
	}

	private void get_Owner_Info() {
		LoadClientHelper.getUserInfoByOwner(this, new UserByOwnerInfoRequest(
				this), this);
	}

	@Override
	public void takecamera() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mTmpFile = FileUtil.createTmpFile(UserByOwnerInfoActivity.this);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
		startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
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
						uploadManager.uploadImage(imgPath);
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
			if (mTmpFile != null && mTmpFile.exists()) {
				mTmpFile.delete();
			}
		}
	}

	@Override
	public void onFailure() {
		LogTool.d(TAG, "==============================================");
		if (mTmpFile != null && mTmpFile.exists()) {
			mTmpFile.delete();
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
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constant.REQUESTCODE_CROP);
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
	public void onReceiveMsg(NotifyMessage message) {
		LogTool.d(TAG, "message=" + message);
		// sendNotifycation(message);
		showNotify(message);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_owner_info;
	}

}
