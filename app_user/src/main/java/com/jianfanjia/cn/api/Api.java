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
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.DiaryInfoList;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.model.DiarySetInfoList;
import com.jianfanjia.api.model.DiaryUpdateInfo;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductList;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.model.UpdateVersion;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.api.model.UserMessageList;
import com.jianfanjia.api.request.common.AddBeautyImgRequest;
import com.jianfanjia.api.request.common.AddCommentRequest;
import com.jianfanjia.api.request.common.AddDiaryFavoriteRequest;
import com.jianfanjia.api.request.common.AddDiaryRequest;
import com.jianfanjia.api.request.common.AddDiarySetFavoriteRequest;
import com.jianfanjia.api.request.common.AddDiarySetRequest;
import com.jianfanjia.api.request.common.AddProductFavoriteRequest;
import com.jianfanjia.api.request.common.AgreeRescheduleRequest;
import com.jianfanjia.api.request.common.ApplyRescheduleRequest;
import com.jianfanjia.api.request.common.CheckVersionRequest;
import com.jianfanjia.api.request.common.DeleteBeautyImgRequest;
import com.jianfanjia.api.request.common.DeleteDiaryRequest;
import com.jianfanjia.api.request.common.DeleteDiarySetFavoriteRequest;
import com.jianfanjia.api.request.common.DeleteImageToProcessRequest;
import com.jianfanjia.api.request.common.DeleteProductFavoriteRequest;
import com.jianfanjia.api.request.common.GetBeautyImgListRequest;
import com.jianfanjia.api.request.common.GetCommentsRequest;
import com.jianfanjia.api.request.common.GetDecorateLiveRequest;
import com.jianfanjia.api.request.common.GetDiarySetFavoriteListRequest;
import com.jianfanjia.api.request.common.GetMsgDetailRequest;
import com.jianfanjia.api.request.common.GetMyDiarySetRequest;
import com.jianfanjia.api.request.common.GetProcessInfoRequest;
import com.jianfanjia.api.request.common.GetProductFavoriteListRequest;
import com.jianfanjia.api.request.common.GetUnReadMsgRequest;
import com.jianfanjia.api.request.common.RefreshSessionRequest;
import com.jianfanjia.api.request.common.RefuseRescheduleRequest;
import com.jianfanjia.api.request.common.SearchUserCommentRequest;
import com.jianfanjia.api.request.common.SearchUserMsgRequest;
import com.jianfanjia.api.request.common.SubmitImageToProcessRequest;
import com.jianfanjia.api.request.common.UpdateDiarySetRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.guest.DesignerHomePageRequest;
import com.jianfanjia.api.request.guest.FeedBackRequest;
import com.jianfanjia.api.request.guest.GetDiaryInfoRequest;
import com.jianfanjia.api.request.guest.GetDiarySetInfoRequest;
import com.jianfanjia.api.request.guest.GetDiaryUpdateRequest;
import com.jianfanjia.api.request.guest.GetHomeProductRequest;
import com.jianfanjia.api.request.guest.GetProductHomePageRequest;
import com.jianfanjia.api.request.guest.GetRecommendDiarySetRequest;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.api.request.guest.PostUserRequirementRequest;
import com.jianfanjia.api.request.guest.RegisterRequest;
import com.jianfanjia.api.request.guest.SearchDecorationImgRequest;
import com.jianfanjia.api.request.guest.SearchDesignerProductRequest;
import com.jianfanjia.api.request.guest.SearchDesignerRequest;
import com.jianfanjia.api.request.guest.SearchDiaryRequest;
import com.jianfanjia.api.request.guest.SearchDiarySetRequest;
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
import com.jianfanjia.api.request.user.GetRequirementListRequest;
import com.jianfanjia.api.request.user.OrderDesignerRequest;
import com.jianfanjia.api.request.user.PublishRequirementRequest;
import com.jianfanjia.api.request.user.ReplaceOrderedDesignerRequest;
import com.jianfanjia.api.request.user.UpdateOwnerInfoRequest;
import com.jianfanjia.api.request.user.UpdateRequirementRequest;
import com.jianfanjia.api.request.user.UserByOwnerInfoRequest;
import com.jianfanjia.cn.config.Url_New;


/**
 * Created by Aldis on 16/2/26.
 */

/**
 * 所有API接口都定义在这里
 */
public class Api {

