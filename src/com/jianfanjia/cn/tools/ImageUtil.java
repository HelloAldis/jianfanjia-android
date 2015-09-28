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
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

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
			// 将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
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
	 * 获得圆角图片
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
	 * 画圆状图片
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
	 * 将图片转化为圆形图片
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
	 * 获得带倒影的图片方法
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
	 * 将Drawable转化为Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}

	/**
	 * 获取裁剪并适当优化后的图片
	 * 
	 * @param filePath
	 *            图片文件路径
	 * @return 目标尺寸位图
	 */
	public static Bitmap getScaledBitmap(String filePath) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		/* 原始位图宽和高 */
		final int h = options.outHeight;
		final int w = options.outWidth;
		/* 目标位图宽和高 */
		final int reqHeight = 600;
		final int reqWidth = 600;
		/* 获取裁剪比率 */
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		/* 计算位图缩放比率 */
		float r = 1.0f;
		if (w >= reqWidth || h >= reqHeight) {
			if (w > h) {
				r = (float) w / reqWidth;
			} else {
				r = (float) h / reqHeight;
			}
		}

		/** 获取裁剪后的位图（尺寸大于目标位图） */
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inDither = true;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];
		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		/* 固定比率缩放位图 */
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createScaledBitmap(bm, (int) (w / r),
					(int) (h / r), true);
		} finally {
			if (bitmap != bm) {
				/* 释放资源 */
				if (bm != null && !bm.isRecycled()) {
					bm.recycle();
					bm = null;
				}
			}
		}
		/* 返回目标位图 */
		return bitmap;
	}

	/**
	 * 计算合适的inSampleSize值
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
	 * 按图片大小(先压缩尺寸 在压缩质量)压缩图片
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 600f;// 这里设置高度为800f
		float ww = 600f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		if(bitmap != null){
			LogTool.d("fjg", "after scaled bitmap width:" + bitmap.getWidth());
			LogTool.d("fjg", "after scaled bitmap height:" + bitmap.getHeight());
			LogTool.d("fjg", "after scaled newOpts width:" + newOpts.outWidth);
			LogTool.d("fjg", "after scaled newOpts height:" + newOpts.outHeight);
		}
		return bitmap;// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 按图片大小(先压缩尺寸 在压缩质量)压缩图片
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getImageForConcurrent(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		newOpts.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int standardH = 300;// 这里设置高度为600 但是 inSample为2 所以和300 比较
		int standardW = 300;// 这里设置宽度为600 同上
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > standardW || h > standardH) {
			while (w / be > standardW && h / be > standardH) {
				be *= 2;
			}
		}
		newOpts.inSampleSize = be;// 设置缩放比例
		newOpts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		LogTool.d("fjg", "newOpts inSampleSize:" + be);
		LogTool.d("fjg", "after scaled bitmap width:" + bitmap.getWidth());
		LogTool.d("fjg", "after scaled bitmap height:" + bitmap.getHeight());
		LogTool.d("fjg", "after scaled newOpts width:" + newOpts.outWidth);
		LogTool.d("fjg", "after scaled newOpts height:" + newOpts.outHeight);
		return bitmap;// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 按图片大小(先压缩尺寸 在压缩质量)压缩图片
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getScaledImageForConcurrent(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		newOpts.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float standardH = 300f;// 这里设置高度为600 但是 inSample为2 所以和300 比较
		float standardW = 300f;// 这里设置宽度为600 同上
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		float be = 1f;// be=1表示不缩放
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
			return bitmap;// 压缩好比例大小后再进行质量压缩
		}
	}

	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 450) {
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 按图片大小(字节大小)压缩图片
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
		// 数字越大读出的图片占用的heap越小 不然总是溢出
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
	 * 按图片大小(字节大小)压缩图片
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
		// 数字越大读出的图片占用的heap越小 不然总是溢出
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
	 * 缩放图片
	 * 
	 * @param source
	 *            源文件(Bitmap类型)
	 * @param width
	 *            输出缩略图的宽度
	 * @param height
	 *            输出缩略图的高度
	 * @return
	 */
	public static Bitmap extractThumbnail(Bitmap source, int width, int height) {
		return extractThumbnail(source, width, height, OPTIONS_NONE);
	}

	/**
	 * 缩放图片
	 * 
	 * @param source
	 *            源文件(Bitmap类型)
	 * @param width
	 *            输出缩略图的宽度
	 * @param height
	 *            输出缩略图的高度
	 * @param options
	 *            如果options定义为OPTIONS_RECYCLE_INPUT,则回收@param source这个资源文件
	 *            (除非缩略图等于@param source)
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
					Bitmap.Config.ARGB_8888);
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
	 * 获取缩略图
	 */

	public static Bitmap createVideoThumbnail(ContentResolver cr, String dataPah) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Video.Media._ID },
				MediaStore.Video.Media.DATA + "='" + dataPah + "'", null, null);
		if (cursor == null || cursor.getCount() == 0) {
			LogTool.d("MM_VideoImageView", "数据库中cursor为空");
			if (cursor != null) {
				cursor.close();
			}
			return null;
		}
		cursor.moveToFirst();
		String videoId = cursor.getString(cursor
				.getColumnIndex(MediaStore.Video.Media._ID)); // image
																// id
																// in
																// image
																// table.s
		if (videoId == null) {
			if (cursor != null) {
				cursor.close();
			}
			LogTool.d("MM_VideoImageView", "数据库中videoId为空");
			return null;
		}
		if (cursor != null) {
			cursor.close();
		}

		long videoIdLong = Long.parseLong(videoId);
		LogTool.d("MM_VideoImageView", "数据库中videoIdLong=" + videoIdLong);
		bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, videoIdLong,
				Images.Thumbnails.MINI_KIND, options);
		return bitmap;
	}

	// 为了微信分享bitmap限制在32以内
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
