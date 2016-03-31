package com.jianfanjia.cn.designer.api;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.api.request.guest.RegisterRequest;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.api.request.guest.UpdatePasswordRequest;
import com.jianfanjia.api.request.guest.VerifyPhoneRequest;
import com.jianfanjia.cn.designer.config.Url_New;

/**
 * Description: com.jianfanjia.cn.designer.api
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-30 15:33
 */
public class Api {

    public static void uploadImage(UploadPicRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okUpload(Url_New.getInstance().UPLOAD_IMAGE, request, request.getBytes(), apiCallback);
    }

    public static void login(LoginRequest request, ApiCallback<ApiResponse<Designer>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().LOGIN_URL, request, apiCallback);
    }

    //游客验证手机是否注册
    public static void verifyPhone(VerifyPhoneRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().VERIFY_PHONE, request, apiCallback);
    }

    public static void sendVerification(SendVerificationRequest
                                                request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_CODE_URL, request, apiCallback);
    }

    public static void register(RegisterRequest request, ApiCallback<ApiResponse<Designer>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REGISTER_URL, request, apiCallback);
    }

    public static void updatePassword(UpdatePasswordRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().UPDATE_PASS_URL, request, apiCallback);
    }

}
