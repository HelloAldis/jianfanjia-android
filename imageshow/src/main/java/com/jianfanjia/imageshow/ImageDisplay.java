package com.jianfanjia.imageshow;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Description: com.jianfanjia.imageshow
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-01 09:45
 */
public class ImageDisplay {

    private static ImageDisplay instance;
    private ImageLoader imageLoader;

    public static ImageDisplay getInstance() {
        if (instance == null) {
            synchronized (ImageDisplay.class) {
                instance = new ImageDisplay();
            }
        }
        return instance;
    }

    private ImageDisplay() {
        imageLoader = ImageLoader.getInstance();
    }

    public void displayImage(String uri, ImageView imageView) {
        displayImage(uri, imageView, null);
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions displayImageOptions) {
        ImageAware imageAware = new ImageViewAware(imageView);
        imageLoader.displayImage(uri, imageAware, displayImageOptions);
    }

    public void loadImage(String uri, DisplayImageOptions displayImageOptions,
                          ImageLoadingListener imageLoadingListener) {
        imageLoader.loadImage(uri, displayImageOptions, imageLoadingListener);
    }

    public Bitmap loadImgaSync(String uri, DisplayImageOptions displayImageOptions) {
        return imageLoader.loadImageSync(uri, displayImageOptions);
    }
}
