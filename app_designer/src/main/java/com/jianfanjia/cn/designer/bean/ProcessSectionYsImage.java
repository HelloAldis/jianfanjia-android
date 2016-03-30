package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * Description: com.jianfanjia.cn.designer.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-30 16:02
 */
public class ProcessSectionYsImage implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String key;
    private String imageid;

    public ProcessSectionYsImage(String key, String imageid) {
        this.key = key;
        this.imageid = imageid;
    }

    public ProcessSectionYsImage() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

}
