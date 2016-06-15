package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-15 16:41
 */
public class DeleteDiaryRequest extends BaseRequest {

    private String diaryid;

    public String getDiaryid() {
        return diaryid;
    }

    public void setDiaryid(String diaryid) {
        this.diaryid = diaryid;
    }
}
