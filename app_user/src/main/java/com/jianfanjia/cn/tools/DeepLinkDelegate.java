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

    public static void deepLinkRoute(Context context, String uri) {
        LogTool.d(uri);
        DeepLinkUri deepLinkUri = DeepLinkUri.parse(uri);
        List<String> pathSegments = deepLinkUri.pathSegments();
        if (pathSegments.contains(DEEPLINK_DIARYSET)) {
            intentToDiarySet(context, deepLinkUri);
        } else if (pathSegments.contains(DEEPLINK_WEBVIEW)) {
            intentToWebView(context, deepLinkUri);
        }
        AppManager.getAppManager().finishActivity((Activity) context);
    }

    public static void intentToWebView(Context context, DeepLinkUri deepLinkUri) {
        Intent mainIntent = new Intent(context, MainActivity.class);
        Intent webViewIntent = new Intent(context, WebViewActivity.class);
        Intent[] intents = new Intent[]{mainIntent, webViewIntent};
        String httpUrl = Uri.decode(deepLinkUri.queryParameter("url"));
        LogTool.d(httpUrl);
        webViewIntent.putExtra(Global.WEB_VIEW_URL, httpUrl);
        context.startActivities(intents);
    }

    public static void intentToDiarySet(Context context, DeepLinkUri deepLinkUri) {
        String diarysetid = deepLinkUri.queryParameter("diarySetid");

        if (!TextUtils.isEmpty(diarysetid)) {
            LogTool.d(diarysetid);

            Intent mainIntent = new Intent(context, MainActivity.class);
            Intent diarySetIntent = new Intent(context, DiarySetInfoActivity.class);
            diarySetIntent.putExtra(IntentConstant.DIARYSET_ID, diarysetid);

            Intent[] intents = new Intent[]{mainIntent, diarySetIntent};
            context.startActivities(intents);

        }


    }

}
