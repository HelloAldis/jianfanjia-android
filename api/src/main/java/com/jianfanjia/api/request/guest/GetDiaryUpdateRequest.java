package com.jianfanjia.api.request.guest;

import java.util.List;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.guest
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-15 16:51
 */
public class GetDiaryUpdateRequest extends BaseRequest {

    private List<String> diaryids;

    public List<String> getDiaryids() {
        return diaryids;
    }

    public void setDiaryids(List<String> diaryids) {
        this.diaryids = diaryids;
    }
}
