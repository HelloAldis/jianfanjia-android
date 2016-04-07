package com.jianfanjia.cn.designer.business;

import java.util.List;

import com.jianfanjia.api.model.PlanPriceDetail;

/**
 * Description: com.jianfanjia.cn.designer.business
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-07 15:32
 */
public class RequirementBusiness {

    public static final int TEN_THOUSAND = 10000;

    public static final int PRICE_EVERY_UNIT_365 = 365;//每平方米365元
    public static final int PRICE_EVERY_UNIT_365_MIN_AREA = 80;//365基础包最小80平方米
    public static final int PRICE_EVERY_UNIT_365_MAX_AREA = 120;//365基础包最大面积120平方米

    public static final String PACKGET_DEFAULT = "0";//默认装修包
    public static final String PACKGET_365 = "1";//365基础包

    public static final String PACKGET_365_ITEM = "365基础包";

    //装修面积是否属于365基础包
    public static boolean isAreaBelong365(int houseArea) {
        return (houseArea >= PRICE_EVERY_UNIT_365_MIN_AREA && houseArea <= PRICE_EVERY_UNIT_365_MAX_AREA);
    }

    public static String covertPriceToShow(float price) {
        return String.format("%.2f", price);
    }

    //拿到单位为元的个性化费用
    public static String getIndividurationPrice(String totalPrice,String basicPrice){
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
}
