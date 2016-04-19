package com.jianfanjia.cn.supervisor.api;

import java.util.List;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.CommentList;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.SuperVisor;
import com.jianfanjia.api.model.UpdateVersion;
import com.jianfanjia.api.request.common.AddCommentRequest;
import com.jianfanjia.api.request.common.AgreeRescheduleRequest;
import com.jianfanjia.api.request.common.ApplyRescheduleRequest;
import com.jianfanjia.api.request.common.CheckVersionRequest;
import com.jianfanjia.api.request.common.DeleteImageToProcessRequest;
import com.jianfanjia.api.request.common.GetCommentsRequest;
import com.jianfanjia.api.request.common.GetProcessInfoRequest;
import com.jianfanjia.api.request.common.RefreshSessionRequest;
import com.jianfanjia.api.request.common.RefuseRescheduleRequest;
import com.jianfanjia.api.request.common.SubmitImageToProcessRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.designer.AddImageToCheckRequest;
import com.jianfanjia.api.request.designer.DeleteCheckImgRequest;
import com.jianfanjia.api.request.designer.FinishSectionItemRequest;
import com.jianfanjia.api.request.designer.GetProcessListRequest;
import com.jianfanjia.api.request.designer.GetRequirementPlanListRequest;
import com.jianfanjia.api.request.designer.NotifyOwnerCheckRequest;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.api.request.supervisor.GetSuperVisorRequest;
import com.jianfanjia.api.request.supervisor.UpdateSuperVisorRequest;
import com.jianfanjia.cn.supervisor.config.Url_New;

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

    public static void login(LoginRequest request, ApiCallback<ApiResponse<SuperVisor>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().LOGIN_URL, request, apiCallback);
    }

    public static void refreshSession(RefreshSessionRequest request, ApiCallback<ApiResponse<SuperVisor>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REFRESH_SESSION, request, apiCallback);
    }

    public static void checkVesion(CheckVersionRequest request, ApiCallback<ApiResponse<UpdateVersion>> apiCallback) {
        ApiClient.okGet(Url_New.getInstance().UPDATE_VERSION_URL, request, apiCallback);
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

    public static void applyReschedule(ApplyRescheduleRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().POST_RESCHDULE, request, apiCallback);
    }

    public static void agreeReschedule(AgreeRescheduleRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().AGREE_RESCHDULE, request, apiCallback);
    }

    public static void refuseReschedule(RefuseRescheduleRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REFUSE_RESCHDULE, request, apiCallback);
    }

    public static void getProcessList(GetProcessListRequest request, ApiCallback<ApiResponse<List<Process>>>
            apiCallback) {
        ApiClient.okGet(Url_New.getInstance().GET_PROCESS_LIST, request, apiCallback);
    }

    public static void getRequirementPlanList(GetRequirementPlanListRequest request,
                                              ApiCallback<ApiResponse<List<Plan>>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().USER_REQUIREMENT_PLANS, request, apiCallback);
    }

    public static void getProcessInfoDetail(GetProcessInfoRequest request, ApiCallback<ApiResponse<Process>>
            apiCallback) {
        String getProcessUrl = Url_New.getInstance().GET_PROCESSINFO_BYID.replace(Url_New.ID,
                request.getProcessId());
        ApiClient.okGet(getProcessUrl, request, apiCallback);
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

    public static void getSuperVisorInfoDetail(GetSuperVisorRequest request,ApiCallback<ApiResponse<SuperVisor>> apiCallback){
        ApiClient.okPost(Url_New.getInstance().GET_SUPERVISOR_INFO,request,apiCallback);
    }

    public static void updateSuperVisorInfo(UpdateSuperVisorRequest request,ApiCallback<ApiResponse<String>> apiCallback){
        ApiClient.okPost(Url_New.getInstance().UPDATE_SUPERVISOR_INFO,request,apiCallback);
    }

}
