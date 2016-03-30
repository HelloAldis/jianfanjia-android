package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * @author fengliang
 * @ClassName: GridItem
 * @Description:
 * @date 2015-8-28 下午5:06:27
 */
public class GridItem implements Serializable {
    private static final long serialVersionUID = 5204218970824963589L;
    private String imgId;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

}
