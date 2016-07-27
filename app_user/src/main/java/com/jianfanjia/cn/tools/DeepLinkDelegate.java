package com.jianfanjia.cn.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

import com.jianfanjia.cn.AppManager;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.activity.MainActivity;
import com.jianfanjia.cn.ui.activity.diary.DiarySetInfoActivity;
import com.jianfanjia.cn.ui.activity.home.WebViewActivity;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-26 17:13
 */
public class DeepLinkDelegate {

    public static final String DEEPLINK_WEBVIEW = "webview";
    public static final String DEEPLINK_DIARYSET = "diaryset";

    public static boolean deepLinkRoute(Context context, String uri) {
        LogTool.d(uri);
        DeepLinkUri deepLinkUri = DeepLinkUri.parse(uri);
        List<String> pathSegments = deepLinkUri.pathSegments();
        if (pathSegments.contains(DEEPLINK_DIARYSET)) {
            return intentToDiarySet(context, deepLinkUri);
        } else if (pathSegments.contains(DEEPLINK_WEBVIEW)) {
            return intentToWebView(context, deepLinkUri);
        }else {
            LogTool.d("not find link target,only start app");
            return false;
        }
    }

    public static boolean intentToWebView(Context context, DeepLinkUri deepLinkUri) {
        String httpUrl = Uri.decode(deepLinkUri.queryParameter("url"));
        if (!TextUtils.isEmpty(httpUrl)) {
            Intent mainIntent = new Intent(context, MainActivity.class);
            Intent webViewIntent = new Intent(context, WebViewActivity.class);
            Intent[] intents = new Intent[]{mainIntent, webViewIntent};
            LogTool.d(httpUrl);
            webViewIntent.putExtra(Global.WEB_VIEW_URL, httpUrl);
            context.startActivities(intents);
            AppManager.getAppManager().finishActivity((Activity) context);
            return true;
        } else {
            return false;
        }
    }

    public static boolean intentToDiarySet(Context context, DeepLinkUri deepLinkUri) {
        String diarysetid = deepLinkUri.queryParameter("diarySetid");

        if (!TextUtils.isEmpty(diarysetid)) {
            LogTool.d(diarysetid);

            Intent mainIntent = new Intent(context, MainActivity.class);
            Intent diarySetIntent = new Intent(context, DiarySetInfoActivity.class);
            diarySetIntent.putExtra(IntentConstant.DIARYSET_ID, diarysetid);

            Intent[] intents = new Intent[]{mainIntent, diarySetIntent};
            context.startActivities(intents);
            AppManager.getAppManager().finishActivity((Activity) context);
            return true;
        } else {
            return false;
        }


    }

}
