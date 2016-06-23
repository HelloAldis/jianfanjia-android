package com.jianfanjia.cn.ui.Event;

import com.jianfanjia.api.model.DiaryInfo;

/**
 * Description: com.jianfanjia.cn.ui.Event
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-23 12:10
 */
public class RefreshDiaryInfoEvent {

    private DiaryInfo mDiaryInfo;

    public RefreshDiaryInfoEvent(DiaryInfo diaryInfo){
        this.mDiaryInfo = diaryInfo;
    }

    public DiaryInfo getDiaryInfo() {
        return mDiaryInfo;
    }

    public void setDiaryInfo(DiaryInfo diaryInfo) {
        mDiaryInfo = diaryInfo;
    }
}
