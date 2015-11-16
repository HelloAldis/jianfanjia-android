package com.jianfanjia.cn.cache;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.config.Constant;

public class BusinessManager {

    public static int getCheckPicCountBySection(String section) {
        if (section.equals(Constant.SHUI_DIAN)) {
            return 5;
        } else if (section.equals(Constant.NI_MU)) {
            return 7;
        } else if (section.equals(Constant.YOU_QI)) {
            return 2;
        } else if (section.equals(Constant.AN_ZHUANG)) {
            return 1;
        } else if (section.equals(Constant.JUN_GONG)) {
            return 1;
        }
        return -1;
    }

    /**
     * 拿到显示的房子类型
     * @param houseType
     * @return
     */
    public static String convertHouseTypeToShow(String houseType) {
        if (houseType == null) return null;
        int housePosition = Integer.parseInt(houseType);
        String[] housetypes = MyApplication.getInstance().getResources().getStringArray(R.array.arr_housetype);
        if (housePosition < 0 || housePosition > housetypes.length) return null;
        return housetypes[housePosition];
    }

    /**
     * 拿到显示的风格喜好
     * @param decStyle
     * @return
     */
    public static String convertDecStyleToShow(String decStyle) {
        if (decStyle == null) return null;
        int decPosition = Integer.parseInt(decStyle);
        String[] decStyles = MyApplication.getInstance().getResources().getStringArray(R.array.arr_decstyle);
        if (decPosition < 0 || decPosition > decStyles.length) return null;
        return decStyles[decPosition];
    }

}
