package com.jianfanjia.cn.api;

import java.util.List;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.BeautifulImageList;
import com.jianfanjia.api.model.CommentList;
import com.jianfanjia.api.model.DecorateLiveList;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.DesignerCanOrderList;
import com.jianfanjia.api.model.DesignerList;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductList;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.api.model.UserMessageList;
import com.jianfanjia.api.request.common.AddBeautyImgRequest;
import com.jianfanjia.api.request.common.AddCollectionRequest;
import com.jianfanjia.api.request.common.AddCommentRequest;
import com.jianfanjia.api.request.common.AgreeRescheduleRequest;
import com.jianfanjia.api.request.common.ApplyRescheduleRequest;
import com.jianfanjia.api.request.common.CheckVersionRequest;
import com.jianfanjia.api.request.common.DeleteBeautyImgRequest;
import com.jianfanjia.api.request.common.DeleteCollectionRequest;
import com.jianfanjia.api.request.common.DeleteImageToProcessRequest;
import com.jianfanjia.api.request.common.GetBeautyImgListRequest;
import com.jianfanjia.api.request.common.GetCollectionRequest;
import com.jianfanjia.api.request.common.GetCommentsRequest;
import com.jianfanjia.api.request.common.GetDecorateLiveRequest;
import com.jianfanjia.api.request.common.GetMsgDetailRequest;
import com.jianfanjia.api.request.common.GetUnReadMsgRequest;
import com.jianfanjia.api.request.common.RefreshSessionRequest;
import com.jianfanjia.api.request.common.RefuseRescheduleRequest;
import com.jianfanjia.api.request.common.SearchUserCommentRequest;
import com.jianfanjia.api.request.common.SearchUserMsgRequest;
import com.jianfanjia.api.request.common.SubmitImageToProcessRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.guest.DesignerHomePageRequest;
import com.jianfanjia.api.request.guest.FeedBackRequest;
import com.jianfanjia.api.request.guest.GetHomeProductRequest;
import com.jianfanjia.api.request.guest.GetProductHomePageRequest;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.api.request.guest.RegisterRequest;
import com.jianfanjia.api.request.guest.SearchDecorationImgRequest;
import com.jianfanjia.api.request.guest.SearchDesignerProductRequest;
import com.jianfanjia.api.request.guest.SearchDesignerRequest;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.api.request.guest.UpdatePasswordRequest;
import com.jianfanjia.api.request.guest.VerifyPhoneRequest;
import com.jianfanjia.api.request.guest.WeiXinRegisterRequest;
import com.jianfanjia.api.request.user.AddFavoriteDesignerRequest;
import com.jianfanjia.api.request.user.BindPhoneRequest;
import com.jianfanjia.api.request.user.BindingWeiXinRequest;
import com.jianfanjia.api.request.user.ChooseDesignerPlanRequest;
import com.jianfanjia.api.request.user.ConfirmCheckRequest;
import com.jianfanjia.api.request.user.ConfirmContractRequest;
import com.jianfanjia.api.request.user.ConfirmMeasureHouseRequest;
import com.jianfanjia.api.request.user.DeleteFavoriteDesignerRequest;
import com.jianfanjia.api.request.user.EvaluateDesignerRequest;
import com.jianfanjia.api.request.user.FavoriteDesignerListRequest;
import com.jianfanjia.api.request.user.GetCanOrderDesignerListRequest;
import com.jianfanjia.api.request.user.GetContractInfoRequest;
import com.jianfanjia.api.request.user.GetDesignerPlanListRequest;
import com.jianfanjia.api.request.user.GetOrderedDesignerListRequest;
import com.jianfanjia.api.request.common.GetProcessInfoRequest;
import com.jianfanjia.api.request.user.GetRequirementListRequest;
import com.jianfanjia.api.request.user.OrderDesignerRequest;
import com.jianfanjia.api.request.user.PublishRequirementRequest;
import com.jianfanjia.api.request.user.ReplaceOrderedDesignerRequest;
import com.jianfanjia.api.request.user.UpdateOwnerInfoRequest;
import com.jianfanjia.api.request.user.UpdateRequirementRequest;
import com.jianfanjia.api.request.user.UserByOwnerInfoRequest;
import com.jianfanjia.api.model.UpdateVersion;
import com.jianfanjia.cn.config.Url_New;


