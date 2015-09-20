package com.jianfanjia.cn.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.Context;
import android.content.SharedPreferences;
import org.apache.mina.util.Base64;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 
 * @ClassName: SharedPrefer
 * @Description: SharedPreferences������
 * @author fengliang
 * @date 2015-8-18 ����1:16:13
 * 
 */
public class SharedPrefer {
	private Context context;
	private SharedPreferences sp = null;
	private Editor edit = null;

	/**
	 * Create DefaultSharedPreferences
	 * 
	 * @param context
	 */
	public SharedPrefer(Context context) {
		this(context, PreferenceManager.getDefaultSharedPreferences(context));
	}

	/**
	 * Create SharedPreferences by filename
	 * 
	 * @param context
	 * @param filename
	 */
	public SharedPrefer(Context context, String filename) {
		this(context, context.getSharedPreferences(filename,
				Context.MODE_PRIVATE));
	}

	/**
	 * Create SharedPreferences by SharedPreferences
	 * 
	 * @param context
	 * @param sp
	 */
	public SharedPrefer(Context context, SharedPreferences sp) {
		this.context = context;
		this.sp = sp;
		edit = sp.edit();
	}

	// Set

	// Boolean
	public void setValue(String key, boolean value) {
		edit.putBoolean(key, value);
		edit.commit();
	}

	public void setValue(int resKey, boolean value) {
		setValue(this.context.getString(resKey), value);
	}

	// Float
	public void setValue(String key, float value) {
		edit.putFloat(key, value);
		edit.commit();
	}

	public void setValue(int resKey, float value) {
		setValue(this.context.getString(resKey), value);
	}

	// Integer
	public void setValue(String key, int value) {
		edit.putInt(key, value);
		edit.commit();
	}

	public void setValue(int resKey, int value) {
		setValue(this.context.getString(resKey), value);
	}

	// Long
	public void setValue(String key, long value) {
		edit.putLong(key, value);
		edit.commit();
	}

	public void setValue(int resKey, long value) {
		setValue(this.context.getString(resKey), value);
	}

	// String
	public void setValue(String key, String value) {
		edit.putString(key, value);
		edit.commit();
	}

	public void setValue(int resKey, String value) {
		setValue(this.context.getString(resKey), value);
	}

	// Object
	public void setValue(String key, Object value) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(value);
			// ��Product����ŵ�OutputStream��
			// ��Product����ת����byte���飬���������base64����
			String str = new String(Base64.encodeBase64(baos.toByteArray()));
			// ���������ַ���д��base64.xml�ļ���
			edit.putString(key, str);
			edit.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				baos.close();
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setValue(int resKey, Object value) {
		setValue(this.context.getString(resKey), value);
	}

	// Get

	// Boolean
	public boolean getValue(String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}

	public boolean getValue(int resKey, boolean defaultValue) {
		return getValue(this.context.getString(resKey), defaultValue);
	}

	// Float
	public float getValue(String key, float defaultValue) {
		return sp.getFloat(key, defaultValue);
	}

	public float getValue(int resKey, float defaultValue) {
		return getValue(this.context.getString(resKey), defaultValue);
	}

	// Integer
	public int getValue(String key, int defaultValue) {
		return sp.getInt(key, defaultValue);
	}

	public int getValue(int resKey, int defaultValue) {
		return getValue(this.context.getString(resKey), defaultValue);
	}

	// Long
	public long getValue(String key, long defaultValue) {
		return sp.getLong(key, defaultValue);
	}

	public long getValue(int resKey, long defaultValue) {
		return getValue(this.context.getString(resKey), defaultValue);
	}

	// String
	public String getValue(String key, String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	public String getValue(int resKey, String defaultValue) {
		return getValue(this.context.getString(resKey), defaultValue);
	}

	// Object
	public Object getValue(String key) {
		String base64Str = sp.getString(key, "");
		// ��Base64��ʽ���ַ������н���
		byte[] base64Bytes = Base64.decodeBase64(base64Str.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			// ��ObjectInputStream�ж�ȡ����
			Object object = ois.readObject();
			ois.close();
			return object;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// Delete
	public void remove(String key) {
		edit.remove(key);
		edit.commit();
	}

	public void clear() {
		edit.clear();
		edit.commit();
	}

}
