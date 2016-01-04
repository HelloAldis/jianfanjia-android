package com.jianfanjia.cn.tools;

import android.app.Activity;

import com.jianfanjia.cn.config.Url_New;
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
    private Url_New url_new = null;
    private int width = 0;

    public static ShareUtil getShareUtil(Activity activity) {
        if (null == shareUtil) {
            shareUtil = new ShareUtil(activity);
        }
        return shareUtil;
    }

    public ShareUtil(Activity activity) {
        url_new = Url_New.getInstance();
        shareAction = new ShareAction(activity);
        width = ScreenUtil.getScreenWidth(activity);
    }


    public void share(Activity activity, String desc, String imgId, SHARE_MEDIA platform, UMShareListener umShareListener) {
        String imageUrl = url_new.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, width + "") + imgId;
        UMImage image = new UMImage(activity, imageUrl);
        shareAction.setPlatform(platform).setCallback(umShareListener).withText(desc).withTargetUrl(imageUrl).withMedia(image).withTitle(desc).share();
    }
}
