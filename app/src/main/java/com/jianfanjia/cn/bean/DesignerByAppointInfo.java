package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: DesignerByAppointInfo
 * User: fengliang
 * Date: 2015-10-19
 * Time: 14:40
 */
public class DesignerByAppointInfo implements Serializable {
    private String designerId;
    private String imgPath;
    private String name;
    private String marchDegree;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getMarchDegree() {
        return marchDegree;
    }

    public void setMarchDegree(String marchDegree) {
        this.marchDegree = marchDegree;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignerId() {

        return designerId;
    }

    public void setDesignerId(String designerId) {
        this.designerId = designerId;
    }
}
