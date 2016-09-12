package com.jianfanjia.cn.designer.business;

import android.text.TextUtils;

import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.BusinessCovertUtil;

/**
 * Description: com.jianfanjia.cn.designer.business
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 17:59
 */
public class ProductBusiness {

    public static final int PRODUCT_AUTH_SUCCESS_MIN_COUNT = 3;//最少需要认证成功3个作品

    public static final String PRODUCT_NOT_AUTH = "0";//为审核
    public static final String PRODUCT_AUTH_SUCCESS = "1";//认证成功
    public static final String PRODUCT_AUTH_FAILURE = "2";//认证失败
    public static final String PRODUCT_AUTH_VIOLATION = "3";//违规屏蔽

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
