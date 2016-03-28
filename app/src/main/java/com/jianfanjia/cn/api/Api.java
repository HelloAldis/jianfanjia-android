package com.jianfanjia.cn.api;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.common.AddBeautyImgRequest;
import com.jianfanjia.api.request.common.AddCollectionRequest;
import com.jianfanjia.api.request.common.AddCommentRequest;
import com.jianfanjia.api.request.common.DeleteBeautyImgRequest;
import com.jianfanjia.api.request.common.DeleteCollectionRequest;
import com.jianfanjia.api.request.common.GetBeautyImgListRequest;
import com.jianfanjia.api.request.common.GetCollectionRequest;
import com.jianfanjia.api.request.common.GetCommentsRequest;
import com.jianfanjia.api.request.common.GetDecorateLiveRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.guest.DesignerHomePageRequest;
import com.jianfanjia.api.request.guest.FeedBackRequest;
import com.jianfanjia.api.request.guest.GetProductHomePageRequest;
import com.jianfanjia.api.request.guest.SearchDecorationImgRequest;
import com.jianfanjia.api.request.guest.SearchDesignerProductRequest;
import com.jianfanjia.api.request.guest.SearchDesignerRequest;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.api.request.guest.VerifyPhoneRequest;
import com.jianfanjia.api.request.user.AddFavoriteDesignerRequest;
import com.jianfanjia.api.request.user.BindingWeiXinRequest;
import com.jianfanjia.api.request.user.DeleteFavoriteDesignerRequest;
import com.jianfanjia.api.request.user.FavoriteDesignerListRequest;
import com.jianfanjia.api.request.user.GetMsgDetailRequest;
import com.jianfanjia.api.request.user.GetRequirementListRequest;
import com.jianfanjia.api.request.user.SearchUserCommentRequest;
import com.jianfanjia.api.request.user.UpdateOwnerInfoRequest;
import com.jianfanjia.api.request.user.UserByOwnerInfoRequest;
import com.jianfanjia.cn.bean.CommentList;
import com.jianfanjia.cn.bean.DecorateLiveList;
import com.jianfanjia.cn.bean.DecorationItemInfo;
import com.jianfanjia.cn.bean.DesignerCaseInfo;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.DesignerWorksInfo;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.bean.NoticeDetailInfo;
import com.jianfanjia.cn.bean.NoticeListInfo;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.ProductInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Url_New;

import java.util.List;


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

    //业主修改个人资料
    public static void put_OwnerInfo(UpdateOwnerInfoRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_OWER_INFO, request, apiCallback);
    }

    //业主获取我的装修需求列表
    public static void get_Requirement_List(GetRequirementListRequest request,
                                            ApiCallback<ApiResponse<List<RequirementInfo>>> apiCallback) {
        ApiClient.okGet(Url_New.getInstance().REQUIREMENT_LIST, request, apiCallback);
    }

    public static void searchDecorationImg(SearchDecorationImgRequest request,
                                           ApiCallback<ApiResponse<DecorationItemInfo>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DECORATION_IMG, request, apiCallback);
    }

    public static void getBeautyImgListByUser(GetBeautyImgListRequest request,
                                              ApiCallback<ApiResponse<DecorationItemInfo>> apiCallback) {
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

    public static void searchUserComment(SearchUserCommentRequest request, ApiCallback<ApiResponse<NoticeListInfo>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_USER_COMMENT, request, apiCallback);
    }

    public static void getProductHomePage(GetProductHomePageRequest request,
                                          ApiCallback<ApiResponse<DesignerCaseInfo>> apiCallback) {
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
                                             ApiCallback<ApiResponse<DesignerWorksInfo>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DESIGNER_PRODUCT, request, apiCallback);
    }

    public static void getDesignerHomePage(DesignerHomePageRequest request, ApiCallback<ApiResponse<DesignerInfo>>
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
                                                  ApiCallback<ApiResponse<MyFavoriteDesigner>>
                                                          apiCallback) {
        ApiClient.okPost(Url_New.getInstance().FAVORITE_DESIGNER_LIST, request, apiCallback);
    }


    public static void getCollectListByUser(GetCollectionRequest request, ApiCallback<ApiResponse<ProductInfo>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_PRODUCT_LIST_BY_COLLECTED, request, apiCallback);
    }


    public static void searchShare(GetDecorateLiveRequest request, ApiCallback<ApiResponse<DecorateLiveList>>
            apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_SHARE, request, apiCallback);
    }

}
