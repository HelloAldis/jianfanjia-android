package com.jianfanjia.cn.designer.ui.activity.common.choose_item;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.activity.common.choose_item.ChooseItemActivity;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-30 09:58
 */
public class ChooseItemIntent extends Intent {

    private ChooseItemIntent() {
    }

    private ChooseItemIntent(Intent o) {
        super(o);
    }

    private ChooseItemIntent(String action) {
        super(action);
    }

    private ChooseItemIntent(String action, Uri uri) {
        super(action, uri);
    }

    private ChooseItemIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public ChooseItemIntent(Context packageContext) {
        super(packageContext, ChooseItemActivity.class);
    }

    public void setSingleChoose(int requestCode, String currentChooseValue) {
        this.putExtra(Global.REQUIRE_DATA, requestCode);
        this.putExtra(ChooseItemActivity.CURRENT_CHOOSED_TYPE, ChooseItemActivity.CHOOSE_TYPE_SINGLE);
        if (!TextUtils.isEmpty(currentChooseValue)) {
            this.putExtra(ChooseItemActivity.CURRENT_CHOOSED_VALUE, currentChooseValue);
        }
    }

    public void setMultipleChoose(int requestCode, List<String> currentChooseValueList) {
        this.putExtra(Global.REQUIRE_DATA, requestCode);
        this.putExtra(ChooseItemActivity.CURRENT_CHOOSED_TYPE, ChooseItemActivity.CHOOSE_TYPE_MULTIPLE);
        if (currentChooseValueList != null) {
            this.putStringArrayListExtra(ChooseItemActivity.CURRENT_CHOOSED_VALUE, (ArrayList) currentChooseValueList);
        }
    }
}
