package com.jianfanjia.cn.tools;

import android.app.Activity;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-04 11:41
 */
public class AuthUtil {

    private Activity activity;
    private UMShareAPI umShareAPI;

    private static AuthUtil authUtil;

    public static AuthUtil getInstance(Activity activity){
        if(authUtil == null){
            authUtil = new AuthUtil(activity);
        }
        return authUtil;
    }

    private AuthUtil(Activity activity){
        this.activity = activity;
        umShareAPI = UMShareAPI.get(activity);
    }

    public   void doOauthVerify(Activity activity,SHARE_MEDIA platform,UMAuthListener umAuthListener){
        umShareAPI.doOauthVerify(activity,platform,umAuthListener);
    }

    public void deleteOauth(Activity activity,SHARE_MEDIA platform,UMAuthListener umAuthListener){
        umShareAPI.doOauthVerify(activity, platform ,umAuthListener);
    }

    public UMShareAPI getUmShareAPI(){
        return umShareAPI;
    }




}
