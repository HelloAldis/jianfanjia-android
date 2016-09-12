package com.jianfanjia.api.model;

/**
 * Created by jyz on 16/3/28.
 */
public class BeautifulImageDetail extends BaseModel {
    private String imageid;
    private int width;
    private int height;

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
