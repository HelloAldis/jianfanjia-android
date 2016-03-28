package com.jianfanjia.cn.api;

import java.util.List;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.common.CheckVersionRequest;
import com.jianfanjia.api.request.common.RefreshSessionRequest;
import com.jianfanjia.api.request.common.SubmitImageToProcessRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.api.request.guest.RegisterRequest;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.api.request.guest.UpdatePasswordRequest;
import com.jianfanjia.api.request.guest.VerifyPhoneRequest;
import com.jianfanjia.api.request.user.AgreeRescheduleRequest;
import com.jianfanjia.api.request.user.ApplyRescheduleRequest;
import com.jianfanjia.api.request.user.ChooseDesignerPlanRequest;
import com.jianfanjia.api.request.user.ConfirmCheckRequest;
import com.jianfanjia.api.request.user.ConfirmContractRequest;
import com.jianfanjia.api.request.user.ConfirmMeasureHouseRequest;
import com.jianfanjia.api.request.user.EvaluateDesignerRequest;
import com.jianfanjia.api.request.user.GetCanOrderDesignerListRequest;
import com.jianfanjia.api.request.user.GetContractInfoRequest;
import com.jianfanjia.api.request.user.GetDesignerPlanListRequest;
import com.jianfanjia.api.request.user.GetOrderedDesignerListRequest;
import com.jianfanjia.api.request.user.GetProcessInfoRequest;
import com.jianfanjia.api.request.user.OrderDesignerRequest;
import com.jianfanjia.api.request.user.RefuseRescheduleRequest;
import com.jianfanjia.api.request.user.ReplaceOrderedDesignerRequest;
import com.jianfanjia.api.request.user.SearchDesignerRequest;
import com.jianfanjia.api.request.user.UpdateRequirementRequest;
import com.jianfanjia.cn.bean.ContractInfo;
import com.jianfanjia.cn.bean.DesignerCanOrderListInfo;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.bean.PlanInfo;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.UpdateVersion;
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

    public static void login(LoginRequest request, ApiCallback<ApiResponse<LoginUserBean>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().LOGIN_URL, request, apiCallback);
    }

    public static void register(RegisterRequest request, ApiCallback<ApiResponse<LoginUserBean>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REGISTER_URL, request, apiCallback);
    }

    public static void refreshSession(RefreshSessionRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REFRESH_SESSION, request, apiCallback);
    }

    public static void updatePassword(UpdatePasswordRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().UPDATE_PASS_URL, request, apiCallback);
    }

    public static void checkVesion(CheckVersionRequest request, ApiCallback<ApiResponse<UpdateVersion>> apiCallback) {
        ApiClient.okGet(Url_New.getInstance().UPDATE_VERSION_URL, request, apiCallback);
    }

    public static void verifyPhone(VerifyPhoneRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().VERIFY_PHONE, request, apiCallback);
    }

    public static void sendVerification(SendVerificationRequest
                                                request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_CODE_URL, request, apiCallback);
    }


    public static void getCanOrderDesigner(GetCanOrderDesignerListRequest request,
                                           ApiCallback<ApiResponse<DesignerCanOrderListInfo>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REQUIREMENT_ORDER_DESIGNER_LIST, request, apiCallback);
    }

    public static void getOrderedDesignerList(GetOrderedDesignerListRequest request,
                                              ApiCallback<ApiResponse<List<OrderDesignerInfo>>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().USER_ORDERD_DESIGNERS, request, apiCallback);
    }

    public static void orderDesigner(OrderDesignerRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().USER_ORDER_DESIGNER, request, apiCallback);
    }

    public static void confirmSectionCheck(ConfirmCheckRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().CONFIRM_CHECK_DONE_BY_OWNER, request, apiCallback);
    }

    public static void getContractInfo(GetContractInfoRequest request, ApiCallback<ApiResponse<ContractInfo>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().ONE_CONTRACT, request, apiCallback);
    }

    public static void confirmContract(ConfirmContractRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().PROCESS, request, apiCallback);
    }

    public static void getDesignerPlanList(GetDesignerPlanListRequest request,
                                           ApiCallback<ApiResponse<List<PlanInfo>>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().USER_REQUIREMENT_PLANS, request, apiCallback);
    }

    public static void confirmMeasureHouse(ConfirmMeasureHouseRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DESIGNER_HOUSE_CHECKED, request, apiCallback);
    }

    public static void getProcessInfoDetail(GetProcessInfoRequest request, ApiCallback<ApiResponse<ProcessInfo>>
            apiCallback) {
        String getProcessUrl = Url_New.getInstance().GET_PROCESSINFO_BYID.replace(Url_New.ID,
                request.getProcessId());
        ApiClient.okGet(getProcessUrl, request, apiCallback);
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

    public static void evaluateDesigner(EvaluateDesignerRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().EVALUATE_DESIGNER_BY_USER, request, apiCallback);
    }

    public static void chooseDesignerPlan(ChooseDesignerPlanRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().USER_CHOOSE_PLAN, request, apiCallback);
    }

    public static void replaceOrderedDesigner(ReplaceOrderedDesignerRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().USER_CHANGE_ORDERD_DESIGNER, request, apiCallback);
    }

    public static void updateRequirement(UpdateRequirementRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REQUIREMENT_UPDATE, request, apiCallback);
    }

    public static void publishRequirement(UpdateRequirementRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().POST_REQUIREMENT, request, apiCallback);
    }

    public static void submitImageToProcess(SubmitImageToProcessRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().POST_PROCESS_IMAGE, request, apiCallback);
    }


}
