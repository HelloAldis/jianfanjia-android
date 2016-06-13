package com.jianfanjia.cn.ui.activity.common.choose_item;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.cn.constant.IntentConstant;


/**
 * Description: com.jianfanjia.cn.designer.ui.activity.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-30 09:58
 */
public class ChooseItemLoveStyleIntent extends Intent {

    private ChooseItemLoveStyleIntent() {
    }

    private ChooseItemLoveStyleIntent(Intent o) {
        super(o);
    }

    private ChooseItemLoveStyleIntent(String action) {
        super(action);
    }

    private ChooseItemLoveStyleIntent(String action, Uri uri) {
        super(action, uri);
    }

    private ChooseItemLoveStyleIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public ChooseItemLoveStyleIntent(Context packageContext) {
        super(packageContext, ChooseItemLovestyleActivity.class);
    }

    public void setSingleChoose(int requestCode, String currentChooseValue,String title) {
        this.putExtra(IntentConstant.REQUIRE_DATA, requestCode);
        this.putExtra(ChooseItemLovestyleActivity.CURRENT_CHOOSED_TYPE, ChooseItemLovestyleActivity.CHOOSE_TYPE_SINGLE);
        this.putExtra(ChooseItemLovestyleActivity.TITLE,title);
        if (!TextUtils.isEmpty(currentChooseValue)) {
            this.putExtra(ChooseItemLovestyleActivity.CURRENT_CHOOSED_VALUE, currentChooseValue);
        }
    }

    public void setMultipleChoose(int requestCode, List<String> currentChooseValueList,String title) {
        this.putExtra(IntentConstant.REQUIRE_DATA, requestCode);
        this.putExtra(ChooseItemLovestyleActivity.CURRENT_CHOOSED_TYPE, ChooseItemLovestyleActivity
                .CHOOSE_TYPE_MULTIPLE);
        this.putExtra(ChooseItemLovestyleActivity.TITLE,title);
        if (currentChooseValueList != null) {
            this.putStringArrayListExtra(ChooseItemLovestyleActivity.CURRENT_CHOOSED_VALUE, (ArrayList)
                    currentChooseValueList);
        }
    }
}
