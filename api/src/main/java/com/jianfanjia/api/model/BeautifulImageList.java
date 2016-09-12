package com.jianfanjia.api.model;

import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class BeautifulImageList extends BaseModel {
    private List<BeautifulImage> beautiful_images;
    private int total;

    public List<BeautifulImage> getBeautiful_images() {
        return beautiful_images;
    }

    public void setBeautiful_images(List<BeautifulImage> beautiful_images) {
        this.beautiful_images = beautiful_images;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
