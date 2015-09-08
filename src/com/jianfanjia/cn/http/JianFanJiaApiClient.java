package com.jianfanjia.cn.http;

import java.io.UnsupportedEncodingException;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.jianfanjia.cn.bean.CommitCommentInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.tools.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 
 * @ClassName: JianFanJiaApi
 * @Description: http接口类
 * @author zhanghao
 * @date 2015-8-19 下午2:17:12
 * 
 */
public class JianFanJiaApiClient {
	/**
	 * 上传个推clientid
	 * 
	 * @param context
	 * @param clientId
	 * @param handler
	 */
	public static void uploadRegisterId(Context context, String clientId,
			AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("cid", clientId);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.BIND_URL, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author zhanghao
	 * 
	 * @param username
	 * @param password
	 * @param handler
	 */
	public static void login(Context context, String username, String password,
			AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("phone", username);
			jsonParams.put("pass", password);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.LOGIN_URL, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户登出
	 * 
	 * @param context
	 * @param handler
	 */
	public static void logout(Context context, AsyncHttpResponseHandler handler) {
		HttpRestClient.get(context, Url.SIGNOUT_URL, handler);
	}

	/**
	 * 检查版本
	 * 
	 * @param context
	 * @param handler
	 */
	public static void checkVersion(Context context,
			AsyncHttpResponseHandler handler) {
		HttpRestClient.get(context, Url.UPDATE_VERSION_URL, handler);
	}

	/**
	 * @author zhanghao
	 * @param phone
	 * @param handler
	 */
	public static void send_verification(Context context, String phone,
			AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("phone", phone);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.GET_CODE_URL, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author zhanghao
	 * @Description 获取业主需求
	 * @param context
	 * @param handler
	 */
	public static void get_Requirement(Context context,
			AsyncHttpResponseHandler handler) {
		HttpRestClient.get(context, Url.REQUIREMENT, handler);
	}

	/**
	 * @author zhanghao
	 * @decription 业主配置工地
	 * @param context
	 * @param requirementInfo
	 * @param handler
	 */
	public static void post_Owner_Process(Context context,
			RequirementInfo requirementInfo, AsyncHttpResponseHandler handler) {
		StringEntity entity;
		try {
			entity = new StringEntity(JsonParser.beanToJson(requirementInfo));
			HttpRestClient.post(context, Url.PROCESS, entity,
					"application/json", handler);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author zhanghao
	 * @param registerInfo
	 * @param handler
	 */
	public static void register(Context context, RegisterInfo registerInfo,
			AsyncHttpResponseHandler handler) {
		StringEntity entity;
		try {
			entity = new StringEntity(JsonParser.beanToJson(registerInfo));
			HttpRestClient.post(context, Url.REGISTER_URL, entity,
					"application/json", handler);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author zhanghao
	 * @param phone
	 * @param hanlder
	 */
	public static void getUserInfoByPhone(Context context, String phone,
			AsyncHttpResponseHandler hanlder) {
		StringEntity entity;
		try {
			entity = new StringEntity(phone);
			HttpRestClient.post(context, Url.REGISTER_URL, entity,
					"application/json", hanlder);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author zhanghao
	 * @param designerid
	 * @param hanlder
	 */
	public static void getDesignerInfoById(Context context, String designerid,
			AsyncHttpResponseHandler hanlder) {
		String getdesignerUrl = Url.GET_OWER_DESIGNER.replace(Url.ID,
				designerid);
		HttpRestClient.get(context, getdesignerUrl, hanlder);
	}

	/**
	 * @author zhanghao
	 * @description 业主获取个人信息
	 * @param context
	 * @param hanlder
	 */
	public static void get_Owner_Info(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_OWER_INFO, hanlder);
	}

	/**
	 * @author zhanghao
	 * @description 设计师获取个人信息
	 * @param context
	 * @param hanlder
	 */
	public static void get_Designer_Info(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_DESIGNER_INFO, hanlder);
	}

	/**
	 * @author zhanghao
	 * @description 设计师获取我的业主
	 * @param context
	 * @param hanlder
	 */
	public static void get_Designer_Owner(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_DESIGNER_OWNER, hanlder);
	}

	/**
	 * @author zhanghao
	 * @description 设计师获取我的工地列表
	 * @param context
	 * @param hanlder
	 */
	public static void get_Designer_Process_List(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_DESIGNER_PROCESS, hanlder);
	}

	/**
	 * 设计师获取业主个人信息
	 * 
	 * @param context
	 * @param hanlder
	 */
	public static void getOwnerInfoById(Context context, String ownerid,
			AsyncHttpResponseHandler hanlder) {
		String getdesignerUrl = Url.GET_ONE_OWNER_INFO.replace(Url.ID, ownerid);
		HttpRestClient.get(context, getdesignerUrl, hanlder);
	}

	/**
	 * @author zhanghao
	 * @description 业主获取我的工地
	 * @param context
	 * @param hanlder
	 */
	public static void get_Owner_Process(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.PROCESS, hanlder);
	}

	/**
	 * 用户同意改期
	 * 
	 * @param context
	 * @param processid
	 * @param handler
	 */
	public static void agreeReschedule(Context context, String processid,
			AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("processid", processid);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.AGREE_RESCHDULE, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户拒绝改期
	 * 
	 * @param context
	 * @param processid
	 * @param handler
	 */
	public static void refuseReschedule(Context context, String processid,
			AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("processid", processid);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.REFUSE_RESCHDULE, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 版本更新
	 * 
	 * @param context
	 * @param hanlder
	 */
	public static void updateVersion(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.UPDATE_VERSION_URL, hanlder);
	}

	/**
	 * 评论装修流程
	 * 
	 * @author zhanghao
	 * @param commitCommentInfo
	 * @param handler
	 */
	public static void comment(Context context,
			CommitCommentInfo commitCommentInfo,
			AsyncHttpResponseHandler handler) {
		StringEntity entity;
		try {
			entity = new StringEntity(JsonParser.beanToJson(commitCommentInfo));
			HttpRestClient.post(context, Url.POST_PROCESS_COMMENT, entity,
					"application/json", handler);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 用户上传图片到装修流程
	 * 
	 * @param context
	 * @param siteId
	 * @param section
	 * @param item
	 * @param imageId
	 * @param handler
	 */
	public static void submitImageToProcess(Context context, String siteId,
			String section, String item, String imageId,
			AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("_id", siteId);
			jsonParams.put("section", section);
			jsonParams.put("item", item);
			jsonParams.put("imageid", imageId);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.POST_PROCESS_IMAGE, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
