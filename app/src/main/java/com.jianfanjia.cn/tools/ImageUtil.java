package com.jianfanjia.cn.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;

/**
 * @author sf
 *
 */
/**
 * @author sf
 *
 */
/**
 * @author sf
 * 
 */
public class ImageUtil {
	/* Options used internally. */
	private static final int OPTIONS_NONE = 0x0;
	private static final int OPTIONS_SCALE_UP = 0x1;
	public static final int OPTIONS_RECYCLE_INPUT = 0x2;
	static int mBorderThickness = 2;

	public static byte[] transformBitmapToBytes(Bitmap bitmap) {
		byte[] buffer = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 灏哹itmap涓€瀛楄妭娴佽緭鍑?Bitmap.CompressFormat.PNG 鍘嬬缉鏍煎紡锛?00锛氬帇缂╃巼锛宐aos锛氬瓧鑺傛祦
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			buffer = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
		return buffer;
	}

	/**
	 * 鑾峰緱鍦嗚鍥剧墖
	 * 
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return bitmap;
		}
	}

	/**
	 * 鐢诲渾鐘跺浘鐗?
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap bmp) {
		int radius = 0;
		int side = 0;
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		side = bmpWidth > bmpHeight ? bmpHeight : bmpWidth;
		radius = side / 2 - mBorderThickness * 2;
		Bitmap out = Bitmap.createBitmap(side, side, Config.ARGB_4444);
		Canvas canvas = new Canvas(out);
		drawCircleBorder(canvas, radius + mBorderThickness / 2, side, side);
		Bitmap roundBitmap = getCroppedRoundBitmap(bmp, radius);
		canvas.drawBitmap(roundBitmap, side / 2 - radius, side / 2 - radius,
				null);
		return out;
	}

	/**
	 * 灏嗗浘鐗囪浆鍖栦负鍦嗗舰鍥剧墖
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();

		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = bitmap.getWidth() / 2;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 鑾峰緱甯﹀€掑奖鐨勫浘鐗囨柟娉?
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * 灏咲rawable杞寲涓築itmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}

	/**
	 * 鑾峰彇瑁佸壀骞堕€傚綋浼樺寲鍚庣殑鍥剧墖
	 * 
	 * @param filePath
	 *            鍥剧墖鏂囦欢璺緞
	 * @return 鐩爣灏哄浣嶅浘
	 */
	public static Bitmap getScaledBitmap(String filePath) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		/* 鍘熷浣嶅浘瀹藉拰楂?*/
		final int h = options.outHeight;
		final int w = options.outWidth;
		/* 鐩爣浣嶅浘瀹藉拰楂?*/
		final int reqHeight = 600;
		final int reqWidth = 600;
		/* 鑾峰彇瑁佸壀姣旂巼 */
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		/* 璁＄畻浣嶅浘缂╂斁姣旂巼 */
		float r = 1.0f;
		if (w >= reqWidth || h >= reqHeight) {
			if (w > h) {
				r = (float) w / reqWidth;
			} else {
				r = (float) h / reqHeight;
			}
		}

