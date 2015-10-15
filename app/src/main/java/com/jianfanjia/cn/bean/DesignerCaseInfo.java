package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: DesignerCaseInfo
 * User: fengliang
 * Date: 2015-10-15
 * Time: 15:11
 */
public class DesignerCaseInfo implements Serializable {
    private String imgPath;
    private String title;
    private String produceInfo;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProduceInfo() {
        return produceInfo;
    }

    public void setProduceInfo(String produceInfo) {
        this.produceInfo = produceInfo;
    }
}
