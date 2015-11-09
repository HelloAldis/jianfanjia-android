package com.jianfanjia.cn.interf;

import android.content.Intent;

/**
 * Created by Administrator on 2015/11/9.
 */
public interface OnActivityResultCallBack {
    void onResult(int requestCode, int resultCode, Intent data);
}
