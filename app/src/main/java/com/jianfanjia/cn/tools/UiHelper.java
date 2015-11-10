package com.jianfanjia.cn.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.UpdateVersion;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.service.UpdateService;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import java.io.File;

public class UiHelper {

	/**
	 * 检查新版本
	 * @param context
	 * @param listener
	 */
	public static void checkNewVersion(Context context,ApiUiUpdateListener listener){
		JianFanJiaClient.checkVersion(context, listener, context);
	}

	/**
	 * 显示新版本对话框
	 * @param context
	 * @param message
	 * @param updateVersion
	 */
	public static void showNewVersionDialog(final Context context,String message,final UpdateVersion updateVersion){
		CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(context);
		dialog.setTitle("版本更新");
		dialog.setMessage(message);
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						startUpdateService(context,updateVersion.getDownload_url());
					}

				});
		dialog.setNegativeButton(R.string.no, null);
		dialog.show();
	}

	/**
	 * 开启更新服务
	 * @param context
	 * @param download_url
	 */
	public static void startUpdateService(Context context,String download_url){
		if (download_url == null)
			return;
		Intent intent = new Intent(context, UpdateService.class);
		intent.putExtra(Constant.DOWNLOAD_URL, download_url);
		context.startService(intent);
	}

	/**
	 * @param root         最外层布局，需要调整的布局
	 * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
	 */
	public static void controlKeyboardLayout(final View root, final View scrollToView) {
		root.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect rect = new Rect();
						// 获取root在窗体的可视区域
						root.getWindowVisibleDisplayFrame(rect);
						// 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
						int rootInvisibleHeight = root.getRootView()
								.getHeight() - rect.bottom;
						// 若不可视区域高度大于100，则键盘显示
						if (rootInvisibleHeight > 100) {
							int[] location = new int[2];
							// 获取scrollToView在窗体的坐标
							scrollToView.getLocationInWindow(location);
							// 计算root滚动高度，使scrollToView在可见区域
							int srollHeight = (location[1] + scrollToView
									.getHeight()) - rect.bottom;
							root.scrollTo(0, srollHeight);
						} else {
							// 键盘隐藏
							root.scrollTo(0, 0);
						}
					}
				});
	}

	/**
	 * 动态计算listview 的高度
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount())-1);
//        listView.getDividerHeight();//获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}


	/**
	 * 获取路径
	 * 
	 * @return
	 */
	public static File getTempPath() {
		File tempFile = null;
		if (checkSDCard()) {
			File dirFile = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + Constant.IMAG_PATH);
			tempFile = new File(dirFile, System.currentTimeMillis() + ".jpg");
			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdirs();
			}
		}
		return tempFile;
	}
	
	/**
	 * 检查sd卡
	 * 
	 * @return
	 */
	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 拍照
	 * 
	 * @return
	 */
	public static Intent createShotIntent(File tempFile) {
		if (isCameraCanUse()) {
			DataManagerNew.getInstance().setPicPath(tempFile.getAbsolutePath());
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			Uri uri = Uri.fromFile(tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			return intent;
		} else {
			return null;
		}
	}
	
	public static boolean isCameraCanUse() {
		boolean canUse = true;
		Camera mCamera = null;
		try {
			mCamera = Camera.open();
			LogTool.d("camera", "can open");
		} catch (Exception e) {
			LogTool.d("camera", "can't open");
			canUse = false;
		}
		if (canUse) {
			if (null != mCamera) {
				mCamera.release();
				mCamera = null;
			}
		}
		return canUse;
	}

}
