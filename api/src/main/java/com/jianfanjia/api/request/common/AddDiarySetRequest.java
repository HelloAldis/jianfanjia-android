package com.jianfanjia.api.request.common;

import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-15 16:38
 */
public class AddDiarySetRequest extends BaseRequest {

    private DiarySetInfo diary_set;

    public DiarySetInfo getDiary_set() {
        return diary_set;
    }

    public void setDiary_set(DiarySetInfo diary_set) {
        this.diary_set = diary_set;
    }
}
