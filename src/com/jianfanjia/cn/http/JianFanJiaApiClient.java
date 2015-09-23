package com.jianfanjia.cn.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import com.jianfanjia.cn.bean.CommitCommentInfo;
import com.jianfanjia.cn.bean.DesignerUpdateInfo;
import com.jianfanjia.cn.bean.OwnerUpdateInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 
 * @ClassName: JianFanJiaApi
 * @Description: http�ӿ���
 * @author zhanghao
 * @date 2015-8-19 ����2:17:12
 * 
 */
public class JianFanJiaApiClient {
	/**
	 * �ϴ�����clientid
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
			StringEntity entity = new StringEntity(jsonParams.toString(),
					"utf-8");
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
			StringEntity entity = new StringEntity(jsonParams.toString(),
					"utf-8");
			HttpRestClient.post(context, Url.LOGIN_URL, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �û��ǳ�
	 * 
	 * @param context
	 * @param handler
	 */
	public static void logout(Context context, AsyncHttpResponseHandler handler) {
		HttpRestClient.get(context, Url.SIGNOUT_URL, handler);
	}

	/**
	 * ���汾
	 * 
	 * @param context
	 * @param handler
	 */
	public static void checkVersion(Context context,
			AsyncHttpResponseHandler handler) {
		HttpRestClient.get(context, Url.UPDATE_VERSION_URL, handler);
	}

	/**
	 * �û�����
	 * 
	 * @param context
	 * @param content
	 * @param platform
	 * @param handler
	 */
	public static void feedBack(Context context, String content,
			String platform, AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("content", content);
			jsonParams.put("platform", platform);
			StringEntity entity = new StringEntity(jsonParams.toString(),
					"utf-8");
			HttpRestClient.post(context, Url.FEEDBACK_URL, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
			StringEntity entity = new StringEntity(jsonParams.toString(),
					"utf-8");
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
	 * @Description ��ȡҵ������
	 * @param context
	 * @param handler
	 */
	public static void get_Requirement(Context context,
			AsyncHttpResponseHandler handler) {
		HttpRestClient.get(context, Url.REQUIREMENT, handler);
	}

	/**
	 * @author zhanghao
	 * @decription ҵ�����ù���
	 * @param context
	 * @param requirementInfo
	 * @param handler
	 */
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
			entity = new StringEntity(JsonParser.beanToJson(registerInfo),
					"utf-8");
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
			entity = new StringEntity(phone, "utf-8");
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
	 * @description ҵ����ȡ������Ϣ
	 * @param context
	 * @param hanlder
	 */
	public static void get_Owner_Info(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_OWER_INFO, hanlder);
	}

	/**
	 * @author zhanghao
	 * @description ���ʦ��ȡ������Ϣ
	 * @param context
	 * @param hanlder
	 */
	public static void get_Designer_Info(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_DESIGNER_INFO, hanlder);
	}

