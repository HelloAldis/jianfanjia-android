package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author zhanghao
 * @class ProcessSectionYs.class
 * @Decription 验收信息实体类
 * @date 2015-8-31 上午11:55
 */
public class ProcessSectionYs implements Serializable {

    private static final long serialVersionUID = 2520641788286345789L;

    private long date;

    private ArrayList<ProcessSectionYsImage> images;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ArrayList<ProcessSectionYsImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<ProcessSectionYsImage> images) {
        this.images = images;
    }

    public void addImageId(ProcessSectionYsImage imageid) {
        if (images != null) {
            images.add(imageid);
        }
    }

    public boolean deleteImageIdBykey(String key) {
        boolean flag = false;
        if (images != null) {
            for (ProcessSectionYsImage imageid : images) {
                if (key.equals(imageid.getKey())) {
                    images.remove(imageid);
                    return true;
                }
            }
        }
        return flag;
    }

}
