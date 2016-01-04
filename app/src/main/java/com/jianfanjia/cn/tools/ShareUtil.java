package com.jianfanjia.cn.tools;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-31 14:25
 */
public class ShareUtil {
    private static ShareUtil shareUtil;
    private ShareAction shareAction = null;

    public static ShareUtil getShareUtil(Activity activity) {
        if (null == shareUtil) {
            shareUtil = new ShareUtil(activity);
        }
        return shareUtil;
    }

    public ShareUtil(Activity activity) {
        shareAction = new ShareAction(activity);
    }


    public void share(Activity activity, String url, SHARE_MEDIA platform, UMShareListener umShareListener) {
        UMImage image = new UMImage(activity, url);
        shareAction.setPlatform(platform).setCallback(umShareListener).withText("分享美图").withMedia(image).share();
    }
}
