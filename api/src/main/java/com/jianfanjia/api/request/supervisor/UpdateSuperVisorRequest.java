package com.jianfanjia.api.request.supervisor;

import com.jianfanjia.api.model.SuperVisor;
import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.supervisor
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-19 15:43
 */
public class UpdateSuperVisorRequest extends BaseRequest {

    private SuperVisor supervisor;

    public SuperVisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SuperVisor supervisor) {
        this.supervisor = supervisor;
    }
}
