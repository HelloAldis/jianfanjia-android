package com.jianfanjia.cn.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.jianfanjia.cn.config.Constant;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * 鏂囦欢鎿嶄綔宸ュ叿锟?
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class FileUtil {
	/**
	 * 鍐欐枃鏈枃锟? 鍦ˋndroid绯荤粺涓紝鏂囦欢淇濆瓨锟? /data/data/PACKAGE_NAME/files 鐩綍锟?
	 * 
	 * @param context
	 * @param msg
	 */
	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";

		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(content.getBytes());

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 璇诲彇鏂囨湰鏂囦欢
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String read(Context context, String fileName) {
		try {
			FileInputStream in = context.openFileInput(fileName);
			return readInStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String readInStream(InputStream inStream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}

			outStream.close();
			inStream.close();
			return outStream.toString();
		} catch (IOException e) {
			Log.i("FileTest", e.getMessage());
		}
		return null;
	}

	public static File createFile(String folderPath, String fileName) {
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		return new File(folderPath, fileName + fileName);
	}

	/**
	 * 鍚戞墜鏈哄啓鍥剧墖
	 * 
	 * @param buffer
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static boolean writeFile(byte[] buffer, String folder,
			String fileName) {
		boolean writeSucc = false;

		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

		String folderPath = "";
		if (sdCardExist) {
			folderPath = Environment.getExternalStorageDirectory()
					+ File.separator + folder + File.separator;
		} else {
			writeSucc = false;
		}

		File fileDir = new File(folderPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File file = new File(folderPath + fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return writeSucc;
	}

	/**
	 * 鏍规嵁鏂囦欢缁濆璺緞鑾峰彇鏂囦欢锟?
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 鏍规嵁鏂囦欢鐨勭粷瀵硅矾寰勮幏鍙栨枃浠跺悕浣嗕笉鍖呭惈鎵╁睍锟?
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
				point);
	}

	/**
	 * 鑾峰彇鏂囦欢鎵╁睍锟?
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 * 鑾峰彇鏂囦欢澶у皬
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 鑾峰彇鏂囦欢澶у皬
	 * 
	 * @param size
	 *            瀛楄妭
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float) size / 1024;
		if (temp >= 1024) {
			return df.format(temp / 1024) + "M";
		} else {
			return df.format(temp) + "K";
		}
	}

	/**
	 * 杞崲鏂囦欢澶у皬
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 鑾峰彇鐩綍鏂囦欢澶у皬
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 閫掑綊璋冪敤缁х画缁熻
			}
		}
		return dirSize;
	}

	/**
	 * 鑾峰彇鐩綍鏂囦欢涓暟
	 * 
	 * @param emojiFragment
	 * @return
	 */
	public long getFileList(File dir) {
		long count = 0;
		File[] files = dir.listFiles();
		count = files.length;
		for (File file : files) {
			if (file.isDirectory()) {
				count = count + getFileList(file);// 閫掑綊
				count--;
			}
		}
		return count;
	}

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}

	/**
	 * 锟?鏌ユ枃浠舵槸鍚﹀瓨锟?
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String name) {
		boolean status;
		if (!name.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + name);
			status = newPath.exists();
		} else {
			status = false;
		}
		return status;
	}

	/**
	 * 锟?鏌ヨ矾寰勬槸鍚﹀瓨锟?
	 * 
	 * @param path
	 * @return
	 */
	public static boolean checkFilePathExists(String path) {
		return new File(path).exists();
	}

	/**
	 * 璁＄畻SD鍗＄殑鍓╀綑绌洪棿
	 * 
	 * @return 杩斿洖-1锛岃鏄庢病鏈夊畨瑁卻d锟?
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = availableBlocks * blockSize / 1024;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		return (freeSpace);
	}

	/**
	 * 鏂板缓鐩綍
	 * 
	 * @param directoryName
	 * @return
	 */
	public static boolean createDirectory(String directoryName) {
		boolean status;
		if (!directoryName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			status = newPath.mkdir();
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 锟?鏌ユ槸鍚﹀畨瑁匰D锟?
	 * 
	 * @return
	 */
	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 锟?鏌ユ槸鍚﹀畨瑁呭缃殑SD锟?
	 * 
	 * @return
	 */
	public static boolean checkExternalSDExists() {

		Map<String, String> evn = System.getenv();
		return evn.containsKey("SECONDARY_STORAGE");
	}

	/**
	 * 鍒犻櫎鐩綍(鍖呮嫭锛氱洰褰曢噷鐨勬墍鏈夋枃锟?)
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteDirectory(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + "/"
								+ listfile[i].toString());
						deletedFile.delete();
					}
					newPath.delete();
					Log.i("DirectoryManager deleteDirectory", fileName);
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}

			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * 鍒犻櫎鏂囦欢
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isFile()) {
				try {
					Log.i("DirectoryManager deleteFile", fileName);
					newPath.delete();
					status = true;
				} catch (SecurityException se) {
					se.printStackTrace();
					status = false;
				}
			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * 鍒犻櫎绌虹洰锟?
	 * 
	 * 杩斿洖 0浠ｈ〃鎴愬姛 ,1 浠ｈ〃娌℃湁鍒犻櫎鏉冮檺, 2浠ｈ〃涓嶆槸绌虹洰锟?,3 浠ｈ〃鏈煡閿欒
	 * 
	 * @return
	 */
	public static int deleteBlankPath(String path) {
		File f = new File(path);
		if (!f.canWrite()) {
			return 1;
		}
		if (f.list() != null && f.list().length > 0) {
			return 2;
		}
		if (f.delete()) {
			return 0;
		}
		return 3;
	}

	/**
	 * 閲嶅懡锟?
	 * 
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public static boolean reNamePath(String oldName, String newName) {
		File f = new File(oldName);
		return f.renameTo(new File(newName));
	}

	/**
	 * 鍒犻櫎鏂囦欢
	 * 
	 * @param filePath
	 */
	public static boolean deleteFileWithPath(String filePath) {
		SecurityManager checker = new SecurityManager();
		File f = new File(filePath);
		checker.checkDelete(filePath);
		if (f.isFile()) {
			Log.i("DirectoryManager deleteFile", filePath);
			f.delete();
			return true;
		}
		return false;
	}

	/**
	 * 娓呯┖锟?涓枃浠跺す
	 * 
	 * @param files
	 */
	public static void clearFileWithPath(String filePath) {
		List<File> files = FileUtil.listPathFiles(filePath);
		if (files.isEmpty()) {
			return;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				clearFileWithPath(f.getAbsolutePath());
			} else {
				f.delete();
			}
		}
	}

	/**
	 * 鑾峰彇SD鍗＄殑鏍圭洰锟?
	 * 
	 * @return
	 */
	public static String getSDRoot() {

		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 鑾峰彇鎵嬫満澶栫疆SD鍗＄殑鏍圭洰锟?
	 * 
	 * @return
	 */
	public static String getExternalSDRoot() {

		Map<String, String> evn = System.getenv();

		return evn.get("SECONDARY_STORAGE");
	}

	/**
	 * 鍒楀嚭root鐩綍涓嬫墍鏈夊瓙鐩綍
	 * 
	 * @param path
	 * @return 缁濆璺緞
	 */
	public static List<String> listPath(String root) {
		List<String> allDir = new ArrayList<String>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		// 杩囨护鎺変互.锟?濮嬬殑鏂囦欢锟?
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				if (f.isDirectory() && !f.getName().startsWith("")) {
					allDir.add(f.getAbsolutePath());
				}
			}
		}
		return allDir;
	}

	/**
	 * 鑾峰彇锟?涓枃浠跺す涓嬬殑锟?鏈夋枃锟?
	 * 
	 * @param root
	 * @return
	 */
	public static List<File> listPathFiles(String root) {
		List<File> allDir = new ArrayList<File>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		File[] files = path.listFiles();
		for (File f : files) {
			if (f.isFile())
				allDir.add(f);
			else
				listPath(f.getAbsolutePath());
		}
		return allDir;
	}

	public enum PathStatus {
		SUCCESS, EXITS, ERROR
	}

	/**
	 * 鍒涘缓鐩綍
	 * 
	 * @param path
	 */
	public static PathStatus createPath(String newPath) {
		File path = new File(newPath);
		if (path.exists()) {
			return PathStatus.EXITS;
		}
		if (path.mkdir()) {
			return PathStatus.SUCCESS;
		} else {
			return PathStatus.ERROR;
		}
	}

	/**
	 * 鎴彇璺緞锟?
	 * 
	 * @return
	 */
	public static String getPathName(String absolutePath) {
		int start = absolutePath.lastIndexOf(File.separator) + 1;
		int end = absolutePath.length();
		return absolutePath.substring(start, end);
	}

	/**
	 * 鑾峰彇搴旂敤绋嬪簭缂撳瓨鏂囦欢澶逛笅鐨勬寚瀹氱洰锟?
	 * 
	 * @param context
	 * @param dir
	 * @return
	 */
	public static String getAppCache(Context context, String dir) {
		String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir
				+ "/";
		File savedir = new File(savePath);
		if (!savedir.exists()) {
			savedir.mkdirs();
		}
		savedir = null;
		return savePath;
	}

	public static File getSaveFile(String stringPath) {
		final File file = new File(stringPath);
		if (!file.exists()) {
			File parent = file.getParentFile();
			if (!parent.exists())
				parent.mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	// public static File createTmpFile(Context context) {
	// String state = Environment.getExternalStorageState();
	// if (state.equals(Environment.MEDIA_MOUNTED)) {
	// // 锟窖癸拷锟斤拷
	// File pic = Environment
	// .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	// String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
	// Locale.CHINA).format(new Date());
	// String fileName = "jyz_image_" + timeStamp + "";
	// File tmpFile = new File(pic, fileName + ".jpg");
	// return tmpFile;
	// } else {
	// File cacheDir = context.getCacheDir();
	// String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
	// Locale.CHINA).format(new Date());
	// String fileName = "jyz_image_" + timeStamp + "";
	// File tmpFile = new File(cacheDir, fileName + ".jpg");
	// return tmpFile;
	// }
	//
	// }

	public static File createTmpFile(Context context) {
		File fileParent = new File(Constant.IMAG_PATH);
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
				.format(new Date());
		String fileName = "jyz_image_" + timeStamp + "";
		File tmpFile = new File(fileParent, fileName + ".jpg");
		if (!tmpFile.exists()) {
			try {
				tmpFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tmpFile;
	}

}