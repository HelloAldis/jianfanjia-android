package com.jianfanjia.api.request.common;

import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-15 16:40
 */
public class AddDiaryRequest extends BaseRequest {

    private DiaryInfo diary;

    public DiaryInfo getDiary() {
        return diary;
    }

    public void setDiary(DiaryInfo diary) {
        this.diary = diary;
    }
}
