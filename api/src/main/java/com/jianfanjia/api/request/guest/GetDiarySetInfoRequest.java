package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.guest
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-15 16:52
 */
public class GetDiarySetInfoRequest extends BaseRequest {

    private String diarySetid;

    public String getDiarySetid() {
        return diarySetid;
    }

    public void setDiarySetid(String diarySetid) {
        this.diarySetid = diarySetid;
    }
}
