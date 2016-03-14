package com.jianfanjia.cn.designer.http;

import android.content.Context;
import android.graphics.Bitmap;

import com.jianfanjia.cn.designer.bean.OwnerInfo;
import com.jianfanjia.cn.designer.bean.OwnerUpdateInfo;
import com.jianfanjia.cn.designer.bean.RegisterInfo;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.bean.WeiXinRegisterInfo;
import com.jianfanjia.cn.designer.config.Url_New;
import com.jianfanjia.cn.designer.http.request.AddCommentRequest;
import com.jianfanjia.cn.designer.http.request.AddPicToCheckRequest;
import com.jianfanjia.cn.designer.http.request.AddPicToSectionItemRequest;
import com.jianfanjia.cn.designer.http.request.AgreeRescheduleRequest;
import com.jianfanjia.cn.designer.http.request.BindingPhoneRequest;
import com.jianfanjia.cn.designer.http.request.BindingWeiXinRequest;
import com.jianfanjia.cn.designer.http.request.CheckVersionRequest;
import com.jianfanjia.cn.designer.http.request.ConfigContractRequest;
import com.jianfanjia.cn.designer.http.request.ConformMeasureHouseRequest;
import com.jianfanjia.cn.designer.http.request.DeletePicRequest;
import com.jianfanjia.cn.designer.http.request.DeletePicToSectionItemRequest;
import com.jianfanjia.cn.designer.http.request.EvaluateDesignerRequest;
import com.jianfanjia.cn.designer.http.request.FeedBackRequest;
import com.jianfanjia.cn.designer.http.request.ForgetPswRequest;
import com.jianfanjia.cn.designer.http.request.GetAllRequirementListRequest;
import com.jianfanjia.cn.designer.http.request.GetAllRescheduleRequest;
import com.jianfanjia.cn.designer.http.request.GetCommentsRequest;
import com.jianfanjia.cn.designer.http.request.GetContractRequest;
import com.jianfanjia.cn.designer.http.request.GetDesignerPlansByUserRequest;
import com.jianfanjia.cn.designer.http.request.GetMsgDetailRequest;
import com.jianfanjia.cn.designer.http.request.GetPlanInfoRequest;
import com.jianfanjia.cn.designer.http.request.GetProcessInfoRequest;
import com.jianfanjia.cn.designer.http.request.GetProcessListRequest;
import com.jianfanjia.cn.designer.http.request.GetRequirementListRequest;
import com.jianfanjia.cn.designer.http.request.GetUnReadMsgRequest;
import com.jianfanjia.cn.designer.http.request.LoginRequest;
import com.jianfanjia.cn.designer.http.request.LogoutRequest;
import com.jianfanjia.cn.designer.http.request.NotifyOwnerCheckRequest;
import com.jianfanjia.cn.designer.http.request.OwnerFinishCheckRequest;
import com.jianfanjia.cn.designer.http.request.PostCollectOwnerInfoRequest;
import com.jianfanjia.cn.designer.http.request.PostProcessRequest;
import com.jianfanjia.cn.designer.http.request.PostRescheduleRequest;
import com.jianfanjia.cn.designer.http.request.PostSectionFinishRequest;
import com.jianfanjia.cn.designer.http.request.RefreshSessionRequest;
import com.jianfanjia.cn.designer.http.request.RefuseRequirementRequest;
import com.jianfanjia.cn.designer.http.request.RefuseRescheduleRequest;
import com.jianfanjia.cn.designer.http.request.RegisterRequest;
import com.jianfanjia.cn.designer.http.request.ResponseRequirementRequest;
import com.jianfanjia.cn.designer.http.request.SearchUserCommentRequest;
import com.jianfanjia.cn.designer.http.request.SearchUserMsgRequest;
import com.jianfanjia.cn.designer.http.request.SendVerificationRequest;
import com.jianfanjia.cn.designer.http.request.UpdateOwnerInfoRequest;
import com.jianfanjia.cn.designer.http.request.UpdateRequirementRequest;
import com.jianfanjia.cn.designer.http.request.UploadPicRequestNew;
import com.jianfanjia.cn.designer.http.request.UploadRegisterIdRequest;
import com.jianfanjia.cn.designer.http.request.UserByOwnerInfoRequest;
import com.jianfanjia.cn.designer.http.request.VerifyPhoneRequest;
import com.jianfanjia.cn.designer.http.request.WeiXinLoginRequest;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.DateFormatTool;
import com.jianfanjia.cn.designer.tools.ImageUtil;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;

