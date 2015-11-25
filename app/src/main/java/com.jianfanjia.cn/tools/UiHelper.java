package com.jianfanjia.cn.tools;

import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;

import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.config.Constant;

import java.io.File;

public class UiHelper {

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
//		if (isCameraCanUse()) {
			DataManagerNew.getInstance().setPicPath(tempFile.getAbsolutePath());
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			Uri uri = Uri.fromFile(tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			return intent;
//		} else {
//			return null;
//		}
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