    public static void searchDesigner(SearchDesignerRequest request, ApiCallback<ApiResponse<DesignerList>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DESIGNER, request, apiCallback, tag);
    }

    public static void uploadImage(UploadPicRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okUpload(Url_New.getInstance().UPLOAD_IMAGE, request, request.getBytes(), apiCallback,tag);
    }

    public static void login(LoginRequest request, ApiCallback<ApiResponse<User>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().LOGIN_URL, request, apiCallback, tag);
    }

    public static void register(RegisterRequest request, ApiCallback<ApiResponse<User>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().REGISTER_URL, request, apiCallback, tag);
    }

    public static void weiXinRegister(WeiXinRegisterRequest request, ApiCallback<ApiResponse<User>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().WEIXIN_LOGIN_URL, request, apiCallback, tag);
    }

    public static void refreshSession(RefreshSessionRequest request, ApiCallback<ApiResponse<User>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().REFRESH_SESSION, request, apiCallback, tag);
    }

    public static void updatePassword(UpdatePasswordRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().UPDATE_PASS_URL, request, apiCallback, tag);
    }

    public static void checkVesion(CheckVersionRequest request, ApiCallback<ApiResponse<UpdateVersion>> apiCallback,Object tag) {
        ApiClient.okGet(Url_New.getInstance().UPDATE_VERSION_URL, request, apiCallback, tag);
    }

    //游客反馈
    public static void feedBack(FeedBackRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().FEEDBACK_URL, request, apiCallback, tag);
    }

    //游客验证手机是否注册
    public static void verifyPhone(VerifyPhoneRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().VERIFY_PHONE, request, apiCallback, tag);
    }

    public static void bindPhone(BindPhoneRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().BIND_PHONE, request, apiCallback, tag);
    }

    public static void sendVerification(SendVerificationRequest
                                                request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_CODE_URL, request, apiCallback, tag);
    }


    public static void getCanOrderDesigner(GetCanOrderDesignerListRequest request,
                                           ApiCallback<ApiResponse<DesignerCanOrderList>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().REQUIREMENT_ORDER_DESIGNER_LIST, request, apiCallback, tag);
    }

    public static void getOrderedDesignerList(GetOrderedDesignerListRequest request,
                                              ApiCallback<ApiResponse<List<Designer>>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().USER_ORDERD_DESIGNERS, request, apiCallback, tag);
    }

    public static void orderDesigner(OrderDesignerRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().USER_ORDER_DESIGNER, request, apiCallback, tag);
    }

    public static void confirmSectionCheck(ConfirmCheckRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().CONFIRM_CHECK_DONE_BY_OWNER, request, apiCallback,tag);
    }

    public static void getContractInfo(GetContractInfoRequest request, ApiCallback<ApiResponse<Requirement>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().ONE_CONTRACT, request, apiCallback, tag);
    }

    public static void confirmContract(ConfirmContractRequest request, ApiCallback<ApiResponse<Process>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().PROCESS, request, apiCallback, tag);
    }

    public static void getDesignerPlanList(GetDesignerPlanListRequest request,
                                           ApiCallback<ApiResponse<List<Plan>>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().USER_REQUIREMENT_PLANS, request, apiCallback, tag);
    }

    public static void confirmMeasureHouse(ConfirmMeasureHouseRequest request, ApiCallback<ApiResponse<String>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().DESIGNER_HOUSE_CHECKED, request, apiCallback, tag);
    }

    public static void getProcessInfoDetail(GetProcessInfoRequest request, ApiCallback<ApiResponse<Process>>
            apiCallback,Object tag) {
        String getProcessUrl = Url_New.getInstance().GET_PROCESSINFO_BYID.replace(Url_New.ID,
                request.getProcessId());
        ApiClient.okGet(getProcessUrl, request, apiCallback, tag);
    }

    public static void applyReschedule(ApplyRescheduleRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().POST_RESCHDULE, request, apiCallback, tag);
    }

    public static void agreeReschedule(AgreeRescheduleRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().AGREE_RESCHDULE, request, apiCallback, tag);
    }

    public static void refuseReschedule(RefuseRescheduleRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().REFUSE_RESCHDULE, request, apiCallback, tag);
    }

    public static void evaluateDesigner(EvaluateDesignerRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().EVALUATE_DESIGNER_BY_USER, request, apiCallback, tag);
    }

    public static void chooseDesignerPlan(ChooseDesignerPlanRequest request, ApiCallback<ApiResponse<String>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().USER_CHOOSE_PLAN, request, apiCallback, tag);
    }

    public static void replaceOrderedDesigner(ReplaceOrderedDesignerRequest request, ApiCallback<ApiResponse<String>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().USER_CHANGE_ORDERD_DESIGNER, request, apiCallback, tag);
    }

    public static void updateRequirement(UpdateRequirementRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().REQUIREMENT_UPDATE, request, apiCallback, tag);
    }

    public static void publishRequirement(PublishRequirementRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().POST_REQUIREMENT, request, apiCallback, tag);
    }

    public static void submitImageToProcess(SubmitImageToProcessRequest request, ApiCallback<ApiResponse<String>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().POST_PROCESS_IMAGE, request, apiCallback, tag);
    }

    public static void deleteImageToProcess(DeleteImageToProcessRequest request, ApiCallback<ApiResponse<String>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().DELETE_PROCESS_PIC, request, apiCallback, tag);
    }

    //业主绑定微信
    public static void bindingWeixin(BindingWeiXinRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().BIND_WEIXIN, request, apiCallback, tag);
    }

    //业主通知详情
    public static void getNoticeDetail(GetMsgDetailRequest request, ApiCallback<ApiResponse<UserMessage>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_USER_MSG_DETAIL, request, apiCallback, tag);
    }

    //业主获取个人信息
    public static void getUserInfo(UserByOwnerInfoRequest request, ApiCallback<ApiResponse<User>>
            apiCallback,Object tag) {
        ApiClient.okGet(Url_New.getInstance().GET_OWER_INFO, request, apiCallback, tag);
    }

    //业主修改个人资料
    public static void updateUserInfo(UpdateOwnerInfoRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_OWER_INFO, request, apiCallback, tag);
    }

    //业主获取我的装修需求列表
    public static void getRequirementList(GetRequirementListRequest request,
                                          ApiCallback<ApiResponse<List<Requirement>>> apiCallback,Object tag) {
        ApiClient.okGet(Url_New.getInstance().REQUIREMENT_LIST, request, apiCallback, tag);
    }

    public static void searchDecorationImg(SearchDecorationImgRequest request,
                                           ApiCallback<ApiResponse<BeautifulImageList>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DECORATION_IMG, request, apiCallback, tag);
    }

    public static void getBeautyImgListByUser(GetBeautyImgListRequest request,
                                              ApiCallback<ApiResponse<BeautifulImageList>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_BEAUTY_IMG_LIST_BY_USER, request, apiCallback, tag);
    }

    public static void addBeautyImgByUser(AddBeautyImgRequest request, ApiCallback<ApiResponse<Object>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().ADD_BEAUTY_IMG, request, apiCallback, tag);
    }

    public static void deleteBeautyImgByUser(DeleteBeautyImgRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().DELETE_BEAUTY_IMG_BY_USER, request, apiCallback, tag);
    }


    public static void getCommentList(GetCommentsRequest request, ApiCallback<ApiResponse<CommentList>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_COMMENT, request, apiCallback, tag);
    }

    public static void addComment(AddCommentRequest request, ApiCallback<ApiResponse<Object>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().ADD_COMMENT, request, apiCallback, tag);
    }

    public static void searchUserComment(SearchUserCommentRequest request, ApiCallback<ApiResponse<UserMessageList>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_USER_COMMENT, request, apiCallback, tag);
    }

    public static void getProductHomePage(GetProductHomePageRequest request,
                                          ApiCallback<ApiResponse<Product>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().PRODUCT_HOME_PAGE, request, apiCallback, tag);
    }

    public static void addProductFavorite(AddProductFavoriteRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().ADD_PRODUCT, request, apiCallback, tag);
    }

    public static void deleteProductFavorite(DeleteProductFavoriteRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().DELETE_PRODUCT_BY_USER, request, apiCallback, tag);
    }

    public static void searchDesignerProduct(SearchDesignerProductRequest request,
                                             ApiCallback<ApiResponse<ProductList>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DESIGNER_PRODUCT, request, apiCallback, tag);
    }

    public static void getDesignerHomePage(DesignerHomePageRequest request, ApiCallback<ApiResponse<Designer>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().DESIGNER_HOME_PAGE, request, apiCallback, tag);
    }

    public static void addFavoriteDesigner(AddFavoriteDesignerRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().ADD_FAVORITE_DESIGNER, request, apiCallback, tag);
    }

    public static void deleteFavoriteDesigner(DeleteFavoriteDesignerRequest request, ApiCallback<ApiResponse<Object>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().DELETE_FAVORITE_DESIGNER, request, apiCallback, tag);
    }

    public static void get_MyFavoriteDesignerList(FavoriteDesignerListRequest request,
                                                  ApiCallback<ApiResponse<DesignerList>>
                                                          apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().FAVORITE_DESIGNER_LIST, request, apiCallback, tag);
    }

    public static void getCollectListByUser(GetProductFavoriteListRequest request, ApiCallback<ApiResponse<ProductList>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_PRODUCT_LIST_BY_COLLECTED, request, apiCallback, tag);
    }

    public static void searchShare(GetDecorateLiveRequest request, ApiCallback<ApiResponse<DecorateLiveList>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_SHARE, request, apiCallback, tag);
    }

    public static void getTopProducts(GetHomeProductRequest request, ApiCallback<ApiResponse<List<Product>>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_TOP_PRODUCTS, request, apiCallback, tag);
    }

    public static void searchUserMsg(SearchUserMsgRequest request, ApiCallback<ApiResponse<UserMessageList>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_USER_MSG, request, apiCallback, tag);
    }

    public static void getUnReadMsg(GetUnReadMsgRequest request, ApiCallback<ApiResponse<List<Integer>>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_UNREAD_MSG_COUNT, request, apiCallback, tag);
    }

    public static void searchDiarySet(SearchDiarySetRequest request, ApiCallback<ApiResponse<DiarySetInfoList>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DIARY_SET, request, apiCallback, tag);
    }

    public static void searchDiary(SearchDiaryRequest request, ApiCallback<ApiResponse<DiaryInfoList>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DIARY, request, apiCallback, tag);
    }

    public static void getDiaryChanges(GetDiaryUpdateRequest request, ApiCallback<ApiResponse<List<DiaryUpdateInfo>>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_DIARY_CHANGES, request, apiCallback,tag);
    }

    public static void getDiaryInfo(GetDiaryInfoRequest request, ApiCallback<ApiResponse<DiaryInfo>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_DIARY_INFO, request, apiCallback, tag);
    }

    public static void getDiarySetInfo(GetDiarySetInfoRequest request, ApiCallback<ApiResponse<DiarySetInfo>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_DIARY_SET_INFO, request, apiCallback, tag);
    }

    public static void addDiaryInfo(AddDiaryRequest request, ApiCallback<ApiResponse<DiaryInfo>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().ADD_DIARY, request, apiCallback, tag);
    }

    public static void addDiarySetInfo(AddDiarySetRequest request, ApiCallback<ApiResponse<DiarySetInfo>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().ADD_DIARYSET, request, apiCallback, tag);
    }

    public static void updateDiarySetInfo(UpdateDiarySetRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().UPDATE_DIARYSET, request, apiCallback, tag);
    }

    public static void deleteDiaryInfo(DeleteDiaryRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().DELETE_DIARY, request, apiCallback, tag);
    }

    public static void getMyDiarySetList(GetMyDiarySetRequest request, ApiCallback<ApiResponse<DiarySetInfoList>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_MY_DIARYSET, request, apiCallback, tag);
    }

    public static void addDiaryFavorite(AddDiaryFavoriteRequest request, ApiCallback<ApiResponse<String>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().FAVORITE_DIARY_ADD, request, apiCallback, tag);
    }

    public static void getFavoriteDiarySetList(GetDiarySetFavoriteListRequest request,
                                               ApiCallback<ApiResponse<DiarySetInfoList>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_FAVORITE_DIARY_SET_LIST, request, apiCallback, tag);
    }

    public static void getRecommendDiarySetList(GetRecommendDiarySetRequest request,
                                                ApiCallback<ApiResponse<List<DiarySetInfo>>> apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().GET_RECOMMEND_DIARY_SET, request, apiCallback, tag);
    }

    public static void addDiarySetFavorite(AddDiarySetFavoriteRequest request, ApiCallback<ApiResponse<String>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().ADD_DIARYSET_FAVORITE, request, apiCallback, tag);
    }

    public static void deleteDairySetFavorite(DeleteDiarySetFavoriteRequest request, ApiCallback<ApiResponse<String>>
            apiCallback,Object tag) {
        ApiClient.okPost(Url_New.getInstance().DELETE_DIARYSET_FAVORITE, request, apiCallback, tag);
    }

    public static void postUserRequirement(PostUserRequirementRequest request, ApiCallback<ApiResponse<String>> apiCallback, Object tag){
        ApiClient.okPost(Url_New.getInstance().ADD_ANGEL_USER, request, apiCallback, tag);
    }
}