import org.json.JSONException;
import org.json.JSONObject;

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
            jsonParams.put("cid", clientId);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(uploadRegisterIdRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查手机号是否被占用
     *
     * @param context
     * @param phone
     * @param listener
     * @param tag
     */
    public static void verifyPhone(Context context, String phone, ApiUiUpdateListener listener, Object tag) {
        VerifyPhoneRequest verifyPhoneRequest = new VerifyPhoneRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("phone", phone);
            LogTool.d(TAG, "verifyPhone --" + verifyPhoneRequest.getUrl() + "---" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(verifyPhoneRequest, jsonParams.toString(), listener, tag);
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
            LogTool.d(TAG, "login --" + loginRequest.getUrl() + "---" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(loginRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void weixinLogin(Context context, WeiXinRegisterInfo weiXinRegisterInfo, ApiUiUpdateListener listener, Object tag) {
        WeiXinLoginRequest weiXinLoginRequest = new WeiXinLoginRequest(context, weiXinRegisterInfo);
        LogTool.d(TAG, "weixinLogin --" + weiXinLoginRequest.getUrl() + "---" + JsonParser.beanToJson(weiXinRegisterInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(weiXinLoginRequest, JsonParser.beanToJson(weiXinRegisterInfo), listener, tag);
    }

    /**
     * 用户登出
     *
     * @param context
     * @param listener
     * @param tag
     */
    public static void logout(Context context, ApiUiUpdateListener listener, Object tag) {
        LogoutRequest logoutRequest = new LogoutRequest(context);
        LogTool.d(TAG, "logout " + logoutRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(logoutRequest, listener, tag);
    }

    /**
     * 用户更新密码
     *
     * @param context
     * @param registerInfo
     * @param listener
     * @param tag
     */
    public static void update_psw(Context context, RegisterInfo registerInfo, ApiUiUpdateListener listener, Object tag) {
        ForgetPswRequest forgetPswRequest = new ForgetPswRequest(context);
        LogTool.d(TAG, "update_psw --" + forgetPswRequest.getUrl() + "---" + JsonParser.beanToJson(registerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(forgetPswRequest, JsonParser.beanToJson(registerInfo), listener, tag);
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
    public static void feedBack(Context context, String content, String version, String platform,
                                ApiUiUpdateListener listener, Object tag) {
        FeedBackRequest feedBackRequest = new FeedBackRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("content", content);
            jsonParams.put("version", version);
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
     * 收集新用户的个人偏好
     *
     * @param context
     * @param ownerInfo
     * @param listener
     * @param tag
     */
    public static void post_collect_ownerinfo(Context context, OwnerInfo ownerInfo, ApiUiUpdateListener listener, Object tag) {
        PostCollectOwnerInfoRequest postCollectOwnerInfoRequest = new PostCollectOwnerInfoRequest(context);
        LogTool.d(TAG, "post_collect_ownerinfo --" + postCollectOwnerInfoRequest.getUrl() + "--" + JsonParser.beanToJson(ownerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(postCollectOwnerInfoRequest, JsonParser.beanToJson(ownerInfo), listener, tag);
    }

    /**
     * @param context
     * @author zhanghao
     * @Description 获取业主需求
     */
    public static void get_Requirement_List(Context context, ApiUiUpdateListener listener, Object tag) {
        GetRequirementListRequest getRequirementListRequest = new GetRequirementListRequest(context);
        LogTool.d(TAG, "get_Requirement_list --" + getRequirementListRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(getRequirementListRequest, listener, tag);
    }

    /**
     * 设计师获取首页的所有需求的api
     *
     * @param context
     * @param listener
     * @param tag
     */
    public static void getAllRequirementList(Context context, ApiUiUpdateListener listener, Object tag) {
        GetAllRequirementListRequest getAllRequirementListRequest = new GetAllRequirementListRequest(context);
        LogTool.d(TAG, "getAllRequirementListRequest --" + getAllRequirementListRequest.getUrl());
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getAllRequirementListRequest, "", listener, tag);
    }

    /**
     * 设计师拒绝业主的预约
     *
     * @param context
     * @param listener
     * @param msg
     * @param tag
     */
    public static void refuseRequirement(Context context, ApiUiUpdateListener listener, String requirementId, String msg, Object tag) {
        RefuseRequirementRequest refuseRequirementRequest = new RefuseRequirementRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementId);
            jsonParams.put("reject_respond_msg", msg);
            LogTool.d(TAG, "refuseRequirement --" + refuseRequirementRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(refuseRequirementRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设计师响应业主的需求，
     *
     * @param context
     * @param listener
     * @param requirementId
     * @param houseCheckTime
     * @param tag
     */
    public static void responseRequirement(Context context, ApiUiUpdateListener listener, String requirementId, long houseCheckTime, Object tag) {
        ResponseRequirementRequest responseRequirementRequest = new ResponseRequirementRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementId);
            if (houseCheckTime != 0L) {//houseCheckTime不传为纯粹响应，传就是设置量房时间
                jsonParams.put("house_check_time", houseCheckTime);
            }
            LogTool.d(TAG, "responseRequirement --" + responseRequirementRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(responseRequirementRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设计师配置合同的开工时间
     *
     * @param context
     * @param listener
     * @param requirementId
     * @param startAt
     * @param tag
     */
    public static void configContract(Context context, ApiUiUpdateListener listener, String requirementId, long startAt, Object tag) {
        ConfigContractRequest configContractRequest = new ConfigContractRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementId);
            jsonParams.put("start_at", startAt);
            LogTool.d(TAG, "configContractRequest --" + configContractRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(configContractRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
     * @decription 业主提交装修流程 配置工地
     */
    public static void post_Owner_Process(Context context,
                                          String requirementid, String final_planid, ApiUiUpdateListener listener, Object tag) {
        PostProcessRequest postProcessRequest = new PostProcessRequest(context, requirementid, final_planid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("requirementid", requirementid);
            jsonParams.put("final_planid", final_planid);
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
        RegisterRequest registerRequest = new RegisterRequest(context, registerInfo);
        LogTool.d(TAG, "register  " + registerRequest.getUrl() + "--" + JsonParser.beanToJson(registerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(registerRequest, JsonParser.beanToJson(registerInfo), listener, tag);
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
     * 绑定手机号
     *
     * @param context
     * @param registerInfo
     * @param listener
     * @param tag
     */
    public static void bindingPhone(Context context, RegisterInfo registerInfo,
                                    ApiUiUpdateListener listener, Object tag) {
        BindingPhoneRequest bindingPhoneRequest = new BindingPhoneRequest(context, registerInfo.getPhone());
        LogTool.d(TAG, "register  " + bindingPhoneRequest.getUrl() + "--" + JsonParser.beanToJson(registerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(bindingPhoneRequest, JsonParser.beanToJson(registerInfo), listener, tag);
    }

    /**
     * 绑定微信
     *
     * @param context
     * @param listener
     * @param tag
     */
    public static void bindingWeixin(Context context, String openid, String unionid,
                                     ApiUiUpdateListener listener, Object tag) {
        BindingWeiXinRequest bindingWeiXinRequest = new BindingWeiXinRequest(context, unionid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("wechat_openid", openid);
            jsonParams.put("wechat_unionid", unionid);
            LogTool.d(TAG, "bindingWeixin  " + bindingWeiXinRequest.getUrl() + "--" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(bindingWeiXinRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主刷新session
     *
     * @param context
     * @param uid
     * @param listener
     * @param tag
     */
    public static void refreshSession(Context context, String uid, ApiUiUpdateListener listener, Object tag) {
        RefreshSessionRequest refreshSessionRequest = new RefreshSessionRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", uid);
            LogTool.d(TAG, "refreshSession  " + refreshSessionRequest.getUrl() + "--" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(refreshSessionRequest, jsonParams.toString(), listener, tag);
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
        LogTool.d(TAG, "get_Owner_Info：" + userByOwnerInfoRequest.getUrl());
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
        GetProcessListRequest processListRequest = new GetProcessListRequest(context);
        LogTool.d("JianFanJiaApiClient", "get_Process_List" + processListRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(processListRequest, listener, tag);
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
    public static void getRescheduleAll(Context context,
                                        ApiUiUpdateListener listener, Object tag) {
        GetAllRescheduleRequest getAllRescheduleRequest = new GetAllRescheduleRequest(context);
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(getAllRescheduleRequest, listener, tag);
    }

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
     * 删除节点图片
     *
     * @param imageid
     * @param context
     * @param imageid
     * @param section
     * @param item
     * @param index
     * @param listener
     * @param tag
     */
    public static final void deleteImageToProcess(Context context, String imageid, String section, String item, int index, ApiUiUpdateListener listener, Object tag) {
        DeletePicToSectionItemRequest deletePicToSectionItemRequest = new DeletePicToSectionItemRequest(context);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("_id", imageid);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            jsonParams.put("index", index);
            LogTool.d(TAG, "deleteImageToProcess -" + deletePicToSectionItemRequest.getUrl() + "----" + jsonParams.toString());
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(deletePicToSectionItemRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        GetProcessInfoRequest getProcessInfoRequest = new GetProcessInfoRequest(context, processid);
        String getProcessUrl = Url_New.getInstance().GET_PROCESSINFO_BYID.replace(Url_New.ID,
                processid);
        getProcessInfoRequest.setUrl(getProcessUrl);
        LogTool.d(TAG, "processItemDone -" + getProcessInfoRequest.getUrl());
        OkHttpClientManager.getInstance().getGetDelegate().getAsyn(getProcessInfoRequest, listener, tag);
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
        UpdateOwnerInfoRequest userByOwnerInfoUpdateRequest = new UpdateOwnerInfoRequest(context, ownerInfo);
        LogTool.d(TAG, "put_OwnerInfo -" + userByOwnerInfoUpdateRequest.getUrl() + JsonParser.beanToJson(ownerInfo));
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(userByOwnerInfoUpdateRequest, JsonParser.beanToJson(ownerInfo), listener, tag);
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
    public static void addComment(Context context, String topicid, String topictype, String section, String item, String content, String to, ApiUiUpdateListener listener, Object tag) {
        AddCommentRequest addCommentRequest = new AddCommentRequest(context, topicid, topictype, content, to);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("topicid", topicid);
            jsonParams.put("topictype", topictype);
            jsonParams.put("content", content);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            jsonParams.put("to", to);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(addCommentRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户获取留言评论并标记为已读
     *
     * @param context
     * @param topicid
     * @param from
     * @param limit
     * @param listener
     * @param tag
     */
    public static void getCommentList(Context context, String topicid, int from, int limit, String section, String item, ApiUiUpdateListener listener, Object tag) {
        GetCommentsRequest getCommentsRequest = new GetCommentsRequest(context, topicid, from, limit);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("topicid", topicid);
            jsonParams.put("from", from);
            jsonParams.put("limit", limit);
            jsonParams.put("section", section);
            jsonParams.put("item", item);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getCommentsRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 业主未读通知个数
     *
     * @param getUnReadMsgRequest
     * @param listener
     * @param tag
     */
    public static void getUnReadUserMsg(GetUnReadMsgRequest getUnReadMsgRequest, ApiUiUpdateListener listener, Object tag) {
        LogTool.d(TAG, "jsonParams:" + getUnReadMsgRequest.getParam());
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getUnReadMsgRequest, getUnReadMsgRequest.getParam(), listener, tag);
    }

    /**
     * 业主搜索业主通知
     *
     * @param searchUserMsgRequest
     * @param listener
     * @param tag
     */
    public static void searchUserMsg(SearchUserMsgRequest searchUserMsgRequest, ApiUiUpdateListener listener, Object tag) {
        LogTool.d(TAG, "jsonParams:" + searchUserMsgRequest.getParam());
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(searchUserMsgRequest, searchUserMsgRequest.getParam(), listener, tag);
    }

    /**
     * 业主搜索我的评论
     * @param searchUserCommentRequest
     * @param listener
     * @param tag
     */
    public static void searchUserComment(SearchUserCommentRequest searchUserCommentRequest, ApiUiUpdateListener listener, Object tag) {
        LogTool.d(TAG, "jsonParams:" + searchUserCommentRequest.getParam());
        OkHttpClientManager.getInstance().getPostDelegate().postAsyn(searchUserCommentRequest, searchUserCommentRequest.getParam(), listener, tag);
    }

    /**
     * 业主通知详情
     *
     * @param context
     * @param messageid
     * @param listener
     * @param tag
     */
    public static void getUserMsgDetail(Context context, String messageid, ApiUiUpdateListener listener, Object tag) {
        GetMsgDetailRequest getMsgDetailRequest = new GetMsgDetailRequest(context, messageid);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("messageid", messageid);
            OkHttpClientManager.getInstance().getPostDelegate().postAsyn(getMsgDetailRequest, jsonParams.toString(), listener, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
