package com.jianfanjia.cn.view.baseview;

import android.content.Context;
import android.widget.FrameLayout;

import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.tools.ImageShow;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Description: com.jianfanjia.cn.view.custom_annotation_view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-26 10:50
 */
public class BaseAnnotationView extends FrameLayout {

//    protected ImageLoader imageLoader;
    protected DisplayImageOptions options;
    protected DataManagerNew dataManagerNew;
    protected ImageShow imageShow;
    protected Context context;


    public BaseAnnotationView(Context context) {
        super(context);
        this.context = context;
//        imageLoader = ImageLoader.getInstance();
      /*  options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pix_default)
                .showImageForEmptyUri(R.mipmap.pix_default)
                .showImageOnFail(R.mipmap.pix_default).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();*/
        imageShow = ImageShow.getImageShow();
        dataManagerNew = DataManagerNew.getInstance();
    }
}
