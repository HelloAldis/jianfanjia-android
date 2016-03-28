package com.jianfanjia.api.request.user;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.user
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-28 15:40
 */
public class AgreeRescheduleRequest extends BaseRequest {

    private String processid;

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }
}
