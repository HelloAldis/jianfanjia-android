package com.jianfanjia.cn.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-11-19 09:33
 */
public class ImageShow {
    public static final String TAG = "ImageShow";
    private static ImageShow imageShow;
    protected ImageLoader imageLoader;
    protected DisplayImageOptions options;

    public ImageShow() {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pix_default)
                .showImageForEmptyUri(R.mipmap.pix_default)
                .showImageOnFail(R.mipmap.pix_default).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }

    public static ImageShow getImageShow() {
        if (imageShow == null) {
            imageShow = new ImageShow();
        }
        return imageShow;
    }

    /**
     * 显示原图
     *
     * @param imageid
     * @param imageView
     */
    public void displayImage(String imageid, ImageView imageView) {
        imageLoader.displayImage(Url_New.GET_IMAGE + imageid, imageView, options);
    }

    /**
     * 显示缩略图
     *
     * @param imageView
     * @param imageid
     * @param width
     */
    public void displayThumbnailImage(String imageid, ImageView imageView, int width) {
        String imageUrl = Url_New.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, width + "") + imageid;
        imageLoader.displayImage(imageUrl, imageView, options);
    }

    public void displayThumbnailImage(String imageid, ImageView imageView, int width, ImageLoadingListener listener) {
        String imageUrl = Url_New.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, width + "") + imageid;
        imageLoader.displayImage(imageUrl, imageView, options, listener);
    }

    /**
     * 显示本地图片
     *
     * @param imageView
     * @param imgPath
     */
    public void displayLocalImage(String imgPath, ImageView imageView) {
        if (!imgPath.contains(Constant.DEFALUT_PIC_HEAD)) {
            LogTool.d(TAG, "this is not local iamge");
            return;
        }
        imageLoader.displayImage(imgPath, imageView);
    }

    /**
     * 显示屏幕宽的缩略图
     *
     * @param context
     * @param imageView
     * @param imageid
     */
    public void displayScreenWidthThumnailImage(Context context, String imageid, ImageView imageView) {
        displayThumbnailImage(imageid, imageView, ScreenUtil.getScreenWidth(context));
    }

    public void displayHalfScreenWidthThumnailImage(Context context, String imageid, ImageView imageView) {
        displayThumbnailImage(imageid, imageView, ScreenUtil.getScreenWidth(context) / 2);
    }

    public void displayScreenWidthThumnailImage(Context context, String imageid, ImageView imageView, ImageLoadingListener listener) {
        displayThumbnailImage(imageid, imageView, ScreenUtil.getScreenWidth(context), listener);
    }

    public void displayHalfScreenWidthThumnailImage(Context context, String imageid, ImageView imageView, ImageLoadingListener listener) {
        displayThumbnailImage(imageid, imageView, ScreenUtil.getScreenWidth(context) / 2, listener);
    }

    /**
     * 显示应用内的用户头像的缩略图，头像宽度为60dp
     *
     * @param context
     * @param imageView
     * @param imageid
     */
    public void displayImageHeadWidthThumnailImage(Context context, String imageid, ImageView imageView) {
        int width = MyApplication.dip2px(context, Global.PIC_WIDTH_SHOW_WIDTH);
        displayThumbnailImage(imageid, imageView, width);
    }

}
