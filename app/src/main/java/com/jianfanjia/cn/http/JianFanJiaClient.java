package com.jianfanjia.cn.http;

import android.content.Context;
import android.graphics.Bitmap;

import com.jianfanjia.cn.bean.CommitCommentInfo;
import com.jianfanjia.cn.bean.DesignerUpdateInfo;
import com.jianfanjia.cn.bean.OwnerUpdateInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.http.request.AddPicToCheckRequest;
import com.jianfanjia.cn.http.request.AddPicToSectionItemRequest;
import com.jianfanjia.cn.http.request.AgreeRescheduleRequest;
import com.jianfanjia.cn.http.request.CheckVersionRequest;
import com.jianfanjia.cn.http.request.CommitCommentRequest;
import com.jianfanjia.cn.http.request.DeletePicRequest;
import com.jianfanjia.cn.http.request.DesignerInfoRequest;
import com.jianfanjia.cn.http.request.FeedBackRequest;
import com.jianfanjia.cn.http.request.GetAllRescheduleRequest;
import com.jianfanjia.cn.http.request.GetRequirementRequest;
import com.jianfanjia.cn.http.request.HomePageRequest;
import com.jianfanjia.cn.http.request.LoginRequest;
import com.jianfanjia.cn.http.request.LogoutRequest;
import com.jianfanjia.cn.http.request.NotifyOwnerCheckRequest;
import com.jianfanjia.cn.http.request.OwnerFinishCheckRequest;
import com.jianfanjia.cn.http.request.OwnerInfoRequest;
import com.jianfanjia.cn.http.request.PostRequirementRequest;
import com.jianfanjia.cn.http.request.PostRescheduleRequest;
import com.jianfanjia.cn.http.request.PostSectionFinishRequest;
import com.jianfanjia.cn.http.request.ProcessInfoRequest;
import com.jianfanjia.cn.http.request.ProcessListRequest;
import com.jianfanjia.cn.http.request.RefuseRescheduleRequest;
import com.jianfanjia.cn.http.request.RegisterRequest;
import com.jianfanjia.cn.http.request.SendVerificationRequest;
import com.jianfanjia.cn.http.request.TotalDurationRequest;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhanghao
 * @ClassName: JianFanJiaApi
 * @Description: http接口类
 * @date 2015-8-19 下午2:17:12
 */
public class JianFanJiaClient {

    public static final String TAG = "JianFanJiaClient.class";

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


   /* *//**
     * @param context
     * @param requirementInfo
     * @param handler
     * @author zhanghao
     * @decription 业主配置工地
     *//*
    public static void post_Owner_Process(Context context,
                                          RequirementInfo requirementInfo, AsyncHttpResponseHandler handler) {
        StringEntity entity;
        try {
            entity = new StringEntity(JsonParser.beanToJson(requirementInfo),
                    "utf-8");
            HttpRestClient.post(context, Url.PROCESS, entity,
                    "application/json", handler);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/

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

   /* *//**
     * @param context
     * @param hanlder
     * @author zhanghao
     * @description 设计师获取我的业主
     *//*
    public static void get_Designer_Owner(Context context,
                                          AsyncHttpResponseHandler hanlder) {
        HttpRestClient.get(context, Url.GET_DESIGNER_PROCESS, hanlder);
    }*/

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
     * 获得总的工期
     *
     * @param context
     * @param planId
     * @param listener
     * @param tag
     */
    public static void get_TotalDuration(Context context, String planId,
                                         ApiUiUpdateListener listener, Object tag) {
        TotalDurationRequest totalDurationRequest = new TotalDurationRequest(context);
        String getTotalDurationUrl = Url.GET_PLAN.replace(Url.ID, planId);
        totalDurationRequest.setUrl(getTotalDurationUrl);
        LogTool.d(TAG, "get_TotalDuration -" + totalDurationRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(totalDurationRequest, listener, tag);
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
    public static void getHomePageDesigners(Context context, String from, String limit, ApiUiUpdateListener listener, Object tag) {
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
}