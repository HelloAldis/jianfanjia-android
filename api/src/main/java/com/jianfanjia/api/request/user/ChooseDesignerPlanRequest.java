package com.jianfanjia.api.request.user;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.user
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-28 15:59
 */
public class ChooseDesignerPlanRequest extends BaseRequest {

    private String requiremendid;

    private String designerid;

    private String planid;

    public String getRequiremendid() {
        return requiremendid;
    }

    public void setRequiremendid(String requiremendid) {
        this.requiremendid = requiremendid;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }
}
