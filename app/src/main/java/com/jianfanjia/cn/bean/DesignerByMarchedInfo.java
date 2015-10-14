package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: DesignerByMarchedInfo
 * User: fengliang
 * Date: 2015-10-14
 * Time: 15:22
 */
public class DesignerByMarchedInfo implements Serializable {
    private String headPath;
    private String designerName;

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getHeadPath() {

        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }
}