		/** 鑾峰彇瑁佸壀鍚庣殑浣嶅浘锛堝昂瀵稿ぇ浜庣洰鏍囦綅鍥撅級 */
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inDither = true;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];
		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		/* 鍥哄畾姣旂巼缂╂斁浣嶅浘 */
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createScaledBitmap(bm, (int) (w / r),
					(int) (h / r), true);
		} finally {
			if (bitmap != bm) {
				/* 閲婃斁璧勬簮 */
				if (bm != null && !bm.isRecycled()) {
					bm.recycle();
					bm = null;
				}
			}
		}
		/* 杩斿洖鐩爣浣嶅浘 */
		return bitmap;
	}

	/**
	 * 璁＄畻鍚堥€傜殑inSampleSize鍊?
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	/**
	 * 鎸夊浘鐗囧ぇ灏?鍏堝帇缂╁昂瀵?鍦ㄥ帇缂╄川閲?鍘嬬缉鍥剧墖
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 寮€濮嬭鍏ュ浘鐗囷紝姝ゆ椂鎶妎ptions.inJustDecodeBounds 璁惧洖true浜?
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 姝ゆ椂杩斿洖bm涓虹┖

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 600f;// 杩欓噷璁剧疆楂樺害涓?00f
		float ww = 600f;// 杩欓噷璁剧疆瀹藉害涓?80f
		// 缂╂斁姣斻€傜敱浜庢槸鍥哄畾姣斾緥缂╂斁锛屽彧鐢ㄩ珮鎴栬€呭鍏朵腑涓€涓暟鎹繘琛岃绠楀嵆鍙?
		int be = 1;// be=1琛ㄧず涓嶇缉鏀?
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 璁剧疆缂╂斁姣斾緥
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		if (bitmap != null) {
			LogTool.d("fjg", "after scaled bitmap width:" + bitmap.getWidth());
			LogTool.d("fjg", "after scaled bitmap height:" + bitmap.getHeight());
			LogTool.d("fjg", "after scaled newOpts width:" + newOpts.outWidth);
			LogTool.d("fjg", "after scaled newOpts height:" + newOpts.outHeight);
		}
		return bitmap;// 鍘嬬缉濂芥瘮渚嬪ぇ灏忓悗鍐嶈繘琛岃川閲忓帇缂?
	}

	/**
	 * 鎸夊浘鐗囧ぇ灏?鍏堝帇缂╁昂瀵?鍦ㄥ帇缂╄川閲?鍘嬬缉鍥剧墖
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getImageForConcurrent(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		newOpts.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 姝ゆ椂杩斿洖bm涓虹┖
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int standardH = 300;// 杩欓噷璁剧疆楂樺害涓?00 浣嗘槸 inSample涓? 鎵€浠ュ拰300 姣旇緝
		int standardW = 300;// 杩欓噷璁剧疆瀹藉害涓?00 鍚屼笂
		// 缂╂斁姣斻€傜敱浜庢槸鍥哄畾姣斾緥缂╂斁锛屽彧鐢ㄩ珮鎴栬€呭鍏朵腑涓€涓暟鎹繘琛岃绠楀嵆鍙?
		int be = 1;// be=1琛ㄧず涓嶇缉鏀?
		if (w > standardW || h > standardH) {
			while (w / be > standardW && h / be > standardH) {
				be *= 2;
			}
		}
		newOpts.inSampleSize = be;// 璁剧疆缂╂斁姣斾緥
		newOpts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		LogTool.d("fjg", "newOpts inSampleSize:" + be);
		LogTool.d("fjg", "after scaled bitmap width:" + bitmap.getWidth());
		LogTool.d("fjg", "after scaled bitmap height:" + bitmap.getHeight());
		LogTool.d("fjg", "after scaled newOpts width:" + newOpts.outWidth);
		LogTool.d("fjg", "after scaled newOpts height:" + newOpts.outHeight);
		return bitmap;// 鍘嬬缉濂芥瘮渚嬪ぇ灏忓悗鍐嶈繘琛岃川閲忓帇缂?
	}

	/**
	 * 鎸夊浘鐗囧ぇ灏?鍏堝帇缂╁昂瀵?鍦ㄥ帇缂╄川閲?鍘嬬缉鍥剧墖
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getScaledImageForConcurrent(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		newOpts.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 姝ゆ椂杩斿洖bm涓虹┖
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float standardH = 300f;// 杩欓噷璁剧疆楂樺害涓?00 浣嗘槸 inSample涓? 鎵€浠ュ拰300 姣旇緝
		float standardW = 300f;// 杩欓噷璁剧疆瀹藉害涓?00 鍚屼笂
		// 缂╂斁姣斻€傜敱浜庢槸鍥哄畾姣斾緥缂╂斁锛屽彧鐢ㄩ珮鎴栬€呭鍏朵腑涓€涓暟鎹繘琛岃绠楀嵆鍙?
		float be = 1f;// be=1琛ㄧず涓嶇缉鏀?
		if (w > standardW || h > standardH) {
			if (w > h) {
				be = w / standardW;
			} else {
				be = h / standardH;
			}
		}
		if (be <= 0) {
			be = 1f;
		}
		newOpts.inJustDecodeBounds = false;
		newOpts.inSampleSize = (int) be;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		if (be > 1) {
			return Bitmap.createScaledBitmap(bitmap, (int) (w * 2 / be),
					(int) (h * 2 / be), true);
		} else {
			return bitmap;// 鍘嬬缉濂芥瘮渚嬪ぇ灏忓悗鍐嶈繘琛岃川閲忓帇缂?
		}
	}

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 璐ㄩ噺鍘嬬缉鏂规硶锛岃繖閲?00琛ㄧず涓嶅帇缂╋紝鎶婂帇缂╁悗鐨勬暟鎹瓨鏀惧埌baos涓?
		int options = 100;
		while (baos.toByteArray().length / 1024 > 450) {
			baos.reset();// 閲嶇疆baos鍗虫竻绌篵aos
			options -= 10;// 姣忔閮藉噺灏?0
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 鎶婂帇缂╁悗鐨勬暟鎹産aos瀛樻斁鍒癇yteArrayInputStream涓?
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 鎶夿yteArrayInputStream鏁版嵁鐢熸垚鍥剧墖
		return bitmap;
	}

	/**
	 * 鎸夊浘鐗囧ぇ灏?瀛楄妭澶у皬)鍘嬬缉鍥剧墖
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap fitSizeImg(String path) {
		if (path == null || path.length() < 1)
			return null;
		File file = new File(path);
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 鏁板瓧瓒婂ぇ璇诲嚭鐨勫浘鐗囧崰鐢ㄧ殑heap瓒婂皬 涓嶇劧鎬绘槸婧㈠嚭
		if (file.length() < 20480) { // 0-20k
			opts.inSampleSize = 1;
		} else if (file.length() < 51200) { // 20-50k
			opts.inSampleSize = 2;
		} else if (file.length() < 307200) { // 50-300k
			opts.inSampleSize = 4;
		} else if (file.length() < 819200) { // 300-800k
			opts.inSampleSize = 6;
		} else if (file.length() < 1048576) { // 800-1024k
			opts.inSampleSize = 8;
		} else {
			opts.inSampleSize = 10;
		}
		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		return resizeBmp;
	}

	/**
	 * 鎸夊浘鐗囧ぇ灏?瀛楄妭澶у皬)鍘嬬缉鍥剧墖
	 * 
	 * @param uri
	 * @return
	 */
	public static Bitmap fitSizeImg(URI uri) {
		if (uri == null)
			return null;
		File file = new File(uri);
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 鏁板瓧瓒婂ぇ璇诲嚭鐨勫浘鐗囧崰鐢ㄧ殑heap瓒婂皬 涓嶇劧鎬绘槸婧㈠嚭
		if (file.length() < 20480) { // 0-20k
			opts.inSampleSize = 1;
		} else if (file.length() < 51200) { // 20-50k
			opts.inSampleSize = 2;
		} else if (file.length() < 307200) { // 50-300k
			opts.inSampleSize = 4;
		} else if (file.length() < 819200) { // 300-800k
			opts.inSampleSize = 6;
		} else if (file.length() < 1048576) { // 800-1024k
			opts.inSampleSize = 8;
		} else {
			opts.inSampleSize = 10;
		}
		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		return resizeBmp;
	}

	/**
	 * 缂╂斁鍥剧墖
	 * 
	 * @param source
	 *            婧愭枃浠?Bitmap绫诲瀷)
	 * @param width
	 *            杈撳嚭缂╃暐鍥剧殑瀹藉害
	 * @param height
	 *            杈撳嚭缂╃暐鍥剧殑楂樺害
	 * @return
	 */
	public static Bitmap extractThumbnail(Bitmap source, int width, int height) {
		return extractThumbnail(source, width, height, OPTIONS_NONE);
	}

	/**
	 * 缂╂斁鍥剧墖
	 * 
	 * @param source
	 *            婧愭枃浠?Bitmap绫诲瀷)
	 * @param width
	 *            杈撳嚭缂╃暐鍥剧殑瀹藉害
	 * @param height
	 *            杈撳嚭缂╃暐鍥剧殑楂樺害
	 * @param options
	 *            濡傛灉options瀹氫箟涓篛PTIONS_RECYCLE_INPUT,鍒欏洖鏀禓param source杩欎釜璧勬簮鏂囦欢
	 *            (闄ら潪缂╃暐鍥剧瓑浜嶡param source)
	 * @return
	 */
	public static Bitmap extractThumbnail(Bitmap source, int width, int height,
			int options) {
		if (source == null) {
			return null;
		}

		float scale;
		if (source.getWidth() < source.getHeight()) {
			scale = width / (float) source.getWidth();
		} else {
			scale = height / (float) source.getHeight();
		}
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap thumbnail = transform(matrix, source, width, height,
				OPTIONS_SCALE_UP | options);
		return thumbnail;
	}

	/**
	 * Transform source Bitmap to targeted width and height.
	 */
	private static Bitmap transform(Matrix scaler, Bitmap source,
			int targetWidth, int targetHeight, int options) {
		boolean scaleUp = (options & OPTIONS_SCALE_UP) != 0;
		boolean recycle = (options & OPTIONS_RECYCLE_INPUT) != 0;

		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
					Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
					+ Math.min(targetWidth, source.getWidth()), deltaYHalf
					+ Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
					- dstY);
			c.drawBitmap(source, src, dst, null);
			if (recycle) {
				source.recycle();
			}
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}

		if (recycle && b1 != source) {
			source.recycle();
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
				targetHeight);

		if (b2 != b1) {
			if (recycle || b1 != source) {
				b1.recycle();
			}
		}

		return b2;
	}

	/*
	 * 鑾峰彇缂╃暐鍥?
	 */

	public static Bitmap createVideoThumbnail(ContentResolver cr, String dataPah) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Config.ARGB_8888;
		Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				new String[] { BaseColumns._ID }, MediaColumns.DATA + "='"
						+ dataPah + "'", null, null);
		if (cursor == null || cursor.getCount() == 0) {
			LogTool.d("MM_VideoImageView", "鏁版嵁搴撲腑cursor涓虹┖");
			if (cursor != null) {
				cursor.close();
			}
			return null;
		}
		cursor.moveToFirst();
		String videoId = cursor.getString(cursor
				.getColumnIndex(BaseColumns._ID)); // image
													// id
													// in
													// image
													// table.s
		if (videoId == null) {
			if (cursor != null) {
				cursor.close();
			}
			LogTool.d("MM_VideoImageView", "鏁版嵁搴撲腑videoId涓虹┖");
			return null;
		}
		if (cursor != null) {
			cursor.close();
		}

		long videoIdLong = Long.parseLong(videoId);
		LogTool.d("MM_VideoImageView", "鏁版嵁搴撲腑videoIdLong=" + videoIdLong);
		bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, videoIdLong,
				Images.Thumbnails.MINI_KIND, options);
		return bitmap;
	}

	// 涓轰簡寰俊鍒嗕韩bitmap闄愬埗鍦?2浠ュ唴
	public static Bitmap fitSizeImgWX(String path) {
		if (path == null || path.length() < 1)
			return null;
		File file = new File(path);
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		if (file.length() < 1024 * 32) { // 0-32k
			opts.inSampleSize = 1;
		} else {
			opts.inSampleSize = (int) (file.length() / (1024 * 32)) + 1;
		}
		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		return resizeBmp;
	}

	public static void drawCircleBorder(Canvas canvas, int radius, int w, int y) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(mBorderThickness);
		canvas.drawCircle(w / 2, y / 2, radius, paint);
	}

	public static Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth) {
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else if (bmpHeight < bmpWidth) {
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else {
			squareBitmap = bmp;
		}

		if (squareBitmap.getWidth() != diameter
				|| squareBitmap.getHeight() != diameter) {
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
					diameter, true);

		} else {
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
				scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		return output;
	}

}
