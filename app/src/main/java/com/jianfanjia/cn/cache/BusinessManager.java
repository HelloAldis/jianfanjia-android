package com.jianfanjia.cn.cache;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;

public class BusinessManager {

    /**
     * 分开装修美图keyword
     *
     * @param keyWord
     * @return
     */
    public static String spilteKeyWord(String keyWord) {
        if (TextUtils.isEmpty(keyWord)) return null;
        String[] keywords = keyWord.split(",");
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : keywords) {
            stringBuffer.append("#");
            stringBuffer.append(key);
            stringBuffer.append("   ");
        }
        return stringBuffer.toString();
    }

    public static String getWorkType(String workType) {
        if(workType == null) return null;
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
    public static Process getDefaultProcessInfo(Context context) {
        Process processInfo = null;
        try {
            InputStream is = context.getAssets()
                    .open("default_processinfo.txt");
            String jsonString = StringUtils.toConvertString(is);
            LogTool.d("getDefault",
                    jsonString);
            processInfo = JsonParser.jsonToBean(jsonString, Process.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return processInfo;
    }

    public static ProcessSection getSectionInfoByName(ArrayList<ProcessSection> sections, String name) {
        try {
            for (ProcessSection info : sections) {
                if (info.getName().equals(name)) {
                    return info;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param
     * @return
     */
    public static String convertDectypeToShow(String decType) {
        if (TextUtils.isEmpty(decType)) return null;
        int decPosition = Integer.parseInt(decType);
        String[] dectypes = MyApplication.getInstance().getResources().getStringArray(R.array.arr_dectype);
        if (decPosition < 0 || decPosition > dectypes.length) return null;
        return dectypes[decPosition];
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

    /**
     * @param designFee
     * @return
     */
    public static String convertDesignFeeToShow(String designFee) {
        if (designFee == null) return null;
        int feePosition = Integer.parseInt(designFee);
        String[] designFees = MyApplication.getInstance().getResources().getStringArray(R.array.arr_fee);
        if (feePosition < 0 || feePosition > designFees.length) return null;
        return designFees[feePosition];
    }

    /**
     * 装修直播显示阶段
     *
     * @param section
     * @return
     */
    public static String convertSectionNameToLiveShow(String section) {
        if (section == null) return null;
        int sectionPosition = Integer.parseInt(section);
        String[] sectionList = MyApplication.getInstance().getResources().getStringArray(R.array.live_site_procedure);
        if (sectionPosition < 0 || sectionPosition > sectionList.length) return null;
        return sectionList[sectionPosition];
    }

    /**
     * @param decTypeText
     * @return
     */
    public static String getDecTypeByText(String decTypeText) {
        try {
            String[] items = MyApplication.getInstance().getResources().getStringArray(R.array.arr_dectype);
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(decTypeText)) {
                    return i + "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static String getDecFeeByText(String decFeeText) {
        try {
            String[] items = MyApplication.getInstance().getResources().getStringArray(R.array.arr_fee);
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(decFeeText)) {
                    return i + "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDecAreaByText(String decAreaText) {
        try {
            String[] items = MyApplication.getInstance().getResources().getStringArray(R.array.arr_area);
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(decAreaText)) {
                    return i + "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> convertDecAreaValueByText(String decAreaText) {
        try {
            String area = getDecAreaByText(decAreaText);
            if (null != area) {
                Map<String, Object> param = new HashMap<>();
                switch (Integer.parseInt(area)) {
                    case 0:
                        param.put("$lt", 90);
                        break;
                    case 1:
                        param.put("$gte", 90);
                        param.put("$lt", 120);
                        break;
                    case 2:
                        param.put("$gte", 120);
                        param.put("$lt", 150);
                        break;
                    case 3:
                        param.put("$gte", 150);
                        param.put("$lt", 200);
                        break;
                    default:
                        param.put("$gte", 200);
                        break;
                }
                return param;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getListByResource(Context context, int resId) {
        List<String> list = new ArrayList<>();
        String[] array = context.getResources().getStringArray(resId);
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        list.add(0, Constant.KEY_WORD);
        return list;
    }

    public static String getHouseTypeStr(List<String> decHouseTypes) {
        StringBuilder builder = new StringBuilder();
        for (String str : decHouseTypes) {
            LogTool.d("getHouseTypeStr()", "str:" + str);
            builder.append(BusinessManager.convertHouseTypeToShow(str) + "  ");
        }
        return builder.toString();
    }

    public static String getDecTypeStr(List<String> decTypes) {
        StringBuilder builder = new StringBuilder();
        for (String str : decTypes) {
            LogTool.d("getDecTypeStr()", "str:" + str);
            builder.append(BusinessManager.convertDectypeToShow(str) + "  ");
        }
        return builder.toString();
    }

    public static String getDecStyleStr(List<String> decStyles) {
        StringBuilder builder = new StringBuilder();
        for (String str : decStyles) {
            LogTool.d("getDecStyleStr()", "str:" + str);
            builder.append(BusinessManager.convertDecStyleToShow(str) + "  ");
        }
        return builder.toString();
    }

    public static String getDecDistrictStr(List<String> dec_districts) {
        StringBuilder builder = new StringBuilder();
        for (String str : dec_districts) {
            LogTool.d("getDecDistrictStr()", "str:" + str);
            builder.append(str + "  ");
        }
        return builder.toString();
    }
}
