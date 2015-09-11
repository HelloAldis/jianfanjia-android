package com.jianfanjia.cn.tools;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import android.graphics.Bitmap;
import com.jianfanjia.cn.config.Constant;

public class PhotoUtils {
	private static final String TAG = PhotoUtils.class.getName();

	/**
	 * ±£´æÍ¼Æ¬
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String savaPicture(Bitmap bitmap) {
		String pictureDir = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] byteArray = baos.toByteArray();
			File dir = new File(Constant.IMAG_PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(Constant.IMAG_PATH, System.currentTimeMillis()
					+ ".jpg");
			file.delete();
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray);
			pictureDir = file.getPath();
			LogTool.d(TAG, "pictureDir:" + pictureDir);
			return pictureDir;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return pictureDir;
	}

}
