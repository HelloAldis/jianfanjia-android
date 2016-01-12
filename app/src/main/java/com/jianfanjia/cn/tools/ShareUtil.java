package com.jianfanjia.cn.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.cache.BusinessManager;
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
    private Context context;

    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    public static ShareUtil getShareUtil(Activity activity) {
        if (null == shareUtil) {
            shareUtil = new ShareUtil(activity);
        }
        return shareUtil;
    }

    public ShareUtil(Activity activity) {
        url_new = Url_New.getInstance();
        shareAction = new ShareAction(activity);
        width = (int) TDevice.getScreenWidth();
        context = activity.getApplicationContext();
    }

    public void shareImage(Activity activity, String title, String style, String tag, String imgId,UMShareListener umShareListener) {
        String desc = context.getString(R.string.share_image_des);
        if (!TextUtils.isEmpty(style) && !TextUtils.isEmpty(tag)) {
            desc = String.format(desc, BusinessManager.convertDecStyleToShow(style), tag);
        }
        String imageUrl = url_new.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, width + "") + imgId;
        UMImage image = new UMImage(context, imageUrl);
        new ShareAction(activity).setDisplayList(displaylist)
                .withText(desc)
                .withTitle(title)
                .withTargetUrl(imageUrl)
                .withMedia(image)
                .open();
    }


    public void shareImage(String title, String style, String tag, String imgId, SHARE_MEDIA platform, UMShareListener umShareListener) {
        String desc = context.getString(R.string.share_image_des);
        if (!TextUtils.isEmpty(style) && !TextUtils.isEmpty(tag)) {
            desc = String.format(desc, BusinessManager.convertDecStyleToShow(style), tag);
        }
        String imageUrl = url_new.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, width + "") + imgId;
        UMImage image = new UMImage(context, imageUrl);
        shareAction.setPlatform(platform).setCallback(umShareListener).withText(desc).withTargetUrl(imageUrl).withMedia(image).withTitle(title).share();
    }

    public void shareApp(SHARE_MEDIA platform, UMShareListener umShareListener) {
        LogTool.d(this.getClass().getName(), context.getPackageResourcePath());
        UMImage image = new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_share));
        shareAction.setPlatform(platform).setCallback(umShareListener).withText(context.getString(R.string.share_app_des)).withTargetUrl(context.getString(R.string.share_app_url)).withMedia(image).withTitle(context.getString(R.string.share_app_title)).share();
    }
}

