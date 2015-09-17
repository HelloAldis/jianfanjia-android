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
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.PhotoUtils;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.CommonWheelDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.cn.view.wheel.ArrayWheelAdapter;
import com.jianfanjia.cn.view.wheel.OnWheelChangedListener;
import com.jianfanjia.cn.view.wheel.WheelView;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: UserByOwnerInfoActivity
 * @Description:�û�������Ϣ(ҵ��)
 * @author fengliang
 * @date 2015-8-18 ����12:11:49
 * 
 */
public class UserByOwnerInfoActivity extends BaseActivity implements
		OnClickListener {
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

	public static String[] provices = { "����", "����", "����" };
	public static String[] cities = { "�人", "��ɳ", "�Ϸ�" };
	public static String[] areas = { "���", "����", "��ɳ��", "����", "��ɽ", "����", "����" };

	private String provice;
	private String city;
	private String area;

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

		ownerInfo = dataManager.getOwnerInfo();
		if (ownerInfo == null) {
			get_Owner_Info();
		} else {
			setData();
		}

		commonWheelDialog = new CommonWheelDialog(this);
	}

	private void setData() {
		imageLoader.displayImage(
				ownerInfo.getImageid() == null ? Constant.DEFALUT_OWNER_PIC
						: (Url.GET_IMAGE + ownerInfo.getImageid()),
				headImageView);
		nameText.setText(ownerInfo.getUsername() == null ? getString(R.string.ower)
				: ownerInfo.getUsername());
		String sexInfo = ownerInfo.getSex();
		if (sexInfo != null) {
			if (sexInfo.equals(Constant.SEX_MAN)) {
				sexText.setText("��");
			} else if (sexInfo.equals(Constant.SEX_WOMEN)) {
				sexText.setText("Ů");
			}
		} else {
			sexText.setText(getString(R.string.not_edit));
		}
		phoneText.setText(ownerInfo.getPhone());
		addressText
				.setText(ownerInfo.getDistrict() == null ? getString(R.string.not_edit)
						: ownerInfo.getDistrict());
		homeText.setText(ownerInfo.getAddress() == null ? getString(R.string.not_edit)
				: ownerInfo.getAddress());
	}

	@Override
	public void setListener() {
		ownerinfo_back.setOnClickListener(this);
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

	// ��ʾѡ���Ա�Ի���
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
			radioGroup
					.check(ownerUpdateInfo.getSex().equals(Constant.SEX_MAN) ? R.id.sex_radio0
							: R.id.sex_radio1);
		}
		commonDialog.setContent(contentView);
		commonDialog.setTitle("ѡ���Ա�");
		commonDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (ownerUpdateInfo != null) {
							ownerUpdateInfo.setSex(sex);
							sexText.setText(sex.equals(Constant.SEX_MAN) ? "��"
									: "Ů");
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
		commonWheelDialog.setTitle("ѡ�����");
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

	// �޸����ʦ��������
	private void put_Owner_Info() {
		JianFanJiaApiClient.put_OwnerInfo(this, ownerInfo,
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
								makeTextLong("�޸ĳɹ�");
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

	private void get_Owner_Info() {
		JianFanJiaApiClient.get_Owner_Info(UserByOwnerInfoActivity.this,
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
								ownerInfo = JsonParser.jsonToBean(
										response.get(Constant.DATA).toString(),
										OwnerInfo.class);
								ownerUpdateInfo = JsonParser.jsonToBean(
										response.get(Constant.DATA).toString(),
										OwnerUpdateInfo.class);
								if (null != ownerInfo) {
									// dataManager.setOwnerInfo(ownerInfo);
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
			if (data != null) {
				String name = data.getStringExtra(Constant.EDIT_CONTENT);
				nameText.setText(name);
				if (ownerUpdateInfo != null) {
					ownerUpdateInfo.setUsername(name);
				}
			}
			break;
		case Constant.REQUESTCODE_EDIT_ADDRESS:
			if (data != null) {
				String address = data.getStringExtra(Constant.EDIT_CONTENT);
				homeText.setText(address);
				if (ownerUpdateInfo != null) {
					ownerUpdateInfo.setAddress(address);
				}
			}
			break;
		case Constant.REQUESTCODE_CAMERA:// ����
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
		case Constant.REQUESTCODE_LOCATION:// ����ѡȡ
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
					// �õ������������ݣ���bitmap���͵�����
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
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
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
		sendNotifycation(message);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_owner_info;
	}

}
