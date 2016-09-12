package com.jianfanjia.cn.ui.Event;

/**
 * Description: com.jianfanjia.cn.Event
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-05 20:00
 */
public class BindingPhoneEvent {

    private String phone;

    public BindingPhoneEvent(String phone){
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
