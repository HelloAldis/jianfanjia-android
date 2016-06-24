package com.jianfanjia.cn.ui.Event;

import com.jianfanjia.api.model.DiarySetInfo;

/**
 * Description: com.jianfanjia.cn.ui.Event
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-23 12:10
 */
public class RefreshDiarySetInfoEvent {

    private DiarySetInfo mDiarySetInfo;

    public RefreshDiarySetInfoEvent(DiarySetInfo diarySetInfo) {
        this.mDiarySetInfo = diarySetInfo;
    }

    public DiarySetInfo getDiarySetInfo() {
        return mDiarySetInfo;
    }

    public void setDiarySetInfo(DiarySetInfo diarySetInfo) {
        mDiarySetInfo = diarySetInfo;
    }
}
