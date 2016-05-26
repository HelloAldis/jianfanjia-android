package com.jianfanjia.api.model;

/**
 * Description: com.jianfanjia.api.model
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-26 13:34
 */
public class DesignerAwardInfo extends BaseModel {

    private String award_imageid;

    private String description;

    public String getAward_imageid() {
        return award_imageid;
    }

    public void setAward_imageid(String award_imageid) {
        this.award_imageid = award_imageid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
