package com.jianfanjia.cn.business;

import android.text.TextUtils;
import android.widget.TextView;

import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.cn.tools.BusinessCovertUtil;

/**
 * Description: com.jianfanjia.cn.business
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-16 10:45
 */
public class DiaryBusiness {

    public static final int UPLOAD_MAX_PIC_COUNT = 9;//上传最大的图片数为9张

    public static String getDiarySetDes(DiarySetInfo diarySetInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(diarySetInfo.getHouse_area() + "㎡  ");
        String showHouseType = BusinessCovertUtil.convertHouseTypeToShow(diarySetInfo.getHouse_type());
        if (!TextUtils.isEmpty(showHouseType)) {
            stringBuilder.append(showHouseType + "  ");
        }
        String showDecStyle = BusinessCovertUtil.convertDecStyleToShow(diarySetInfo.getDec_style());
        if (!TextUtils.isEmpty(showDecStyle)) {
            stringBuilder.append(showDecStyle + "  ");
        }
        String showWorkType = BusinessCovertUtil.convertWorktypeToShow(diarySetInfo.getWork_type());
        if (!TextUtils.isEmpty(showWorkType)) {
            stringBuilder.append(showWorkType);
        }
        return stringBuilder.toString();
    }

    public static String getShowDiarySectionLabel(String sectionLabel) {
        return sectionLabel + "阶段";
    }

    public static boolean isDiarySetStageFinish(String section) {
        if (!TextUtils.isEmpty(section) && section.equals("入住")) {
            return true;
        }
        return false;
    }

    public static String getCommentCountShow(int count) {
        if (count == 0) return "评论";
        return count + "";
    }

    public static String getFavoriteCountShow(int count) {
        if (count == 0) return "点赞";
        return count + "";
    }

}
