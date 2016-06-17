package com.jianfanjia.cn.business;

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

    public static String getDiarySetDes(DiarySetInfo diarySetInfo){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(diarySetInfo.getHouse_area() + "㎡/");
        stringBuilder.append(BusinessCovertUtil.convertHouseTypeToShow(diarySetInfo.getHouse_type()) + "/");
        stringBuilder.append(BusinessCovertUtil.convertDecStyleToShow(diarySetInfo.getDec_style())+ "/");
        stringBuilder.append(BusinessCovertUtil.convertWorktypeToShow(diarySetInfo.getWork_type()));
        return stringBuilder.toString();
    }

}
