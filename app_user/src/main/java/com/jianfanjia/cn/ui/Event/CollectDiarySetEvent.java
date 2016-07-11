package com.jianfanjia.cn.ui.Event;

/**
 * Description: com.jianfanjia.cn.ui.Event
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-11 10:48
 */
public class CollectDiarySetEvent {

    private String diarySetid;

    private boolean isCollect;

    public CollectDiarySetEvent(String diarySetid, boolean isCollect) {
        this.diarySetid = diarySetid;
        this.isCollect = isCollect;
    }

    public String getDiarySetid() {
        return diarySetid;
    }

    public void setDiarySetid(String diarySetid) {
        this.diarySetid = diarySetid;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }
}