	/**
	 * @author zhanghao
	 * @description ���ʦ��ȡ�ҵ�ҵ��
	 * @param context
	 * @param hanlder
	 */
	public static void get_Designer_Owner(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_DESIGNER_PROCESS, hanlder);
	}

	/**
	 * @author zhanghao
	 * @description ���ʦ��ȡ�ҵĹ����б�
	 * @param context
	 * @param hanlder
	 */
	public static void get_Designer_Process_List(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_DESIGNER_PROCESS, hanlder);
	}

	/**
	 * ���ʦ��ȡҵ��������Ϣ
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
	 * @description ҵ����ȡ�ҵĹ���
	 * @param context
	 * @param hanlder
	 */
	public static void get_Owner_Process(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.PROCESS, hanlder);
	}

	/**
	 * �û��ύ����
	 * 
	 * @param context
	 * @param processId
	 * @param userId
	 * @param designerId
	 * @param section
	 * @param newDate
	 * @param handler
	 */
	public static void postReschedule(Context context, String processId,
			String userId, String designerId, String section, String newDate,
			AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("processid", processId);
			jsonParams.put("userid", userId);
			jsonParams.put("designerid", designerId);
			jsonParams.put("section", section);
			jsonParams.put("new_date",
					DateFormatTool.covertStringToLong(newDate));
			Log.i("JianFanJiaApiClient", "jsonParams��" + jsonParams.toString());
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.POST_RESCHDULE, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �û�ͬ�����
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
			StringEntity entity = new StringEntity(jsonParams.toString(),
					"utf-8");
			HttpRestClient.post(context, Url.AGREE_RESCHDULE, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �û��ܾ�����
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
			StringEntity entity = new StringEntity(jsonParams.toString(),
					"utf-8");
			HttpRestClient.post(context, Url.REFUSE_RESCHDULE, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �û���ȡ�ҵĸ�������
	 * 
	 * @param context
	 * @param hanlder
	 */
	public static void rescheduleAll(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_RESCHDULE_ALL, hanlder);
	}

	/**
	 * �汾����
	 * 
	 * @param context
	 * @param hanlder
	 */
	public static void updateVersion(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.UPDATE_VERSION_URL, hanlder);
	}

	/**
	 * ����װ������
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
			Log.i("jianfanjiaApi", JsonParser.beanToJson(commitCommentInfo));
			entity = new StringEntity(JsonParser.beanToJson(commitCommentInfo),
					"utf-8");
			HttpRestClient.post(context, Url.POST_PROCESS_COMMENT, entity,
					"application/json;charset=utf-8", handler);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * �û��ϴ�ͼƬ
	 * 
	 * @param context
	 * @param hanlder
	 */
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
	}

	/**
	 * �û��ϴ�ͼƬ��װ������
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

	/**
	 * ���ʦ�ύ����ͼƬ
	 * 
	 * @param context
	 * @param siteId
	 * @param section
	 * @param key
	 * @param imageId
	 * @param handler
	 */
	public static void submitYanShouImage(Context context, String siteId,
			String section, String key, String imageId,
			AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("_id", siteId);
			jsonParams.put("section", section);
			jsonParams.put("key", key);
			jsonParams.put("imageid", imageId);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.SUBMIT_YAHSHOU_IMAGE, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ʦɾ������ͼƬ
	 * 
	 * @param context
	 * @param siteId
	 * @param key
	 * @param handler
	 */
	public static void deleteYanShouImage(Context context, String siteId,
			String key, AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("_id", siteId);
			jsonParams.put("key", key);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.DELETE_YAHSHOU_IMAGE, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �û��깤װ������С�ڵ�
	 * 
	 * @param context
	 * @param siteId
	 * @param section
	 * @param item
	 * @param handler
	 */
	public static void processItemDone(Context context, String siteId,
			String section, String item, AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("_id", siteId);
			jsonParams.put("section", section);
			jsonParams.put("item", item);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.POST_PROCESS_DONE_ITEM, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����id�õ�������Ϣ
	 * 
	 * @param context
	 * @param processid
	 * @param handler
	 */
	public static void get_ProcessInfo_By_Id(Context context, String processid,
			AsyncHttpResponseHandler handler) {
		String getProcessUrl = Url.GET_PROCESSINFO_BYID.replace(Url.ID,
				processid);
		HttpRestClient.get(context, getProcessUrl, handler);
	}

	/**
	 * �޸�ҵ��������Ϣ
	 * 
	 * @param context
	 * @param ownerInfo
	 * @param handler
	 */
	public static void put_OwnerInfo(Context context,
			OwnerUpdateInfo ownerInfo, AsyncHttpResponseHandler handler) {
		StringEntity entity;
		try {
			entity = new StringEntity(JsonParser.beanToJson(ownerInfo), "utf-8");
			HttpRestClient.put(context, Url.GET_OWER_INFO, entity,
					"application/json", handler);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * �޸����ʦ������Ϣ
	 * 
	 * @param context
	 * @param designerInfo
	 * @param handler
	 */
	public static void put_DesignerInfo(Context context,
			DesignerUpdateInfo designerInfo, AsyncHttpResponseHandler handler) {
		StringEntity entity;
		try {
			entity = new StringEntity(JsonParser.beanToJson(designerInfo),
					"utf-8");
			HttpRestClient.put(context, Url.GET_DESIGNER_INFO, entity,
					"application/json", handler);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ���ʦȷ�Ͽ��Կ�ʼ����
	 * 
	 * @param context
	 * @param siteid
	 * @param processid
	 * @param handler
	 */
	public static void confirm_canCheckBydesigner(Context context,
			String siteid, String processid, AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("_id", siteid);
			jsonParams.put("section", processid);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.CONFIRM_CHECK_BY_DESIGNER, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ҵ��ȷ�϶Ա��������
	 * 
	 * @param context
	 * @param siteid
	 * @param processid
	 * @param handler
	 */
	public static void confirm_CheckDoneByOwner(Context context, String siteid,
			String processid, AsyncHttpResponseHandler handler) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("_id", siteid);
			jsonParams.put("section", processid);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(context, Url.CONFIRM_CHECK_DONE_BY_OWNER,
					entity, "application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void get_TotalDuration(Context context, String planId,
			AsyncHttpResponseHandler handler) {
		String getTotalDurationUrl = Url.GET_PLAN.replace(Url.ID, planId);
		HttpRestClient.get(context, getTotalDurationUrl, handler);
	}

}
