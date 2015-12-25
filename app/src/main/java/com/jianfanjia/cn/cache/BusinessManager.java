package com.jianfanjia.cn.cache;

import android.content.Context;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BusinessManager {

    public static String getWorkType(String workType) {
        String str = null;
        if (workType.equals("0")) {
            str = "半包";
        } else if (workType.equals("1")) {
            str = "全包";
        } else if (workType.equals("2")) {
            str = "纯设计";
        }
        return str;
    }


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
     *
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
     *
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

    public static String getHouseTypeByText(String houseTypeText) {
        try {
            String[] items = MyApplication.getInstance().getResources().getStringArray(R.array.arr_housetype);
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(houseTypeText)) {
                    return i + "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDecStyleByText(String decStyleText) {
        try {
            String[] items = MyApplication.getInstance().getResources().getStringArray(R.array.arr_decstyle);
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(decStyleText)) {
                    return i + "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 比较需求是否改变
     *
     * @param src
     * @param target
     * @return
     */
    public static boolean isRequirementChange(RequirementInfo src, RequirementInfo target) {
        LogTool.d("isRequirementChange", "isRequirementChange");
        try {
            Class clazz = src.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                LogTool.d("isRequirementChange", field.getName());
                field.setAccessible(true);
                Object srcValue = field.get(src);
                Object targetValue = field.get(target);
                if (srcValue == null && target == null) {
                    continue;
                }
                if (srcValue == null && targetValue != null) {
                    return true;
                }
                if (srcValue != null && targetValue == null) {
                    return true;
                }
                if (srcValue == targetValue || srcValue.equals(targetValue)) {
                    continue;
                } else {
                    return true;
                }
            }
        } catch (
                IllegalAccessException e
                ) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> getListByResource(Context context, int resId) {
        List<String> list = new ArrayList<String>();
        String[] array = context.getResources().getStringArray(resId);
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        list.add(0, Constant.KEY_WORD);
        return list;
    }
}
