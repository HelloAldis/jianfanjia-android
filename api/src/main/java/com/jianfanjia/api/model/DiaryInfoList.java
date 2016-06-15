package com.jianfanjia.api.model;

import java.util.List;

/**
 * Description: com.jianfanjia.api.model
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-15 17:19
 */
public class DiaryInfoList extends BaseModel {

    private List<DiaryInfo> diaries;
    private int total;

    public List<DiaryInfo> getDiaries() {
        return diaries;
    }

    public void setDiaries(List<DiaryInfo> diaries) {
        this.diaries = diaries;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
