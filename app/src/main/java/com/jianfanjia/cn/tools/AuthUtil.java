package com.jianfanjia.cn.tools;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.jianfanjia.cn.activity.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-04 11:41
 */
public class AuthUtil {

    UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

    private static AuthUtil authUtil;

    public static AuthUtil getInstance(Activity activity) {
        if (authUtil == null) {
            authUtil = new AuthUtil(activity);
        }
        return authUtil;
    }

    private AuthUtil(Activity activity) {
        Log.LOG = true;//友盟的log开关
        mController.getConfig().setSsoHandler(new UMWXHandler(activity.getApplicationContext(),"wx391daabfce27e728", "f7c8e3e1b5910dd93be2744dacb3a1cc"));
    }

    public void doOauthVerify(final Activity activity, final SHARE_MEDIA platform, final SocializeListeners.UMDataListener umDataListener) {
        mController.doOauthVerify(activity, platform, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA share_media) {
                String uid = value.getString("uid");
                if (!TextUtils.isEmpty(uid)) {
                    getPlatformInfo(activity, platform, umDataListener);
                } else {
                    Toast.makeText(activity, activity.getString(R.string.authorize_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA share_media) {
                Toast.makeText(activity, activity.getString(R.string.authorize_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                Toast.makeText(activity, activity.getString(R.string.authorize_cancel), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteOauth(final Activity activity,final SHARE_MEDIA platform){
        mController.deleteOauth(activity, platform, new SocializeListeners.SocializeClientListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int status, SocializeEntity socializeEntity) {
                String showText = "解除" + platform.toString() + "平台授权成功";
                if (status != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "解除" + platform.toString() + "平台授权失败[" + status + "]";
                }
                LogTool.d(this.getClass().getName(),showText);
//                Toast.makeText(activity, showText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPlatformInfo(Activity activity, SHARE_MEDIA platform, SocializeListeners.UMDataListener umDataListener) {
        mController.getPlatformInfo(activity, platform, umDataListener);
    }

}
