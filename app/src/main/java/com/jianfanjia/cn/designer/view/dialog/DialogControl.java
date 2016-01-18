package com.jianfanjia.cn.designer.view.dialog;

public interface DialogControl {

    void hideWaitDialog();

    WaitDialog showWaitDialog();

    WaitDialog showWaitDialog(int resid);

    WaitDialog showWaitDialog(String text);
}
