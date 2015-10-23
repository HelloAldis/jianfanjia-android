package com.jianfanjia.cn.http;

import android.content.Context;
import android.graphics.Bitmap;

import com.jianfanjia.cn.bean.CommitCommentInfo;
import com.jianfanjia.cn.bean.DesignerUpdateInfo;
import com.jianfanjia.cn.bean.OwnerUpdateInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.request.AddCommentRequest;
import com.jianfanjia.cn.http.request.AddFavoriteDesignerRequest;
import com.jianfanjia.cn.http.request.AddPicToCheckRequest;
import com.jianfanjia.cn.http.request.AddPicToSectionItemRequest;
import com.jianfanjia.cn.http.request.AgreeRescheduleRequest;
import com.jianfanjia.cn.http.request.CheckVersionRequest;
import com.jianfanjia.cn.http.request.ChoosePlanByUserRequest;
import com.jianfanjia.cn.http.request.CommitCommentRequest;
import com.jianfanjia.cn.http.request.ConformMeasureHouseRequest;
import com.jianfanjia.cn.http.request.DeletePicRequest;
import com.jianfanjia.cn.http.request.DesignerHomePageRequest;
import com.jianfanjia.cn.http.request.DesignerInfoRequest;
import com.jianfanjia.cn.http.request.EvaluateDesignerRequest;
import com.jianfanjia.cn.http.request.FavoriteDesignerListRequest;
import com.jianfanjia.cn.http.request.FeedBackRequest;
import com.jianfanjia.cn.http.request.GetAllRescheduleRequest;
import com.jianfanjia.cn.http.request.GetContractRequest;
import com.jianfanjia.cn.http.request.GetDesignerPlansByUserRequest;
import com.jianfanjia.cn.http.request.GetOrderDesignerListByUserRequest;
import com.jianfanjia.cn.http.request.GetOrderedDesignerRequest;
import com.jianfanjia.cn.http.request.GetPlanInfoRequest;
import com.jianfanjia.cn.http.request.GetProductHomePageRequest;
import com.jianfanjia.cn.http.request.GetRequirementRequest;
import com.jianfanjia.cn.http.request.HomePageRequest;
import com.jianfanjia.cn.http.request.LoginRequest;
import com.jianfanjia.cn.http.request.LogoutRequest;
import com.jianfanjia.cn.http.request.NotifyOwnerCheckRequest;
import com.jianfanjia.cn.http.request.OrderDesignerByUserRequest;
import com.jianfanjia.cn.http.request.OwnerFinishCheckRequest;
import com.jianfanjia.cn.http.request.OwnerInfoRequest;
import com.jianfanjia.cn.http.request.PostProcessRequest;
import com.jianfanjia.cn.http.request.PostRequirementRequest;
import com.jianfanjia.cn.http.request.PostRescheduleRequest;
import com.jianfanjia.cn.http.request.PostSectionFinishRequest;
import com.jianfanjia.cn.http.request.ProcessInfoRequest;
import com.jianfanjia.cn.http.request.ProcessListRequest;
import com.jianfanjia.cn.http.request.RefuseRescheduleRequest;
import com.jianfanjia.cn.http.request.RegisterRequest;
import com.jianfanjia.cn.http.request.SearchDesignerProductRequest;
import com.jianfanjia.cn.http.request.SendVerificationRequest;
import com.jianfanjia.cn.http.request.UpdateRequirementRequest;
import com.jianfanjia.cn.http.request.UploadPicRequestNew;
import com.jianfanjia.cn.http.request.UploadRegisterIdRequest;
import com.jianfanjia.cn.http.request.UserByDesignerInfoRequest;
import com.jianfanjia.cn.http.request.UserByDesignerInfoUpdateRequestApi;
import com.jianfanjia.cn.http.request.UserByOwnerInfoRequest;
import com.jianfanjia.cn.http.request.UserByOwnerInfoUpdateRequestApi;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author zhanghao
 * @ClassName: JianFanJiaApi
 * @Description: http接口类
 * @date 2015-8-19 下午2:17:12
 */
