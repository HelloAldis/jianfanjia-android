package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: DesignerWorksInfo
 * User: fengliang
 * Date: 2015-10-15
 * Time: 13:41
 */
public class DesignerWorksInfo implements Serializable {
    private String imgPath;
    private String xiaoquName;
    private String produce;

    public String getProduce() {
        return produce;
    }

    public void setProduce(String produce) {
        this.produce = produce;
    }

    public String getXiaoquName() {

        return xiaoquName;
    }

    public void setXiaoquName(String xiaoquName) {
        this.xiaoquName = xiaoquName;
    }

    public String getImgPath() {

        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
