package com.jianfanjia.api.request.user;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.user
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-28 15:50
 */
public class EvaluateDesignerRequest extends BaseRequest {

    private String designerid;

    private String requirementid;

    private int service_attitude;

    private int respond_speed;

    private String comment;

    private String is_anonymous;

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public int getService_attitude() {
        return service_attitude;
    }

    public void setService_attitude(int service_attitude) {
        this.service_attitude = service_attitude;
    }

    public int getRespond_speed() {
        return respond_speed;
    }

    public void setRespond_speed(int respond_speed) {
        this.respond_speed = respond_speed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIs_anonymous() {
        return is_anonymous;
    }

    public void setIs_anonymous(String is_anonymous) {
        this.is_anonymous = is_anonymous;
    }
}