public class JianFanJiaClient {
    public static final String TAG = JianFanJiaClient.class.getName();

    /**
     * 上传个推clientid
     *
     * @param context
     * @param clientId
     * @param listener
     */
    public static void uploadRegisterId(Context context, String clientId,
                                        ApiUiUpdateListener listener, Object tag) {
        UploadRegisterIdRequest uploadRegisterIdRequest = new UploadRegisterIdRequest(context, clientId);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("clientId", clientId);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(uploadRegisterIdRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param listener
     * @author zhanghao
     */
    public static void login(Context context, String username, String password,
                             ApiUiUpdateListener listener, Object tag) {
        LoginRequest loginRequest = new LoginRequest(context, username, password);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("phone", username);
            jsonParams.put("pass", password);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(loginRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户登出
     *
     * @param context
     * @param listener
     * @param tag
     */
    public static void logout(Context context, ApiUiUpdateListener listener, Object tag) {
//		HttpRestClient.get(context, Url.SIGNOUT_URL, handler);
        LogoutRequest logoutRequest = new LogoutRequest(context);
        LogTool.d(TAG, "logout " + logoutRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(logoutRequest, listener, tag);
    }

    /**
     * 检查版本
     *
     * @param context
     * @param listener
     * @param tag
     */
    public static void checkVersion(Context context,
                                    ApiUiUpdateListener listener, Object tag) {
        CheckVersionRequest checkVersionRequest = new CheckVersionRequest(context);
        LogTool.d(TAG, "checkVersion --" + checkVersionRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(checkVersionRequest, listener, tag);
    }

    /**
     * 用户反馈
     *
     * @param context
     * @param content
     * @param listener
     * @param tag
     */
    public static void feedBack(Context context, String content, String platform,
                                ApiUiUpdateListener listener, Object tag) {
        FeedBackRequest feedBackRequest = new FeedBackRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("content", content);
            jsonParams.put("platform", platform);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(feedBackRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @param listener
     * @param tag
     * @author zhanghao
     */
    public static void send_verification(Context context, String phone,
                                         ApiUiUpdateListener listener, Object tag) {
        SendVerificationRequest sendVerificationRequest = new SendVerificationRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("phone", phone);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(sendVerificationRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @author zhanghao
     * @Description 获取业主需求
     */
    public static void get_Requirement_List(Context context, ApiUiUpdateListener listener, Object tag) {
        GetRequirementRequest getRequirementRequest = new GetRequirementRequest(context);
        LogTool.d(TAG, "get_Requirement_list --" + getRequirementRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(getRequirementRequest, listener, tag);
    }

    /**
     * 发布需求
     *
     * @param context
     * @param requirementInfo
     * @param listener
     * @param tag
     */
    public static void add_Requirement(Context context, RequirementInfo requirementInfo, ApiUiUpdateListener listener, Object tag) {
        PostRequirementRequest postRequirementRequest = new PostRequirementRequest(context, requirementInfo);
        LogTool.d(TAG, "add_Requirement --" + postRequirementRequest.getUrl() + "--" + JsonParser.beanToJson(requirementInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(postRequirementRequest, JsonParser.beanToJson(requirementInfo), listener, tag);
    }

    /**
     * 更新装修需求
     *
     * @param context
     * @param requirementInfo
     * @param listener
     * @param tag
     */
    public static void update_Requirement(Context context, RequirementInfo requirementInfo, ApiUiUpdateListener listener, Object tag) {
        UpdateRequirementRequest updateRequirementRequest = new UpdateRequirementRequest(context, requirementInfo);
        LogTool.d(TAG, "add_Requirement --" + updateRequirementRequest.getUrl() + "--" + JsonParser.beanToJson(requirementInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(updateRequirementRequest, JsonParser.beanToJson(requirementInfo), listener, tag);
    }


    /**
     * @param context
     * @author zhanghao
     * @decription 业主配置工地
     */
    public static void post_Owner_Process(Context context,
                                          String requirementid, String planid, ApiUiUpdateListener listener, Object tag) {
        PostProcessRequest postProcessRequest = new PostProcessRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("planid", planid);
            LogTool.d(TAG, "postProcessRequest --" + postProcessRequest.getUrl() + "---" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(postProcessRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户注册
     *
     * @param registerInfo
     * @param listener
     * @author zhanghao
     */
    public static void register(Context context, RegisterInfo registerInfo,
                                ApiUiUpdateListener listener, Object tag) {
        RegisterRequest registerRequest = new RegisterRequest(context);
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(registerRequest, JsonParser.beanToJson(registerInfo), listener, tag);
    }

    /**
     * 根据设计师id拿到设计师信息
     *
     * @param designerid
     * @param listener
     * @author zhanghao
     */
    public static void getDesignerInfoById(Context context, String designerid,
                                           ApiUiUpdateListener listener, Object tag) {
        String getdesignerUrl = Url.GET_OWER_DESIGNER.replace(Url.ID,
                designerid);
        DesignerInfoRequest designerInfoRequest = new DesignerInfoRequest(context);
        designerInfoRequest.setUrl(getdesignerUrl);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(designerInfoRequest, listener, tag);

    }

    /**
     * 拿到我的意向设计师列表
     *
     * @param context
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void get_MyFavoriteDesignerList(Context context, String from, String limit, ApiUiUpdateListener listener, Object tag) {
        FavoriteDesignerListRequest favoriteDesignerListRequest = new FavoriteDesignerListRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            LogTool.d(TAG, "get_MyFavoriteDesignerList --" + favoriteDesignerListRequest.getUrl() + "---" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(favoriteDesignerListRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param listener
     * @author zhanghao
     * @description 业主获取个人信息
     */
    public static void get_Owner_Info(Context context,
                                      ApiUiUpdateListener listener, Object tag) {
        UserByOwnerInfoRequest userByOwnerInfoRequest = new UserByOwnerInfoRequest(context);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(userByOwnerInfoRequest, listener, tag);
    }

    /**
     * @param context
     * @param listener
     * @author zhanghao
     * @description 设计师获取个人信息
     */
    public static void get_Designer_Info(Context context,
                                         ApiUiUpdateListener listener, Object tag) {
        UserByDesignerInfoRequest userByOwnerInfoRequest = new UserByDesignerInfoRequest(context);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(userByOwnerInfoRequest, listener, tag);
    }

    /**
     * @param context
     * @param listener
     * @author zhanghao
     * @description 获取我的工地列表
     */
    public static void get_Process_List(Context context,
                                        ApiUiUpdateListener listener, Object tag) {
        ProcessListRequest processListRequest = new ProcessListRequest(context);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(processListRequest, listener, tag);
    }

    /**
     * 设计师获取业主个人信息
     *
     * @param context
     * @param listener
     */
    public static void getOwnerInfoById(Context context, String ownerid,
                                        ApiUiUpdateListener listener, Object tag) {
        String getdesignerUrl = Url.GET_ONE_OWNER_INFO.replace(Url.ID, ownerid);
        OwnerInfoRequest ownerInfoRequest = new OwnerInfoRequest(context);
        ownerInfoRequest.setUrl(getdesignerUrl);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(ownerInfoRequest, listener, tag);
    }

    /**
     * 业主获取装修工地的设计师的信息
     *
     * @param context
     * @param listener
     */
    public static void getOwnerDesignerInfoById(Context context,
                                                String designerid, ApiUiUpdateListener listener, Object tag) {
        DesignerInfoRequest designerInfoRequest = new DesignerInfoRequest(context);
        designerInfoRequest.setUrl(Url.GET_OWER_DESIGNER_INFO);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("designerid", designerid);
            LogTool.d(TAG, "getOwnerDesignerInfoById " + designerInfoRequest.getUrl() + "----" + "jsonParams：" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(designerInfoRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户提交改期
     *
     * @param context
     * @param processId
     * @param userId
     * @param designerId
     * @param section
     * @param newDate
     * @param listener
     */
    public static void postReschedule(Context context, String processId,
                                      String userId, String designerId, String section, String newDate,
                                      ApiUiUpdateListener listener, Object tag) {
        PostRescheduleRequest postRescheduleRequest = new PostRescheduleRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("processid", processId);
            jsonParams.put("userid", userId);
            jsonParams.put("designerid", designerId);
            jsonParams.put("section", section);
            jsonParams.put("new_date",
                    DateFormatTool.covertStringToLong(newDate));
            LogTool.d("JianFanJiaApiClient", "jsonParams：" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(postRescheduleRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户同意改期
     *
     * @param context
     * @param processid
     * @param listener
     */
    public static void agreeReschedule(Context context, String processid,
                                       ApiUiUpdateListener listener, Object tag) {
        AgreeRescheduleRequest agreeRescheduleRequest = new AgreeRescheduleRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("processid", processid);
            LogTool.d("JianFanJiaApiClient", "jsonParams：" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(agreeRescheduleRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户拒绝改期
     *
     * @param context
     * @param processid
     * @param listener
     */
    public static void refuseReschedule(Context context, String processid,
                                        ApiUiUpdateListener listener, Object tag) {
        RefuseRescheduleRequest refuseRescheduleRequest = new RefuseRescheduleRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("processid", processid);
            LogTool.d("JianFanJiaApiClient", "jsonParams：" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(refuseRescheduleRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户获取我的改期提醒
     *
     * @param context
     * @param listener
     */
    public static void rescheduleAll(Context context,
                                     ApiUiUpdateListener listener, Object tag) {
//        HttpRestClient.get(context, Url.GET_RESCHDULE_ALL, hanlder);
        GetAllRescheduleRequest getAllRescheduleRequest = new GetAllRescheduleRequest(context);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(getAllRescheduleRequest, listener, tag);
    }

    /**
     * 评论装修流程
     *
     * @param commitCommentInfo
     * @param listener
     * @author zhanghao
     */
    public static void comment(Context context,
                               CommitCommentInfo commitCommentInfo,
                               ApiUiUpdateListener listener, Object tag) {
        CommitCommentRequest commitCommentRequest = new CommitCommentRequest(context);
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(commitCommentRequest, JsonParser.beanToJson(commitCommentInfo), listener, tag);
    }

   /* *//**
     * 用户上传图片
     *
     * @param context
     * @param listener
     *
     *//*
    public static void uploadImage(Context context, String imgPath,
                                   AsyncHttpResponseHandler handler) {
        try {
            File file = new File(imgPath);
            RequestParams params = new RequestParams();
            params.put("Filedata", file);
            HttpRestClient.post(context, Url.UPLOAD_IMAGE, params, handler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/

    /**
     * 用户上传图片
     *
     * @param context
     * @param listener
     */
    public static void uploadImage(Context context, Bitmap bitmap,
                                   ApiUiUpdateListener listener, Object tag) {
        UploadPicRequestNew uploadPicRequest = new UploadPicRequestNew(context);
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(uploadPicRequest, ImageUtil.transformBitmapToBytes(bitmap), listener, tag);
    }

    /**
     * 用户上传图片到装修流程
     *
     * @param context
     * @param siteId
     * @param section
     * @param item
     * @param imageId
     * @param listener
     */
    public static void submitImageToProcess(Context context, String siteId,
                                            String section, String item, String imageId,
                                            ApiUiUpdateListener listener, Object tag) {
        AddPicToSectionItemRequest addPicToSectionItemRequest = new AddPicToSectionItemRequest(context, siteId, section, item, imageId);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", siteId);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            jsonParams.put("imageid", imageId);
            LogTool.d(TAG, "submitImageToProcess -" + addPicToSectionItemRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addPicToSectionItemRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设计师提交验收图片
     *
     * @param context
     * @param siteId
     * @param section
     * @param key
     * @param imageId
     * @param listener
     */
    public static void submitYanShouImage(Context context, String siteId,
                                          String section, String key, String imageId,
                                          ApiUiUpdateListener listener, Object tag) {
        AddPicToCheckRequest addPicToCheckRequest = new AddPicToCheckRequest(context, siteId, section, key, imageId);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", siteId);
            jsonParams.put("section", section);
            jsonParams.put("key", key);
            jsonParams.put("imageid", imageId);
            LogTool.d(TAG, "submitYanShouImage -" + addPicToCheckRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addPicToCheckRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户完工装修流程小节点
     *
     * @param context
     * @param siteId
     * @param section
     * @param item
     * @param listener
     */
    public static void processItemDone(Context context, String siteId,
                                       String section, String item, ApiUiUpdateListener listener, Object tag) {
        PostSectionFinishRequest postSectionFinishRequest = new PostSectionFinishRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", siteId);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            LogTool.d(TAG, "processItemDone -" + postSectionFinishRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(postSectionFinishRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id拿到工地信息
     *
     * @param context
     * @param processid
     * @param listener
     */
    public static void get_ProcessInfo_By_Id(Context context, String processid,
                                             ApiUiUpdateListener listener, Object tag) {
        ProcessInfoRequest processInfoRequest = new ProcessInfoRequest(context, processid);
        String getProcessUrl = Url.GET_PROCESSINFO_BYID.replace(Url.ID,
                processid);
        processInfoRequest.setUrl(getProcessUrl);
        LogTool.d(TAG, "processItemDone -" + processInfoRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(processInfoRequest, listener, tag);
    }

    /**
     * 修改业主个人信息
     *
     * @param context
     * @param ownerInfo
     * @param listener
     */
    public static void put_OwnerInfo(Context context,
                                     OwnerUpdateInfo ownerInfo, ApiUiUpdateListener listener, Object tag) {
        UserByOwnerInfoUpdateRequestApi userByOwnerInfoUpdateRequest = new UserByOwnerInfoUpdateRequestApi(context, ownerInfo);
        LogTool.d(TAG, "put_OwnerInfo -" + userByOwnerInfoUpdateRequest.getUrl() + JsonParser.beanToJson(ownerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(userByOwnerInfoUpdateRequest, JsonParser.beanToJson(ownerInfo), listener, tag);
    }

    /**
     * 修改设计师个人信息
     *
     * @param context
     * @param designerInfo
     * @param listener
     */
    public static void put_DesignerInfo(Context context,
                                        DesignerUpdateInfo designerInfo, ApiUiUpdateListener listener, Object tag) {
        UserByDesignerInfoUpdateRequestApi userByDesignerInfoUpdateRequest = new UserByDesignerInfoUpdateRequestApi(context, designerInfo);
        LogTool.d(TAG, "put_OwnerInfo -" + userByDesignerInfoUpdateRequest.getUrl());
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(userByDesignerInfoUpdateRequest, JsonParser.beanToJson(userByDesignerInfoUpdateRequest), listener, tag);
    }


    /**
     * 设计师提醒业主验收
     *
     * @param context
     * @param processid
     * @param section
     * @param listener
     */
    public static void confirm_canCheckBydesigner(Context context,
                                                  String processid, String section, ApiUiUpdateListener listener, Object tag) {
        NotifyOwnerCheckRequest notifyOwnerCheckRequest = new NotifyOwnerCheckRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", processid);
            jsonParams.put("section", section);
            LogTool.d(TAG, "confirm_canCheckBydesigner -" + notifyOwnerCheckRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(notifyOwnerCheckRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主确认验收
     *
     * @param context
     * @param processid
     * @param section
     * @param listener
     */
    public static void confirm_CheckDoneByOwner(Context context, String processid,
                                                String section, ApiUiUpdateListener listener, Object tag) {
        OwnerFinishCheckRequest ownerFinishCheckRequest = new OwnerFinishCheckRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", processid);
            jsonParams.put("section", section);
            LogTool.d(TAG, "confirm_CheckDoneByOwner -" + ownerFinishCheckRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(ownerFinishCheckRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设计师删除验收照片
     *
     * @param context
     * @param processId
     * @param section
     * @param key
     * @param listener
     * @param tag
     */
    public static void deleteYanshouImgByDesigner(Context context,
                                                  String processId, String section, String key,
                                                  ApiUiUpdateListener listener, Object tag) {
        DeletePicRequest deletePicRequest = new DeletePicRequest(context, processId, section, key);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", processId);
            jsonParams.put("section", section);
            jsonParams.put("key", key);
            LogTool.d(TAG, "deleteYanshouImgByDesigner -" + deletePicRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(deletePicRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取首页数据
     *
     * @param context
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void getHomePageDesigners(Context context, int from, int limit, ApiUiUpdateListener listener, Object tag) {
        HomePageRequest homePageRequest = new HomePageRequest(context, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(homePageRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取设计师信息主页
     *
     * @param context
     * @param designerid
     * @param listener
     * @param tag
     */
    public static void getDesignerHomePage(Context context, String designerid, ApiUiUpdateListener listener, Object tag) {
        DesignerHomePageRequest homePageRequest = new DesignerHomePageRequest(context, designerid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", designerid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(homePageRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取设计师作品
     *
     * @param context
     * @param designerid
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void getDesignerProduct(Context context, String designerid, int from, int limit, ApiUiUpdateListener listener, Object tag) {
        SearchDesignerProductRequest productRequest = new SearchDesignerProductRequest(context, designerid, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            JSONObject params = new JSONObject();
            params.put("designerid", designerid);
            jsonParams.put("query", params);
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            LogTool.d(TAG, "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(productRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加设计师到意向列表
     *
     * @param context
     * @param designerid
     * @param listener
     * @param tag
     */
    public static void Add_Favorite_Designer_List(Context context, String designerid, ApiUiUpdateListener listener, Object tag) {
        AddFavoriteDesignerRequest addFavoriteDesignerRequest = new AddFavoriteDesignerRequest(context, designerid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", designerid);
            LogTool.d(TAG, "Add_Favorite_Designer_List --" + "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addFavoriteDesignerRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某个作品主页
     *
     * @param context
     * @param productid
     * @param listener
     * @param tag
     */
    public static void getProductHomePage(Context context, String productid, ApiUiUpdateListener listener, Object tag) {
        GetProductHomePageRequest getProductHomePageRequest = new GetProductHomePageRequest(context, productid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", productid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getProductHomePageRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主获取自己可以预约的设计师列表
     *
     * @param context
     * @param requirementid
     * @param listener
     * @param tag
     */
    public static void getOrderDesignerListByUser(Context context, String requirementid, ApiUiUpdateListener listener, Object tag) {
        GetOrderDesignerListByUserRequest getOrderDesignerListByUserRequest = new GetOrderDesignerListByUserRequest(context, requirementid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getOrderDesignerListByUserRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主预约设计师
     *
     * @param context
     * @param requirementid
     * @param designerids
     * @param listener
     * @param tag
     */
    public static void orderDesignerByUser(Context context, String requirementid, List<String> designerids, ApiUiUpdateListener listener, Object tag) {
        OrderDesignerByUserRequest orderDesignerByUserRequest = new OrderDesignerByUserRequest(context, requirementid, designerids);
        JSONObject jsonParams = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for (String designerid : designerids) {
                array.put(designerid);
            }
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("designerids", array);
            LogTool.d(TAG, "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(orderDesignerByUserRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主获取我预约了的设计师
     *
     * @param context
     * @param requirementid
     * @param listener
     * @param tag
     */
    public static void getOrderedDesignerList(Context context, String requirementid, ApiUiUpdateListener listener, Object tag) {
        GetOrderedDesignerRequest getOrderedDesignerRequest = new GetOrderedDesignerRequest(context, requirementid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            LogTool.d(TAG, "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getOrderedDesignerRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主获取我的方案
     *
     * @param context
     * @param requirementid
     * @param designerid
     * @param listener
     * @param tag
     */
    public static void getDesignerPlansByUser(Context context, String requirementid, String designerid, ApiUiUpdateListener listener, Object tag) {
        GetDesignerPlansByUserRequest getDesignerPlansByUserRequest = new GetDesignerPlansByUserRequest(context, requirementid, designerid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("designerid", designerid);
            LogTool.d(TAG, "jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getDesignerPlansByUserRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主确认已量房
     *
     * @param context
     * @param requirementid
     * @param designerid
     * @param listener
     * @param tag
     */
    public static void confirmMeasureHouse(Context context, String requirementid, String designerid, ApiUiUpdateListener listener, Object tag) {
        ConformMeasureHouseRequest conformMeasureHouseRequest = new ConformMeasureHouseRequest(context, requirementid, designerid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("designerid", designerid);
            LogTool.d(TAG, "confirmMeasureHouse" + " -- " + conformMeasureHouseRequest.getUrl() + "--jsonParams:" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(conformMeasureHouseRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主选定方案
     *
     * @param context
     * @param requirementid
     * @param designerid
     * @param planid
     * @param listener
     * @param tag
     */
    public static void chooseDesignerPlan(Context context, String requirementid, String designerid, String planid, ApiUiUpdateListener listener, Object tag) {
        ChoosePlanByUserRequest choosePlanByUserRequest = new ChoosePlanByUserRequest(context, requirementid, designerid, planid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("designerid", designerid);
            jsonParams.put("planid", planid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(choosePlanByUserRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户获取某个方案信息
     *
     * @param context
     * @param planid
     * @param listener
     * @param tag
     */
    public static void getPlanInfo(Context context, String planid, ApiUiUpdateListener listener, Object tag) {
        GetPlanInfoRequest getPlanInfoRequest = new GetPlanInfoRequest(context, planid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", planid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getPlanInfoRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主评价设计师
     *
     * @param context
     * @param requirementid
     * @param designerid
     * @param service_attitude
     * @param respond_speed
     * @param comment
     * @param is_anonymous
     * @param listener
     * @param tag
     */
    public static void evaluateDesignerByUser(Context context, String requirementid, String designerid, int service_attitude, int respond_speed, String comment, String is_anonymous, ApiUiUpdateListener listener, Object tag) {
        EvaluateDesignerRequest evaluateDesignerRequest = new EvaluateDesignerRequest(context, requirementid, designerid, service_attitude, respond_speed, comment, is_anonymous);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("designerid", designerid);
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("service_attitude", service_attitude);
            jsonParams.put("respond_speed", respond_speed);
            jsonParams.put("comment", comment);
            jsonParams.put("is_anonymous", is_anonymous);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(evaluateDesignerRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户获取合同
     *
     * @param context
     * @param requirementid
     * @param listener
     * @param tag
     */
    public static void getContractInfo(Context context, String requirementid, ApiUiUpdateListener listener, Object tag) {
        GetContractRequest getContractRequest = new GetContractRequest(context, requirementid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getContractRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户添加评论留言
     *
     * @param context
     * @param topicid
     * @param topictype
     * @param content
     * @param to
     * @param listener
     * @param tag
     */
    public static void addComment(Context context, String topicid, String topictype, String content, String to, ApiUiUpdateListener listener, Object tag) {
        AddCommentRequest addCommentRequest = new AddCommentRequest(context, topicid, topictype, content, to);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("topicid", topicid);
            jsonParams.put("topictype", topictype);
            jsonParams.put("content", content);
            jsonParams.put("to", to);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addCommentRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
