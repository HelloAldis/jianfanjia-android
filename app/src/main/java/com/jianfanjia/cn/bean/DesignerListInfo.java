package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: DesignerListInfo
 * User: fengliang
 * Date: 2015-10-14
 * Time: 14:05
 */
public class DesignerListInfo implements Serializable {
    private String imgPath;
    private String headPath;
    private String xiaoquInfo;
    private String produceInfo;

    public String getProduceInfo() {
        return produceInfo;
    }

    public void setProduceInfo(String produceInfo) {
        this.produceInfo = produceInfo;
    }

    public String getHeadPath() {

        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getImgPath() {

        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getXiaoquInfo() {
        return xiaoquInfo;
    }

    public void setXiaoquInfo(String xiaoquInfo) {
        this.xiaoquInfo = xiaoquInfo;
    }
}
