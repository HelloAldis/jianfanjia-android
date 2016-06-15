package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.guest
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-15 16:52
 */
public class GetDiaryInfoRequest extends BaseRequest {

    private String diaryid;

    public String getDiaryid() {
        return diaryid;
    }

    public void setDiaryid(String diaryid) {
        this.diaryid = diaryid;
    }
}
