package com.jianfanjia.cn.designer.tools;

import android.content.Context;
import android.widget.ImageView;

import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.config.Url_New;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.jianfanjia.imageshow.ImageDisplay;
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
    protected ImageDisplay imageDisplay;
    protected Url_New url_new;

    private ImageShow() {
        imageDisplay = ImageDisplay.getInstance();
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
     * 加载缩略图
     *
     * @param imageView
     * @param imageid
     * @param width
     */
    public void displayThumbnailImage(String imageid, ImageView imageView, int width) {
        displayThumbnailImage(imageid, imageView, width, true);
    }

    public void displayThumbnailImage(String imageid, ImageView imageView, int width, boolean isCacheInMemory) {
        String imageUrl = url_new.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, width + "") + imageid;
        imageDisplay.displayImage(imageUrl, imageView, DisplayImageOptionsWrap.getDisplayImageOptionsIsMemoryCache
                (isCacheInMemory));
    }

    /**
     * 加载宽高缩略图
     *
     * @param imageid
     * @param imageView
     * @param width
     * @param height
     */
    public void displayThumbnailImageByHeightAndWidth(String imageid, ImageView imageView, int width, int height) {
        displayThumbnailImageByHeightAndWidth(imageid, imageView, width, height, true);
    }

    public void displayThumbnailImageByHeightAndWidth(String imageid, ImageView imageView, int width, int height,
                                                      boolean isCacheInMemory) {
        String imageUrl = url_new.GET_THUMBNAIL_IMAGE2.replace(Url_New.WIDTH, width + "").replace(Url_New.HEIGHT,
                height + "") + imageid;
        imageDisplay.displayImage(imageUrl, imageView, DisplayImageOptionsWrap.getDisplayImageOptionsIsMemoryCache
                (isCacheInMemory));
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
        imageDisplay.displayImage(imgPath, imageView);
    }

    /**
     * 显示屏幕宽的缩略图
     *
     * @param context
     * @param imageView
     * @param imageid
     */
    public void displayScreenWidthThumnailImage(Context context, String imageid, ImageView imageView) {
        displayThumbnailImage(imageid, imageView, (int) TDevice.getScreenWidth(), true);
    }

    public void displayHalfScreenWidthThumnailImage(Context context, String imageid, ImageView imageView) {
        displayThumbnailImage(imageid, imageView, (int) TDevice.getScreenWidth() / 2, true);
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
        displayThumbnailImage(imageid, imageView, width, true);
    }

    public void loadImage(String imageid, ImageLoadingListener listener) {
        String imageUrl = url_new.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, TDevice.getScreenWidth() + "") + imageid;
        imageDisplay.loadImage(imageUrl, DisplayImageOptionsWrap.getDisplayImageOptionsIsMemoryCache(true), listener);
    }


}
