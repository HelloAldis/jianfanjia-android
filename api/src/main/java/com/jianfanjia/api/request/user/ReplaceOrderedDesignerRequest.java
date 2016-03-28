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

    private String old_designserid;

    private String new_designerid;

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public String getOld_designserid() {
        return old_designserid;
    }

    public void setOld_designserid(String old_designserid) {
        this.old_designserid = old_designserid;
    }

    public String getNew_designerid() {
        return new_designerid;
    }

    public void setNew_designerid(String new_designerid) {
        this.new_designerid = new_designerid;
    }
}
