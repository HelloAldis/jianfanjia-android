package com.jianfanjia.cn.business;

import java.lang.reflect.Field;
import java.util.List;

import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.PlanPriceDetail;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.common.base.application.BaseApplication;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.business
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-29 10:35
 */
public class RequirementBusiness {

    public static final int TEN_THOUSAND = 10000;

    public static final int PRICE_EVERY_UNIT_365 = 365;//每平方米365元
    public static final int PRICE_EVERY_UNIT_365_MIN_AREA = 80;//365基础包最小80平方米
    public static final int PRICE_EVERY_UNIT_365_MAX_AREA = 120;//365基础包最大面积120平方米

    public static final int HIGH_POINT_PRICE_EVERY_UNIT_HALF = 1000;//高端包半包要求：1000元/平方米
    public static final int HIGH_POINT_PRICE_EVERY_UNIT_ALL = 2500;//高端包的全包要求：2500元/平方米
    public static final int HIGH_POINT_PRICE_EVERY_UNIT_DESIGNER = 200;//高端包的设计要求：200元/平方米

    public static final String PACKGET_DEFAULT = "0";//默认装修包
    public static final String PACKGET_365 = "1";//365基础包
    public static final String PACKGET_HIGH_POINT = "2";//匠心定制包

    public static final String PACKGET_365_ITEM = "365基础包";

    public static final String TAG_NEW_GENERATE = "新锐先锋";
    public static final String TAG_HIGH_POINT = "匠心定制";
    public static final String TAG_MIDDER_GENERATE = "暖暖走心";

    public static final String WORK_TYPE_HALF_PACKGET = "0";//半包
    public static final String WORK_TYPE_ALL_PACKGET = "1";//全包
    public static final String WORK_TYPE_PURE_DESIGNER = "2";//纯设计

    //装修面积是否属于365基础包
    public static boolean isAreaBelong365(int houseArea) {
        return (houseArea >= PRICE_EVERY_UNIT_365_MIN_AREA && houseArea <= PRICE_EVERY_UNIT_365_MAX_AREA);
    }

    //判断需求的包的类型
    public static String getReqPackgetType(String workType, int totalBudget, int houseArea) {
        if (totalBudget * TEN_THOUSAND / houseArea >= HIGH_POINT_PRICE_EVERY_UNIT_HALF && workType.equals
                (WORK_TYPE_HALF_PACKGET)) {
            return PACKGET_HIGH_POINT;
        }
        if (totalBudget * TEN_THOUSAND / houseArea >= HIGH_POINT_PRICE_EVERY_UNIT_ALL && workType.equals
                (WORK_TYPE_ALL_PACKGET)) {
            return PACKGET_HIGH_POINT;
        }
        if (totalBudget * TEN_THOUSAND / houseArea >= HIGH_POINT_PRICE_EVERY_UNIT_DESIGNER && workType.equals
                (WORK_TYPE_PURE_DESIGNER)) {
            return PACKGET_HIGH_POINT;
        }

        if (isAreaBelong365(houseArea) && !workType.equals(WORK_TYPE_PURE_DESIGNER)) {
            return PACKGET_365;
        }

        return PACKGET_DEFAULT;
    }

    public static String covertPriceToShow(float price) {
        return String.format("%.2f", price);
    }

    //拿到单位为元的个性化费用
    public static String getIndividurationPrice(String totalPrice, String basicPrice) {
        int inividurationPrice = Integer.parseInt(totalPrice) - Integer.parseInt(basicPrice);
        return inividurationPrice + "";
    }

    public static PlanPriceDetail getPackget365PriceDetail(List<PlanPriceDetail> planPriceDetails) {
        for (PlanPriceDetail planPriceDetail : planPriceDetails) {
            if (planPriceDetail.getItem().equals(PACKGET_365_ITEM)) {
                return planPriceDetail;
            }
        }
        return null;
    }

    public static boolean isHighPointDesigner(Designer designerInfo) {
        boolean isHighPoint = false;
        List<String> tags = designerInfo.getTags();
        if (tags != null && tags.size() > 0) {
            String tag = tags.get(0);
            if (tag.equals(RequirementBusiness.TAG_HIGH_POINT)) {
                isHighPoint = true;
            }
        }
        return isHighPoint;
    }

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
