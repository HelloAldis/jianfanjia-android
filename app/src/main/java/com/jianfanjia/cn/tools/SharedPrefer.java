package com.jianfanjia.cn.tools;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.umeng.socialize.net.utils.Base64;

/**
 * Description:SharedPreferences操作类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:13
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
            // 将Product对象放到OutputStream中
            // 将Product对象转换成byte数组，并将其进行base64编码
            String str = new String(Base64.encodeBase64String(baos.toByteArray()));
            // 将编码后的字符串写到base64.xml文件中
            edit.putString(key, str);
            edit.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
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
        // 对Base64格式的字符串进行解码
        byte[] base64Bytes = Base64.decodeBase64(base64Str);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            // 从ObjectInputStream中读取对象
            Object object = ois.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
