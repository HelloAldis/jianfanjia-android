package com.jianfanjia.cn.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Url_New;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-31 14:25
 */
public class ShareUtil {
    private Url_New url_new = null;
    private int width = 0;
    private Context context;
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    public ShareUtil(Activity activity) {
        url_new = Url_New.getInstance();
        width = (int) TDevice.getScreenWidth();
        context = activity.getApplicationContext();
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(activity, "wx391daabfce27e728", "f7c8e3e1b5910dd93be2744dacb3a1cc");
        wxHandler.showCompressToast(false);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(activity, "wx391daabfce27e728", "f7c8e3e1b5910dd93be2744dacb3a1cc");
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.showCompressToast(false);
        wxCircleHandler.addToSocialSDK();

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, "1104973048", "FuDs7s4vJGAEzCrz");
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, "1104973048", "FuDs7s4vJGAEzCrz");
        qZoneSsoHandler.addToSocialSDK();

        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        SinaSsoHandler sinaSsoHandler = new SinaSsoHandler(activity);
//        sinaSsoHandler.addToSocialSDK();
    }

    public UMSocialService getUmSocialService() {
        return mController;
    }

    public void shareImage(Activity activity, String title, String style, String tag, String imgId, SocializeListeners.SnsPostListener listener) {
        String desc = context.getString(R.string.share_image_des);
        if (!TextUtils.isEmpty(style) && !TextUtils.isEmpty(tag)) {
            desc = String.format(desc, BusinessManager.convertDecStyleToShow(style), tag);
        }
        try {
            String urlTitle = URLEncoder.encode(title, "utf-8");
            String targetUrl = url_new.SHARE_IMAGE + urlTitle + "&imageId=" + imgId;
            String imageUrl = url_new.GET_THUMBNAIL_IMAGE.replace(Url_New.WIDTH, width + "") + imgId;
            UMImage image = new UMImage(context, imageUrl);
            setShareContent(image, title, desc, targetUrl);
            mController.registerListener(listener);
            mController.openShare(activity, false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setShareContent(UMImage image, String title, String content, String targetUrl) {
        setWeiXinShareContent(image, title, content, targetUrl);
        setWeiXinCicleShareContent(image, title, content, targetUrl);
        setQQShareContent(image, title, content, targetUrl);
        setQZONEShareContent(image, title, content, targetUrl);
        setSinaShareContent(image, title, content, targetUrl);
    }

    public void setWeiXinShareContent(UMImage image, String title, String content, String targetUrl) {
        WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
        weiXinShareContent.setShareContent(content);
        weiXinShareContent.setShareImage(image);
        weiXinShareContent.setTitle(title);
        weiXinShareContent.setTargetUrl(targetUrl);
        mController.setShareMedia(weiXinShareContent);
    }

    public void setWeiXinCicleShareContent(UMImage image, String title, String content, String targetUrl) {
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setShareImage(image);
        circleMedia.setTitle(title);
        circleMedia.setTargetUrl(targetUrl);
        mController.setShareMedia(circleMedia);
    }

    public void setQQShareContent(UMImage image, String title, String content, String targetUrl) {
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setShareImage(image);
        qqShareContent.setTitle(title);
        qqShareContent.setTargetUrl(targetUrl);
        mController.setShareMedia(qqShareContent);
    }

    public void setQZONEShareContent(UMImage image, String title, String content, String targetUrl) {
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setShareImage(image);
        qzone.setTitle(title);
        qzone.setTargetUrl(targetUrl);
        mController.setShareMedia(qzone);
    }

    public void setSinaShareContent(UMImage image, String title, String content, String targetUrl) {
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(content + targetUrl);
        sinaContent.setShareImage(image);
//        sinaContent.setTitle(title);
//        sinaContent.setTargetUrl(targetUrl);
        mController.setShareMedia(sinaContent);
    }

    public void shareApp(Activity activity, SocializeListeners.SnsPostListener listener) {
        LogTool.d(this.getClass().getName(), context.getPackageResourcePath());
        UMImage image = new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_share_app));
        setShareContent(image, context.getString(R.string.share_app_title), context.getString(R.string.share_app_des), context.getString(R.string.share_app_url));
        mController.registerListener(listener);
        mController.openShare(activity, false);
    }

}

