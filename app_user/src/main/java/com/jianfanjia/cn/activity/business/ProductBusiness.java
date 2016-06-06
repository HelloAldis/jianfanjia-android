package com.jianfanjia.cn.activity.business;

import android.text.TextUtils;

import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.activity.config.Global;
import com.jianfanjia.cn.activity.tools.BusinessCovertUtil;

/**
 * Description: com.jianfanjia.cn.activity.business
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-06 15:13
 */
public class ProductBusiness {

    public static String getProductBaseShowLine1(Product product) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(product.getHouse_area() + "㎡，");
        stringBuilder.append(BusinessCovertUtil.convertDectypeToShow(product.getDec_type()) + "，");
        if (product.getDec_type().equals(Global.DEC_TYPE_BUSINESS)) {
            if (!TextUtils.isEmpty(product.getBusiness_house_type())) {
                stringBuilder.append(BusinessCovertUtil.convertBusinessHouseTypeToShow(product.getBusiness_house_type
                        ()) + "，");
            }
        } else {
            if (!TextUtils.isEmpty(product.getHouse_type())) {
                stringBuilder.append(BusinessCovertUtil.convertHouseTypeToShow(product.getHouse_type()) + "，");
            }
        }
        stringBuilder.append(BusinessCovertUtil.convertDecStyleToShow(product.getDec_style()) + "风格");
        return stringBuilder.toString();
    }

    public static String getProductBaseShowLine2(Product product) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BusinessCovertUtil.convertWorktypeToShow(product.getWork_type()) + "，");
        stringBuilder.append(product.getTotal_price() + "万元");
        return stringBuilder.toString();
    }
}
