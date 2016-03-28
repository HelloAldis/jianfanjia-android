package com.jianfanjia.cn.api;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiClient;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.api.request.user.SearchDesignerRequest;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.config.Url_New;

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
}
