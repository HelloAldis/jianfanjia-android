package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.user
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-28 15:34
 */
public class ApplyRescheduleRequest extends BaseRequest {

    private String processid;
    private String userid;
    private String designerid;
    private String section;
    private long new_date;

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public long getNew_date() {
        return new_date;
    }

    public void setNew_date(long new_date) {
        this.new_date = new_date;
    }
}
