package com.jianfanjia.cn.designer.Event;

/**
 * Description: com.jianfanjia.cn.designer.Event
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-20 17:59
 */
public class UpdateEvent {

    private Object data;

    public UpdateEvent(Object data){
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
