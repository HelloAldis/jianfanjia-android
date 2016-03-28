package com.jianfanjia.api.model;

import java.io.Serializable;

/**
 * Created by Aldis on 16/3/28.
 */
public class ProcessSectionYsImage extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String key;
    private String imageid;

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
