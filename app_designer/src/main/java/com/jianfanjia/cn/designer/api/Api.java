package com.jianfanjia.cn.designer.api;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.CommentList;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.UserMessageList;
import com.jianfanjia.api.request.common.AddCommentRequest;
import com.jianfanjia.api.request.common.CheckVersionRequest;
import com.jianfanjia.api.request.common.GetCommentsRequest;
import com.jianfanjia.api.request.common.RefreshSessionRequest;
import com.jianfanjia.api.request.common.SearchUserCommentRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.api.request.guest.RegisterRequest;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.api.request.guest.UpdatePasswordRequest;
import com.jianfanjia.api.request.guest.VerifyPhoneRequest;
import com.jianfanjia.cn.designer.bean.UpdateVersion;
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

    public static void refreshSession(RefreshSessionRequest request, ApiCallback<ApiResponse<Designer>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REFRESH_SESSION, request, apiCallback);
    }

    public static void checkVesion(CheckVersionRequest request, ApiCallback<ApiResponse<UpdateVersion>> apiCallback) {
        ApiClient.okGet(Url_New.getInstance().UPDATE_VERSION_URL, request, apiCallback);
    }

    public static void getCommentList(GetCommentsRequest request, ApiCallback<ApiResponse<CommentList>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_COMMENT, request, apiCallback);
    }

    public static void addComment(AddCommentRequest request, ApiCallback<ApiResponse<Object>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().ADD_COMMENT, request, apiCallback);
    }

    public static void searchUserComment(SearchUserCommentRequest request, ApiCallback<ApiResponse<UserMessageList>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_USER_COMMENT, request, apiCallback);
    }

}
