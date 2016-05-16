package com.jianfanjia.cn.ui.Event;

/**
 * Description: com.jianfanjia.cn.Event
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-27 12:05
 */
public class CollectBeautyImageEvent {

    private String imageid;

    private boolean isCollect;

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }
}
