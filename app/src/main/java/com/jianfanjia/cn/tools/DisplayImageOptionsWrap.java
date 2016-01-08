package com.jianfanjia.cn.tools;

import android.graphics.Bitmap;

import com.jianfanjia.cn.activity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-08 14:58
 */
public class DisplayImageOptionsWrap {

    private static DisplayImageOptions.Builder getBaseOptionsBuilder() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pix_default)
                .showImageForEmptyUri(R.mipmap.pix_default)
                .showImageOnFail(R.mipmap.pix_default)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT);
    }

    public static DisplayImageOptions getDisplayImageOptionsIsMemoryCache(boolean iscache){
        return getBaseOptionsBuilder().cacheInMemory(iscache).build();
    }

}
