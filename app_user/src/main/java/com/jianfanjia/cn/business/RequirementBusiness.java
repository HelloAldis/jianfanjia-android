package com.jianfanjia.cn.business;

import java.lang.reflect.Field;

import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.base.application.BaseApplication;

/**
 * Description: com.jianfanjia.cn.business
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-29 10:35
 */
public class RequirementBusiness {

    private String dec_style = "0";
    private String dec_type = "0";
    private String work_type = "0";
    private String communication_type = "0";
    private String prefer_sex = "2";

    public static void initHomeRequirement(Requirement requirement) {
        requirement.setDec_type("0");
        requirement.setDec_style("0");
        requirement.setWork_type("0");
        requirement.setPrefer_sex("2");
        requirement.setHouse_type("2");
        requirement.setProvince(BaseApplication.getInstance().getString(R.string.default_provice));
        requirement.setCity(BaseApplication.getInstance().getString(R.string.default_city));
    }

    public static void initBussinessRequirement(Requirement requirement) {
        requirement.setDec_type("1");
        requirement.setDec_style("0");
        requirement.setWork_type("0");
        requirement.setPrefer_sex("2");
        requirement.setBusiness_house_type("0");
        requirement.setProvince(BaseApplication.getInstance().getString(R.string.default_provice));
        requirement.setCity(BaseApplication.getInstance().getString(R.string.default_city));
    }

    /**
     * 比较需求是否改变
     *
     * @param src
     * @param target
     * @return
     */
    public static boolean isRequirementChange(Requirement src, Requirement target) {
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
