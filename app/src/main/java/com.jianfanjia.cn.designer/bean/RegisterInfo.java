package com.jianfanjia.cn.designer.bean;

import com.jianfanjia.cn.designer.tools.LogTool;

import java.io.Serializable;

/**
 * @author zhanghao
 * @version 1.0
 * @Decription 此类是一个注册实体类
 */
public class RegisterInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String phone;// 手机号
    private String pass;// 密码
    private String code;// 验证码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        String regJson = "{\"phone\" : \"" + phone + "\"," + "\"pass\" : \""
                + pass + "\"," + "\"code\" : \"" + code + "\"}";
        LogTool.d(this.getClass().getName(), regJson);
        return regJson;
    }
}
