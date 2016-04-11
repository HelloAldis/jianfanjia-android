package com.jianfanjia.api.request.user;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.user
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-28 16:10
 */
public class ReplaceOrderedDesignerRequest extends BaseRequest {

    private String requirementid;

    private String old_designerid;

    private String new_designerid;

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public String getOld_designerid() {
        return old_designerid;
    }

    public void setOld_designerid(String old_designerid) {
        this.old_designerid = old_designerid;
    }

    public String getNew_designerid() {
        return new_designerid;
    }

    public void setNew_designerid(String new_designerid) {
        this.new_designerid = new_designerid;
    }
}
