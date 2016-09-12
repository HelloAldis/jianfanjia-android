package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-11 09:22
 */
public class AddDiarySetFavoriteRequest extends BaseRequest {

    private String diarySetid;

    public String getDiarySetid() {
        return diarySetid;
    }

    public void setDiarySetid(String diarySetid) {
        this.diarySetid = diarySetid;
    }
}
