package com.jianfanjia.cn.tools;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.jianfanjia.common.tool.LogTool;

/**
 * Created by Aldis.Zhan on 16/3/18.
 */
public class JavaScriptObject {
    private static final String TAG = JavaScriptObject.class.getName();

    private static final String descriptionCode = "javascript:var meta = document.getElementsByTagName('meta');\n" +
            "for (i in meta) {\n" +
            "  if (typeof meta[i].name!=\"undefined\" && meta[i].name.toLowerCase()==\"description\") {\n" +
            "    window.local_obj.description(meta[i].content);\n" +
            "  }\n" +
            "}";

    private static final String imageCode = "javascript:window.local_obj.imageUrl(document.getElementsByTagName('img')[0].src);";

    private String description;
    private String imageUrl;

    public String getDescription() {
        if (StringUtils.isEmpty(this.description)) {
            return "我在使用 #简繁家# 的App，业内一线设计师为您量身打造房间，比传统装修便宜20%，让你一手轻松掌控装修全过程。";
        } else {
            return this.description;
        }
    }

    public String getImageUrl() {
        if (StringUtils.isEmpty(this.imageUrl)) {
            return "http://www.jianfanjia.com/static/img/design/head.jpg";
        } else {
            return this.imageUrl;
        }
    }

    public void injectIntoWebView(WebView webView) {
        webView.addJavascriptInterface(this, "local_obj");
    }

    public void runGetDescriptionCode(WebView webView) {
        webView.loadUrl(descriptionCode);
    }

    public void runGetImageCode(WebView webView) {
        webView.loadUrl(imageCode);
    }

    @JavascriptInterface
    public void description(String description) {
        LogTool.d(TAG, "description = " + description);
        this.description = description;
    }

    @JavascriptInterface
    public void imageUrl(String imageUrl) {
        LogTool.d(TAG, "image url = " + imageUrl);
        this.imageUrl = imageUrl;
    }
}
