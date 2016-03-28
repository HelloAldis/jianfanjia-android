package com.jianfanjia.api.request.user;

import java.util.List;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.user
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-28 11:45
 */
public class OrderDesignerRequest extends BaseRequest {

    private String requirementid;

    private List<String> designerids;

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public List<String> getDesignerids() {
        return designerids;
    }

    public void setDesignerids(List<String> designerids) {
        this.designerids = designerids;
    }
}
