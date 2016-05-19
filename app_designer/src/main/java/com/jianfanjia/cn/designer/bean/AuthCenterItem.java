package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * Description: com.jianfanjia.cn.designer.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-19 17:38
 */
public class AuthCenterItem implements Serializable {
    private static final long serialVersionUID = 4182916919219646175L;

    private int iconResId;

    private String title;

    private String status;

    public AuthCenterItem(int iconResId, String title, String status) {
        this.iconResId = iconResId;
        this.title = title;
        this.status = status;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
