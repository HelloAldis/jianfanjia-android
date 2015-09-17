package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import com.jianfanjia.cn.adapter.MyGridViewAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.GridItem;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.UploadImageListener;
import com.jianfanjia.cn.interf.UploadListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.PhotoUtils;

/**
 * 
 * @ClassName: CheckActivity
 * @Description: ����
 * @author fengliang
 * @date 2015-8-28 ����2:25:36
 * 
 */
public class CheckActivity extends BaseActivity implements OnClickListener,
		UploadListener, UploadImageListener {
	private static final String TAG = CheckActivity.class.getName();
	private TextView backView = null;// ������ͼ
	private GridView gridView = null;
	private Button btn_confirm_check = null;
	private static final int ICON[] = { R.drawable.pix_default,
			R.drawable.btn_icon_home_add, R.drawable.pix_default,
			R.drawable.btn_icon_home_add, R.drawable.pix_default,
			R.drawable.btn_icon_home_add };
	private List<GridItem> gridList = new ArrayList<GridItem>();
	private int currentList;// ��ǰ�Ĺ���
	private View view = null;

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
		view = inflater.inflate(R.layout.activity_check_pic, null);
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

			break;
		default:
			break;
		}
	}

	@Override
	public void onUpload(int position) {
		LogTool.d(TAG, "position:" + position);
		showPopWindow(view);
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
		startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
	}

	@Override
	public void takePhoto() {
		Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
		albumIntent.setType("image/*");
		startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.REQUESTCODE_CAMERA:// ����
			LogTool.d(TAG, "data:" + data);
			if (data != null) {
				Uri mImageUri = data.getData();
				LogTool.d(TAG, "mImageUri:" + mImageUri);
				if (mImageUri != null) {
					startPhotoZoom(mImageUri);
				}
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
						uploadManager.uploadCheckImage(imgPath, "", "", "",
								this);
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
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constant.REQUESTCODE_CROP);
	}

	/**
	 * ���ݹ�������ȡ����ͼƬ
	 * 
	 * @param sectionId
	 * @return
	 */
	private List<GridItem> getCheckedImageById(String sectionId) {
		List<GridItem> gridList = new ArrayList<GridItem>();
		return gridList;

	}

	@Override
	public void onReceiveMsg(NotifyMessage message) {
		LogTool.d(TAG, "message=" + message);
		sendNotifycation(message);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_check_pic;
	}

}
