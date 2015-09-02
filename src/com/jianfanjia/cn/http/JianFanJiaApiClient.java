package com.jianfanjia.cn.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.bean.ProcedureInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.bean.SiteInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
	 * �û��ǳ�
	 * 
	 * @param context
	 * @param handler
	 */
	public static void logout(Context context, AsyncHttpResponseHandler handler) {
		HttpRestClient.get(context, Url.SIGNOUT_URL, handler);
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
			entity = new StringEntity(JsonParser.beanToJson(requirementInfo));
			HttpRestClient.post(context, Url.REGISTER_URL, entity,
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
	 * @description ҵ����ȡ�ҵ����ʦ
	 * @param context
	 * @param hanlder
	 */
	public static void get_Owner_Designer(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_OWER_DESIGNER, hanlder);
	}

	/**
	 * @author zhanghao
	 * @description ���ʦ��ȡ�ҵ�ҵ��
	 * @param context
	 * @param hanlder
	 */
	public static void get_Designer_Owner(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.GET_DESIGNER_OWNER, hanlder);
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
	 * �汾����
	 * 
	 * @param context
	 * @param hanlder
	 */
	public static void updateVersion(Context context,
			AsyncHttpResponseHandler hanlder) {
		HttpRestClient.get(context, Url.UPDATE_VERSION_URL, hanlder);
	}

	// �õ����е�ģ�⹤������
	public static ArrayList<SiteInfo> getAllSites(Context context) {
		ArrayList<SiteInfo> allSites = null;
		try {
			InputStream is = context.getAssets().open("site_json.txt");
			String jsonString = StringUtils.toConvertString(is);
			Gson gson = new Gson();
			allSites = gson.fromJson(jsonString,
					new TypeToken<ArrayList<SiteInfo>>() {
					}.getType());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allSites;
	}

	// �õ��û���ӵ�еĹ���
	public static ArrayList<SiteInfo> getAllSites(Context context,
			String phone, String userType) {
		ArrayList<SiteInfo> allSites = getAllSites(context);
		ArrayList<SiteInfo> userSite = new ArrayList<SiteInfo>();
		if (userType.equals(Constant.IDENTITY_DESIGNER)) {
			for (SiteInfo site : allSites) {
				if (site.getDesignerPhone().equals(phone)) {
					AddProcudureToSite(context, site);
					userSite.add(site);
				}
			}
		} else {
			for (SiteInfo site : allSites) {
				if (site.getUserPhone().equals(phone)) {
					AddProcudureToSite(context, site);
					userSite.add(site);
				}
			}
		}
		allSites.clear();
		return userSite;
	}

	// ���ݹ����õ����򼯺�
	public static void AddProcudureToSite(Context context, SiteInfo site) {
		ArrayList<ProcedureInfo> allProcedure = null;
		try {
			InputStream is = context.getAssets().open(site.getUserPhone());
			String jsonString = StringUtils.toConvertString(is);
			Gson gson = new Gson();
			allProcedure = gson.fromJson(jsonString,
					new TypeToken<ArrayList<ProcedureInfo>>() {
					}.getType());
			site.setProcedures(allProcedure);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
