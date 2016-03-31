package com.jianfanjia.cn.designer.api;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.CommentList;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.api.model.UserMessageList;
import com.jianfanjia.api.request.common.AddCommentRequest;
import com.jianfanjia.api.request.common.AgreeRescheduleRequest;
import com.jianfanjia.api.request.common.ApplyRescheduleRequest;
import com.jianfanjia.api.request.common.CheckVersionRequest;
import com.jianfanjia.api.request.common.DeleteImageToProcessRequest;
import com.jianfanjia.api.request.common.GetCommentsRequest;
import com.jianfanjia.api.request.common.GetMsgDetailRequest;
import com.jianfanjia.api.request.common.GetProcessInfoRequest;
import com.jianfanjia.api.request.common.GetUnReadMsgRequest;
import com.jianfanjia.api.request.common.RefreshSessionRequest;
import com.jianfanjia.api.request.common.RefuseRescheduleRequest;
import com.jianfanjia.api.request.common.SearchUserCommentRequest;
import com.jianfanjia.api.request.common.SearchUserMsgRequest;
import com.jianfanjia.api.request.common.SubmitImageToProcessRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.designer.AddImageToCheckRequest;
import com.jianfanjia.api.request.designer.ConfigContractRequest;
import com.jianfanjia.api.request.designer.ConfigMeaHouseTimeRequest;
import com.jianfanjia.api.request.designer.DeleteCheckImgRequest;
import com.jianfanjia.api.request.designer.FinishSectionItemRequest;
import com.jianfanjia.api.request.designer.GetProcessListRequest;
import com.jianfanjia.api.request.designer.GetRequirementListRequest;
import com.jianfanjia.api.request.designer.GetRequirementPlanListRequest;
import com.jianfanjia.api.request.designer.NotifyOwnerCheckRequest;
import com.jianfanjia.api.request.designer.NotifyOwnerMeasureHouseRequest;
import com.jianfanjia.api.request.designer.RefuseRequirementRequest;
import com.jianfanjia.api.request.designer.ResponseRequirementRequest;
import com.jianfanjia.api.request.guest.FeedBackRequest;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.api.request.guest.RegisterRequest;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.api.request.guest.UpdatePasswordRequest;
import com.jianfanjia.api.request.guest.VerifyPhoneRequest;
import com.jianfanjia.cn.designer.bean.UpdateVersion;
import com.jianfanjia.cn.designer.config.Url_New;

import java.util.List;

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

    //游客反馈
    public static void feedBack(FeedBackRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().FEEDBACK_URL, request, apiCallback);
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

    public static void configContract(ConfigContractRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DESIGNER_CONFIG_CONTRACT, request, apiCallback);
    }

    public static void configMeaHouse(ConfigMeaHouseTimeRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DESIGNER_HOUSE_CHECKED, request, apiCallback);
    }

    public static void deleteCheckImg(DeleteCheckImgRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DELETE_YANSHOU_IMG_BY_DESIGNER, request, apiCallback);
    }

    public static void addImageToCheck(AddImageToCheckRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SUBMIT_YAHSHOU_IMAGE, request, apiCallback);
    }

    public static void notifyOwnerCheck(NotifyOwnerCheckRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().CONFIRM_CHECK_BY_DESIGNER, request, apiCallback);
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

    public static void applyReschedule(ApplyRescheduleRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().POST_RESCHDULE, request, apiCallback);
    }

    public static void agreeReschedule(AgreeRescheduleRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().AGREE_RESCHDULE, request, apiCallback);
    }

    public static void refuseReschedule(RefuseRescheduleRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REFUSE_RESCHDULE, request, apiCallback);
    }

    public static void searchUserMsg(SearchUserMsgRequest request, ApiCallback<ApiResponse<UserMessageList>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_USER_MSG, request, apiCallback);
    }

    public static void getUnReadMsg(GetUnReadMsgRequest request, ApiCallback<ApiResponse<List<Integer>>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_UNREAD_MSG_COUNT, request, apiCallback);
    }

    public static void getUserMsgDetail(GetMsgDetailRequest request, ApiCallback<ApiResponse<UserMessage>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_USER_MSG_DETAIL, request, apiCallback);
    }

    public static void getAllRequirementList(GetRequirementListRequest request,
                                             ApiCallback<ApiResponse<List<Requirement>>>
                                                     apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_ALL_REQUIREMENT_LIST, request, apiCallback);
    }

    public static void getProcessList(GetProcessListRequest request, ApiCallback<ApiResponse<List<Process>>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_PROCESS_LIST, request, apiCallback);
    }

    public static void notifyOwnerConfirmHouse(NotifyOwnerMeasureHouseRequest request,
                                               ApiCallback<ApiResponse<String>>
                                                       apiCallback) {
        ApiClient.okPost(Url_New.getInstance().NOTIFY_OWNER_MEASURE_HOUSE, request, apiCallback);
    }

    public static void refuseRequirement(RefuseRequirementRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REFUSE_REQUIREMENT, request, apiCallback);
    }

    public static void responseRequirement(ResponseRequirementRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().RESPONSE_REQUIREMENT, request, apiCallback);
    }

    public static void getRequirementPlanList(GetRequirementPlanListRequest request,
                                              ApiCallback<ApiResponse<List<Plan>>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().USER_REQUIREMENT_PLANS, request, apiCallback);
    }

    public static void getProcessInfoDetail(GetProcessInfoRequest request, ApiCallback<ApiResponse<com.jianfanjia.api
            .model
            .Process>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_PROCESSINFO_BYID, request, apiCallback);
    }

    public static void finishSectionItem(FinishSectionItemRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().POST_PROCESS_DONE_ITEM, request, apiCallback);
    }

    public static void deleteImageToProcess(DeleteImageToProcessRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DELETE_PROCESS_PIC, request, apiCallback);
    }

    public static void submitImageToProcess(SubmitImageToProcessRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().POST_PROCESS_IMAGE, request, apiCallback);
    }

}
