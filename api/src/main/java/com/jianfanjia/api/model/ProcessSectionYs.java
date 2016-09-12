package com.jianfanjia.api.model;

import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class ProcessSectionYs extends BaseModel {
    private long date;
    private List<ProcessSectionYsImage> images;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<ProcessSectionYsImage> getImages() {
        return images;
    }

    public void setImages(List<ProcessSectionYsImage> images) {
        this.images = images;
    }

    public void addImageId(ProcessSectionYsImage ysImage) {
        if (images != null) {
            images.add(ysImage);
        }
    }

    public boolean deleteImageIdBykey(String key) {
        boolean flag = false;
        if (images != null) {
            for (ProcessSectionYsImage ysImage : images) {
                if (key.equals(ysImage.getKey())) {
                    images.remove(ysImage);
                    return true;
                }
            }
        }
        return flag;
    }
}
