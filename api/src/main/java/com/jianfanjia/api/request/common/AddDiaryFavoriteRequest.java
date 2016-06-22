package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.guest
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-22 09:14
 */
public class AddDiaryFavoriteRequest extends BaseRequest {

    private String diaryid;

    public String getDiaryid() {
        return diaryid;
    }

    public void setDiaryid(String diaryid) {
        this.diaryid = diaryid;
    }
}
