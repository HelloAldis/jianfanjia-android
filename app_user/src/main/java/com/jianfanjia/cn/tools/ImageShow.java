package com.jianfanjia.cn.tools;

import android.content.Context;
import android.widget.ImageView;

import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.nostra13.universalimageloader.core.ImageLoader;
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
    protected Url_New url_new;

    public ImageShow() {
        imageLoader = ImageLoader.getInstance();
        url_new = Url_New.getInstance();
    }

    public static ImageShow getImageShow() {
        if (imageShow == null) {
            synchronized (ImageShow.class) {
                if (imageShow == null) {
                    imageShow = new ImageShow();
                }
            }
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
        imageLoader.displayImage(url_new.GET_IMAGE + imageid, imageView, DisplayImageOptionsWrap.getDisplayImageOptionsIsMemoryCache(true));
    }

    /**
     * 显示缩略图
     *
     * @param imageView
     * @param imageid
     * @param width
     */
    public void displayThumbnailImage(String imageid, ImageView imageView, int width) {
        String imageUrl = url_new.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, width + "") + imageid;
        imageLoader.displayImage(imageUrl, imageView, DisplayImageOptionsWrap.getDisplayImageOptionsIsMemoryCache(true));
    }

    public void displayThumbnailImage(String imageid, ImageView imageView, int width, ImageLoadingListener listener) {
        String imageUrl = url_new.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, width + "") + imageid;
        imageLoader.displayImage(imageUrl, imageView, DisplayImageOptionsWrap.getDisplayImageOptionsIsMemoryCache(true), listener);
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
        displayThumbnailImage(imageid, imageView, (int) TDevice.getScreenWidth());
    }

    public void displayHalfScreenWidthThumnailImage(Context context, String imageid, ImageView imageView) {
        displayThumbnailImage(imageid, imageView, (int) TDevice.getScreenWidth() / 2);
    }

    public void displayScreenWidthThumnailImage(Context context, String imageid, ImageView imageView, ImageLoadingListener listener) {
        displayThumbnailImage(imageid, imageView, (int) TDevice.getScreenWidth(), listener);
    }

    public void displayHalfScreenWidthThumnailImage(Context context, String imageid, ImageView imageView, ImageLoadingListener listener) {
        displayThumbnailImage(imageid, imageView, (int) TDevice.getScreenWidth() / 2, listener);
    }

    /**
     * 显示应用内的用户头像的缩略图，头像宽度为60dp
     *
     * @param context
     * @param imageView
     * @param imageid
     */
    public void displayImageHeadWidthThumnailImage(Context context, String imageid, ImageView imageView) {
        int width = TDevice.dip2px(context, Global.PIC_WIDTH_SHOW_WIDTH);
        String imageUrl = url_new.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, width + "") + imageid;
        imageLoader.displayImage(imageUrl, imageView, DisplayImageOptionsWrap.getDisplayImageOptionsIsMemoryCache(true));
    }

    public void loadImage(String imageid, ImageLoadingListener listener) {
        String imageUrl = url_new.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, TDevice.getScreenWidth() + "") + imageid;
        imageLoader.loadImage(imageUrl, DisplayImageOptionsWrap.getDisplayImageOptionsIsMemoryCache(true), listener);
    }


}
