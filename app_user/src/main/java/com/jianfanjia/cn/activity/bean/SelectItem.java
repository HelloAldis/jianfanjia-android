package com.jianfanjia.cn.activity.bean;

import android.support.v4.app.Fragment;

import java.io.Serializable;

public class SelectItem implements Serializable {
    private static final long serialVersionUID = 4141670217738397473L;
    private Fragment fragment;
    private String desc;

    public SelectItem(Fragment fragment, String desc) {
        this.fragment = fragment;
        this.desc = desc;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
