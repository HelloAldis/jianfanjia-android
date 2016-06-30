package com.jianfanjia.api;

import android.text.TextUtils;

/**
 * Description: com.jianfanjia.api
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-29 17:04
 */
public class StringUtils {

    public static String unEscapeHtmp(String src) {
        if (TextUtils.isEmpty(src)) return null;
        if (src.contains("&quot;")) {
            src = src.replace("&quot;", "\"");
        }
        if (src.contains("&amp;")) {
            src = src.replace("&amp;", "&");
        }
        if (src.contains("&lt;")) {
            src = src.replace("&lt;", "<");
        }
        if (src.contains("&gt;")) {
            src = src.replace("&gt;", ">");
        }
        if (src.contains("&nbsp;")) {
            src = src.replace("&nbsp;", " ");
        }
        return src.toString();
    }
}
