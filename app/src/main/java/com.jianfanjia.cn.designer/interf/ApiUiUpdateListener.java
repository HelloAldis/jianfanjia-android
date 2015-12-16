package com.jianfanjia.cn.designer.interf;

public interface ApiUiUpdateListener {
    void preLoad();

    void loadSuccess(Object data);

    void loadFailture(String errorMsg);
}
