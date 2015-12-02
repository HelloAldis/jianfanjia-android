package com.jianfanjia.cn.cache;

import android.content.Context;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;

import java.io.IOException;
import java.io.InputStream;

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

    // 拿到所有的模拟工地数据
    public static ProcessInfo getDefaultProcessInfo(Context context) {
        ProcessInfo processInfo = null;
        try {
            InputStream is = context.getAssets()
                    .open("default_processinfo.txt");
            String jsonString = StringUtils.toConvertString(is);
            LogTool.d("getDefault",
                    jsonString);
            processInfo = JsonParser.jsonToBean(jsonString, ProcessInfo.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return processInfo;
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
