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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Message;
import com.jianfanjia.cn.bean.UserByOwnerInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.PhotoUtils;
import com.jianfanjia.cn.view.dialog.CommonWheelDialog;
import com.jianfanjia.cn.view.wheel.ArrayWheelAdapter;
import com.jianfanjia.cn.view.wheel.OnWheelChangedListener;
import com.jianfanjia.cn.view.wheel.WheelView;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: UserByOwnerInfoActivity
 * @Description:用户个人信息(业主)
 * @author fengliang
 * @date 2015-8-18 下午12:11:49
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
	private Button btn_confirm = null;
	private RelativeLayout addressLayout;
	private RelativeLayout userNameRelativeLayout = null;
	private RelativeLayout homeRelativeLayout = null;

	private CommonWheelDialog commonWheelDialog;

	public static String[] provices = { "湖北", "湖南", "安徽" };
	public static String[] cities = { "武汉", "长沙", "合肥" };
	public static String[] areas = { "武昌", "汉口", "长沙县", "常州", "青山", "江夏", "汉阳" };

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
		addressLayout = (RelativeLayout) this.findViewById(R.id.home_layout);
		userNameRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.name_layout);
		homeRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.home_layout);

		get_Owner_Info();

		commonWheelDialog = new CommonWheelDialog(this);
	}

	@Override
	public void setListener() {
		ownerinfo_back.setOnClickListener(this);
		headLayout.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		addressLayout.setOnClickListener(this);
		homeRelativeLayout.setOnClickListener(this);
		userNameRelativeLayout.setOnClickListener(this);
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
		default:
			break;
		}
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
								UserByOwnerInfo info = JsonParser.jsonToBean(
										response.get(Constant.DATA).toString(),
										UserByOwnerInfo.class);
								if (null != info) {
									nameText.setText(info.getUsername() == null ? getString(R.string.ower)
											: info.getUsername());
									String sexInfo = info.getSex();
									if (sexInfo != null) {
										if (sexInfo.equals("1")) {
											sexText.setText("男");
										} else {
											sexText.setText("女");
										}
									} else {
										sexText.setText(getString(R.string.not_edit));
									}
									phoneText.setText(info.getPhone());
									addressText
											.setText(info.getDistrict() == null ? getString(R.string.not_edit)
													: info.getDistrict());
									homeText.setText(info.getAddress() == null ? getString(R.string.not_edit)
											: info.getAddress());
								}
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
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
		return R.layout.activity_owner_info;
	}

}