/**
 * Created by Aldis on 16/2/26.
 */

/**
 * 所有API接口都定义在这里
 */
public class Api {

    public static void searchDesigner(SearchDesignerRequest request, ApiCallback<ApiResponse<DesignerList>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DESIGNER, request, apiCallback);
    }

    public static void uploadImage(UploadPicRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okUpload(Url_New.getInstance().UPLOAD_IMAGE, request, request.getBytes(), apiCallback);
    }

    public static void login(LoginRequest request, ApiCallback<ApiResponse<User>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().LOGIN_URL, request, apiCallback);
    }

    public static void register(RegisterRequest request, ApiCallback<ApiResponse<User>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REGISTER_URL, request, apiCallback);
    }

    public static void weiXinRegister(WeiXinRegisterRequest request, ApiCallback<ApiResponse<User>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().WEIXIN_LOGIN_URL, request, apiCallback);
    }

    public static void refreshSession(RefreshSessionRequest request, ApiCallback<ApiResponse<User>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REFRESH_SESSION, request, apiCallback);
    }

    public static void updatePassword(UpdatePasswordRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().UPDATE_PASS_URL, request, apiCallback);
    }

    public static void checkVesion(CheckVersionRequest request, ApiCallback<ApiResponse<UpdateVersion>> apiCallback) {
        ApiClient.okGet(Url_New.getInstance().UPDATE_VERSION_URL, request, apiCallback);
    }

    //游客反馈
    public static void feedBack(FeedBackRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().FEEDBACK_URL, request, apiCallback);
    }

    //游客验证手机是否注册
    public static void verifyPhone(VerifyPhoneRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().VERIFY_PHONE, request, apiCallback);
    }

    public static void bindPhone(BindPhoneRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().BIND_PHONE, request, apiCallback);
    }

    public static void sendVerification(SendVerificationRequest
                                                request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_CODE_URL, request, apiCallback);
    }


    public static void getCanOrderDesigner(GetCanOrderDesignerListRequest request,
                                           ApiCallback<ApiResponse<DesignerCanOrderList>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REQUIREMENT_ORDER_DESIGNER_LIST, request, apiCallback);
    }

    public static void getOrderedDesignerList(GetOrderedDesignerListRequest request,
                                              ApiCallback<ApiResponse<List<Designer>>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().USER_ORDERD_DESIGNERS, request, apiCallback);
    }

    public static void orderDesigner(OrderDesignerRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().USER_ORDER_DESIGNER, request, apiCallback);
    }

    public static void confirmSectionCheck(ConfirmCheckRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().CONFIRM_CHECK_DONE_BY_OWNER, request, apiCallback);
    }

    public static void getContractInfo(GetContractInfoRequest request, ApiCallback<ApiResponse<Requirement>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().ONE_CONTRACT, request, apiCallback);
    }

    public static void confirmContract(ConfirmContractRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().PROCESS, request, apiCallback);
    }

    public static void getDesignerPlanList(GetDesignerPlanListRequest request,
                                           ApiCallback<ApiResponse<List<Plan>>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().USER_REQUIREMENT_PLANS, request, apiCallback);
    }

    public static void confirmMeasureHouse(ConfirmMeasureHouseRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DESIGNER_HOUSE_CHECKED, request, apiCallback);
    }

    public static void getProcessInfoDetail(GetProcessInfoRequest request, ApiCallback<ApiResponse<Process>>
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

    public static void updateRequirement(UpdateRequirementRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().REQUIREMENT_UPDATE, request, apiCallback);
    }

    public static void publishRequirement(PublishRequirementRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().POST_REQUIREMENT, request, apiCallback);
    }

    public static void submitImageToProcess(SubmitImageToProcessRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().POST_PROCESS_IMAGE, request, apiCallback);
    }

    public static void deleteImageToProcess(DeleteImageToProcessRequest request, ApiCallback<ApiResponse<String>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DELETE_PROCESS_PIC, request, apiCallback);
    }

    //业主绑定微信
    public static void bindingWeixin(BindingWeiXinRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().BIND_WEIXIN, request, apiCallback);
    }

    //业主通知详情
    public static void getNoticeDetail(GetMsgDetailRequest request, ApiCallback<ApiResponse<UserMessage>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_USER_MSG_DETAIL, request, apiCallback);
    }

    //业主获取个人信息
    public static void getUserInfo(UserByOwnerInfoRequest request, ApiCallback<ApiResponse<User>>
            apiCallback) {
        ApiClient.okGet(Url_New.getInstance().GET_OWER_INFO, request, apiCallback);
    }

    //业主修改个人资料
    public static void updateUserInfo(UpdateOwnerInfoRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_OWER_INFO, request, apiCallback);
    }

    //业主获取我的装修需求列表
    public static void getRequirementList(GetRequirementListRequest request,
                                          ApiCallback<ApiResponse<List<Requirement>>> apiCallback) {
        ApiClient.okGet(Url_New.getInstance().REQUIREMENT_LIST, request, apiCallback);
    }

    public static void searchDecorationImg(SearchDecorationImgRequest request,
                                           ApiCallback<ApiResponse<BeautifulImageList>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DECORATION_IMG, request, apiCallback);
    }

    public static void getBeautyImgListByUser(GetBeautyImgListRequest request,
                                              ApiCallback<ApiResponse<BeautifulImageList>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_BEAUTY_IMG_LIST_BY_USER, request, apiCallback);
    }

    public static void addBeautyImgByUser(AddBeautyImgRequest request, ApiCallback<ApiResponse<Object>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().ADD_BEAUTY_IMG, request, apiCallback);
    }

    public static void deleteBeautyImgByUser(DeleteBeautyImgRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DELETE_BEAUTY_IMG_BY_USER, request, apiCallback);
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

    public static void getProductHomePage(GetProductHomePageRequest request,
                                          ApiCallback<ApiResponse<Product>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().PRODUCT_HOME_PAGE, request, apiCallback);
    }

    public static void addCollectionByUser(AddCollectionRequest request, ApiCallback<ApiResponse<Object>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().ADD_PRODUCT, request, apiCallback);
    }

    public static void deleteCollectionByUser(DeleteCollectionRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DELETE_PRODUCT_BY_USER, request, apiCallback);
    }

    public static void searchDesignerProduct(SearchDesignerProductRequest request,
                                             ApiCallback<ApiResponse<ProductList>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DESIGNER_PRODUCT, request, apiCallback);
    }

    public static void getDesignerHomePage(DesignerHomePageRequest request, ApiCallback<ApiResponse<Designer>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DESIGNER_HOME_PAGE, request, apiCallback);
    }

    public static void addFavoriteDesigner(AddFavoriteDesignerRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().ADD_FAVORITE_DESIGNER, request, apiCallback);
    }

    public static void deleteFavoriteDesigner(DeleteFavoriteDesignerRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DELETE_FAVORITE_DESIGNER, request, apiCallback);
    }

    public static void get_MyFavoriteDesignerList(FavoriteDesignerListRequest request,
                                                  ApiCallback<ApiResponse<DesignerList>>
                                                          apiCallback) {
        ApiClient.okPost(Url_New.getInstance().FAVORITE_DESIGNER_LIST, request, apiCallback);
    }

    public static void getCollectListByUser(GetCollectionRequest request, ApiCallback<ApiResponse<ProductList>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_PRODUCT_LIST_BY_COLLECTED, request, apiCallback);
    }

    public static void searchShare(GetDecorateLiveRequest request, ApiCallback<ApiResponse<DecorateLiveList>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_SHARE, request, apiCallback);
    }

    public static void getTopProducts(GetHomeProductRequest request, ApiCallback<ApiResponse<List<Product>>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_TOP_PRODUCTS, request, apiCallback);
    }

    public static void searchUserMsg(SearchUserMsgRequest request, ApiCallback<ApiResponse<UserMessageList>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_USER_MSG, request, apiCallback);
    }

    public static void getUnReadMsg(GetUnReadMsgRequest request, ApiCallback<ApiResponse<List<Integer>>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_UNREAD_MSG_COUNT, request, apiCallback);
    }
}
