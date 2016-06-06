package com.jianfanjia.cn.designer.business;

import android.text.TextUtils;

import com.jianfanjia.api.model.Designer;

/**
 * Created by asus on 2016/5/19.
 */
public class DesignerBusiness {

    public static final String DESIGNER_NOT_APPLY = "0";//未提交认证
    public static final String DESIGNER_NOT_AUTH = "1";//未审核
    public static final String DESIGNER_AUTH_SUCCESS = "2";//认证成功
    public static final String DESIGNER_AUTH_FAILURE = "3";//认证失败
    public static final String DESIGNER_AUTH_VIOLATION = "4";//违规屏蔽

    public static final String AGREE_LICENSE = "1";//同意入驻协议
    public static final String DISAGREE_LICENSE = "0";//没有同意入驻协议

    public static int getAuthProcessPercent(Designer designer) {
        int authprocess = 0;
        if (!TextUtils.isEmpty(designer.getAuth_type()) && designer.getAuth_type().equals(DESIGNER_AUTH_SUCCESS)) {
            authprocess += 20;
        }
        if (!TextUtils.isEmpty(designer.getUid_auth_type()) && designer.getUid_auth_type().equals
                (DESIGNER_AUTH_SUCCESS)) {
            authprocess += 20;
        }
        if (!TextUtils.isEmpty(designer.getWork_auth_type()) && designer.getWork_auth_type().equals
                (DESIGNER_AUTH_SUCCESS)) {
            authprocess += 20;
        }
        if (!TextUtils.isEmpty(designer.getEmail_auth_type()) && designer.getEmail_auth_type().equals
                (DESIGNER_AUTH_SUCCESS)) {
            authprocess += 20;
        }
        if (designer.getAuthed_product_count() > ProductBusiness.PRODUCT_AUTH_SUCCESS_MIN_COUNT) {
            authprocess += 20;
        }
        return authprocess;
    }

    public static boolean isFinishBaseAuth(Designer designer){
        if(!TextUtils.isEmpty(designer.getAuth_type()) && designer.getAuth_type().equals(DESIGNER_AUTH_SUCCESS)
                && designer.getAuthed_product_count() >= 3){
            return true;
        }else{
            return false;
        }
    }
}
