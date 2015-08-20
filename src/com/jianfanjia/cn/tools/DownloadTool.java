package com.jianfanjia.cn.tools;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.http.Header;
import com.jianfanjia.cn.http.HttpRestClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

/**
 * 
 * @ClassName: DownloadTool
 * @Description:���ع�����
 * @author fengliang
 * @date 2015-8-18 ����4:08:17
 * 
 */
public class DownloadTool {
	private static final String TAG = "DownloadTool";

	public static void download(String url, final String filePath,
			final String fileName) {
		// ָ���ļ�����
		String[] allowedContentTypes = new String[] { "image/png",
				"image/jpeg", "application/octet-stream", "application/zip",
				"application/vnd.android.package-archive" };
		HttpRestClient.get(url, new BinaryHttpResponseHandler(
				allowedContentTypes) {

			@Override
			public void onStart() {
				LogTool.d(TAG, "onStart()");
			}

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				LogTool.d(TAG, "bytesWritten:" + bytesWritten + "  totalSize:"
						+ totalSize);
				String process = (int) ((bytesWritten * 1.0 / totalSize) * 100)
						+ "%";
				LogTool.d(TAG, "process:" + process);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogTool.d(TAG, "onSuccess()");
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				File apkFile = new File(file, fileName);
				try {
					FileOutputStream fos = new FileOutputStream(apkFile);
					fos.write(arg2);
					fos.flush();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogTool.d(TAG, "arg3:" + arg3);
			}
		});
	}
}
