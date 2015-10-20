package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: DesignerByIntentionInfo
 * User: fengliang
 * Date: 2015-10-19
 * Time: 14:45
 */
public class DesignerByIntentionInfo implements Serializable {
    private String designerId;
    private String imgPath;
    private String name;
    private String starLevel;

    public String getDesignerId() {
        return designerId;
    }

    public void setDesignerId(String designerId) {
        this.designerId = designerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {

        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }
}
