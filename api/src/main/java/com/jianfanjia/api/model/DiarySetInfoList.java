package com.jianfanjia.api.model;

import java.util.List;

/**
 * Description: com.jianfanjia.api.model
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-15 17:19
 */
public class DiarySetInfoList extends BaseModel {

    private List<DiarySetInfo> diarySets;

    private int total;

    public List<DiarySetInfo> getDiarySets() {
        return diarySets;
    }

    public void setDiarySets(List<DiarySetInfo> diarySets) {
        this.diarySets = diarySets;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
