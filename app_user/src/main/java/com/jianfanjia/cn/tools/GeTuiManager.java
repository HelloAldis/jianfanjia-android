package com.jianfanjia.cn.tools;

import android.content.Context;

import com.igexin.sdk.PushManager;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-05 16:19
 */
public class GeTuiManager {

    private static final String TAG = "GeTuiManager";

    public static void initGeTui(Context context) {
        PushManager.getInstance().initialize(context);
    }

    /**
     * 绑定个推
     *
     * @param context
     * @param userid
     */
    public static void bindGeTui(Context context, String userid) {
//        initGeTui(context);
        LogTool.d("userid =" + userid);
        if (userid != null) {
            PushManager.getInstance().bindAlias(context, userid);
        }
    }

    /**
     * 解绑个推
     *
     * @param context
     * @param userid
     */
    public static void cancelBind(Context context, String userid) {
        if (userid != null) {
            PushManager.getInstance().unBindAlias(context, userid, true);
        }
    }

    /**
     * 个推是否已经打开
     *
     * @param context
     * @return
     */
    public static boolean isPushTurnOn(Context context) {
        return PushManager.getInstance().isPushTurnedOn(context);
    }

    /**
     * 打开个推
     *
     * @param context
     */
    public static void turnOnPush(Context context) {
        PushManager.getInstance().turnOnPush(context);
    }

    /**
     * 关闭个推
     *
     * @param context
     */
    public static void turnOffPush(Context context) {
        PushManager.getInstance().turnOffPush(context);
    }
}
