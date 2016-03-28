package com.jianfanjia.cn.api;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.user.AddFavoriteDesignerRequest;
import com.jianfanjia.api.request.guest.DesignerHomePageRequest;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.common.AddBeautyImgRequest;
import com.jianfanjia.api.request.common.AddCollectionRequest;
import com.jianfanjia.api.request.common.AddCommentRequest;
import com.jianfanjia.api.request.common.DeleteBeautyImgRequest;
import com.jianfanjia.api.request.common.DeleteCollectionRequest;
import com.jianfanjia.api.request.common.GetBeautyImgListRequest;
import com.jianfanjia.api.request.common.GetCommentsRequest;
import com.jianfanjia.api.request.guest.GetProductHomePageRequest;
import com.jianfanjia.api.request.guest.SearchDecorationImgRequest;
import com.jianfanjia.api.request.guest.SearchDesignerProductRequest;
import com.jianfanjia.api.request.guest.SearchDesignerRequest;
import com.jianfanjia.api.request.user.SearchUserCommentRequest;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.api.request.user.DeleteFavoriteDesignerRequest;


/**
 * Created by Aldis on 16/2/26.
 */

/**
 * 所有API接口都定义在这里
 */
public class Api {

    public static void searchDesigner(SearchDesignerRequest request, ApiCallback<ApiResponse<MyFavoriteDesigner>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DESIGNER, request, apiCallback);
    }

    public static void uploadImage(UploadPicRequest request, ApiCallback<ApiResponse<String>> apiCallback) {
        ApiClient.okUpload(Url_New.getInstance().UPLOAD_IMAGE, request, request.getBytes(), apiCallback);
    }

    public static void searchDecorationImg(SearchDecorationImgRequest request, ApiCallback<ApiResponse<DecorationItemInfo>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DECORATION_IMG, request, apiCallback);
    }

    public static void getBeautyImgListByUser(GetBeautyImgListRequest request, ApiCallback<ApiResponse<DecorationItemInfo>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_BEAUTY_IMG_LIST_BY_USER, request, apiCallback);
    }

    public static void addBeautyImgByUser(AddBeautyImgRequest request, ApiCallback<ApiResponse<Object>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().ADD_BEAUTY_IMG, request, apiCallback);
    }

    public static void deleteBeautyImgByUser(DeleteBeautyImgRequest request, ApiCallback<ApiResponse<Object>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DELETE_BEAUTY_IMG_BY_USER, request, apiCallback);
    }


    public static void getCommentList(GetCommentsRequest request, ApiCallback<ApiResponse<CommentList>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().GET_COMMENT, request, apiCallback);
    }

    public static void addComment(AddCommentRequest request, ApiCallback<ApiResponse<Object>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().ADD_COMMENT, request, apiCallback);
    }

    public static void searchUserComment(SearchUserCommentRequest request, ApiCallback<ApiResponse<NoticeListInfo>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_USER_COMMENT, request, apiCallback);
    }

    public  static void getProductHomePage(GetProductHomePageRequest request, ApiCallback<ApiResponse<DesignerCaseInfo>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().PRODUCT_HOME_PAGE, request, apiCallback);
    }

    public  static void addCollectionByUser(AddCollectionRequest request, ApiCallback<ApiResponse<Object>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().ADD_PRODUCT, request, apiCallback);
    }

    public  static void deleteCollectionByUser(DeleteCollectionRequest request, ApiCallback<ApiResponse<Object>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DELETE_PRODUCT_BY_USER, request, apiCallback);
    }

    public static void searchDesignerProduct(SearchDesignerProductRequest request, ApiCallback<ApiResponse<DesignerWorksInfo>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().SEARCH_DESIGNER_PRODUCT, request, apiCallback);
    }

    public static void getDesignerHomePage(DesignerHomePageRequest request, ApiCallback<ApiResponse<DesignerInfo>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DESIGNER_HOME_PAGE, request, apiCallback);
    }

    public static void addFavoriteDesigner(AddFavoriteDesignerRequest request, ApiCallback<ApiResponse<Object>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().ADD_FAVORITE_DESIGNER, request, apiCallback);
    }

    public static void deleteFavoriteDesigner(DeleteFavoriteDesignerRequest request, ApiCallback<ApiResponse<Object>> apiCallback) {
        ApiClient.okPost(Url_New.getInstance().DELETE_FAVORITE_DESIGNER, request, apiCallback);
    }

//    public static void deleteFavoriteDesigner(Context context, String designerid, ApiUiUpdateListener listener, Object tag) {
//        DeleteFavoriteDesignerRequest deleteFavoriteDesignerRequest = new DeleteFavoriteDesignerRequest(context, designerid);
//        JSONObject jsonParams = new JSONObject();
//        try {
//            jsonParams.put("_id", designerid);
//            LogTool.d(TAG, "deleteFavoriteDesignerRequest --" + deleteFavoriteDesignerRequest.getUrl() + "  jsonParams:" + jsonParams.toString());
//            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(deleteFavoriteDesignerRequest, jsonParams.toString(), listener, tag);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    //    public static void addFavoriteDesigner(Context context, String designerid, ApiUiUpdateListener listener, Object tag) {
//        AddFavoriteDesignerRequest addFavoriteDesignerRequest = new AddFavoriteDesignerRequest(context, designerid);
//        JSONObject jsonParams = new JSONObject();
//        try {
//            jsonParams.put("_id", designerid);
//            LogTool.d(TAG, "addFavoriteDesigner --" + "jsonParams:" + jsonParams.toString());
//            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addFavoriteDesignerRequest, jsonParams.toString(), listener, tag);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

}
