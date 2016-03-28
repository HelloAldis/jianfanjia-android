package com.jianfanjia.cn.api;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.guest.FeedBackRequest;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.api.request.guest.VerifyPhoneRequest;
import com.jianfanjia.api.request.user.BindingWeiXinRequest;
import com.jianfanjia.api.request.user.GetMsgDetailRequest;
import com.jianfanjia.api.request.user.SearchDesignerRequest;
import com.jianfanjia.api.request.user.UserByOwnerInfoRequest;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.bean.NoticeDetailInfo;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.config.Url_New;

/**
 * Created by Aldis on 16/2/26.
 */

/**
 * 所有API接口都定义在这里
 */
public class Api {

    public static void searchDesigner(SearchDesignerRequest request, ApiCallback<ApiResponse<MyFavoriteDesigner>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DESIGNER, request, apiCallback);
    }

    public static void uploadImage(UploadPicRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okUpload(Url_New.getInstance().UPLOAD_IMAGE, request, request.getBytes(), apiCallback);
    }

    //游客反馈
    public static void feedBack(FeedBackRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().FEEDBACK_URL, request, apiCallback);
    }

    //游客验证手机是否注册
    public static void verifyPhone(VerifyPhoneRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().VERIFY_PHONE, request, apiCallback);
    }

    //游客发送验证码
    public static void sendVerification(SendVerificationRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_CODE_URL, request, apiCallback);
    }

    //业主绑定微信
    public static void bindingWeixin(BindingWeiXinRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().BIND_WEIXIN, request, apiCallback);
    }

    //业主通知详情
    public static void getNoticeDetail(GetMsgDetailRequest request, ApiCallback<ApiResponse<NoticeDetailInfo>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_USER_MSG_DETAIL, request, apiCallback);
    }

    //业主获取个人信息
    public static void get_Owner_Info(UserByOwnerInfoRequest request, ApiCallback<ApiResponse<OwnerInfo>>
            apiCallback) {
        ApiClient.okGet(Url_New.getInstance().GET_OWER_INFO, request, apiCallback);
    }
}
